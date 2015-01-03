package com.codebattle.network.dataHandle;

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
        this.data = jsonObject.getString("Data");
    }

    public Message(String type, String opt, Object data) {
        // Encapsulate data
        this.type = type;
        this.opt = opt;
        this.data = data;
    }
}
