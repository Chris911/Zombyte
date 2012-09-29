package com.game.zombyte;

import com.bag.lib.GameObject;
import com.bag.lib.gl.TextureRegion;

public class LevelObject extends GameObject{

	public static final int GAMEOBJECT_TYPE_TREE 	= 0;
	public static final int STATE_IDLE		 		= 1;
	public static final int STATE_COLLIDED 			= 2;
	
	public static final int TREE_SIZE 				= 8;

	
	public int state;
	public int type;
	public float alphaLevel;
	public TextureRegion asset;
	
	
	public LevelObject(float x, float y, int type) {
		super(x, y, TREE_SIZE, TREE_SIZE);
		this.type = type;
		this.state = STATE_IDLE;
		
		if(type == GAMEOBJECT_TYPE_TREE){
			this.asset = Assets.tree;
		}
	}
	
	public void update()
	{
		if(state == STATE_COLLIDED){
			
		}
	}
}
