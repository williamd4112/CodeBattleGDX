package com.codebattle.model;

public enum Owner {
	RED(0), BLUE(1), GREEN(2);
	
	final public int index;
	private Owner(int index){
		this.index = index;
	}
}
