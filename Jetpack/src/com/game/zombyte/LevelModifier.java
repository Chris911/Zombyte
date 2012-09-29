package com.game.zombyte;

public class LevelModifier {
	
	public static void addTreesToMap(World world)
	{
		world.levelObjectsArray.add(new LevelObject(10, 10, LevelObject.GAMEOBJECT_TYPE_TREE));
		world.levelObjectsArray.add(new LevelObject(20, 20, LevelObject.GAMEOBJECT_TYPE_TREE));
	}
	
}
