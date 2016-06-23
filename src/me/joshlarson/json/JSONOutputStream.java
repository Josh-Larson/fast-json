package me.joshlarson.json;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * This output stream will write RFC 4627 JSON strings out
 * 
 * @author josh
 */
public class JSONOutputStream extends OutputStream {
	
	private final OutputStream os;
	private String indentation;
	private boolean compact;
	
	/**
	 * Wraps this JSON output stream around the specified output stream
	 * 
	 * @param os the output stream to wrap
	 */
	public JSONOutputStream(OutputStream os) {
		this.os = os;
		this.indentation = "    ";
		this.compact = false;
	}
	
	/**
	 * Sets the indentation for each additional tab. This is not used if compact is set to true
	 * 
	 * @param indentation the indentation for each tab
	 * @see setCompact
	 */
	public void setIndentation(String indentation) {
		this.indentation = indentation;
	}
	
	/**
	 * Sets the mode to compact. If TRUE, there is no indentation or newlines
	 * 
	 * @param compact TRUE to enable compact, FALSE otherwise
	 */
	public void setCompact(boolean compact) {
		this.compact = compact;
	}
	
	/**
	 * Writes the specified JSONObject to the output stream
	 * 
	 * @param obj the JSONObject to write
	 * @throws IOException if there is an I/O error
	 */
	public void writeObject(JSONObject obj) throws IOException {
		writeObject(obj, 0);
	}
	
	/**
	 * Writes the specified JSONArray to the output stream
	 * 
	 * @param array the JSONArray to write
	 * @throws IOException if there is an I/O error
	 */
	public void writeArray(JSONArray array) throws IOException {
		writeArray(array, 0);
	}
	
	private void writeObject(JSONObject obj, int depth) throws IOException {
		write('{');
		if (!compact)
			write('\n');
		int i = 0;
		Set<String> keys = obj.keySet();
		for (String key : keys) {
			if (!compact)
				writeIndentation(depth + 1);
			writeStringSafe("\"" + escapeString(key) + "\": ");
			writeValue(obj.get(key), depth + 1);
			if (i + 1 < keys.size())
				write(',');
			if (!compact)
				write('\n');
			i++;
		}
		if (!compact)
			writeIndentation(depth);
		write('}');
	}
	
	private void writeArray(JSONArray array, int depth) throws IOException {
		write('[');
		if (!compact)
			write('\n');
		for (int i = 0; i < array.size(); i++) {
			if (!compact)
				writeIndentation(depth + 1);
			Object o = array.get(i);
			writeValue(o, depth + 1);
			if (i + 1 < array.size())
				write(',');
			if (!compact)
				write('\n');
		}
		if (!compact)
			writeIndentation(depth);
		write(']');
	}
	
	private void writeValue(Object o, int depth) throws IOException {
		if (o instanceof String)					// String
			writeStringSafe("\"" + escapeString((String) o) + "\"");
		else if (o instanceof Number)				// Number
			writeNumber((Number) o);
		else if (o instanceof Boolean)				// Boolean
			writeString(o.toString());
		else if (o == null)							// Null
			writeString("null");
		else if (o instanceof JSONObject)			// Object
			writeObject(((JSONObject) o), depth);
		else if (o instanceof JSONArray)			// Array
			writeArray(((JSONArray) o), depth);
	}
	
	private void writeNumber(Number n) throws IOException {
		if (n instanceof Double && (Double.isNaN((Double) n) || Double.isInfinite((Double) n))) {
			write('0');
			return;
		}
		if (n instanceof Float && (Float.isNaN((Float) n) || Float.isInfinite((Float) n))) {
			write('0');
			return;
		}
		writeString(n.toString());
	}
	
	private void writeIndentation(int depth) throws IOException {
		for (int i = 0; i < depth; ++i)
			writeString(indentation);
	}
	
	private void writeString(String str) throws IOException {
		for (int i = 0; i < str.length(); ++i)
			write(str.charAt(i));
	}
	
	private void writeStringSafe(String str) throws IOException {
		write(str.getBytes(StandardCharsets.UTF_8));
	}
	
	private String escapeString(String str) {
		StringBuilder builder = new StringBuilder(str.length());
		char c;
		for (int i = 0; i < str.length(); ++i) {
			c = str.charAt(i);
			if (c == '\\' || c == '\"') {
				builder.append('\\');
				builder.append(c);
			} else if (c <= 0x1F) {
				builder.append('\\');
				switch (c) {
					case '\n':
						builder.append('n');
						break;
					case '\r':
						builder.append('r');
						break;
					case '\t':
						builder.append('t');
						break;
					case '\b':
						builder.append('b');
						break;
					default:
						builder.append(String.format("u%04X", (int) c));
						break;
				}
			} else
				builder.append(c);
		}
		return builder.toString();
	}
	
	public void write(int b) throws IOException {
		os.write(b);
	}
	
	public void write(byte[] b) throws IOException {
		os.write(b);
	}
	
	public void write(byte[] b, int off, int len) throws IOException {
		os.write(b, off, len);
	}
	
	public void flush() throws IOException {
		os.flush();
	}
	
	public void close() throws IOException {
		os.close();
	}
	
}
