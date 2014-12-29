package com.codebattle.network.dataHandle;

import org.json.JSONException;
import org.json.JSONObject;

import com.codebattle.network.server.Message;

public class DataHandler {

    private DataHandler() {
    }

    public static JSONObject loginServer(String data) {
        return new JSONObject(new Message("Server", "Login", data));
    }

    public static JSONObject getRoomList(String data) {
        return new JSONObject(new Message("Server", "RoomList", data));
    }

    public static JSONObject createRoom(String data) {
        return new JSONObject(new Message("Room", "Create", data));
    }

    public static JSONObject joinRoom(String data) {
        return new JSONObject(new Message("Room", "Join", data));
    }

    public static JSONObject closeGame(String data) {
        return new JSONObject(new Message("Room", "Close", data));
    }

    /*
     * public static JSONObject report(String data){
     * return new JSONObject(new Message("Room", "Report", data));
     * }
     */

    public static JSONObject assignScene(String data) {
        return new JSONObject(new Message("Room", "Assign", data));
    }

    public static JSONObject initGame(String data) {
        return new JSONObject(new Message("Game", "Initiate", data));
    }

    public static JSONObject startGame(String data) {
        return new JSONObject(new Message("Game", "Start", data));
    }

    public static JSONObject script(String data) {
        return new JSONObject(new Message("Game", "Script", data));
    }

    public static JSONObject stringToJSONObject(String string) {
        try {
            return new JSONObject(string);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
