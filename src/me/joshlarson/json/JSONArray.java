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
	
	@Override
	public int size() {
		return array.size();
	}
	
	@Override
	public void clear() {
		array.clear();
	}
	
	@Override
	public Object remove(int index) {
		return array.remove(index);
	}
	
	@Override
	public void add(int index, Object o) {
		array.add(index, o);
	}
	
	@Override
	public boolean add(Object o) {
		return array.add(o);
	}
	
	@Override
	public boolean addAll(Collection<? extends Object> c) {
		return array.addAll(c);
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends Object> c) {
		return array.addAll(index, c);
	}
	
	@Override
	public boolean containsAll(Collection<?> c) {
		return array.containsAll(c);
	}
	
	@Override
	public int indexOf(Object o) {
		return array.indexOf(o);
	}
	
	@Override
	public boolean isEmpty() {
		return array.isEmpty();
	}
	
	@Override
	public int lastIndexOf(Object o) {
		return array.lastIndexOf(o);
	}
	
	@Override
	public boolean remove(Object o) {
		return array.remove(o);
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		return array.removeAll(c);
	}
	
	@Override
	public boolean retainAll(Collection<?> c) {
		return array.retainAll(c);
	}
	
	@Override
	public Object set(int index, Object element) {
		return array.set(index, element);
	}
	
	@Override
	public List<Object> subList(int fromIndex, int toIndex) {
		return array.subList(fromIndex, toIndex);
	}
	
	@Override
	public Object[] toArray() {
		return array.toArray();
	}
	
	@Override
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
	 * Gets the object at the specified index from the array. The returned object will always be one
	 * of the supported JSON types: JSONObject, JSONArray, Number, Boolean, String, or null
	 * 
	 * @param index the index to retrieve over the interval [0, size())
	 * @return the object at the specified index
	 */
	@Override
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
	@Override
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
	@Override
	public String toString() {
		ByteArrayOutputStream str = new ByteArrayOutputStream();
		try (JSONOutputStream stream = new JSONOutputStream(str)) {
			stream.writeArray(this);
		} catch (IOException e) {
			return "Failed: " + e.getMessage();
		}
		return str.toString();
	}
	
}
