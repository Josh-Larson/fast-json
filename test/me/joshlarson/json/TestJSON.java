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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RunWith(JUnit4.class)
public class TestJSON {
	
	@Test
	public void testReadEither() throws IOException, JSONException {
		JSONObject original = new JSONObject();
		original.put("obj", true);
		original.put("array", false);
		Assert.assertEquals(original, JSON.readNext(original.toString(true)));
		Assert.assertEquals(original, JSON.readNext(original.toString(), true));
		Assert.assertEquals(original, JSON.readNext(wrap(original.toString())));
		Assert.assertEquals(original, JSON.readNext(wrap(original.toString()), true));
		
		JSONArray array = new JSONArray();
		array.add("obj");
		array.add(true);
		Assert.assertEquals(array, JSON.readNext(array.toString(true)));
		Assert.assertEquals(array, JSON.readNext(array.toString(), true));
		Assert.assertEquals(array, JSON.readNext(wrap(array.toString())));
		Assert.assertEquals(array, JSON.readNext(wrap(array.toString()), true));
	}
	
	@Test
	public void testReadObject() throws IOException, JSONException {
		JSONObject original = new JSONObject();
		original.put("string", "test");
		Assert.assertEquals(original, JSON.readObject(original.toString()));
		Assert.assertEquals(original, JSON.readObject(original.toString(), true));
		Assert.assertEquals(original, JSON.readObject(wrap(original.toString())));
		Assert.assertEquals(original, JSON.readObject(wrap(original.toString()), true));
	}
	
	@Test
	public void testReadArray() throws IOException, JSONException {
		JSONArray original = new JSONArray();
		original.add("test");
		Assert.assertEquals(original, JSON.readArray(original.toString()));
		Assert.assertEquals(original, JSON.readArray(original.toString(), true));
		Assert.assertEquals(original, JSON.readArray(wrap(original.toString())));
		Assert.assertEquals(original, JSON.readArray(wrap(original.toString()), true));
	}
	
	@Test
	public void testUnicodeRead() throws JSONException, IOException {
		String str = "{\"key1\":\"\\u0041\"}";
		JSONObject obj = JSON.readObject(str);
		Assert.assertNotNull(obj);
		Assert.assertEquals("A", obj.get("key1"));
	}
	
	@Test
	public void testReadObjectString() throws JSONException, IOException {
		JSONObject obj;
		String str;
		str = "{\"key1\":\"value1\",\"key2\":2}";
		obj = JSON.readObject(str);
		Assert.assertNotNull(obj);
		Assert.assertEquals("value1", obj.getString("key1"));
		Assert.assertEquals(2, obj.getInt("key2"));
	}
	
	@Test
	public void testReadArrayString() throws JSONException, IOException {
		JSONArray array;
		String str;
		
		str = "[\"key1\",\"value1\",1.5,2]";
		array = JSON.readArray(str);
		Assert.assertNotNull(array);
		Assert.assertEquals("key1", array.getString(0));
		Assert.assertEquals("value1", array.getString(1));
		Assert.assertEquals(1.5, array.getDouble(2), 1E-7);
		Assert.assertEquals(2, array.getLong(3));
		
		str = "[\"val1\",null,false,true,1.5,2]";
		array = JSON.readArray(wrap(str));
		Assert.assertNotNull(array);
		Assert.assertEquals("val1", array.getString(0));
		Assert.assertNull(array.get(1));
		Assert.assertEquals(false, array.getBoolean(2));
		Assert.assertEquals(true, array.getBoolean(3));
		Assert.assertEquals(1.5, array.getDouble(4), 1E-7);
		Assert.assertEquals(2, array.getLong(5));
	}
	
	@Test(expected=EOFException.class)
	public void testObjectReadError1() throws JSONException, IOException {
		Assert.assertNull(JSON.readObject("{"));
	}
	
	@Test(expected=EOFException.class)
	public void testObjectReadError2() throws JSONException, IOException {
		Assert.assertNull(JSON.readObject("{\"testing\""));
	}
	
	@Test(expected=EOFException.class)
	public void testObjectReadError3() throws JSONException, IOException {
		Assert.assertNull(JSON.readObject("{\"testing\":"));
	}
	
	@Test(expected=EOFException.class)
	public void testObjectReadError4() throws JSONException, IOException {
		Assert.assertNull(JSON.readObject("{\"testing\":\"value\""));
	}
	
	@Test(expected=EOFException.class)
	public void testObjectReadError5() throws JSONException, IOException {
		Assert.assertNull(JSON.readObject("{\"testing\":\"value\","));
	}
	
	@Test
	public void testObjectEmpty() throws JSONException, IOException {
		Assert.assertNotNull(JSON.readObject("{}"));
	}
	
	@Test
	public void testArrayEmpty() throws JSONException, IOException {
		Assert.assertNotNull(JSON.readArray("[]"));
	}
	
	private ByteArrayInputStream wrap(String str) {
		return new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
	}
	
}
