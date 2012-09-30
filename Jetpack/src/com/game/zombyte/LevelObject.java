package com.game.zombyte;

import com.bag.lib.GameObject;
import com.bag.lib.gl.TextureRegion;
import com.bag.lib.math.Circle;

public class LevelObject extends GameObject{

	public static final int GAMEOBJECT_TYPE_TREE 	= 0;
	public static final int STATE_IDLE		 		= 1;
	public static final int STATE_COLLIDED 			= 2;
	
	public static final int TREE_SIZE 				= 8;

	
	public int state;
	public int type;
	public float alphaLevel;
	public TextureRegion asset;
	public int size;
	public Circle c;
	
	public LevelObject(float x, float y, int type, int size) {
		super(x, y, size, size);
		this.type = type;
		this.state = STATE_IDLE;
		this.size = size;
		this.alphaLevel = 1.0f;
		this.c = new Circle(x,y,size/2);
		
		if(type == GAMEOBJECT_TYPE_TREE){
			this.asset = Assets.tree;
		}
	}
	
	public void update()
	{
		if(state == STATE_COLLIDED){
			this.alphaLevel = 0.6f;
		} else {
			this.alphaLevel = 1.0f;
		}
	}
}
