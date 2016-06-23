package me.joshlarson.json.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import me.joshlarson.json.JSONException;
import me.joshlarson.json.JSONInputStream;
import me.joshlarson.json.JSONObject;
import me.joshlarson.json.JSONOutputStream;

public class BigFileTest {
	
	public static void main(String [] args) throws IOException, JSONException {
		File file = File.createTempFile("BigFileJsonTest", ".txt");
		file.deleteOnExit();
		
		long start = System.nanoTime();
		testOutputSpeed(file);
		long end = System.nanoTime();
		System.out.printf("Output: %.6fms%n", (end-start)/1E6);
		
		start = System.nanoTime();
		testInputSpeed(file);
		end = System.nanoTime();
		System.out.printf("Input:  %.6fms%n", (end-start)/1E6);
	}
	
	private static void testOutputSpeed(File file) throws IOException {
		JSONObject obj = createObject();
		try (JSONOutputStream out = new JSONOutputStream(new FileOutputStream(file))) {
			out.writeObject(obj);
		}
	}
	
	private static void testInputSpeed(File file) throws IOException, JSONException {
		for (int i = 0; i < 10; ++i) {
			try (JSONInputStream in = new JSONInputStream(new FileInputStream(file))) {
				in.readObject();
			}
		}
	}
	
	private static JSONObject createObject() {
		JSONObject obj = new JSONObject();
		obj.put("data", getLargeString());
		return obj;
	}
	
	private static String getLargeString() {
		byte [] data = new byte[190*1024*1024];
		Random r = new Random();
		for (int i = 0; i < data.length; ++i) {
			data[i] = (byte) ('a' + r.nextInt(26));
		}
		return new String(data, StandardCharsets.US_ASCII);
	}
	
}
