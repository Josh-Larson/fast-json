package me.joshlarson.json;

import java.io.ByteArrayInputStream;
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
	private int bufferPos;
	private int bufferSize;
	private char peek;
	private boolean hasPeek;
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
		this.buffer = new byte[256];
		this.bufferPos = 0;
		this.bufferSize = 0;
		this.peek = 0;
		this.hasPeek = false;
		this.previousTokenString = false;
	}
	
	/**
	 * Reads a JSONObject from the stream
	 * 
	 * @return the read JSONObject
	 * @throws IOException if there is an exception within the input stream
	 * @throws JSONException if there is a JSON parsing error
	 */
	public JSONObject readObject() throws IOException, JSONException {
		readAssert(getNextToken().equals("{"), "JSONObject must start with '{'");
		return getNextObjectInternal();
	}
	
	/**
	 * Reads a JSONArray from the stream
	 * 
	 * @return the read JSONArray
	 * @throws IOException if there is an exception within the input stream
	 * @throws JSONException if there is a JSON parsing error
	 */
	public JSONArray readArray() throws IOException, JSONException {
		readAssert(getNextToken().equals("["), "JSONArray must start with '['");
		return getNextArrayInternal();
	}
	
	private JSONObject getNextObjectInternal() throws IOException, JSONException {
		JSONObject obj = new JSONObject();
		String token = getNextToken();
		while (!token.equals("}")) {
			String key = token;
			readAssert(getNextToken().equals(":"), "Attributes must be key-value pairs separated by ':'");
			token = getNextToken();
			if (previousTokenString)
				obj.put(key, token);
			else if (token.equals("null"))
				obj.putNull(key);
			else if (token.equals("false"))
				obj.put(key, false);
			else if (token.equals("true"))
				obj.put(key, true);
			else if (token.equals("["))
				obj.put(key, getNextArrayInternal());
			else if (token.equals("{"))
				obj.put(key, getNextObjectInternal());
			else {
				try {
					if (token.indexOf('.') != -1)
						obj.put(key, Double.valueOf(token));
					else
						obj.put(key, Long.valueOf(token));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			token = getNextToken();
			if (!token.equals("}")) {
				readAssert(token.equals(","), "Attributes must be key-value pairs enumerated by ','");
				token = getNextToken();
			}
		}
		return obj;
	}
	
	private JSONArray getNextArrayInternal() throws IOException, JSONException {
		JSONArray array = new JSONArray();
		String token = getNextToken();
		while (!token.equals("]")) {
			if (previousTokenString)
				array.add(token);
			else if (token.equals("null"))
				array.addNull();
			else if (token.equals("false"))
				array.add(false);
			else if (token.equals("true"))
				array.add(true);
			else if (token.equals("["))
				array.add(getNextArrayInternal());
			else if (token.equals("{"))
				array.add(getNextObjectInternal());
			else {
				try {
					if (token.indexOf('.') != -1)
						array.add(Double.valueOf(token));
					else
						array.add(Long.valueOf(token));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			token = getNextToken();
			if (!token.equals("]")) {
				readAssert(token.equals(","), "Values must be enumerated by ','");
				token = getNextToken();
			}
		}
		return array;
	}
	
	private String getNextToken() throws IOException, JSONException {
		char c = ingestWhitespace();
		previousTokenString = false;
		if (c == 0)
			return "";
		if (c == '{' || c == '[' || c == '}' || c == ']' || c == ',' || c == ':') {
			return Character.toString(c);
		}
		StringBuilder builder = new StringBuilder();
		if (c == '\"') {
			getNextTokenString(builder);
			previousTokenString = true;
		} else {
			builder.append(c);
			getNextTokenOther(builder);
		}
		return builder.toString();
	}
	
	private void readAssert(boolean val, String message) throws JSONException {
		if (!val)
			throw new JSONException(message);
	}
	
	private void getNextTokenString(StringBuilder builder) throws IOException, JSONException {
		char c = readChar();
		boolean prevEscape = false;
		while (c != '\"' || prevEscape) {
			if (c != '\\' || prevEscape)
				builder.append(c);
			prevEscape = (c == '\\' && !prevEscape);
			c = readChar();
			if (prevEscape) {
				switch (c) {
					case 'n':
						c = '\n';
						break;
					case 'r':
						c = '\r';
						break;
					case 't':
						c = '\t';
						break;
					case 'b':
						c = '\b';
						break;
					case 'u':
						c = (char) Integer.valueOf("" + readChar() + readChar() + readChar() + readChar(), 16).intValue();
						break;
					case '\"':
					case '\\':
						break;
					default:
						throw new JSONException("Unknown escaped character: " + c);
				}
			}
		}
	}
	
	private void getNextTokenOther(StringBuilder builder) throws IOException {
		while (isLetterOrNumber(peekChar())) {
			builder.append(readChar());
		}
	}
	
	private boolean isLetterOrNumber(char c) {
		return isUppercase(c) || isLowercase(c) || isDigit(c) || isExtraNumCharacter(c);
	}
	
	private boolean isUppercase(char c) {
		return c >= 'A' && c <= 'Z';
	}
	
	private boolean isLowercase(char c) {
		return c >= 'a' && c <= 'z';
	}
	
	private boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}
	
	private boolean isExtraNumCharacter(char c) {
		return c == '.' || c == '-' || c == 'E' || c == 'e' || c == '+';
	}
	
	private boolean isWhitespace(char c) {
		return c == ' ' || c == '\n' || c == '\t' || c == '\r';
	}
	
	private char ingestWhitespace() throws IOException {
		char c;
		do {
			c = readChar();
		} while (isWhitespace(c));
		return (char) c;
	}
	
	public char peekChar() throws IOException {
		if (hasPeek)
			return peek;
		peek = readChar();
		hasPeek = true;
		return peek;
	}
	
	public char readChar() throws IOException {
		if (hasPeek) {
			hasPeek = false;
			return peek;
		}
		int r = read();
		if (r == -1)
			return 0;
		return (char) r;
	}
	
	@Override
	public int read() throws IOException {
		if (bufferPos >= bufferSize) {
			bufferSize = read(buffer, 0, Math.min(buffer.length, available()));
			bufferPos = 0;
			if (bufferSize < 0)
				return -1;
			if (bufferSize == 0)
				return is.read();
		}
		return buffer[bufferPos++];
	}
	
	public int read(byte[] b) throws IOException {
		return is.read(b);
	}
	
	public int read(byte[] b, int off, int len) throws IOException {
		return is.read(b, off, len);
	}
	
	public long skip(long n) throws IOException {
		return is.skip(n);
	}
	
	public int available() throws IOException {
		return is.available();
	}
	
	public void close() throws IOException {
		is.close();
	}
	
	public void reset() throws IOException {
		is.reset();
	}
	
}
