package com.codebattle.network.dataHandle;
import org.json.JSONException;
import org.json.JSONObject;


public class DataHandler {
	
	private DataHandler(){}
	
	public static JSONObject createRoom(String data){
		return new JSONObject(new Packet("Room", "Create", data));
	}
	
	public static JSONObject shutDownRoom(String data){
		return new JSONObject(new Packet("Room", "ShutDown", data));
	}
	
	public static JSONObject report(String data){
		return new JSONObject(new Packet("Room", "Report", data));
	}
	
	public static JSONObject initGame(String data){
		return new JSONObject(new Packet("Game", "Initiate", data));
	}
	
	public static JSONObject startGame(String data){
		return new JSONObject(new Packet("Game", "Start", data));
	}
	
	public static JSONObject script(String data){
		return new JSONObject(new Packet("Game", "Script", data));
	}
	
	public static JSONObject stringToJSONObject(String string){
		try {
			return new JSONObject(string);
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
