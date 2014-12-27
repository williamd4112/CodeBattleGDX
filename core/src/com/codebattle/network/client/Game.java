package com.codebattle.network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Game implements GameAgent{
	
	public static void main(String[] args) throws IOException{
		Client client = new Client();
		BufferedReader reader = new BufferedReader( new InputStreamReader(System.in)); 
		String IP;
		String port;
		
		IP = reader.readLine();
		port = reader.readLine();
		
		client.connectToServer(IP, port);
		while(true){
			client.sendMessage(reader.readLine());
		}
	}
	@Override
	public void MessageReceived(String msg) {
		System.out.println("Got msg from server");
	}

}
