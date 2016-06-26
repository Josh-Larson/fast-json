package me.joshlarson.json;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TestJSON {
	
	@Test
	public void testReadObject() {
		JSONObject original = new JSONObject();
		original.put("string", "test");
		JSONObject read = JSON.readObject(original.toString(), true);
		Assert.assertNotNull(read);
		Assert.assertEquals(original.get("string"), read.get("string"));
	}
	
	@Test
	public void testReadArray() {
		JSONArray original = new JSONArray();
		original.add("test");
		JSONArray read = JSON.readArray(original.toString(), true);
		Assert.assertNotNull(read);
		Assert.assertEquals(original.get(0), read.get(0));
	}
	
}
