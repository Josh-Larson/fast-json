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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * This class contains key-value pairs where the key is a string and the value is one of the
 * following types: JSONObject, JSONArray, Number, Boolean, String, or null
 * 
 * @author josh
 */
public class JSONObject implements Map<String, Object> {
	
	private final Map<String, Object> attributes;
	
	public JSONObject() {
		this(new HashMap<String, Object>());
	}
	
	public JSONObject(Map<String, Object> map) {
		this.attributes = map;
	}
	
	/**
	 * Returns the number of key-value pairs in the map
	 * 
	 * @return the number of key-value pairs in the map
	 */
	@Override
	public int size() {
		return attributes.size();
	}
	
	/**
	 * Clears the internal map of all key-value pairs
	 */
	@Override
	public void clear() {
		attributes.clear();
	}
	
	/**
	 * Removes the key-value pair from the map
	 * 
	 * @param key the key to remove
	 * @return the previous value for this key
	 */
	public Object remove(String key) {
		return attributes.remove(Objects.requireNonNull(key, "key"));
	}
	
	@Override
	public boolean containsKey(Object key) {
		return attributes.containsKey(key);
	}
	
	@Override
	public boolean containsValue(Object value) {
		return attributes.containsValue(value);
	}
	
	@Override
	public Object get(Object key) {
		return attributes.get(key);
	}
	
	@Override
	public boolean isEmpty() {
		return attributes.isEmpty();
	}
	
	@Override
	public Object put(String key, Object value) {
		return attributes.put(key, value);
	}
	
	/**
	 * Puts the value into this object using a layered syntax. For example: "data.username" looks for the "data" JSONObject, then puts the value for
	 * the key "username". This is a recursive call, so it put to any depth.
	 *
	 * @param key the layered key
	 * @param value the value to add
	 * @return the previous value at that location, or null if none existed
	 */
	public Object putLayered(String key, Object value) {
		int dotIndex = key.indexOf('.');
		if (dotIndex == -1)
			return put(key, value);
		String firstStr = key.substring(0, dotIndex);
		JSONObject obj = getObject(firstStr);
		if (obj == null)
			put(firstStr, obj = new JSONObject());
		return obj.putLayered(key.substring(dotIndex+1), value);
	}
	
	@Override
	public void putAll(Map<? extends String, ?> m) {
		for (Entry<? extends String, ?> e : m.entrySet()) {
			put(e.getKey(), e.getValue());
		}
	}
	
	@Override
	public Object remove(Object key) {
		return attributes.remove(key);
	}
	
	@Override
	public Set<Entry<String, Object>> entrySet() {
		return attributes.entrySet();
	}
	
	@Override
	public Set<String> keySet() {
		return attributes.keySet();
	}
	
	@Override
	public Collection<Object> values() {
		return attributes.values();
	}
	
	@Override
	public boolean equals(Object o) {
		return attributes.equals(o);
	}
	
	@Override
	public int hashCode() {
		return attributes.hashCode();
	}
	
	public Object get(String key) {
		return attributes.get(Objects.requireNonNull(key, "key"));
	}
	
	/**
	 * Gets the value from this object using a layered syntax. For example: "data.username" looks for the "data" JSONObject, then gets the value for
	 * the key "username". This is a recursive call, so it get at any depth.
	 *
	 * @param key the layered key
	 * @return the value at that location, or null if none exists
	 */
	public Object getLayered(String key) {
		int dotIndex = key.indexOf('.');
		if (dotIndex == -1)
			return get(key);
		String firstStr = key.substring(0, dotIndex);
		JSONObject obj = getObject(firstStr);
		if (obj == null)
			return null;
		return obj.getLayered(key.substring(dotIndex+1));
	}
	
	/**
	 * Gets the value associated with the specified key. The value is casted to a JSONObject
	 * internally
	 * 
	 * @param key the key for the map
	 * @return the JSONObject associated with the specified key
	 * @throws NullPointerException if the specified key is null
	 * @throws ClassCastException if the object is not a JSONObject
	 */
	public JSONObject getObject(String key) {
		return (JSONObject) get(key);
	}
	
	/**
	 * Gets the value associated with the specified key. The value is casted to a JSONArray
	 * internally
	 * 
	 * @param key the key for the map
	 * @return the JSONArray associated with the specified key
	 * @throws NullPointerException if the specified key is null
	 * @throws ClassCastException if the object is not a JSONArray
	 */
	public JSONArray getArray(String key) {
		return (JSONArray) get(key);
	}
	
	/**
	 * Gets the value associated with the specified key. The value is casted to a int internally
	 * 
	 * @param key the key for the map
	 * @return the int associated with the specified key
	 * @throws NullPointerException if the specified key is null or if the value is null
	 */
	public int getInt(String key) {
		return ((Number) get(key)).intValue();
	}
	
	/**
	 * Gets the value associated with the specified key. The value is casted to a long internally
	 * 
	 * @param key the key for the map
	 * @return the long associated with the specified key
	 * @throws NullPointerException if the specified key is null or if the value is null
	 */
	public long getLong(String key) {
		return ((Number) get(key)).longValue();
	}
	
	/**
	 * Gets the value associated with the specified key. The value is casted to a float internally
	 * 
	 * @param key the key for the map
	 * @return the float associated with the specified key
	 * @throws NullPointerException if the specified key is null or if the value is null
	 */
	public float getFloat(String key) {
		return ((Number) get(key)).floatValue();
	}
	
	/**
	 * Gets the value associated with the specified key. The value is casted to a double internally
	 * 
	 * @param key the key for the map
	 * @return the double associated with the specified key
	 * @throws NullPointerException if the specified key is null or if the value is null
	 */
	public double getDouble(String key) {
		return ((Number) get(key)).doubleValue();
	}
	
	/**
	 * Gets the value associated with the specified key. The value is casted to a boolean internally
	 * 
	 * @param key the key for the map
	 * @return the boolean associated with the specified key
	 * @throws NullPointerException if the specified key is null or if the value is null
	 * @throws ClassCastException if the object is not a boolean
	 */
	public boolean getBoolean(String key) {
		return (boolean) get(key);
	}
	
	/**
	 * Gets the value associated with the specified key. The value is casted to a String internally
	 * 
	 * @param key the key for the map
	 * @return the String associated with the specified key
	 * @throws NullPointerException if the specified key is null
	 * @throws ClassCastException if the object is not a String
	 */
	public String getString(String key) {
		return (String) get(key);
	}
	
	/**
	 * Returns a JSON string (RFC 4627) containing this object
	 * 
	 * @return a JSON string compatible with RFC 4627
	 */
	@Override
	public String toString() {
		return toString(false);
	}
	
	/**
	 * Returns a JSON string (RFC 4627) containing this object
	 * 
	 * @param compact create the compact version of this object
	 * @return a JSON string compatible with RFC 4627
	 */
	public String toString(boolean compact) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (JSONOutputStream stream = new JSONOutputStream(baos)) {
			stream.setCompact(compact);
			stream.writeObject(this);
		} catch (IOException e) {
			return "Failed: " + e.getMessage();
		}
		return baos.toString();
	}
	
}
