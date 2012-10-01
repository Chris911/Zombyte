package com.game.zombyte;

public class LevelModifier {
	
	public static void addTreesToMap(World world)
	{
		world.levelObjectsArray.add(new LevelObject(4, 5, LevelObject.GAMEOBJECT_TYPE_TREE, 6));
		world.levelObjectsArray.add(new LevelObject(34, 18, LevelObject.GAMEOBJECT_TYPE_TREE, 5));
		world.levelObjectsArray.add(new LevelObject(30, 2, LevelObject.GAMEOBJECT_TYPE_TREE, 8));

	}
	
	public static void addTreesToMap(MultiWorld world)
	{
		world.levelObjectsArray.add(new LevelObject(10, 10, LevelObject.GAMEOBJECT_TYPE_TREE, 6));
		world.levelObjectsArray.add(new LevelObject(20, 20, LevelObject.GAMEOBJECT_TYPE_TREE, 4));
		world.levelObjectsArray.add(new LevelObject(30, 2, LevelObject.GAMEOBJECT_TYPE_TREE, 8));

	}
	
}
