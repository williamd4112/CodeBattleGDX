package com.codebattle.network.server;

import org.json.JSONObject;

public class Message {

    public String type;
    public String opt;
    public String data;

    public Message(JSONObject jsonObject) {
        // Extract data
        this.type = jsonObject.getString("Type");
        this.opt = jsonObject.getString("Opt");
        this.data = jsonObject.getString("Data");
    }

    public Message(String rawMessage) {
        this(new JSONObject()); // TODO: Data Handler will handle this
    }
}
