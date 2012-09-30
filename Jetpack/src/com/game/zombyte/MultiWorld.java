package com.game.zombyte;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.net.NetworkInfo.DetailedState;

import com.bag.lib.math.OverlapTester;
import com.game.network.*;
/*
 * Master class holding all game objects and regulating their interactions
 */

public class MultiWorld {
	
	// Interface, mostly used to access sound effects
    public interface MultiWorldListener {
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
    
    public final Player player;
    public final Player player2;
    
    public final List<Bullet> bulletArray;
    public final List<Enemy> EnemyArray;
    public final List<PowerUp> PowerUpArray;
    public final List<LevelObject> levelObjectsArray;

    public final List<RocketExplosion> rocketExplosionArray;
    public Explosion explosion;
    public final MultiWorldListener listener;
    public final Random rand;
    
    public float lastBulletFiredTime;
    
    public int state;
    public int score;
    
    public com.game.network.server server;
    
    public MultiWorld(MultiWorldListener listener) {

        this.bulletArray = new ArrayList<Bullet>();
        this.EnemyArray = new ArrayList<Enemy>();
        this.PowerUpArray = new ArrayList<PowerUp>();
        this.levelObjectsArray = new ArrayList<LevelObject>();
        this.rocketExplosionArray = new ArrayList<RocketExplosion>();
        
    	player = new Player(WORLD_WIDTH/2, 10);
    	player2 = new Player(WORLD_WIDTH/2, 10);

    	this.listener = listener;
        
        rand = new Random();
       
        this.state = WORLD_STATE_RUNNING;
        lastBulletFiredTime = 0.0f;
        score = 0;
        
        explosion = null;
        
        initEnemies();
        LevelModifier.addTreesToMap(this);
        
        // Network time
        server = new server();
        server.initConnection();
    }
    
    private void initEnemies()
    {
//    	for (int i = 0; i < 10; i++) {
//			addEnemy(); 
//		}
    }
	
	public void update(float deltaTime, float speed) {
		updateNetwork(deltaTime);
		updatePlayer(deltaTime, speed);
		updateBullet(deltaTime);
		updateEnemies(deltaTime);
		updatePowerUp(deltaTime);
		updateExplosions(deltaTime);
		updateLevelObjects(deltaTime);
		updateRocketExplosions(deltaTime);
		checkCollisions();
		checkGameOver();
	}
	
	private void updateNetwork(float deltaTime){
		try{
			player2.position.x = Float.parseFloat(server.getPlayerInfo("x"));
			player2.position.y = Float.parseFloat(server.getPlayerInfo("y"));
		
			if(server.getBulletInfo("avail").equals("true")){
				bulletArray.add(new Bullet(player2.position.x, player2.position.y, Float.parseFloat(server.getBulletInfo("angle")), 20));
			}
		
		} 
		catch(Exception e){}
		try{
		server.setPlayerData(String.valueOf(player.position.x), String.valueOf(player.position.y), "5", "4");
		server.sendData();
		} catch(Exception e){};
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
//	    if(len <= 2)
//	    {
//	    	for(int i=0; i<10; i++)
//	    	{
//	    		addEnemy();
//	    	}
//	    }
	    
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
	        if(player.state == Player.PLAYER_STATE_HIDDEN && !player.isHiddenForTooLong)
	        { 
	        	enemy.state = Enemy.ENEMY_STATE_RETARDED;
	        }
	        else{
	        	enemy.state = Enemy.ENEMY_STATE_ALIVE;
	        }
	    }
	}
	private void updateExplosions(float deltaTime) {
		try{	
			explosion.update(deltaTime);
		} catch(Exception e){}
	}
//	
	
	private void updateRocketExplosions(float deltaTime) {
		try{
			for (int i = 0; i < rocketExplosionArray.size(); i++) {
				RocketExplosion exp = rocketExplosionArray.get(i);
				if(exp.state == RocketExplosion.ROCKETEXP_STATE_ACTIVE)
				{
					exp.update(deltaTime);
				}
				else
				{
					rocketExplosionArray.remove(i);
				}
			}
		} catch(Exception e){}
	}
	
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
				        	if(player.weapon.getType() == Weapon.WEAPON_ROCKET)
				        	{
					        	float xPos = bul.position.x;
					        	float yPos = bul.position.y;
					        	rocketExplosionArray.add(new RocketExplosion(xPos, yPos));
				        	}
				        	
				        	bulletArray.remove(bul);
				            alen = bulletArray.size();
				            
				            enemy.life -= 1; 
				            score += enemy.score;
				            // Add particle effect 
					    	explosion = new Explosion(20, (int)enemy.position.x, (int)enemy.position.y, 5);
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
		        	player.state = Player.PLAYER_STATE_HIDDEN;
		        }else {
		        	player.isHiddenForTooLong = false;
		        	lev.state = LevelObject.STATE_IDLE;
		        }
		    }
		}   
	}
//	
	private void checkGameOver() {  	  	
    	if (player.life <=  0) {
            state = WORLD_STATE_GAME_OVER;
            //listener.gameOver();
        }
	}
	
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
					try{
					server.setBulletData(String.valueOf(player.position.x), String.valueOf(player.position.y),
							String.valueOf(angle), "3", "true"); 
					} catch(Exception e){}
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

