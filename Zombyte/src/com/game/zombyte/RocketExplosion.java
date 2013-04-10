package com.game.zombyte;

import com.bag.lib.GameObject;

public class RocketExplosion extends GameObject {

	public static final int ROCKETEXP_STATE_ACTIVE     = 0;
	public static final int ROCKETEXP_STATE_NOT_ACTIVE = 1;
	
	public static final float ROCKETEXP_BASIC_HEIGHT = 1.5f;
	public static final float ROCKETEXP_BASIC_WIDTH  = 1.5f;
	
	public int state;
	public float stateTime; 
	
	public RocketExplosion(float x, float y) {
		super(x, y, ROCKETEXP_BASIC_WIDTH, ROCKETEXP_BASIC_HEIGHT);

		this.stateTime = 0.0f;
		this.state     = ROCKETEXP_STATE_ACTIVE;
	}
	
    public void update(float deltaTime) {    
    	stateTime += deltaTime;
    	if(stateTime > 1.12/2)
    	{
    		this.state = ROCKETEXP_STATE_NOT_ACTIVE;
    	}
    }
	
}
