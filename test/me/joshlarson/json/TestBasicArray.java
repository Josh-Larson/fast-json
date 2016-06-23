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
			out = in.readArray();
		}
		Assert.assertEquals(7, out.size());
		Assert.assertEquals((Long) 1234L, (Long) out.get(0));
		Assert.assertEquals((Long) (-1234L), (Long) out.get(1));
		Assert.assertEquals((Double) 10E10, (Double) out.get(2), 1E-5);
		Assert.assertEquals((Double) 12.34, (Double) out.get(3), 1E-5);
		Assert.assertEquals((Long) 0L, (Long) out.get(4));
		Assert.assertEquals((Long) 0L, (Long) out.get(5));
		Assert.assertEquals((Long) 0L, (Long) out.get(6));
	}
	
	@Test
	public void testBasicStrings() throws IOException, JSONException {
		JSONArray obj = new JSONArray();
		obj.add("NoPunctuation");
		obj.add(".!,?;:}{][@#$%^&*)(-_+='`~|/><");
		obj.add("TestingQuote\"Does this work\\?");
		obj.add("Testing null, carriage and lines: \0..\r..\n..");
		JSONArray out;
		String str = obj.toString();
		Assert.assertEquals(4, obj.size());
		Assert.assertTrue("String must be entirely ascii", isAscii(str));
		try (JSONInputStream in = new JSONInputStream(str)) {
			out = in.readArray();
		}
		Assert.assertEquals(4, out.size());
		for (int i = 0; i < 4; i++)
			Assert.assertEquals(obj.get(i), out.get(i));
	}
	
	@Test
	public void testOperations() {
		JSONArray obj = new JSONArray();
		Assert.assertEquals(0, obj.size());
		obj.add(false);
		Assert.assertTrue(obj.contains(false));
		Assert.assertEquals(false, obj.get(0));
		Assert.assertEquals(1, obj.size());
		obj.remove(0);
		Assert.assertFalse(obj.contains(false));
		Assert.assertEquals(0, obj.size());
		obj.add(false);
		Assert.assertTrue(obj.contains(false));
		Assert.assertEquals(false, obj.get(0));
		Assert.assertEquals(1, obj.size());
		obj.clear();
		Assert.assertFalse(obj.contains(false));
		Assert.assertEquals(0, obj.size());
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
