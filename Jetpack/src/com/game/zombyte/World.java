package com.game.zombyte;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bag.lib.math.OverlapTester;
import com.bag.lib.math.Vector2;

/*
 * Master class holding all game objects and regulating their interactions
 */

public class World {
	
	// Interface, mostly used to access sound effects
    public interface WorldListener {
          //public void sound();
		  int getTime();
    }

    // World's size
    public static final float WORLD_WIDTH 			= 40;
    public static final float WORLD_HEIGHT 			= 22;
    
    // World's states
    public static final int WORLD_STATE_RUNNING 	= 0;
    public static final int WORLD_STATE_NEXT_LEVEL 	= 1;
    public static final int WORLD_STATE_GAME_OVER 	= 2;
    
    public static final Vector2 friction = new Vector2(-12, 0);

    public final Player player;
    public final List<Bullet> bulletArray;
    public final List<Enemy> EnemyArray;
    public final List<PowerUp> PowerUpArray;
    public final List<LevelObject> levelObjectsArray;

    public Explosion explosion;
    public final WorldListener listener;
    public final Random rand;
    
    public float lastBulletFiredTime;
    
    public int state;
    

    public World(WorldListener listener) {
        this.bulletArray = new ArrayList<Bullet>();
        this.EnemyArray = new ArrayList<Enemy>();
        this.PowerUpArray = new ArrayList<PowerUp>();
        this.levelObjectsArray = new ArrayList<LevelObject>();
    	player = new Player(WORLD_WIDTH/2, 10);
        
    	this.listener = listener;
        
        rand = new Random();
       
        this.state = WORLD_STATE_RUNNING;
        lastBulletFiredTime = 0.0f;
        
        explosion = null;
        initEnemies();
        LevelModifier.addTreesToMap(this);
    }
    
    private void initEnemies()
    {
    	for (int i = 0; i < 20; i++) {
			addEnemy(); 
		}
    }
	
	public void update(float deltaTime, float speed) {
		updatePlayer(deltaTime, speed);
		updateBullet(deltaTime);
		updateEnemies(deltaTime);
		updatePowerUp(deltaTime);
		updateExplosions(deltaTime);
		updateLevelObjects(deltaTime);
		checkCollisions();
		//checkGameOver();
	}

	private void updatePlayer(float deltaTime, float speed) {
	    //if(speed == 0)
	    //	player.state = Player.PLAYER_STATE_IDLE;
	    player.update(deltaTime);
	    if(player.state == Player.PLAYER_STATE_HIT_WALL) {
	    	player.state = player.previousState;
	    }
	}
	
	private void updateBullet(float deltaTime) {
		synchronized (bulletArray) {
			for(int i = 0; i < bulletArray.size(); i ++){
				bulletArray.get(i).update(deltaTime);
				
				if( bulletArray.get(i).state == Bullet.NOT_ACTIVE )
					bulletArray.remove(i);
			}
		}
	}
	
	private void updatePowerUp(float deltaTime) {
		synchronized (PowerUpArray) {
			for(int i = 0; i < PowerUpArray.size(); i ++){
				PowerUpArray.get(i).update(deltaTime);
			}
		}
	}
	
	private void updateLevelObjects(float deltaTime) {
		synchronized (levelObjectsArray) {
			for(int i = 0; i < levelObjectsArray.size(); i ++){
				levelObjectsArray.get(i).update();
			}
		}
	}
	
	
	private void updateEnemies(float deltaTime) {
	    int len = EnemyArray.size();
	    
	    // Add enemies if 2 enemies remaining
	    if(len <= 2)
	    {
	    	for(int i=0; i<10; i++)
	    	{
	    		addEnemy();
	    	}
	    }
	    
	    // Update the enemies
	    for (int i = 0; i < len; i++) {
	        Enemy enemy = EnemyArray.get(i);
	        float distX = player.position.x - enemy.position.x;
	        float distY = player.position.y - enemy.position.y;
	        float angle = (float) Math.atan2(distY, distX);
	        enemy.rotationAngle = angle;
	        enemy.update(deltaTime);
	        
	        if(enemy.state == Enemy.ENEMY_STATE_DEAD){
	        	float xPos = enemy.position.x;
	        	float yPos = enemy.position.y; 
	        	EnemyArray.remove(enemy);
	        	i = EnemyArray.size();	
	        	int genPowerUp = rand.nextInt(100);
	        	if(genPowerUp > 95)
	        	{
	        		addPowerUp(xPos, yPos, PowerUp.POWERUP_TYPE_SHOTGUN);
	        	}
	        	else if(genPowerUp > 85 && genPowerUp < 90)
	        	{
	        		addPowerUp(xPos, yPos, PowerUp.POWERUP_TYPE_ROCKET);
	        	}
	        	else if(genPowerUp > 80 && genPowerUp < 85)
	        	{
	        		addPowerUp(xPos, yPos, PowerUp.POWERUP_TYPE_RIFLE);
	        	}
	        }
	    }
	}
	private void updateExplosions(float deltaTime) {
		try{	
			explosion.update(deltaTime);
		} catch(Exception e){}
	}
//	
	private void checkCollisions() {
		checkEnemyBulletCollisions();
		checkPlayerEnemyCollisions();
	    //checkAmmoCollisions();
	    checkPowerUpCollisions();
	    checkLevelPlayerCollisions();
	}
	
	// Enemy - Player collision
	private void checkPlayerEnemyCollisions() {
	    int len = EnemyArray.size();
	    synchronized (EnemyArray) {
	    	for (int i = 0; i < len; i++) {
		        Enemy enemy = EnemyArray.get(i);

		        if (OverlapTester.overlapRectangles(enemy.bounds, player.bounds)) {
		        	
		        	len = EnemyArray.size();
		        	player.state = Player.PLAYER_STATE_HIT;
		            //listener.hit();
		        }
		    }
		}   
	}
	
	// Enemy - Ammo collision
	private void checkEnemyBulletCollisions() {
	    int elen = EnemyArray.size();
	    int alen = bulletArray.size();
	    synchronized (EnemyArray) {
		    for (int i = 0; i < elen; i++) {
		        Enemy enemy = EnemyArray.get(i);
		        synchronized (enemy) {
		        	for (int j = 0; j < alen; j++){
			        	Bullet bul = bulletArray.get(j);
				        if (OverlapTester.overlapRectangles(bul.bounds, enemy.bounds)) {
				        	bulletArray.remove(bul);
				            alen = bulletArray.size();
				            
				            enemy.life -= 1; 
				            
				            // Add particle effect 
					    	explosion = new Explosion(20, (int)enemy.position.x, (int)enemy.position.y);
				            //enemy.life -= bul.weaponDamage; 
				            //listener.enemyHit();
				        }
			        }
				} 
		    }
		}
	}
	
	// Powerup - Player collision
	private void checkPowerUpCollisions() {
	    int len = PowerUpArray.size();
	    synchronized (PowerUpArray) {
	    	for (int i = 0; i < len; i++) {
		        PowerUp pup = PowerUpArray.get(i);
		        if (OverlapTester.overlapRectangles(pup.bounds, player.bounds))
		        {
		        	player.weapon.setType(pup.type);
		        	//score += pup.score;
		        	PowerUpArray.remove(pup);
		        	len = PowerUpArray.size();
		            //listener.pickUpPup();
		        }
		    }
		}   
	}
	
	// LevelObjects - Player collision
	private void checkLevelPlayerCollisions() {
	    int len = levelObjectsArray.size();
	    synchronized (levelObjectsArray) {
	    	for (int i = 0; i < len; i++) {
		        LevelObject lev = levelObjectsArray.get(i);
		        if (OverlapTester.overlapRectangles(lev.bounds, player.bounds))
		        {
		        	lev.state = LevelObject.STATE_COLLIDED;
		        	//listener.pickUpPup();
		        }else lev.state = LevelObject.STATE_IDLE;
		    }
		}   
	}
//	
//	private void checkGameOver() {  	  	
////    	if (tank.life <=  0) {
////            state = WORLD_STATE_GAME_OVER;
////            listener.gameOver();
////        }
//	}
	
	private void addEnemy(){
		int pos  = rand.nextInt(4);
		int diff = rand.nextInt(5) + 2;
		if(pos == 0)
		{
			EnemyArray.add(new Enemy(WORLD_WIDTH/2+rand.nextInt(5) - 5, -10, Enemy.ENEMY_TYPE_ZOMBIE, diff));
		}
		else if(pos == 1)
		{
			EnemyArray.add(new Enemy(-10, WORLD_HEIGHT/2+rand.nextInt(5) - 5, Enemy.ENEMY_TYPE_ZOMBIE, diff));
		}
		else if(pos == 2)
		{
			EnemyArray.add(new Enemy(WORLD_WIDTH/2+rand.nextInt(5) - 5, WORLD_HEIGHT + 10, Enemy.ENEMY_TYPE_ZOMBIE, diff));
		}
		else
		{
			EnemyArray.add(new Enemy(WORLD_WIDTH + 10, WORLD_HEIGHT/2+rand.nextInt(5) - 5, Enemy.ENEMY_TYPE_ZOMBIE, diff));
		}
	}
	
	public void addBullet(float angle){
		synchronized (bulletArray) {	
			if(lastBulletFiredTime > player.weapon.getFireRate()) {	
				// Condition to regulate the bullets being fired
					if(player.weapon.getType() == Weapon.WEAPON_PISTOL ||
					   player.weapon.getType() == Weapon.WEAPON_RIFLE  || 
					   player.weapon.getType() == Weapon.WEAPON_ROCKET )
					{
						bulletArray.add(new Bullet(player.position.x + (float)(Math.cos(angle/180*3.146)), 
													   player.position.y + (float)(Math.sin(angle/180*3.146)),
													   angle, player.weapon.getBulletSpeed()));
					
					}
					else if (player.weapon.getType() == Weapon.WEAPON_SHOTGUN) 
					{
						bulletArray.add(new Bullet(player.position.x + (float)(Math.cos(angle/180*3.146)), 
								   player.position.y + (float)(Math.sin(angle/180*3.146)),
								   angle + 5,
								   player.weapon.getBulletSpeed()));
						bulletArray.add(new Bullet(player.position.x + (float)(Math.cos((angle)/180*3.146)), 
								   player.position.y + (float)(Math.sin(angle/180*3.146)),
								   angle,
								   player.weapon.getBulletSpeed()));
						bulletArray.add(new Bullet(player.position.x + (float)(Math.cos(angle)/180*3.146), 
								   player.position.y + (float)(Math.sin(angle/180*3.146)),
								   angle - 5,
								   player.weapon.getBulletSpeed()));
						
					}
					lastBulletFiredTime = 0.0f;
					//Decrement ammo
					player.weapon.fire(); 
			}
			else {
				lastBulletFiredTime += 0.1f;
			}
		}
	}
	
	public void addPowerUp(float xPos, float yPos, int type){
		// Max of 5 powerups at the same time
		if(PowerUpArray.size() < 5)
		{
			PowerUpArray.add(new PowerUp(xPos, yPos, type));
		}
	}
}

