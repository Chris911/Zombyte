package com.game.zombyte;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.util.Log;

import com.bag.lib.math.OverlapTester;
import com.bag.lib.math.Rectangle;
import com.game.network.server;
/*
 * Master class holding all game objects and regulating their interactions
 */

public class MultiWorld {
	
	// Interface, mostly used to access sound effects
    public interface MultiWorldListener {
          //public void sound();
		int getTime();
		void playBulletHit();
		void playRocketHit();
		void playPlayerHit();
		void powerUpHit();
    }

    // World's size
    public static final float WORLD_WIDTH 			= 40;
    public static final float WORLD_HEIGHT 			= 22;
    
    // World's states
    public static final int WORLD_STATE_RUNNING 	= 0;
    public static final int WORLD_STATE_NEXT_LEVEL 	= 1;
    public static final int WORLD_STATE_GAME_OVER 	= 2;
    public static final int WORLD_STATE_FAILURE 	= 5;

    
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
    
    public Rectangle pause;
    
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
        
        LevelModifier.addTreesToMap(this);
        
        // Network time
        try{
        	server = new server();
        } catch(Exception e){
        	this.state = WORLD_STATE_FAILURE;
        }
        
        try{
            server.initConnection();
        } catch(Exception e){
        	this.state = WORLD_STATE_FAILURE;
        }
        player.life= 20;
        player2.life= 20;

    }
    
	public void update(float deltaTime, float speed) {
		updatePlayer(deltaTime, speed);
		updateBullet(deltaTime);
		updateExplosions(deltaTime);
		updateLevelObjects(deltaTime);
		checkCollisions();
		checkPlayerLife();
		//checkGameOver();
	}
	public boolean enemySpawn = false;
	
	private void updatePlayer(float deltaTime, float speed) {
	   
		// Get Enemy player Data 
		try{
			player2.position.x = Float.parseFloat(server.getPlayerInfo("x"));
			player2.position.y = Float.parseFloat(server.getPlayerInfo("y"));
			//Log.d("P2 incoming: ", "x: "+ server.getPlayerInfo("x"));
		} 
		catch(Exception e){
			Log.d("ERR: ",":"+e.toString());
		}
		
		try{
			if(server.getBulletInfo("avail").equals("true")){
				bulletArray.add(new Bullet(player2.position.x, player2.position.y, Float.parseFloat(server.getBulletInfo("angle")),20,Bullet.ENEMY_TEAM));
			}
		}
		catch (Exception e){}

		// update our player
	    player.update(deltaTime);
		
		// Send Our Player Data
		try{
			server.setPlayerData(String.valueOf(player.position.x), String.valueOf(player.position.y), "5", "1",String.valueOf(player.life));
			server.sendData();
		} catch(Exception e){};

	    Log.d("Life","P1"+player.life);
	    //.d("Life","P2"+player2.life);
	    
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
	
	private void updateLevelObjects(float deltaTime) {
		synchronized (levelObjectsArray) {
			for(int i = 0; i < levelObjectsArray.size(); i ++){
				levelObjectsArray.get(i).update();
			}
		}
	}
	
	private void updateExplosions(float deltaTime) {
		try{	
			explosion.update(deltaTime);
		} catch(Exception e){}
	}
	
	private void checkCollisions() {
		checkOurPlayerBulletCollisions();
		checkEnemyPlayerBulletCollisions();
	    checkPowerUpCollisions();
	    checkLevelPlayerCollisions();
	}
	
	private void checkOurPlayerBulletCollisions() {
	    int len = bulletArray.size();
	    synchronized (bulletArray) {
	    	for (int i = 0; i < len; i++) {
		        Bullet bul = bulletArray.get(i);

		        if (OverlapTester.overlapRectangles(bul.bounds, player.bounds) && bul.team == Bullet.ENEMY_TEAM) {
		        	bulletArray.remove(bul);
		        	len = bulletArray.size();
		        	//player.state = Player.PLAYER_STATE_BLINKING;
		        	listener.playPlayerHit();
		        	//We were hit! Oh Noes!!!
		        	player.life --;
		        	
		        }
		    }
		}
	}
	
	// We touched the enemy player with a bullet!
	private void checkEnemyPlayerBulletCollisions() {
	    int len = bulletArray.size();
	    synchronized (bulletArray) {
	    	for (int i = 0; i < len; i++) {
		        Bullet bul = bulletArray.get(i);

		        if (OverlapTester.overlapRectangles(bul.bounds, player2.bounds)) {
		        	bulletArray.remove(bul);
		        	len = bulletArray.size();
		        	player.state = Player.PLAYER_STATE_BLINKING;
		        	//The enemy was hit!
//		        	player2.life --;
		        	Assets.powerUp.play(0.5f);
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
	private void checkPlayerLife(){
		
		try{
			if (player.life <= 5) {
				player.position.x = 20;
				player.position.y = 10;
				server.setPlayerData("20", "10", "0", "0", "100");
				player.life = 20;
			} 
//			else if (player2.life <= 5) {
//				player2.position.x = Float.parseFloat(server.getPlayerInfo("x"));
//				player2.position.y = Float.parseFloat(server.getPlayerInfo("y"));
//				player.life = 20;
//			}
				//server.setPlayerData("0", "0", "0", "0", "0");
				//state = WORLD_STATE_GAME_OVER;
				//server.sendData();
			
		} catch(Exception e){}
	}
	
	private void checkGameOver() {  
		try{
			String gameOver = server.getPlayerInfo("avail");
			if(gameOver.equals("0")){
			if (player.life <=  0 || player2.life <= 0) {
				state = WORLD_STATE_GAME_OVER;
				//listener.gameOver();
			}
    	}
		} catch(Exception e){}
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
												   angle, player.weapon.getBulletSpeed(), Bullet.MY_TEAM));
					
					if(player.weapon.getType() != Weapon.WEAPON_ROCKET)
						listener.playBulletHit();
					else{
						listener.playRocketHit();
					}
				}
				else if (player.weapon.getType() == Weapon.WEAPON_SHOTGUN) 
				{
					bulletArray.add(new Bullet(player.position.x + (float)(Math.cos(angle/180*3.146)), 
							   player.position.y + (float)(Math.sin(angle/180*3.146)),
							   angle + 5,
							   player.weapon.getBulletSpeed(),Bullet.MY_TEAM));
					bulletArray.add(new Bullet(player.position.x + (float)(Math.cos((angle)/180*3.146)), 
							   player.position.y + (float)(Math.sin(angle/180*3.146)),
							   angle,
							   player.weapon.getBulletSpeed(),Bullet.MY_TEAM));
					bulletArray.add(new Bullet(player.position.x + (float)(Math.cos(angle)/180*3.146), 
							   player.position.y + (float)(Math.sin(angle/180*3.146)),
							   angle - 5,
							   player.weapon.getBulletSpeed(),Bullet.MY_TEAM));
					listener.playBulletHit();
					
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

