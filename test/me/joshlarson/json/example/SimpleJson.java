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
package me.joshlarson.json.example;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;

import me.joshlarson.json.JSONArray;
import me.joshlarson.json.JSONException;
import me.joshlarson.json.JSONInputStream;
import me.joshlarson.json.JSONObject;
import me.joshlarson.json.JSONOutputStream;

public class SimpleJson {
	
	public static void main(String [] args) throws IOException, JSONException {
		JSONObject obj = createObject();
		long start = System.nanoTime();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JSONOutputStream out = new JSONOutputStream(baos);
		out.setCompact(true);
		out.writeObject(obj);
		long end = System.nanoTime();
		System.out.print(baos.toString());
		System.out.printf("%n%.6fms%n", (end-start)/1E6);
		start = System.nanoTime();
		System.out.print(obj.toString());
		end = System.nanoTime();
		System.out.printf("%n%.6fms%n", (end-start)/1E6);
		
		String json = obj.toString();
		try (JSONInputStream reader = new JSONInputStream(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)))) {
			start = System.nanoTime();
			Map<String, Object> readObj = reader.readObject();
			end = System.nanoTime();
			for (Entry<String, Object> e : readObj.entrySet()) {
				System.out.println(e.getKey() + ": " + e.getValue());
			}
			System.out.printf("%.6fms%n", (end-start)/1E6);
		}
		out.close();
	}
	
	private static JSONObject createObject() {
		JSONObject obj = new JSONObject();
		obj.put("from", "thephpdev");
		obj.put("text", "This is some long chunk of text!");
		obj.put("time", System.currentTimeMillis());
		obj.put("invalid_double", Double.NaN);
		JSONArray array = new JSONArray();
		array.add(true);
		array.add(1234);
		array.add("testing!");
		array.add(null);
		obj.put("array", array);
		obj.put("my_null", null);
		return obj;
	}
	
}
