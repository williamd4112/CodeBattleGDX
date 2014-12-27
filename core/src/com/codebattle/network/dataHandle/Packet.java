package com.codebattle.network.dataHandle;

public class Packet {
	private String type;
	private String option;
	private String data;
	
	public Packet(String type, String option, String data){
		this.type = type;
		this.option = option;
		this.data = data;
	}
}
