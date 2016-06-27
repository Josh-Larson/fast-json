package me.joshlarson.json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * This class contains a list of values, which can be one of the following types: JSONObject,
 * JSONArray, Number, Boolean, String, or null
 * 
 * @author josh
 */
public class JSONArray implements List<Object>, Iterable<Object> {
	
	private final ArrayList<Object> array; // Specifically ArrayList for null value support
	
	public JSONArray() {
		array = new ArrayList<Object>();
	}
	
	public int size() {
		return array.size();
	}
	
	public void clear() {
		array.clear();
	}
	
	public Object remove(int index) {
		if (index < 0 || index >= array.size())
			throw new IndexOutOfBoundsException("Specified index " + index + " is out of range! [0, " + size() + ")");
		return array.remove(index);
	}
	
	public void add(int index, Object o) {
		if (o instanceof JSONObject || o instanceof JSONArray)
			array.add(index, o);
		else if (o instanceof Number || o instanceof Boolean)
			array.add(index, o);
		else if (o instanceof String)
			array.add(index, o);
		else if (o == null)
			array.add(index, null);
		else
			throw new IllegalArgumentException("Object must be of type: JSONObject, JSONArray, Number, Boolean, String, or null!");
	}
	
	public boolean add(Object o) {
		if (o instanceof JSONObject)
			add((JSONObject) o);
		else if (o instanceof JSONArray)
			add((JSONArray) o);
		else if (o instanceof Number)
			add((Number) o);
		else if (o instanceof Boolean)
			add((Boolean) o);
		else if (o instanceof String)
			add((String) o);
		else if (o == null)
			addNull();
		else
			throw new IllegalArgumentException("Object must be of type: JSONObject, JSONArray, Number, Boolean, String, or null!");
		return true;
	}
	
	public boolean addAll(Collection<? extends Object> c) {
		ensureCapacity(size() + c.size());
		for (Object o : c) {
			add(o);
		}
		return true;
	}
	
	public boolean addAll(int index, Collection<? extends Object> c) {
		ensureCapacity(size() + c.size());
		int i = index;
		for (Object o : c) {
			add(i++, o);
		}
		return true;
	}
	
	public boolean containsAll(Collection<?> c) {
		return array.containsAll(c);
	}
	
	public void ensureCapacity(int minCapacity) {
		array.ensureCapacity(minCapacity);
	}
	
	public int indexOf(Object o) {
		return array.indexOf(o);
	}
	
	public boolean isEmpty() {
		return array.isEmpty();
	}
	
	public int lastIndexOf(Object o) {
		return array.lastIndexOf(o);
	}
	
	public boolean remove(Object o) {
		return array.remove(o);
	}
	
	public boolean removeAll(Collection<?> c) {
		return array.removeAll(c);
	}
	
	public boolean retainAll(Collection<?> c) {
		return array.retainAll(c);
	}
	
	public Object set(int index, Object element) {
		return array.set(index, element);
	}
	
	public List<Object> subList(int fromIndex, int toIndex) {
		return array.subList(fromIndex, toIndex);
	}
	
	public Object[] toArray() {
		return array.toArray();
	}
	
	public <T> T[] toArray(T[] a) {
		return array.toArray(a);
	}
	
	@Override
	public ListIterator<Object> listIterator() {
		return array.listIterator();
	}
	
	@Override
	public ListIterator<Object> listIterator(int index) {
		return array.listIterator(index);
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
