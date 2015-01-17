package com.codebattle.network.dataHandle;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DataHandler {

    private DataHandler() {
    }

    private static JSONObject create(final String type, final String opt, final Object data) {
        final JSONObject obj = new JSONObject();
        obj.put("Type", type);
        obj.put("Opt", opt);
        obj.put("Data", data);

        return obj;
    }

    public static JSONObject loginServer(final String data) {
        return create("Server", "Login", data);
    }

    public static JSONObject RoomList(final Map<String, String> data) {
        return create("Server", "List", data);
    }

    public static JSONObject requestRoomList() {
        return create("Server", "RoomList", "");
    }

    public static JSONObject accept(final String data) {
        return create("Server", "Success", data);
    }

    public static JSONObject deny(final String data) {
        return create("Server", "Deny", data);
    }

    public static JSONObject createRoom(final String data) {
        return create("Room", "Create", data);
    }

    public static JSONObject joinRoom(final String data) {
        return create("Room", "Join", data);
    }

    public static JSONObject leaveRoom(final String data) {
        return create("Room", "Leave", data);
    }

    public static JSONObject playerstState(final Map<String, String> data) {
        return create("Room", "PlayerState", data);
    }

    public static JSONObject ready(final String team) {
        return create("Room", "Ready", team);
    }

    public static JSONObject requestPlayerState() {
        return create("Room", "RequestPlayerState", "");
    }

    public static JSONObject assignScene(final String data) {
        return create("Room", "Assign", data);
    }

    public static JSONObject getTeam(final String data) {
        return create("Game", "Team", data);
    }

    public static JSONObject startGame(final String data) {
        return create("Game", "Start", data);
    }

    public static JSONObject select(final String data) {
        return create("Game", "Select", data);
    }

    public static JSONObject script(final String data) {
        return create("Game", "Script", data);
    }

    public static JSONObject closeGame(final String data) {
        return create("Game", "Close", data);
    }

    public static JSONObject report() {
        return create("Report", "", "");
    }

    public static Map<Object, Object> JsonToMap(final JSONObject obj) {
        final Map<Object, Object> map = new HashMap<Object, Object>();
        for (final String key : obj.keySet()) {
            map.put(key, obj.get(key));
        }
        return map;
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
