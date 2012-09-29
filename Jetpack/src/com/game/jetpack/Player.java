package com.game.jetpack;

import android.util.Log;

import com.bag.lib.DynamicGameObject;

public class Player extends DynamicGameObject {

    public static final float PLAYER_WIDTH 			= 1.4f;
    public static final float PLAYER_HEIGHT 		= 1.4f;
    public static final float PLAYER_FLOOR_POSITION = 0.5f + PLAYER_HEIGHT/2;
    public static final float PLAYER_MAX_VELOCITY	= 12.0f;
    public static final float PLAYER_DAMAGE_TIME	= 0.8f;
    
    public static final int PLAYER_STATE_IDLE 		= 0;
    public static final int PLAYER_STATE_MOVING 	= 1;
    //public static final int PLAYER_STATE_RUNNING 	= 2;
    public static final int PLAYER_STATE_DAMAGE 	= 3;
    public static final int PLAYER_STATE_HIT_WALL 	= 4;
    public static final int PLAYER_STATE_DEAD 		= 5;
    public static final int PLAYER_STATE_HIT 		= 6;


    public int state;
    
    public int previousState;
    
    // Number of life remaining
    private float life;
    
    // Speed of the player (walking / running)
    private float speed;
    
    private boolean isRunning;
    
    // Current weapon
    public Weapon weapon;
    
    // Direction angle
    private float angle;
    
    // In damage cumulated state time
    private long inDamageStateTime;
    
	public Player(float x, float y) {
		super(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
		
		this.state = PLAYER_STATE_IDLE;
		this.life = 6;
		this.speed = 1;
		this.isRunning = false;
		this.weapon = new Weapon(Weapon.WEAPON_SHOTGUN);
		this.angle = 0;
		this.inDamageStateTime = 0;
	}
	
	public void update(float deltaTime) {
		// Check current State
		if(this.state == PLAYER_STATE_IDLE)
		{
			this.speed = 0;
		}
		else if(this.state == PLAYER_STATE_MOVING)
		{
			if(isRunning)
				this.speed = 1.2f;
			else
				this.speed = 1;
		}
		else if(this.state == PLAYER_STATE_DAMAGE)
		{
			Log.d("STATE"," ANIM DAMAGE "); 
			inDamageStateTime += deltaTime;
			if(inDamageStateTime >= PLAYER_DAMAGE_TIME)
			{
				Log.d("STATE","Return to other state");
				state = previousState;
			}
		}
		else if(this.state == PLAYER_STATE_HIT_WALL)
		{
			
		}
		else if(state == PLAYER_STATE_HIT)
		{
			Log.d("STATE"," HIT ");

			previousState = state;
			this.life--;
			state = PLAYER_STATE_DAMAGE;
			if(life <= 0)
			{
				this.state = PLAYER_STATE_DEAD;
			}
		}
		
		// Modify velocity
		//velocity.add(0, World.gravity.y * deltaTime);
		
		// Modify position
		position.add(velocity.x * deltaTime * speed, velocity.y * deltaTime * speed);
		
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
			
		} else if (position.x < 0 + PLAYER_WIDTH/2) {
			position.x = PLAYER_WIDTH/2;
		}
		else if(position.y > World.WORLD_HEIGHT - PLAYER_HEIGHT/2) {
			position.y = World.WORLD_HEIGHT - PLAYER_HEIGHT/2;
			
		} else if (position.y < 0 + PLAYER_HEIGHT/2) {
			position.y = PLAYER_HEIGHT/2;
		}
	}
	
	private void applyFloorFriction(float dt){
		if(velocity.x > 0){ velocity.x += World.friction.x * dt;}
		else if(velocity.x < 0){velocity.x -= World.friction.x * dt;}
		else velocity.x = 0;
	}
}
