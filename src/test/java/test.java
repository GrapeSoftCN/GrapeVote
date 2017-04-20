import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import esayhelper.JSONHelper;
import interfaceApplication.vote;

public class test {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		String string = "{\"itemid\":\"12\",\"itemName\":\"t1\",\"count\":\"0\"}";
		JSONArray array = new JSONArray();
		array.add(JSONHelper.string2json(string));
		array.add(JSONHelper.string2json(string));
		array.add(JSONHelper.string2json(string));
		JSONObject object = new JSONObject();
		object.put("votes", array.toString());
		object.put("name", "td");
		String s= object.toString(); 
		System.out.println(JSONHelper.string2json(s));
		System.out.println(new vote().VoteUpdate("58f6d44d0fd84d17f0b67b44", s));
	}
}
