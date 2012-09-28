package com.game.jetpack;

import java.util.Random;

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
    //public final Tank tank;
    //public final List<Ammo> AmmoArray;
    //public final List<Enemy> EnemyArray;
    //public final List<PowerUp> PowerUpArray;
    public Explosion explosion;
    public final WorldListener listener;
    public final Random rand;
    
    public int state;
    
    // Tiles map
    public int[][] level;

    public World(WorldListener listener) {
        //this.AmmoArray = new ArrayList<Ammo>();
        //this.EnemyArray = new ArrayList<Enemy>();
        //this.PowerUpArray = new ArrayList<PowerUp>();
    	player = new Player(WORLD_WIDTH/2, 10);
        
    	this.listener = listener;
        
        rand = new Random();
       
        level = new int[(int) WORLD_WIDTH][(int) WORLD_HEIGHT];
        this.state = WORLD_STATE_RUNNING;
        
        // Initialize tiles map
        for(int i = 0; i < WORLD_WIDTH; i++)
        	for(int j = 0; j < WORLD_HEIGHT; j++)
        		level[i][j] = 0;
        
        explosion = null;
        generateLevel();
    }

    //Populate the level (tiles map) and adds enemies
	private void generateLevel() {
			
		int tileStyle = 0;
	    for(int i = 0; i < WORLD_WIDTH; i++)
	    	for(int j = 0; j < WORLD_HEIGHT; j++){
	    	
	    		if(j%2 == 0 && i%2 == 0)
	    			tileStyle = 1;
	    		else
	    			tileStyle = 2;
	    	
	    		level[i][j] = tileStyle;
	    	}
	}
	
	public void update(float deltaTime, float speed) {
		updatePlayer(deltaTime, speed);
		//updateAmmo(deltaTime);
		//updateEnemies(deltaTime);
		//updatePowerUp(deltaTime);
		updateExplosions(deltaTime);
		//checkCollisions();
		//checkGameOver();
	}

	private void updatePlayer(float deltaTime, float speed) {
	    //if(speed == 0)
	    //	player.state = Player.PLAYER_STATE_IDLE;
	    player.update(deltaTime);
	    if(player.state == Player.PLAYER_STATE_HIT_WALL) {
	    	explosion = new Explosion(50, (int)player.position.x, (int)player.position.y);
	    	player.state = player.previousState;
	    }
	}
	
//	private void updateAmmo(float deltaTime) {
////		synchronized (AmmoArray) {
////			for(int i = 0; i < AmmoArray.size(); i ++){
////				AmmoArray.get(i).update(deltaTime);
////				
////				if( AmmoArray.get(i).state == 0 )
////					AmmoArray.remove(i);
////			}
////		}
//	}
//	
//	private void updatePowerUp(float deltaTime) {
////		synchronized (PowerUpArray) {
////			for(int i = 0; i < PowerUpArray.size(); i ++){
////				PowerUpArray.get(i).update(deltaTime);
////			}
////		}
//	}
//	
//	
//	private void updateEnemies(float deltaTime) {
////	    int len = EnemyArray.size();
////	    for (int i = 0; i < len; i++) {
////	        Enemy enemy = EnemyArray.get(i);
////	        enemy.update(deltaTime);
////	        
////	    }
//	}
	private void updateExplosions(float deltaTime) {
		try{	
			explosion.update(deltaTime);
		} catch(Exception e){}
	}
//	
//	private void checkCollisions() {
//	    checkEnemyCollisions();
//	    checkAmmoCollisions();
//	    checkPowerUpCollisions();
//	}
	
//	// Enemy - Tank collision
//	private void checkEnemyCollisions() {
////	    int len = EnemyArray.size();
////	    synchronized (EnemyArray) {
////	    	for (int i = 0; i < len; i++) {
////		        Enemy enemy = EnemyArray.get(i);
////		        if (OverlapTester.overlapRectangles(enemy.bounds, tank.bounds)) {
////		        	
////		        	if(enemy.enemyType != Enemy.Enemy_TYPE_BOSS)
////		        		EnemyArray.remove(enemy);
////		        	
////		        	len = EnemyArray.size();
////		        	tank.life -= enemy.damage;
////		            tank.hitEnemy();
////		            listener.hit();
////		            addEnemy();
////		        }
////		    }
////		}   
//	}
//	
//	// Enemy - Ammo collision
//	private void checkAmmoCollisions() {
////	    int elen = EnemyArray.size();
////	    int alen = AmmoArray.size();
////	    synchronized (EnemyArray) {
////		    for (int i = 0; i < elen; i++) {
////		        Enemy enemy = EnemyArray.get(i);
////		        synchronized (enemy) {
////		        	for (int j = 0; j < alen; j++){
////			        	Ammo ammo = AmmoArray.get(j);
////				        if (OverlapTester.overlapRectangles(ammo.bounds, enemy.bounds)) {
////				            AmmoArray.remove(ammo);
////				            alen = AmmoArray.size();
////				            score += enemy.score;
////				            enemy.life -= ammo.weaponDamage; 
////				            listener.enemyHit();
////				            enemiesKilled++;
////				        }
////			        }
////				} 
////		    }
////		}
//	}
//	
//	private void checkGameOver() {  	  	
////    	if (tank.life <=  0) {
////            state = WORLD_STATE_GAME_OVER;
////            listener.gameOver();
////        }
//	}
	
	@SuppressWarnings("unused")
	private void addEnemy(){
		// add enemy here
	}
	
	public void addBullet(float angle){
		// add bullets
	}
	
	public void addPowerUp(int type){
		// add powerup
	}
}

