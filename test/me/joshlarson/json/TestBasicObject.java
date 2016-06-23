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
		Assert.assertEquals(1234, (long) out.get("positive"));
		Assert.assertEquals(-1234, (long) out.get("negative"));
		Assert.assertEquals(10E10, (double) out.get("exponent"), 1E-5);
		Assert.assertEquals(12.34, (double) out.get("decimal"), 1E-5);
		Assert.assertEquals(0, (long) out.get("pos_infinity"));
		Assert.assertEquals(0, (long) out.get("neg_infinity"));
		Assert.assertEquals(0, (long) out.get("nan"));
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
