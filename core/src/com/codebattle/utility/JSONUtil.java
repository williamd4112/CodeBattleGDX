package com.codebattle.utility;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

public class JSONUtil {
	
	public static <T> T readJSONFromFile(Class<T> type , String path)
	{
		return (T) new Json().fromJson(type, Gdx.files.internal(path + ".json"));
	}
}
