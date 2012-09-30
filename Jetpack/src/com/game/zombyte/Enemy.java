package com.game.zombyte;

import java.util.Random;

import com.bag.lib.DynamicGameObject;

public class Enemy extends DynamicGameObject {

	public static final int ENEMY_TYPE_ZOMBIE 		= 0;
	public static final int ENEMY_TYPE_BOSS 		= 1;

	public static final int ENEMY_STATE_ALIVE 		= 2;
	public static final int ENEMY_STATE_DEAD 		= 3;
	public static final int ENEMY_STATE_COLLIDE 	= 4;

	public static final int ENEMY_STATE_RETARDED	= 9000;
	
    public static final float ENEMY_MOVE_VELOCITY 	= 3.2f;
    public static final float ENEMY_BASIC_WIDTH 	= 1.0f;
    public static final float ENEMY_BASIC_HEIGHT 	= 1.0f;
    public static final float ENEMY_BOSS_WIDTH 		= 4.0f;
    public static final float ENEMY_BOSS_HEIGHT 	= 4.0f;
    
    public static final int ENEMY_SCORE = 50;
    
    public int type;
    public int state;
    public int life;
    
    private float speed;
    private int   difficulty;
    public float rotationAngle;
    public int   score;
    public float randomAngleX;
    public float randomAngleY;
    
    public float stateTime;
	
	public Enemy(float x, float y, int type, int difficulty) {
		super(x, y, ENEMY_BASIC_WIDTH, ENEMY_BASIC_HEIGHT); 
		
		this.state = ENEMY_STATE_ALIVE;
		this.rotationAngle = 0;
		this.life = 20;
		this.difficulty = difficulty;
		this.type = type;
		this.stateTime = 0.0f;
		this.randomAngleX = randInRangeInc(0, 360);
		this.randomAngleY = randInRangeInc(0, 360);

		initialize();
	}
	
    public void initialize()
    {
    	if(type == ENEMY_TYPE_ZOMBIE)
    	{
    		this.score = ENEMY_SCORE;
    		this.speed = ENEMY_MOVE_VELOCITY + (difficulty / 4);
	        
    	} 
    	else if (type == ENEMY_TYPE_BOSS)
    	{
    		this.score = (int) (500 * (difficulty * 2));
    		this.life = (int) (100 + (difficulty * 25));
    		this.speed = ENEMY_MOVE_VELOCITY *difficulty ; 
    		this.bounds.width += ENEMY_BOSS_WIDTH/2;
    		this.bounds.height += ENEMY_BOSS_WIDTH/2;
    	}	
    }
    
    public void update(float deltaTime) {    
    	bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);
    	stateTime += deltaTime;
    	
    	updateVelocity(); 

        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        
        //stateTime += deltaTime;
        

        
        if(life <= 0)
        {
        	state = ENEMY_STATE_DEAD;
        	
        	if(type == ENEMY_TYPE_BOSS)
        	{
        		//DO SOMETHING?
        	}
        }
    }
    
    private void updateVelocity() 
    {
    	if(state != ENEMY_STATE_RETARDED){
    		velocity.x = (float) (speed*Math.cos(rotationAngle));
    		velocity.y = (float) (speed*Math.sin(rotationAngle));
    	}
    	else{
        	velocity.x = (float) (speed*Math.cos(randomAngleX));
        	velocity.y = (float) (speed*Math.sin(randomAngleX));
        	rotationAngle = randomAngleX;
    	}
    }
    public static int randInRangeInc(int min, int max) {
        return min + (int) (Math.random() * (max - min));
}
}
