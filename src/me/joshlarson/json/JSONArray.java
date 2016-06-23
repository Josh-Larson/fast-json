package me.joshlarson.json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class JSONArray implements Iterable<Object> {
	
	private final ArrayList<Object> array; // Specifically ArrayList for null value support
	
	public JSONArray() {
		array = new ArrayList<Object>();
	}
	
	/**
	 * @return the size of the array
	 */
	public int size() {
		return array.size();
	}
	
	/**
	 * Clears the internal array of all elements
	 */
	public void clear() {
		array.clear();
	}
	
	/**
	 * Removes the specified index from the array
	 * @param index the index to remove
	 */
	public Object remove(int index) {
		if (index < 0 || index >= array.size())
			throw new IndexOutOfBoundsException("Specified index " + index + " is out of range! [0, "+size()+")");
		return array.remove(index);
	}
	
	/**
	 * Adds a JSONObject to the array
	 * 
	 * @param obj the JSONObject to add
	 */
	public void add(JSONObject obj) {
		array.add(obj);
	}
	
	/**
	 * Adds a JSONArray to the array
	 * 
	 * @param array the JSONArray to add
	 */
	public void add(JSONArray array) {
		this.array.add(array);
	}
	
	/**
	 * Adds a number to the array
	 * 
	 * @param n the number to add
	 */
	public void add(Number n) {
		array.add(n);
	}
	
	/**
	 * Adds a boolean to the array
	 * 
	 * @param b the boolean to add
	 */
	public void add(Boolean b) {
		array.add(b);
	}
	
	/**
	 * Adds a string to the array
	 * 
	 * @param str the string to add
	 */
	public void add(String str) {
		array.add(str);
	}
	
	/**
	 * Adds a null value to the array
	 */
	public void addNull() {
		array.add(null);
	}
	
	/**
	 * Gets the object at the specified index from the array. The returned object will always be one
	 * of the supported JSON types: JSONObject, JSONArray, Number, Boolean, String, or null
	 * 
	 * @param index the index to retrieve over the interval [0, size())
	 * @return the object at the specified index
	 */
	public Object get(int index) {
		return array.get(index);
	}
	
	/**
	 * Determines whether or not the specified object exists inside this array
	 * 
	 * @param o the object to search for within the array
	 * @return TRUE if the object exists, FALSE otherwise
	 */
	public boolean contains(Object o) {
		return array.contains(o);
	}
	
	@Override
	public Iterator<Object> iterator() {
		return array.iterator();
	}
	
	/**
	 * Returns a JSON string (RFC 4627) containing this array
	 * 
	 * @return a JSON string compatible with RFC 4627
	 */
	public String toString() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			new JSONOutputStream(baos).writeArray(this);
		} catch (IOException e) {
			return "Failed: " + e.getMessage();
		}
		return baos.toString();
	}
	
}
