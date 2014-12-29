package com.codebattle.network.server;

import org.json.JSONObject;

public class Message {

    public String type;
    public String opt;
    public String data;

    public Message(String rawMessage) {
        // Extract data
    	JSONObject jsonObject = new JSONObject(rawMessage);
        this.type = jsonObject.getString("Type");
        this.opt = jsonObject.getString("Opt");
        this.data = jsonObject.getString("Data");
    }
    
    public Message(String type, String opt, String data){
    	// Encapsulate data
		this.type = type;
		this.opt = opt;
		this.data = data;
	}
}
