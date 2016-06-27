## How to use it:

#### Read:

To read, there are several options. First is using the convenience class JSON:
```
String json = "...";
JSONObject obj = JSON.readObject(json, true); // Second argument is whether or not to print errors
```
Second is using a string:
```
String json = "...";
JSONObject obj;
try (JSONInputStream in = new JSONInputStream(json)) {
	obj = in.readObject();
} catch (IOException | JSONException e) {
	e.printStackTrace();
}
```
Third is using an input stream, such as a file:
```
JSONObject obj;
try (JSONInputStream in = new JSONInputStream(new FileInputStream(new File("myjson.txt")))) {
	obj = in.readObject();
} catch (IOException | JSONException e) {
	e.printStackTrace();
}
```


#### Write:

For write, there are two options. First is using the toString() function:
```
JSONObject obj = new JSONObject();
obj.put("myint", 5);
System.out.println(obj.toString());
```
Behind the scenes, it creates a new byte array output stream and uses the following method to create the string. Second option is using an output stream:
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
* JSON will automatically clean up stream resources
