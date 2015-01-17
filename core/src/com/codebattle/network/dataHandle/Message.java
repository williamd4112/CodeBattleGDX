package com.codebattle.network.dataHandle;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Message {

    public String type;
    public String opt;
    public Object data;

    public Message(final String rawMessage) {
        // Extract data
        final JSONObject jsonObject = new JSONObject(rawMessage);
        this.type = jsonObject.getString("Type");
        this.opt = jsonObject.getString("Opt");
        this.data = jsonObject.get("Data");
    }

    public Message(final String type, final String opt, final Object data) {
        // Encapsulate data
        this.type = type;
        this.opt = opt;
        this.data = data;
    }

    public static void main(final String[] args) {
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("Room01", "Scene01");
        map.put("Room02", "Scene02");

        final JSONObject obj = DataHandler.RoomList(map);
        final Message msg = new Message(obj.toString());
        final JSONObject trans = new JSONObject(obj.toString());
        final JSONObject jmap = (JSONObject) msg.data;

        final Map<String, String> m = new HashMap<String, String>();
        for (final String key : jmap.keySet()) {
            m.put(key, jmap.getString(key));
        }

        System.out.println(jmap);

        for (final String key : m.keySet()) {
            System.out.println(key + " : " + m.get(key));
        }

    }
}
