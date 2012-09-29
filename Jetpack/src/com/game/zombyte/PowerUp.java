package com.game.zombyte;

import com.bag.lib.DynamicGameObject;

public class PowerUp extends DynamicGameObject {

	public static final int ACTIVE     = 1;
	public static final int NOT_ACTIVE = 0;
	
	public static final int POWERUP_TYPE_SHOTGUN = Weapon.WEAPON_SHOTGUN;
	public static final int POWERUP_TYPE_ROCKET  = Weapon.WEAPON_ROCKET;
	public static final int POWERUP_TYPE_RIFLE   = Weapon.WEAPON_RIFLE;
	
	public int state;
	public int type;
	
	public float rotationAngle; 
	
	public static final float BASIC_HEIGHT = 3.0f;
	public static final float BASIC_WIDTH  = 3.0f;
	
	public PowerUp(float x, float y, int type) {
		super(x, y, BASIC_WIDTH, BASIC_HEIGHT);
		
		this.type = type;
		this.rotationAngle = 0;
		this.state = ACTIVE;
	}
	
	public void update(float deltaTime)
	{
		if(state == ACTIVE)
		{
			// Rotate weapon / bonus
			rotationAngle += (0.60 * deltaTime) % 360;
			
			bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);
		}
	}

}
