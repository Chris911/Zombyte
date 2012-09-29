package com.game.zombyte;

import com.bag.lib.DynamicGameObject;

public class Bullet extends DynamicGameObject {

	public float rotationAngle; 
	private float bulletSpeed;
	
	public static final int ACTIVE = 1;
	public static final int NOT_ACTIVE = 0;
	
	public int state;
	
	public static final float BASIC_HEIGHT = 1.0f;
	public static final float BASIC_WIDTH = 1.0f;
	
	public Bullet(float x, float y, float rotationAngle, float bulletSpeed) {
		super(x, y, BASIC_HEIGHT, BASIC_WIDTH);
		this.rotationAngle = rotationAngle;
		this.state = ACTIVE;
		this.bulletSpeed = bulletSpeed;
	}
	
	public void update(float deltaTime)
	{
		if(state == ACTIVE)
		{
			if(position.x > World.WORLD_WIDTH || position.x < 0 || position.y > World.WORLD_HEIGHT || position.y < 0 )
			{
				state = NOT_ACTIVE;
			}
			
			bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);
	    	velocity.x = (float) (bulletSpeed*Math.cos(rotationAngle/180*3.146f));
	    	velocity.y = (float) (bulletSpeed*Math.sin(rotationAngle/180*3.146f));
			position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		}
	}

}
