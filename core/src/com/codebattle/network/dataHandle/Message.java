package com.codebattle.network.dataHandle;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class Message {

    public String type;
    public String opt;
    public Object data;

    public Message(String rawMessage) {
        // Extract data
        JSONObject jsonObject = new JSONObject(rawMessage);
        this.type = jsonObject.getString("Type");
        this.opt = jsonObject.getString("Opt");
        this.data = jsonObject.get("Data");
    }

    public Message(String type, String opt, Object data) {
        // Encapsulate data
        this.type = type;
        this.opt = opt;
        this.data = data;
    }

    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("Room01", "Scene01");
        map.put("Room02", "Scene02");

        JSONObject obj = DataHandler.RoomList(map);
        Message msg = new Message(obj.toString());
        JSONObject trans = new JSONObject(obj.toString());
        JSONObject jmap = (JSONObject) msg.data;

        Map<String, String> m = new HashMap<String, String>();
        for (String key : jmap.keySet()) {
            m.put(key, jmap.getString(key));
        }

        System.out.println(jmap);

        for (String key : m.keySet())
            System.out.println(key + " : " + m.get(key));

    }
}
