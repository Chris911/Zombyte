package com.game.zombyte;


public class LevelModifier {
	
	public static void addTreesToMap(World world)
	{
		//world.levelObjectsArray.add(new LevelObject(10, 5, LevelObject.GAMEOBJECT_TYPE_ROCK, 6));
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
	
	public static void collidePlayerRock(Player p, LevelObject l)
	{
		float pright = p.position.x;
		float pleft = p.position.x;
		
		float ppos = Math.abs(p.position.y);
		//float pbot = p.position.y;

		float lright = l.position.x - l.bounds.width/2;
		float lleft = l.position.x + l.bounds.width/2;
	
		float ltop = Math.abs(l.position.y - l.bounds.height/2);
		float lbot = Math.abs(l.position.y + l.bounds.height/2);

		
//		if(pright <= lleft)
//			p.position.x = lleft;
//		else if(pleft >= lright)
//			p.position.x = lright;
		
		if(ppos <= ltop && ppos > lbot)
			p.position.y = p.lastPos.y;
		else if(ppos == lbot && ppos < ltop)
			p.position.y = p.lastPos.y;
		
	}
	
}
