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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Random;

import me.joshlarson.json.JSONException;
import me.joshlarson.json.JSONInputStream;
import me.joshlarson.json.JSONObject;
import me.joshlarson.json.JSONOutputStream;

public class BigFileTest {
	
	public static void main(String [] args) throws IOException, JSONException, InterruptedException {
		byte [] bigArrayData = Files.readAllBytes(new File("testdata/json-bigarray.json").toPath());
		byte [] bigStringData = Files.readAllBytes(new File("testdata/json-bigstring.json").toPath());
		JSONObject obj;
		long start, end;
		
		System.out.println("Running...");
		start = System.nanoTime();
		obj = testInputSpeed(bigArrayData);
		end = System.nanoTime();
		System.out.printf("Input-bigarray:   %.6fms%n", (end-start)/1E6);
		start = System.nanoTime();
		testOutputSpeed(obj);
		end = System.nanoTime();
		System.out.printf("Output-bigarray:  %.6fms%n", (end-start)/1E6);
		
		start = System.nanoTime();
		obj = testInputSpeed(bigStringData);
		end = System.nanoTime();
		System.out.printf("Input-bigstring:  %.6fms%n", (end-start)/1E6);
		start = System.nanoTime();
		testOutputSpeed(obj);
		end = System.nanoTime();
		System.out.printf("Output-bigstring: %.6fms%n", (end-start)/1E6);
	}
	
	private static void testOutputSpeed(JSONObject obj) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (JSONOutputStream out = new JSONOutputStream(baos)) {
			out.writeObject(obj);
		}
	}
	
	private static JSONObject testInputSpeed(byte [] data) throws IOException, JSONException {
		JSONObject obj = null;
		for (int i = 0; i < 10; ++i) {
			try (JSONInputStream in = new JSONInputStream(new ByteArrayInputStream(data))) {
				obj = in.readObject();
			}
		}
		return obj;
	}
	
}
