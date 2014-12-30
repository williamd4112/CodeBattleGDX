package com.codebattle.network.dataHandle;
import java.util.Map;

import org.json.JSONObject;

public class DataHandler {
	
	private DataHandler(){}
	
	public static JSONObject loginServer(String data){
		return new JSONObject(new Message("Server", "Login", data));
	}
	
	public static JSONObject RoomList(Map<String, String> data){
		return new JSONObject(new Message("Server", "List", data));
	}
	
	public static JSONObject accept(String data) {
		return new JSONObject(new Message("Server", "Success", data));
	}
	
	public static JSONObject deny(String data) {
		return new JSONObject(new Message("Server", "Deny", data));
	}
	
	public static JSONObject createRoom(String data){
		return new JSONObject(new Message("Room", "Create", data));
	}
	
	public static JSONObject joinRoom(String data){
		return new JSONObject(new Message("Room", "Join", data));
	}
	
	public static JSONObject closeRoom(String data){
		return new JSONObject(new Message("Room", "Close", data));
	}
	
	public static JSONObject assignScene(String data){
		return new JSONObject(new Message("Room", "Assign", data));
	}
	
	public static JSONObject startGame(String data){
		return new JSONObject(new Message("Game", "Start", data));
	}
	
	public static JSONObject select(String data){
		return new JSONObject(new Message("Game", "Select", data));
	}
	
	public static JSONObject script(String data){
		return new JSONObject(new Message("Game", "Script", data));
	}
	
	public static JSONObject closeGame(String data){
		return new JSONObject(new Message("Game", "Close", data));
	}
	
	public static JSONObject report(){
		return new JSONObject(new Message("Report", "", ""));
	}
	
	/*public static JSONObject stringToJSONObject(String string){
		try {
			return new JSONObject(string);
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}*/
}
