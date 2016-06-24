package me.joshlarson.json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class contains a list of values, which can be one of the following types: JSONObject,
 * JSONArray, Number, Boolean, String, or null
 * 
 * @author josh
 */
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
	 * 
	 * @param index the index to remove
	 */
	public Object remove(int index) {
		if (index < 0 || index >= array.size())
			throw new IndexOutOfBoundsException("Specified index " + index + " is out of range! [0, " + size() + ")");
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
	 * Gets the object at the specified index from the array. The returned object is casted to a
	 * JSONObject
	 * 
	 * @param index the index to retrieve over the interval [0, size())
	 * @return the object at the specified index
	 * @throws ClassCastException if the object is not a JSONObject
	 */
	public JSONObject getObject(int index) {
		return (JSONObject) get(index);
	}
	
	/**
	 * Gets the object at the specified index from the array. The returned object is casted to a
	 * JSONArray
	 * 
	 * @param index the index to retrieve over the interval [0, size())
	 * @return the array at the specified index
	 * @throws ClassCastException if the object is not a JSONArray
	 */
	public JSONArray getArray(int index) {
		return (JSONArray) get(index);
	}
	
	/**
	 * Gets the object at the specified index from the array. The returned object is casted to a int
	 * 
	 * @param index the index to retrieve over the interval [0, size())
	 * @return the int at the specified index
	 * @throws NullPointerException if the object is null
	 */
	public int getInt(int index) {
		return ((Number) get(index)).intValue();
	}
	
	/**
	 * Gets the object at the specified index from the array. The returned object is casted to a
	 * long
	 * 
	 * @param index the index to retrieve over the interval [0, size())
	 * @return the long at the specified index
	 * @throws NullPointerException if the object is null
	 */
	public long getLong(int index) {
		return ((Number) get(index)).longValue();
	}
	
	/**
	 * Gets the object at the specified index from the array. The returned object is casted to a
	 * float
	 * 
	 * @param index the index to retrieve over the interval [0, size())
	 * @return the float at the specified index
	 * @throws NullPointerException if the object is null
	 */
	public float getFloat(int index) {
		return ((Number) get(index)).floatValue();
	}
	
	/**
	 * Gets the object at the specified index from the array. The returned object is casted to a
	 * double
	 * 
	 * @param index the index to retrieve over the interval [0, size())
	 * @return the double at the specified index
	 * @throws NullPointerException if the object is null
	 */
	public double getDouble(int index) {
		return ((Number) get(index)).doubleValue();
	}
	
	/**
	 * Gets the object at the specified index from the array. The returned object is casted to a
	 * boolean
	 * 
	 * @param index the index to retrieve over the interval [0, size())
	 * @return the boolean at the specified index
	 * @throws NullPointerException if the object is null
	 * @throws ClassCastException if the object is not a boolean
	 */
	public boolean getBoolean(int index) {
		return (boolean) get(index);
	}
	
	/**
	 * Gets the object at the specified index from the array. The returned object is casted to a
	 * String
	 * 
	 * @param index the index to retrieve over the interval [0, size())
	 * @return the string at the specified index
	 * @throws ClassCastException if the object is not a String
	 */
	public String getString(int index) {
		return (String) get(index);
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
