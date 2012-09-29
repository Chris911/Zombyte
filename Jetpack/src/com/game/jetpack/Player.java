package com.game.jetpack;

import android.util.Log;

import com.bag.lib.DynamicGameObject;

public class Player extends DynamicGameObject {

    public static final float PLAYER_WIDTH 			= 1.4f;
    public static final float PLAYER_HEIGHT 		= 1.4f;
    public static final float PLAYER_FLOOR_POSITION = 0.5f + PLAYER_HEIGHT/2;
    public static final float PLAYER_MAX_VELOCITY	= 12.0f;
    public static final float PLAYER_DAMAGE_TIME	= 3.0f;
    public static final float PLAYER_BASE_SPEED		= 1.2f;
    public static final float PLAYER_MAX_SPEED		= 1.8f;

    public static final int PLAYER_STATE_IDLE 		= 0;
    public static final int PLAYER_STATE_MOVING 	= 1;
    //public static final int PLAYER_STATE_RUNNING 	= 2;
    public static final int PLAYER_STATE_HIT_WALL 	= 3;
    public static final int PLAYER_STATE_DEAD 		= 4;
    public static final int PLAYER_STATE_HIT 		= 5;


    public int state;
    
    public int previousState;
    
    // Number of life remaining
    private float life;
    
    // Speed of the player (walking / running)
    private float speed;
    
    private boolean isImmuneToDamge;
    
    // Current weapon
    public Weapon weapon;
    
    // Direction angle
    private float angle;
    
    // In damage state time
    private float inDamageStateTime;
    
	public Player(float x, float y) {
		super(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
		
		this.state = PLAYER_STATE_IDLE;
		this.life = 6;
		this.speed = 0.8f;
		this.isImmuneToDamge = false;
		this.weapon = new Weapon(Weapon.WEAPON_SHOTGUN);
		this.angle = 0;
		this.inDamageStateTime = 0;
	}
	
	public void update(float deltaTime) {
    	bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);
		
    	// Check if the player is currently immune to damage
		if(isImmuneToDamge) 
		{
			speed = PLAYER_MAX_SPEED;
			inDamageStateTime += deltaTime;
			if(inDamageStateTime >= PLAYER_DAMAGE_TIME)
			{
				inDamageStateTime = 0; 
				isImmuneToDamge = false;
				speed = PLAYER_BASE_SPEED;
			}
		}
    	
		// Check current State
		if(this.state == PLAYER_STATE_IDLE)
		{
			this.velocity.set(0,0);
		}
		else if(this.state == PLAYER_STATE_MOVING)
		{

		}
		else if(this.state == PLAYER_STATE_HIT_WALL)
		{
			
		}
		else if(state == PLAYER_STATE_HIT)
		{			
			previousState = state;
			this.life--;
			
			isImmuneToDamge = true;
			
			if(life <= 0)
			{
				this.state = PLAYER_STATE_DEAD;
			}
		}
		
		// Modify position
		position.add(velocity.x * deltaTime * speed, velocity.y * deltaTime * speed);
		
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
}
