package com.codebattle.network.dataHandle;

import java.util.Map;

import org.json.JSONObject;

public class DataHandler {

    private DataHandler() {
    }

    private static JSONObject create(String type, String opt, Object data) {
        JSONObject obj = new JSONObject();
        obj.put("Type", type);
        obj.put("Opt", opt);
        obj.put("Data", data);

        return obj;
    }

    public static JSONObject loginServer(String data) {
        return create("Server", "Login", data);
    }

    public static JSONObject RoomList(Map<String, String> data) {
        return create("Server", "List", data);
    }

    public static JSONObject accept(String data) {
        return create("Server", "Success", data);
    }

    public static JSONObject deny(String data) {
        return create("Server", "Deny", data);
    }

    public static JSONObject createRoom(String data) {
        return create("Room", "Create", data);
    }

    public static JSONObject joinRoom(String data) {
        return create("Room", "Join", data);
    }

    public static JSONObject closeRoom(String data) {
        return create("Room", "Close", data);
    }

    public static JSONObject assignScene(String data) {
        return create("Room", "Assign", data);
    }

    public static JSONObject startGame(String data) {
        return create("Game", "Start", data);
    }

    public static JSONObject select(String data) {
        return create("Game", "Select", data);
    }

    public static JSONObject script(String data) {
        return create("Game", "Script", data);
    }

    public static JSONObject closeGame(String data) {
        return create("Game", "Close", data);
    }

    public static JSONObject report() {
        return create("Report", "", "");
    }

    /*
     * public static JSONObject stringToJSONObject(String string){
     * try {
     * return new JSONObject(string);
     * }
     * catch (JSONException e) {
     * e.printStackTrace();
     * return null;
     * }
     * }
     */
}
