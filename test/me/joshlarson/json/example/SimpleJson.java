package me.joshlarson.json.example;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map.Entry;

import me.joshlarson.json.JSONArray;
import me.joshlarson.json.JSONException;
import me.joshlarson.json.JSONObject;
import me.joshlarson.json.JSONInputStream;
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
			JSONObject readObj = reader.readObject();
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
		array.addNull();
		obj.put("array", array);
		obj.putNull("my_null");
		return obj;
	}
	
}
