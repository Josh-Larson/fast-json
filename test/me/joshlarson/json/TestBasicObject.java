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
		Assert.assertEquals((Long) 1234L, (Long) out.get("positive"));
		Assert.assertEquals((Long) (-1234L), (Long) out.get("negative"));
		Assert.assertEquals((Double) 10E10, (Double) out.get("exponent"), 1E-5);
		Assert.assertEquals((Double) 12.34, (Double) out.get("decimal"), 1E-5);
		Assert.assertEquals((Long) 0L, (Long) out.get("pos_infinity"));
		Assert.assertEquals((Long) 0L, (Long) out.get("neg_infinity"));
		Assert.assertEquals((Long) 0L, (Long) out.get("nan"));
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
		obj.putNull("null");
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
