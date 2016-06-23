package me.joshlarson.json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
