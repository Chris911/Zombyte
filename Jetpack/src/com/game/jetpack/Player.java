package com.game.jetpack;

import com.bag.lib.DynamicGameObject;

public class Player extends DynamicGameObject {

    public static final float PLAYER_WIDTH 			= 1.4f;
    public static final float PLAYER_HEIGHT 		= 1.4f;
    public static final float PLAYER_FLOOR_POSITION = 0.5f + PLAYER_HEIGHT/2;
    public static final float PLAYER_MAX_VELOCITY	= 12.0f;
    
    public static final int PLAYER_STATE_IDLE 		= 0;
    public static final int PLAYER_STATE_RUNNING 	= 1;
    public static final int PLAYER_STATE_FLYING 	= 2;
    public static final int PLAYER_STATE_FALLING 	= 3;
    public static final int PLAYER_STATE_HIT_WALL 	= 9;

    
    public static final float JETPACK_ACCELERATION	= 5.0f;

    float moveX = 15.0f; // Test value that makes the pacman move in X
    public int state;
    public int previousState;
    
    private float jetpackVelocity;
    
	public Player(float x, float y) {
		super(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
		
		this.state = PLAYER_STATE_IDLE;
		this.jetpackVelocity = 0.0f;
	}
	
	public void update(float deltaTime) {
		//velocity.x = moveX;
		
		// Check current State
		if(this.state == PLAYER_STATE_FLYING) {
			jetpackVelocity += JETPACK_ACCELERATION*deltaTime;
			
			if(jetpackVelocity >= 2) {
				jetpackVelocity = 2;
			}
		}
		else if (this.state == PLAYER_STATE_FALLING) {
			jetpackVelocity = 0;
		}
		else if (this.state == PLAYER_STATE_IDLE) {
			jetpackVelocity = 0;
		}
		
		// Modify velocity
		velocity.y += jetpackVelocity;
		velocity.add(0, World.gravity.y * deltaTime);
		if(velocity.y >= PLAYER_MAX_VELOCITY){
			velocity.y = PLAYER_MAX_VELOCITY;
		}
		
		// Modify position
		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		
		// Check if out of floor bounds 
		if(position.y < PLAYER_FLOOR_POSITION)
		{
			position.y = PLAYER_FLOOR_POSITION;
			velocity.y = 0;
			
			// Add the floor's friction to the movement
			applyFloorFriction(deltaTime);

		}
		
		// Out of World's bounds
		if(position.x > World.WORLD_WIDTH - PLAYER_WIDTH/2) {
			position.x = World.WORLD_WIDTH - PLAYER_WIDTH/2;
			reverse();
			
		} else if (position.x < 0 + PLAYER_WIDTH/2) {
			position.x = PLAYER_WIDTH/2;
			reverse();
		}
	}
	
	private void reverse(){
		velocity.x *=-0.8;
		previousState = state;
		state = PLAYER_STATE_HIT_WALL;
	}
	
	private void applyFloorFriction(float dt){
		if(velocity.x > 0){ velocity.x += World.friction.x * dt;}
		else if(velocity.x < 0){velocity.x -= World.friction.x * dt;}
		else velocity.x = 0;
	}
}
