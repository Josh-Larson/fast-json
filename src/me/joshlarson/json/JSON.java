package me.joshlarson.json;

import java.io.ByteArrayInputStream;
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
		JSONInputStream in = new JSONInputStream(is);
		try {
			return in.readObject();
		} catch (IOException e) {
			if (printError)
				e.printStackTrace();
		} catch (JSONException e) {
			if (printError)
				e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				if (printError)
					e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Opens a new JSONInputStream with the specified string and reads a JSONObject
	 * 
	 * @param str the string to read from
	 * @param printError TRUE if exception stack traces should be printed, FALSE otherwise
	 * @return the JSONObject read from the string, or null if there was an exception
	 */
	public static JSONObject readObject(String str, boolean printError) {
		return readObject(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)), printError);
	}
	
	/**
	 * Opens a new JSONInputStream with the specified InputStream and reads a JSONArray. After
	 * reading, the input streams are closed
	 * 
	 * @param is the input stream to read from
	 * @param printError TRUE if exception stack traces should be printed, FALSE otherwise
	 * @return the JSONArray read from the stream, or null if there was an exception
	 */
	public static JSONArray readArray(InputStream is, boolean printError) {
		JSONInputStream in = new JSONInputStream(is);
		try {
			return in.readArray();
		} catch (IOException e) {
			if (printError)
				e.printStackTrace();
		} catch (JSONException e) {
			if (printError)
				e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				if (printError)
					e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Opens a new JSONInputStream with the specified string and reads a JSONArray
	 * 
	 * @param str the string to read from
	 * @param printError TRUE if exception stack traces should be printed, FALSE otherwise
	 * @return the JSONArray read from the string, or null if there was an exception
	 */
	public static JSONArray readArray(String str, boolean printError) {
		return readArray(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)), printError);
	}
	
}
