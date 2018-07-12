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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TestBasicArray {
	
	@Test
	public void testBasicNumbers() throws IOException, JSONException {
		JSONArray array = new JSONArray();
		array.add(1234);
		array.add(-1234);
		array.add(10E10);
		array.add(12.34);
		array.add(Double.POSITIVE_INFINITY);
		array.add(Double.NEGATIVE_INFINITY);
		array.add(Double.NaN);
		Assert.assertEquals(7, array.size());
		JSONArray out;
		try (JSONInputStream in = new JSONInputStream(array.toString())) {
			out = new JSONArray(in.readArray());
		}
		Assert.assertEquals(7, out.size());
		Assert.assertEquals(1234L, out.get(0));
		Assert.assertEquals((-1234L), out.get(1));
		Assert.assertEquals(10E10, (Double) out.get(2), 1E-5);
		Assert.assertEquals(12.34, (Double) out.get(3), 1E-5);
		Assert.assertEquals(0L, out.get(4));
		Assert.assertEquals(0L, out.get(5));
		Assert.assertEquals(0L, out.get(6));
	}
	
	@Test
	public void testBasicStrings() throws IOException, JSONException {
		JSONArray array = new JSONArray();
		array.add("NoPunctuation");
		array.add(".!,?;:}{][@#$%^&*)(-_+='`~|/><");
		array.add("TestingQuote\"Does this work\\?");
		array.add("Testing null, carriage and lines: \0..\r..\n..");
		JSONArray out;
		String str = array.toString();
		Assert.assertEquals(4, array.size());
		Assert.assertTrue("String must be entirely ascii", isAscii(str));
		try (JSONInputStream in = new JSONInputStream(str)) {
			out = new JSONArray(in.readArray());
		}
		Assert.assertEquals(4, out.size());
		for (int i = 0; i < 4; i++)
			Assert.assertEquals(array.get(i), out.get(i));
	}
	
	@Test
	public void testOperations() {
		JSONArray array = new JSONArray();
		Assert.assertEquals(0, array.size());
		array.add(false);
		Assert.assertTrue(array.contains(false));
		Assert.assertEquals(false, array.get(0));
		Assert.assertEquals(1, array.size());
		array.remove(0);
		Assert.assertFalse(array.contains(false));
		Assert.assertEquals(0, array.size());
		array.add(false);
		Assert.assertTrue(array.contains(false));
		Assert.assertEquals(false, array.get(0));
		Assert.assertEquals(1, array.size());
		array.clear();
		Assert.assertFalse(array.contains(false));
		Assert.assertEquals(0, array.size());
	}
	
	@Test
	public void testGet() {
		JSONArray array = new JSONArray();
		JSONObject subObj = new JSONObject();
		JSONArray subArray = new JSONArray();
		array.add(subObj);
		array.add(subArray);
		array.add(123);
		array.add(123E15);
		array.add(12.3f);
		array.add(12.3);
		array.add(true);
		array.add("testing");
		array.add(null);
		Assert.assertEquals(subObj, array.getObject(0));
		Assert.assertEquals(subArray, array.getArray(1));
		Assert.assertEquals(123, array.getInt(2));
		Assert.assertEquals((long) 123E15, array.getLong(3));
		Assert.assertEquals(12.3f, array.getFloat(4), 1E-5);
		Assert.assertEquals(12.3, array.getDouble(5), 1E-5);
		Assert.assertEquals(true, array.getBoolean(6));
		Assert.assertEquals("testing", array.getString(7));
		Assert.assertNull(array.get(8));
	}
	
	private boolean isAscii(String str) {
		char c;
		for (int i = 0; i < str.length(); ++i) {
			c = str.charAt(i);
			if (c != '\n' && (c < 32 || c > 126))
				return false;
		}
		return true;
	}
	
}
