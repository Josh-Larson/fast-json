package me.joshlarson.json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This class contains key-value pairs where the key is a string and the value is one of the
 * following types: JSONObject, JSONArray, Number, Boolean, String, or null
 * 
 * @author josh
 */
public class JSONObject {
	
	private final Map<String, Object> attributes;
	
	public JSONObject() {
		attributes = new LinkedHashMap<String, Object>();
	}
	
	/**
	 * Returns the number of key-value pairs in the map
	 * 
	 * @return the number of key-value pairs in the map
	 */
	public int size() {
		return attributes.size();
	}
	
	/**
	 * Clears the internal map of all key-value pairs
	 */
	public void clear() {
		attributes.clear();
	}
	
	/**
	 * Removes the key-value pair from the map
	 * 
	 * @param key the key to remove
	 */
	public void remove(String key) {
		if (key == null)
			throw new NullPointerException("Key cannot be null!");
		
		attributes.remove(key);
	}
	
	/**
	 * Puts a JSONObject into the map with the specified key
	 * 
	 * @param key the key for the map
	 * @param value the value associated with the specified key
	 * @throws NullPointerException if the specified key is null
	 */
	public void put(String key, JSONObject value) {
		if (key == null)
			throw new NullPointerException("Key cannot be null!");
		
		attributes.put(key, value);
	}
	
	/**
	 * Puts a JSONArray into the map with the specified key
	 * 
	 * @param key the key for the map
	 * @param value the value associated with the specified key
	 * @throws NullPointerException if the specified key is null
	 */
	public void put(String key, JSONArray value) {
		if (key == null)
			throw new NullPointerException("Key cannot be null!");
		
		attributes.put(key, value);
	}
	
	/**
	 * Puts a number into the map with the specified key
	 * 
	 * @param key the key for the map
	 * @param value the value associated with the specified key
	 * @throws NullPointerException if the specified key is null
	 */
	public void put(String key, Number value) {
		if (key == null)
			throw new NullPointerException("Key cannot be null!");
		
		attributes.put(key, value);
	}
	
	/**
	 * Puts a boolean into the map with the specified key
	 * 
	 * @param key the key for the map
	 * @param value the value associated with the specified key
	 * @throws NullPointerException if the specified key is null
	 */
	public void put(String key, Boolean value) {
		if (key == null)
			throw new NullPointerException("Key cannot be null!");
		
		attributes.put(key, value);
	}
	
	/**
	 * Puts a string into the map with the specified key
	 * 
	 * @param key the key for the map
	 * @param value the value associated with the specified key
	 * @throws NullPointerException if the specified key is null
	 */
	public void put(String key, String value) {
		if (key == null)
			throw new NullPointerException("Key cannot be null!");
		
		attributes.put(key, value);
	}
	
	/**
	 * Puts a null value into the map with the specified key
	 * 
	 * @param key the key for the map
	 * @param value the value associated with the specified key
	 * @throws NullPointerException if the specified key is null
	 */
	public void putNull(String key) {
		if (key == null)
			throw new NullPointerException("Key cannot be null!");
		
		attributes.put(key, null);
	}
	
	/**
	 * Gets the value associated with the specified key. The value will always be a supported JSON
	 * object: JSONObject, JSONArray, Number, Boolean, String, or null
	 * 
	 * @param key the key for the map
	 * @return the value associated with the specified key
	 * @throws NullPointerException if the specified key is null
	 */
	public Object get(String key) {
		if (key == null)
			throw new NullPointerException("Key cannot be null!");
		
		return attributes.get(key);
	}
	
	/**
	 * Gets the value associated with the specified key. The value is casted to a JSONObject
	 * internally
	 * 
	 * @param key the key for the map
	 * @return the JSONObject associated with the specified key
	 * @throws NullPointerException if the specified key is null
	 * @throws InvalidClassException if the object is not a JSONObject
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
	 * @throws InvalidClassException if the object is not a JSONArray
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
	 * @throws InvalidClassException if the object is not a boolean
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
	 * @throws InvalidClassException if the object is not a String
	 */
	public String getString(String key) {
		return (String) get(key);
	}
	
	/**
	 * Determines whether or not the specified key exists within the map
	 * 
	 * @param key the key to check for
	 * @return TRUE if the key exists, FALSE otherwise
	 */
	public boolean containsKey(String key) {
		return attributes.containsKey(key);
	}
	
	/**
	 * Returns a {@link Set} view of the mappings contained in this map. The set is backed by the
	 * map, so changes to the map are reflected in the set, and vice-versa. If the map is modified
	 * while an iteration over the set is in progress (except through the iterator's own
	 * <tt>remove</tt> operation, or through the <tt>setValue</tt> operation on a map entry returned
	 * by the iterator) the results of the iteration are undefined. The set supports element
	 * removal, which removes the corresponding mapping from the map, via the
	 * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and
	 * <tt>clear</tt> operations. It does not support the <tt>add</tt> or <tt>addAll</tt>
	 * operations.
	 *
	 * @return a set view of the mappings contained in this map
	 */
	public Set<Entry<String, Object>> entrySet() {
		return attributes.entrySet();
	}
	
	/**
	 * Returns a {@link Set} view of the keys contained in this map. The set is backed by the map,
	 * so changes to the map are reflected in the set, and vice-versa. If the map is modified while
	 * an iteration over the set is in progress (except through the iterator's own <tt>remove</tt>
	 * operation), the results of the iteration are undefined. The set supports element removal,
	 * which removes the corresponding mapping from the map, via the <tt>Iterator.remove</tt>,
	 * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt> operations.
	 * It does not support the <tt>add</tt> or <tt>addAll</tt> operations.
	 *
	 * @return a set view of the keys contained in this map
	 */
	public Set<String> keySet() {
		return attributes.keySet();
	}
	
	/**
	 * Returns a JSON string (RFC 4627) containing this object
	 * 
	 * @return a JSON string compatible with RFC 4627
	 */
	public String toString() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			new JSONOutputStream(baos).writeObject(this);
		} catch (IOException e) {
			return "Failed: " + e.getMessage();
		}
		return baos.toString();
	}
	
}
