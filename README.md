## How to use it:

#### Read:

To read, there are two options. First is via a string:
```
String json = "...";
JSONObject obj;
try (JSONInputStream in = new JSONInputStream(json)) {
	obj = in.readObject();
} catch (IOException | JSONException e) {
	e.printStackTrace();
}
```
Second is via an input stream, such as a file:
```
JSONObject obj;
try (JSONInputStream in = new JSONInputStream(new FileInputStream(new File("myjson.txt")))) {
	obj = in.readObject();
} catch (IOException | JSONException e) {
	e.printStackTrace();
}
```


#### Write:

Once again, for write, there are two options. First is using the toString() function:
```
JSONObject obj = new JSONObject();
obj.put("myint", 5);
System.out.println(obj.toString());
```
Behind the scenes, it creates a new byte array output stream and uses the following method to create the string. Second option is through an output stream:
```
JSONObject obj = new JSONObject();
obj.put("myint", 5);
try (JSONOutputStream out = new JSONOutputStream(new FileOutputStream(new File("myjson.txt")))) {
	out.writeObject(obj);
} catch (IOException e) {
	e.printStackTrace();
}
```


#### Additional Note(s):

* There is both a JSONObject and an JSONArray, both are compatible with the input and output streams.
