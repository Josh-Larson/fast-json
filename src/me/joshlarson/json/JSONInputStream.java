/*
 **********************************************************************************
 * MIT License                                                                    *
 *                                                                                *
 * Copyright (c) 2017 Josh Larson                                                 *
 *                                                                                *
 * Permission is hereby granted, free of charge, to any person obtaining a copy   *
 * of this software and associated documentation files (the "Software"), to deal  *
 * in the Software without restriction, including without limitation the rights   *
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell      *
 * copies of the Software, and to permit persons to whom the Software is          *
 * furnished to do so, subject to the following conditions:                       *
 *                                                                                *
 * The above copyright notice and this permission notice shall be included in all *
 * copies or substantial portions of the Software.                                *
 *                                                                                *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR     *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,       *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE    *
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER         *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,  *
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE  *
 * SOFTWARE.                                                                      *
 **********************************************************************************
 */
package me.joshlarson.json;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * This input stream will read RFC 4627 compatible JSON strings from either a string or an input
 * stream
 *
 * @author josh
 */
public class JSONInputStream extends InputStream {
	
	private final InputStream is;
	private final byte[] buffer;
	private final StringBuilder stringBuilder;
	private int bufferPos;
	private int bufferSize;
	private boolean previousTokenString;
	
	/**
	 * Creates a new input stream around the specified string
	 *
	 * @param str a RFC 4627 JSON string
	 */
	public JSONInputStream(String str) {
		this(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
	}
	
	/**
	 * Creates a new input stream around the specified input stream
	 *
	 * @param is the input stream pointing to the RFC 4627 JSON string
	 */
	public JSONInputStream(InputStream is) {
		this.is = is;
		this.buffer = new byte[4096];
		this.stringBuilder = new StringBuilder();
		this.bufferPos = 0;
		this.bufferSize = 0;
		this.previousTokenString = false;
	}
	
	/**
	 * Reads a JSONObject from the stream
	 *
	 * @return the read JSONObject
	 * @throws IOException   if there is an exception within the input stream
	 * @throws JSONException if there is a JSON parsing error
	 */
	public JSONObject readObject() throws IOException, JSONException {
		try {
			if (ingestWhitespace() != '{')
				throw new JSONException("JSONObject must start with '{'");
		} catch (EOFException e) {
			return null;
		}
		return getNextObjectInternal();
	}
	
	/**
	 * Reads a JSONArray from the stream
	 *
	 * @return the read JSONArray
	 * @throws IOException   if there is an exception within the input stream
	 * @throws JSONException if there is a JSON parsing error
	 */
	public JSONArray readArray() throws IOException, JSONException {
		try {
			if (ingestWhitespace() != '[')
				throw new JSONException("JSONArray must start with '['");
		} catch (EOFException e) {
			return null;
		}
		return getNextArrayInternal();
	}
	
	@Override
	public int read() throws IOException {
		return is.read();
	}
	
	@Override
	public int read(byte[] b) throws IOException {
		return is.read(b);
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return is.read(b, off, len);
	}
	
	@Override
	public long skip(long n) throws IOException {
		return is.skip(n);
	}
	
	@Override
	public int available() throws IOException {
		return is.available();
	}
	
	@Override
	public void close() throws IOException {
		is.close();
	}
	
	@Override
	public void reset() throws IOException {
		is.reset();
	}
	
	private JSONObject getNextObjectInternal() throws IOException, JSONException {
		JSONObject obj = new JSONObject();
		
		char separator;
		String key;
		do {
			key = getNextToken();
			if (ingestWhitespace() != ':')
				throw new JSONException("Attributes must be key-value pairs separated by ':'");
			
			obj.put(key, getNextInternal());
			
			separator = ingestWhitespace();
			if (separator == '}')
				return obj;
			if (separator != ',')
				throw new JSONException("Expected ',' or '}' after value!");
		} while (true);
	}
	
	private JSONArray getNextArrayInternal() throws IOException, JSONException {
		JSONArray array = new JSONArray();
		
		char separator;
		do {
			array.add(getNextInternal());
			
			separator = ingestWhitespace();
			if (separator == ']')
				return array;
			if (separator != ',')
				throw new JSONException("Expected ',' or ']' after value!");
		} while (true);
	}
	
	private Object getNextInternal() throws IOException, JSONException {
		String token = getNextToken();
		if (previousTokenString)
			return token;
		
		switch (token) {
			case "null":
				return null;
			case "false":
				return false;
			case "true":
				return true;
			case "[":
				return getNextArrayInternal();
			case "{":
				return getNextObjectInternal();
		}
		
		if (token.indexOf('.') != -1)
			return Double.valueOf(token);
		
		return Long.valueOf(token);
	}
	
	private String getNextToken() throws IOException {
		char c = ingestWhitespace();
		previousTokenString = false;
		
		stringBuilder.setLength(0);
		if (c == '\"') {
			c = readChar();
			while (c != '\"') {
				if (c == '\\')
					c = handleEscape(readChar());
				stringBuilder.append(c);
				
				c = readChar();
			}
			previousTokenString = true;
		} else {
			stringBuilder.append(c);
			c = peekChar();
			while (isTokenChar(c)) {
				stringBuilder.append(readChar());
				c = peekChar();
			}
		}
		return stringBuilder.toString();
	}
	
	private char handleEscape(char c) throws IOException {
		switch (c) {
			case 'n':
				return '\n';
			case 'r':
				return '\r';
			case 't':
				return '\t';
			case 'b':
				return '\b';
			case 'u': {
				char result = 0;
				for (int i = 0; i < 4; i++) {
					c = readChar();
					if (c >= '0' && c <= '9')
						c -= '0';
					else if (c >= 'a' && c <= 'f')
						c -= 'a' - 10;
					else if (c >= 'A' && c <= 'F')
						c -= 'A' - 10;
					else
						throw new NumberFormatException();
					result |= c << ((3-i) * 4);
				}
				return result;
			}
			default:
				return c;
		}
	}
	
	private boolean isTokenChar(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '.' || c == '-' || c == '+';
	}
	
	private char ingestWhitespace() throws IOException {
		char c;
		do {
			c = readChar();
		} while (c == ' ' || c == '\n' || c == '\t' || c == '\r');
		return c;
	}
	
	private char peekChar() throws IOException {
		if (bufferPos >= bufferSize) {
			bufferSize = is.read(buffer);
			bufferPos = 0;
			if (bufferSize <= 0)
				throw new EOFException();
		}
		return (char) buffer[bufferPos];
	}
	
	private char readChar() throws IOException {
		if (bufferPos >= bufferSize) {
			bufferSize = is.read(buffer);
			bufferPos = 0;
			if (bufferSize <= 0)
				throw new EOFException();
		}
		return (char) buffer[bufferPos++];
	}
	
}
