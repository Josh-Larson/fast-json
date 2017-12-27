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
 * Provides convenience methods for stream operations that do automatic resource cleanup
 * 
 * @author josh
 */
public class JSON {
	
	/**
	 * Opens a new JSONInputStream with the specified InputStream and reads a JSONObject. After
	 * reading, the input streams are closed
	 * 
	 * @param is the input stream to read from
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
	 * Opens a new JSONInputStream with the specified InputStream and reads a JSONObject. After
	 * reading, the input streams are closed
	 * 
	 * @param is the input stream to read from
	 * @return the JSONObject read from the stream, or null if there was an exception
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
	 * @param printError TRUE if exception stack traces should be printed, FALSE otherwise
	 * @return the JSONObject read from the string, or null if there was an exception
	 */
	public static JSONObject readObject(String str, boolean printError) {
		try (JSONInputStream in = new JSONInputStream(str)) {
			return in.readObject();
		} catch (EOFException | JSONException e) {
			if (printError)
				e.printStackTrace();
		} catch (IOException e) {
			// Suppress - IOException shouldn't be possible
		}
		return null;
	}
	
	/**
	 * Opens a new JSONInputStream with the specified string and reads a JSONObject
	 * 
	 * @param str the string to read from
	 * @return the JSONObject read from the string, or null if there was an exception
	 */
	public static JSONObject readObject(String str) throws IOException, JSONException {
		try (JSONInputStream in = new JSONInputStream(str)) {
			return in.readObject();
		}
	}
	
	/**
	 * Opens a new JSONInputStream with the specified InputStream and reads a JSONArray. After
	 * reading, the input stream is closed
	 * 
	 * @param is the input stream to read from
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
	 * Opens a new JSONInputStream with the specified InputStream and reads a JSONArray. After
	 * reading, the input stream is closed
	 * 
	 * @param is the input stream to read from
	 * @return the JSONArray read from the stream
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
	 * Opens a new JSONInputStream with the specified string and reads a JSONArray
	 * 
	 * @param str the string to read from
	 * @return the JSONArray read from the string
	 */
	public static JSONArray readArray(String str) throws IOException, JSONException {
		try (JSONInputStream in = new JSONInputStream(str)) {
			return in.readArray();
		}
	}
	
}
