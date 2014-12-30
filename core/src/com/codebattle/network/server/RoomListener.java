package com.codebattle.network.server;

public interface RoomListener {
	public void destroyRoom(String roomName);
	public void ChangeScene(String roomName, String sceneName);
    public void ChangeName(String newRoomName, String oldRoomName);
}
