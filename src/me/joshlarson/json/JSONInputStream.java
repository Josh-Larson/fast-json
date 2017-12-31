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
	
	private static final boolean [] STRING_SEPARATORS = new boolean[256];
	private static final boolean [] TOKEN_MATCHERS = new boolean[256];
	private static final boolean [] WHITESPACE_MATCHERS = new boolean[256];
	
	static {
		/*
			case ' ':
			case '\n':
			case '\t':
			case '\r':
			case ',':
			case ']':
			case '}':
		 */
		STRING_SEPARATORS['\\'] = true;
		STRING_SEPARATORS['\"'] = true;
		
		WHITESPACE_MATCHERS[' '] = true;
		WHITESPACE_MATCHERS['\n'] = true;
		WHITESPACE_MATCHERS['\t'] = true;
		WHITESPACE_MATCHERS['\r'] = true;
		
		System.arraycopy(WHITESPACE_MATCHERS, 0, TOKEN_MATCHERS, 0, 256);
		TOKEN_MATCHERS[','] = true;
		TOKEN_MATCHERS[']'] = true;
		TOKEN_MATCHERS['}'] = true;
	}
	
	private final InputStream is;
	private final byte[] buffer;
	
	private int bufferPos;
	private int bufferSize;
	
	private char[] strData;
	private int strLength;
	private int strMaxLength;
	
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
		this.buffer = new byte[1024*4];
		this.bufferPos = 0;
		this.bufferSize = 0;
		
		this.strData = new char[512];
		this.strLength = 0;
		this.strMaxLength = 512;
	}
	
	/**
	 * Reads a JSONObject or a JSONArray from the stream
	 * 
	 * @return the read JSONObject/JSONArray or null if it's the end of the stream
	 * @throws IOException   if there is an exception within the input stream
	 * @throws JSONException if there is a JSON parsing error
	 */
	public Object readNext() throws IOException, JSONException {
		char c;
		try {
			c = ingestWhitespace();
		} catch (EOFException e) {
			return null;
		}
		switch (c) {
			case '{':
				return getNextObjectInternal();
			case '[':
				return getNextArrayInternal();
			default:
				throw new JSONException("Invalid start to object/array!");
		}
	}
	
	/**
	 * Reads a JSONObject from the stream
	 *
	 * @return the read JSONObject, or null if it's the end of the stream
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
	 * @return the read JSONArray or null if it's the end of the stream
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
		
		String key;
		do {
			if (ingestWhitespace() != '\"')
				throw new JSONException("Keys must start with \"!");
			key = getNextTokenString();
			if (ingestWhitespace() != ':')
				throw new JSONException("Attributes must be key-value pairs separated by ':'");
			
			obj.put(key, getNextInternal());
		} while (ingestSeparator('}'));
		return obj;
	}
	
	private JSONArray getNextArrayInternal() throws IOException, JSONException {
		JSONArray array = new JSONArray();
		
		do {
			array.add(getNextInternal());
		} while (ingestSeparator(']'));
		return array;
	}
	
	private Object getNextInternal() throws IOException, JSONException {
		char c = ingestWhitespace();
		switch (c) {
			case '\"':
				return getNextTokenString();
			case '[':
				return getNextArrayInternal();
			case '{':
				return getNextObjectInternal();
		}
		
		strLength = 0;
		stringAppend(c);
		return getNextTokenOther();
	}
	
	private String getNextTokenString() throws IOException {
		int c, min;
		int pos = bufferPos;
		int size = bufferSize;
		byte [] buf = buffer;
		char [] str = strData;
		int strLen = 0;
		while (true) {
			min = strMaxLength - strLen + pos;
			if (size < min)
				min = size;
			while (pos < min) {
				c = buf[pos++];
				if (!STRING_SEPARATORS[c]) {
					str[strLen++] = (char) c;
					continue;
				}
				bufferPos = pos;
				bufferSize = size;
				strLength = strLen;
				strData = str;
				if (c == '\\') {
					stringAppend(readEscape());
					pos = bufferPos;
					size = bufferSize;
					strLen = strLength;
					str = strData;
					break;
				} else if (c == '\"') {
					return stringCreate();
				}
				throw new IllegalStateException("getNextTokenString()");
			}
			if (pos >= size) {
				if ((size = is.read(buf)) <= 0)
					throw new EOFException();
				pos = 0;
			}
			if (strLen >= strMaxLength) {
				strMaxLength *= 8;
				char [] replacement = new char[strMaxLength];
				System.arraycopy(str, 0, replacement, 0, strLen);
				str = replacement;
			}
		}
	}
	
	private Object getNextTokenOther() throws IOException, JSONException {
		int c, min;
		int pos = bufferPos;
		int size = bufferSize;
		byte [] buf = buffer;
		char [] str = strData;
		int strLen = strLength;
		outer_loop:
		while (true) {
			min = strMaxLength - strLen + pos;
			if (size < min)
				min = size;
			while (pos < min) {
				c = buf[pos];
				if (!TOKEN_MATCHERS[c]) {
					str[strLen++] = (char) c;
					pos++;
					continue;
				}
				break outer_loop;
			}
			if (pos >= size) {
				if ((size = is.read(buf)) <= 0)
					throw new EOFException();
				pos = 0;
			}
			if (strLen >= strMaxLength) {
				strMaxLength *= 8;
				char [] replacement = new char[strMaxLength];
				System.arraycopy(str, 0, replacement, 0, strLen);
				str = replacement;
			}
		}
		strData = str;
		strLength = strLen;
		bufferPos = pos;
		bufferSize = size;
		
		return parseToken();
	}
	
	private Object parseToken() throws JSONException {
		boolean decimal = false;
		int len = strLength;
		for (int i = 0; i < len; i++) {
			switch (strData[i]) {
				case '.':
					decimal = true;
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
				case '-':
				case '+':
				case 'E':
				case 'e':
					break;
				default:
					if (stringEquals("null"))
						return null;
					if (stringEquals("false"))
						return Boolean.FALSE;
					if (stringEquals("true"))
						return Boolean.TRUE;
					
					throw new JSONException("Invalid token: " + stringCreate());
			}
		}
		
		String ret = stringCreate();
		if (decimal)
			return Double.valueOf(ret);
		return Long.valueOf(ret);
		
	}
	
	private boolean ingestSeparator(char endChar) throws IOException, JSONException {
		int c;
		int pos = bufferPos;
		int size = bufferSize;
		byte [] buf = buffer;
		while (true) {
			while (pos < size) {
				c = buf[pos++];
				if (WHITESPACE_MATCHERS[c]) {
					continue;
				}
				bufferPos = pos;
				bufferSize = size;
				if (c == ',')
					return true;
				if (c == endChar)
					return false;
				throw new JSONException("Expected ',' or '"+endChar+"' after value!");
			}
			if ((size = is.read(buf)) <= 0)
				throw new EOFException();
			pos = 0;
		}
	}
	
	private char ingestWhitespace() throws IOException {
		int c;
		int pos = bufferPos;
		int size = bufferSize;
		byte [] buf = buffer;
		while (true) {
			while (pos < size) {
				c = buf[pos++];
				if (WHITESPACE_MATCHERS[c]) {
					continue;
				}
				bufferPos = pos;
				bufferSize = size;
				return (char) c;
			}
			if ((size = is.read(buf)) <= 0)
				throw new EOFException();
			pos = 0;
		}
	}
	
	private char readEscape() throws IOException {
		char c = readChar();
		switch (c) {
			case 'n':
				return '\n';
			case 'r':
				return '\r';
			case 't':
				return '\t';
			case 'b':
				return '\b';
			case 'u':
				break;
			default:
				return c;
		}
		
		char result = 0;
		for (int i = 0; i < 4; i++) {
			result |= readHexCharacter() << ((3 - i) * 4);
		}
		return result;
	}
	
	private byte readHexCharacter() throws IOException {
		char c = readChar();
		if (c >= '0' && c <= '9')
			return (byte) (c - '0');
		else if (c >= 'a' && c <= 'f')
			return (byte) (c - 'a' + 10);
		else if (c >= 'A' && c <= 'F')
			return (byte) (c - 'A' + 10);
		
		throw new NumberFormatException();
	}
	
	private char readChar() throws IOException {
		int pos = bufferPos;
		if (pos >= bufferSize) {
			if ((bufferSize = is.read(buffer)) <= 0)
				throw new EOFException();
			pos = 0;
		}
		bufferPos = pos + 1;
		return (char) buffer[pos];
	}
	
	private void stringAppend(char c) {
		int len = strLength;
		int max = strMaxLength;
		strData[len] = c;
		len++;
		strLength = len;
		if (len < max)
			return;
		max *= 2;
		char [] replacement = new char[max];
		System.arraycopy(strData, 0, replacement, 0, len);
		strData = replacement;
		strMaxLength = max;
	}
	
	private boolean stringEquals(String str) {
		int len = str.length();
		if (len != strLength)
			return false;
		for (int i = 0; i < len; ++i) {
			if (strData[i] != str.charAt(i))
				return false;
		}
		return true;
	}
	
	private String stringCreate() {
		return new String(strData, 0, strLength);
	}
	
}
