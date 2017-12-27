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
public class TestBasicObject {
	
	@Test
	public void testBasicNumbers() throws IOException, JSONException {
		JSONObject obj = new JSONObject();
		obj.put("positive", 1234);
		obj.put("negative", -1234);
		obj.put("exponent", 10E10);
		obj.put("decimal", 12.34);
		obj.put("pos_infinity", Double.POSITIVE_INFINITY);
		obj.put("neg_infinity", Double.NEGATIVE_INFINITY);
		obj.put("nan", Double.NaN);
		JSONObject out;
		try (JSONInputStream in = new JSONInputStream(obj.toString())) {
			out = in.readObject();
		}
		Assert.assertEquals(1234L, out.get("positive"));
		Assert.assertEquals((-1234L), out.get("negative"));
		Assert.assertEquals(10E10, (Double) out.get("exponent"), 1E-5);
		Assert.assertEquals(12.34, (Double) out.get("decimal"), 1E-5);
		Assert.assertEquals(0L, out.get("pos_infinity"));
		Assert.assertEquals(0L, out.get("neg_infinity"));
		Assert.assertEquals(0L, out.get("nan"));
	}
	
	@Test
	public void testBasicStrings() throws IOException, JSONException {
		JSONObject obj = new JSONObject();
		obj.put("no_punct", "NoPunctuation");
		obj.put("punct", ".!,?;:}{][@#$%^&*)(-_+='`~|/><");
		obj.put("quotes", "TestingQuote\"Does this work\\?");
		obj.put("non_ascii", "Testing null, carriage and lines: \0..\r..\n..");
		JSONObject out;
		String str = obj.toString();
		Assert.assertTrue("String must be entirely ascii", isAscii(str));
		try (JSONInputStream in = new JSONInputStream(str)) {
			out = in.readObject();
		}
		testString(obj, out, "no_punct");
		testString(obj, out, "punct");
		testString(obj, out, "quotes");
		testString(obj, out, "non_ascii");
	}
	
	@Test
	public void testOperations() {
		JSONObject obj = new JSONObject();
		Assert.assertFalse(obj.containsKey("test_key"));
		Assert.assertNull(obj.get("test_key"));
		Assert.assertEquals(0, obj.size());
		obj.put("test_key", false);
		Assert.assertTrue(obj.containsKey("test_key"));
		Assert.assertEquals(false, obj.get("test_key"));
		Assert.assertEquals(1, obj.size());
		obj.remove("test_key");
		Assert.assertFalse(obj.containsKey("test_key"));
		Assert.assertNull(obj.get("test_key"));
		Assert.assertEquals(0, obj.size());
		obj.put("test_key", false);
		Assert.assertTrue(obj.containsKey("test_key"));
		Assert.assertEquals(false, obj.get("test_key"));
		Assert.assertEquals(1, obj.size());
		obj.clear();
		Assert.assertFalse(obj.containsKey("test_key"));
		Assert.assertNull(obj.get("test_key"));
		Assert.assertEquals(0, obj.size());
	}
	
	@Test
	public void testGet() {
		JSONObject obj = new JSONObject();
		JSONObject subObj = new JSONObject();
		JSONArray subArray = new JSONArray();
		obj.put("object", subObj);
		obj.put("array", subArray);
		obj.put("int", 123);
		obj.put("long", 123E15);
		obj.put("float", 12.3f);
		obj.put("double", 12.3);
		obj.put("bool", true);
		obj.put("str", "testing");
		obj.put("null", null);
		Assert.assertEquals(subObj, obj.getObject("object"));
		Assert.assertEquals(subArray, obj.getArray("array"));
		Assert.assertEquals(123, obj.getInt("int"));
		Assert.assertEquals((long) 123E15, obj.getLong("long"));
		Assert.assertEquals(12.3f, obj.getFloat("float"), 1E-5);
		Assert.assertEquals(12.3, obj.getDouble("double"), 1E-5);
		Assert.assertEquals(true, obj.getBoolean("bool"));
		Assert.assertEquals("testing", obj.getString("str"));
		Assert.assertNull(obj.get("null"));
	}
	
	private void testString(JSONObject expected, JSONObject actual, String key) {
		Assert.assertEquals(expected.get(key), actual.get(key));
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
