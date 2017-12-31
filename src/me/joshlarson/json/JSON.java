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

import java.io.IOException;
import java.io.InputStream;

/**
 * Provides convenience methods for stream operations that do automatic resource cleanup
 *
 * @author josh
 */
public class JSON {
	
	/**
	 * Opens a new JSONInputStream with the specified InputStream and reads either a JSONObject or a JSONArray based on the input stream. After
	 * reading, the input stream is closed
	 *
	 * @param is         the input stream to read from
	 * @param printError TRUE if exception stack traces should be printed, FALSE otherwise
	 * @return the JSONObject read from the stream, or null if there was an exception
	 */
	public static Object readNext(InputStream is, boolean printError) {
		try (JSONInputStream in = new JSONInputStream(is)) {
			return in.readNext();
		} catch (IOException | JSONException e) {
			if (printError)
				e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Opens a new JSONInputStream with the specified string and reads either a JSONObject or a JSONArray based on the string
	 *
	 * @param str        the string to read from
	 * @param printError TRUE if exception stack traces should be printed, FALSE otherwise
	 * @return the JSONObject read from the stream, or null if there was an exception
	 */
	public static Object readNext(String str, boolean printError) {
		try (JSONInputStream in = new JSONInputStream(str)) {
			return in.readNext();
		} catch (IOException | JSONException e) {
			if (printError)
				e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Opens a new JSONInputStream with the specified InputStream and reads a JSONObject. After reading, the input stream is closed
	 *
	 * @param is         the input stream to read from
	 * @param printError TRUE if exception stack traces should be printed, FALSE otherwise
	 * @return the JSONObject read from the stream, or null if there was an exception
	 */
	public static JSONObject readObject(InputStream is, boolean printError) {
		try (JSONInputStream in = new JSONInputStream(is)) {
			return in.readObject();
		} catch (IOException | JSONException e) {
			if (printError)
				e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Opens a new JSONInputStream with the specified string and reads a JSONObject
	 *
	 * @param str        the string to read from
	 * @param printError TRUE if exception stack traces should be printed, FALSE otherwise
	 * @return the JSONObject read from the string, or null if there was an exception
	 */
	public static JSONObject readObject(String str, boolean printError) {
		try (JSONInputStream in = new JSONInputStream(str)) {
			return in.readObject();
		} catch (IOException | JSONException e) {
			if (printError)
				e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Opens a new JSONInputStream with the specified InputStream and reads a JSONArray. After reading, the input stream is closed
	 *
	 * @param is         the input stream to read from
	 * @param printError TRUE if exception stack traces should be printed, FALSE otherwise
	 * @return the JSONArray read from the stream, or null if there was an exception
	 */
	public static JSONArray readArray(InputStream is, boolean printError) {
		try (JSONInputStream in = new JSONInputStream(is)) {
			return in.readArray();
		} catch (IOException | JSONException e) {
			if (printError)
				e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Opens a new JSONInputStream with the specified string and reads a JSONArray
	 *
	 * @param str        the string to read from
	 * @param printError TRUE if exception stack traces should be printed, FALSE otherwise
	 * @return the JSONArray read from the string, or null if there was an exception
	 */
	public static JSONArray readArray(String str, boolean printError) {
		try (JSONInputStream in = new JSONInputStream(str)) {
			return in.readArray();
		} catch (IOException | JSONException e) {
			if (printError)
				e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Opens a new JSONInputStream with the specified InputStream and reads a JSONObject or JSONArray. After reading, the input stream is closed
	 *
	 * @param is the input stream to read from
	 * @return the JSONObject or JSONArray read from the stream, or null if there was an exception
	 * @throws IOException   if there is an exception within the input stream
	 * @throws JSONException if there is a JSON parsing error
	 */
	public static Object readNext(InputStream is) throws IOException, JSONException {
		try (JSONInputStream in = new JSONInputStream(is)) {
			return in.readNext();
		}
	}
	
	/**
	 * Opens a new JSONInputStream with the specified string and reads a JSONObject
	 *
	 * @param str the string to read from
	 * @return the JSONObject or JSONArray read from the stream
	 * @throws IOException   if there is an exception within the input stream
	 * @throws JSONException if there is a JSON parsing error
	 */
	public static Object readNext(String str) throws IOException, JSONException {
		try (JSONInputStream in = new JSONInputStream(str)) {
			return in.readNext();
		}
	}
	
	/**
	 * Opens a new JSONInputStream with the specified InputStream and reads a JSONObject. After reading, the input stream is closed
	 *
	 * @param is the input stream to read from
	 * @return the JSONObject read from the stream, or null if there was an exception
	 * @throws IOException   if there is an exception within the input stream
	 * @throws JSONException if there is a JSON parsing error
	 */
	public static JSONObject readObject(InputStream is) throws IOException, JSONException {
		try (JSONInputStream in = new JSONInputStream(is)) {
			return in.readObject();
		}
	}
	
	/**
	 * Opens a new JSONInputStream with the specified string and reads a JSONObject
	 *
	 * @param str the string to read from
	 * @return the JSONObject read from the string, or null if there was an exception
	 * @throws IOException   if there is an exception within the input stream
	 * @throws JSONException if there is a JSON parsing error
	 */
	public static JSONObject readObject(String str) throws IOException, JSONException {
		try (JSONInputStream in = new JSONInputStream(str)) {
			return in.readObject();
		}
	}
	
	/**
	 * Opens a new JSONInputStream with the specified InputStream and reads a JSONArray. After reading, the input stream is closed
	 *
	 * @param is the input stream to read from
	 * @return the JSONArray read from the stream
	 * @throws IOException   if there is an exception within the input stream
	 * @throws JSONException if there is a JSON parsing error
	 */
	public static JSONArray readArray(InputStream is) throws IOException, JSONException {
		try (JSONInputStream in = new JSONInputStream(is)) {
			return in.readArray();
		}
	}
	
	/**
	 * Opens a new JSONInputStream with the specified string and reads a JSONArray
	 *
	 * @param str the string to read from
	 * @return the JSONArray read from the string
	 * @throws IOException   if there is an exception within the input stream
	 * @throws JSONException if there is a JSON parsing error
	 */
	public static JSONArray readArray(String str) throws IOException, JSONException {
		try (JSONInputStream in = new JSONInputStream(str)) {
			return in.readArray();
		}
	}
	
}
