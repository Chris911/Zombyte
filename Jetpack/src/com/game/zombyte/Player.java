package com.game.zombyte;
import com.bag.lib.DynamicGameObject;
public class Player extends DynamicGameObject {

    public static final float PLAYER_WIDTH 			= 1.4f;
    public static final float PLAYER_HEIGHT 		= 1.4f;
    public static final float PLAYER_FLOOR_POSITION = 0.5f + PLAYER_HEIGHT/2;
    public static final float PLAYER_DAMAGE_TIME	= 0.7f;
    public static final float PLAYER_BASE_SPEED		= 1.2f;
    public static final float PLAYER_MAX_SPEED		= 1.6f;

    public static final int PLAYER_STATE_IDLE 		= 0;
    public static final int PLAYER_STATE_MOVING 	= 1;
    //public static final int PLAYER_STATE_RUNNING 	= 2;
    public static final int PLAYER_STATE_HIT_WALL 	= 3;
    public static final int PLAYER_STATE_DEAD 		= 4;
    public static final int PLAYER_STATE_HIT 		= 5;
    public static final int PLAYER_STATE_HIDDEN		= 6;

    public int state;
    
    public int previousState;
    
    // Number of life remaining
    public int life;
    
    // Speed of the player (walking / running)
    private float speed;
    
    public boolean isImmuneToDamage;
    public boolean isTakingDamage;
    public boolean	isHiddenForTooLong;
    
    // Current weapon
    public Weapon weapon;
    public Weapon pistol;
    public Weapon tempWeapon;

    // Direction angle
    public float rotationAngle;
    public float stateTime;
    
    // In damage state time
    private float inDamageStateTime;
    
	public Player(float x, float y) {
		super(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
		
		this.state = PLAYER_STATE_IDLE;
		this.life = 6;
		this.speed = PLAYER_BASE_SPEED;
		this.isImmuneToDamage = false;
		this.isTakingDamage = false;
		this.weapon = new Weapon(Weapon.WEAPON_PISTOL);
		this.rotationAngle = 0;
		this.pistol = new Weapon(Weapon.WEAPON_PISTOL);
		this.inDamageStateTime = 0;
		this.isHiddenForTooLong = false;
		this.stateTime = 0;
	}
	
	public void update(float deltaTime) {
    	bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);
		
    	stateTime += deltaTime;
    	
		if (isTakingDamage){
			life --;
			isTakingDamage = false;
			isImmuneToDamage = true;
		}
    	
    	// Check if the player is currently immune to damage
		if(isImmuneToDamage) 
		{
			speed = PLAYER_MAX_SPEED;
			inDamageStateTime += deltaTime;
			if(inDamageStateTime >= PLAYER_DAMAGE_TIME)
			{
				inDamageStateTime = 0; 
				isImmuneToDamage = false;
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
			if(!isImmuneToDamage)
				isTakingDamage = true;
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
	
	public void toggleWeapons()
	{
		try{
			if(weapon.getType() != Weapon.WEAPON_PISTOL) {
			tempWeapon = weapon;
			weapon = pistol;
			}
			else if(tempWeapon != null){
				weapon = tempWeapon;
			}
		} catch(Exception e){}
	}
}
