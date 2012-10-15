package com.game.zombyte;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bag.lib.math.OverlapTester;

/*
 * Master class holding all game objects and regulating their interactions
 */

public class World {
	
	// Interface, mostly used to access sound effects
    public interface WorldListener {
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
    
    public static final int WORLD_BASE_ENEMIES_NB 	= 16;
    
    public final Player player;
    public final List<Bullet> bulletArray;
    public final List<Enemy> EnemyArray;
    public final List<PowerUp> PowerUpArray;
    public final List<LevelObject> levelObjectsArray;
    public final List<Explosion> explosionArray;
    public final List<RocketExplosion> rocketExplosionArray;
    
    public final WorldListener listener;
    public final Random rand;
    
    public float lastBulletFiredTime;
    public float gameTime;
    
    public int state;
    
    public int score;
    public int difficulty = 2; 
    public int round = 1;
	public int enemyCounter = 0;
    public int numberOfEnemiesKilled = 0;
    public int numberOfEnemiesToKillForNextRound = WORLD_BASE_ENEMIES_NB;
    public int numberOfEnemiesPreSpawend = 0;
    public int numberOfEnemiesToSpawn = 0;
    public boolean lastRoundWasBoss = false;
    
    public World(WorldListener listener) {
        this.bulletArray = new ArrayList<Bullet>();
        this.EnemyArray = new ArrayList<Enemy>();
        this.PowerUpArray = new ArrayList<PowerUp>();
        this.levelObjectsArray = new ArrayList<LevelObject>();
        this.explosionArray = new ArrayList<Explosion>();
        this.rocketExplosionArray = new ArrayList<RocketExplosion>();
        
    	player = new Player(WORLD_WIDTH/2, 10);
        
    	this.listener = listener;
        
        rand = new Random();
       
        this.state = WORLD_STATE_RUNNING;
        lastBulletFiredTime = 0.0f;
        score = 0;
        gameTime = 0;
        
        initEnemies();
        LevelModifier.addTreesToMap(this);
    }
    
	/***************************************
	 * 
	 * 	INITIALIZATION METHODS
	 * 
	 ***************************************/
    
    private void initEnemies()
    {
    	// Round 1 enemies
    	numberOfEnemiesToKillForNextRound = 15 + difficulty;
    	numberOfEnemiesPreSpawend = numberOfEnemiesToKillForNextRound;
    	for (int i = 0; i < numberOfEnemiesToKillForNextRound; i++) {
			addEnemy(); 
		}
    	numberOfEnemiesToSpawn = numberOfEnemiesToKillForNextRound - numberOfEnemiesPreSpawend;
    }
	 
    
	/***************************************
	 * 
	 * 	UPDATE METHODS
	 * 
	 ***************************************/
	public void update(float deltaTime, float speed) {
		gameTime += deltaTime;
		
		updatePlayer(deltaTime, speed);
		updateBullet(deltaTime);
		updateEnemies(deltaTime);
		updatePowerUp(deltaTime);
		updateExplosions(deltaTime);
		updateLevelObjects(deltaTime);
		updateRocketExplosions(deltaTime);
		checkCollisions();
		checkNextRound();
		checkGameOver();
	}

	private void updatePlayer(float deltaTime, float speed) {
	    checkPlayerEnemyCollisions();
		player.update(deltaTime);
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
	        	
	        	numberOfEnemiesKilled ++;
	        	if(numberOfEnemiesToSpawn > 0){
	        		addEnemy();
	        		numberOfEnemiesToSpawn--;
	        	}
	        	
	        	score += enemy.score*2;
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
	        	else if(genPowerUp > 77 && genPowerUp < 80)
	        	{
	        		addPowerUp(xPos, yPos, PowerUp.POWERUP_TYPE_LIFE);
	        	}
	        }
	        if(player.state == Player.PLAYER_STATE_HIDDEN)
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
			synchronized (explosionArray) {
				for(int i = 0; i < explosionArray.size(); i ++){
					Explosion exp = explosionArray.get(i);
					exp.update(deltaTime);
					
					if(exp.state == Explosion.STATE_DEAD)
						explosionArray.remove(i);
				}
			}
		} catch(Exception e){}
	}
	
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
	
	
	/***************************************
	 * 
	 * 	COLLISIONS DETECTION
	 * 
	 ***************************************/
	private void checkCollisions() {
		checkEnemyBulletCollisions();
		//checkPlayerEnemyCollisions();
		//checkEnemyEnemyCollisions();
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
		        	if(player.canTakeDamage){
		        		listener.playPlayerHit();
		        	}
		        	player.state = Player.PLAYER_STATE_BLINKING;
		        }
		    }
		}   
	}
	
//	// Enemy - Player collision
//	private void checkEnemyEnemyCollisions() {
//	    int len = EnemyArray.size();
//	    synchronized (EnemyArray) {
//	    	for (int i = 0; i < len; i++) {
//	    		Enemy enemy = EnemyArray.get(i);
//	    		for (int j = 0; j < len; j++) {
//			        
//	    			Enemy enemy2 = EnemyArray.get(j);
//	    			if(i != j){
//				        if (OverlapTester.overlapRectangles(enemy.bounds, enemy2.bounds)) {
//				        	enemy2.state = Enemy.ENEMY_STATE_COLLIDE;
//		
//				        }
//	    			}
//			    }
//		    }
//		}   
//	}
	
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
				            
				            enemy.life -= player.weapon.getDamage(); 
				            score += enemy.score;
				            
				            // Add particle effect 
					    	explosionArray.add(new Explosion(10, (int)enemy.position.x, (int)enemy.position.y, 0.5f));
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
		        	if(pup.type == PowerUp.POWERUP_TYPE_LIFE)
		        	{
		        		if(player.life <= 6)
		        			player.life+=2;
		        		else
		        			score += 500;
		        	}
		        	else
		        	{
		        		if(player.weapon.getType() != pup.type)
		        			player.weapon.setType(pup.type);
		        		else
		        			player.weapon.bulletsRemaining += 10;
		        	}	
		        	PowerUpArray.remove(pup);
		        	len = PowerUpArray.size();
		        	listener.powerUpHit();
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
		        	if(lev.type == LevelObject.GAMEOBJECT_TYPE_ROCK){
		        		LevelModifier.collidePlayerRock(player, lev);
		        	}
		        	lev.state = LevelObject.STATE_COLLIDED;
		        	player.state = Player.PLAYER_STATE_HIDDEN;
		        }
		        else
		        	lev.state = LevelObject.STATE_IDLE;
		    }
		}   
	}
	
	
	/***************************************
	 * 
	 * 	GAME STATE CHECKING
	 * 
	 ***************************************/
	// Next round
	private void checkNextRound()
	{
		if(numberOfEnemiesKilled == numberOfEnemiesToKillForNextRound )
		{
			listener.powerUpHit();
			EnemyArray.clear();
			explosionArray.clear();
			PowerUpArray.clear();
			bulletArray.clear();
			rocketExplosionArray.clear();
			
			numberOfEnemiesPreSpawend = 10 + difficulty;
			
			// Boss round
			if(round%5 == 0) {
				numberOfEnemiesToKillForNextRound = 4;
				numberOfEnemiesPreSpawend = numberOfEnemiesToKillForNextRound;
		    	for(int i=0; i < numberOfEnemiesPreSpawend; i++)
		    	{
		    		addBoss();
		    	}
		    	lastRoundWasBoss = true;
		    	
			} 
			// Regular round
			else {
				if(lastRoundWasBoss){
					numberOfEnemiesToKillForNextRound = WORLD_BASE_ENEMIES_NB + difficulty;
					lastRoundWasBoss = false;
				}
				
				numberOfEnemiesPreSpawend = 10 + difficulty;
		    	for(int i=0; i < numberOfEnemiesPreSpawend; i++)
		    	{
		    		addEnemy();
		    	}
		        numberOfEnemiesToKillForNextRound += difficulty;
		        numberOfEnemiesToSpawn = numberOfEnemiesToKillForNextRound - numberOfEnemiesPreSpawend;
			}
	    	
	        numberOfEnemiesKilled = 0;
			difficulty ++;
			player.life ++;
			score += 100 * (difficulty/2);
			this.state = WORLD_STATE_NEXT_LEVEL;

		}
	}
	
	private void checkGameOver() {  	  	
    	if (player.life <=  0) {
            state = WORLD_STATE_GAME_OVER;
        }
	}
	

	/***************************************
	 * 
	 * 	WORLD MODIFICATIONS METHODS
	 * 
	 ***************************************/
	
	// Add an enemy to the world
	private void addEnemy(){
		int pos  = rand.nextInt(4);
		
		if(pos == 0)
		{
			EnemyArray.add(new Enemy(enemyCounter%40, -10, Enemy.ENEMY_TYPE_ZOMBIE, difficulty));
		}
		else if(pos == 1)
		{
			EnemyArray.add(new Enemy(-10, enemyCounter%22, Enemy.ENEMY_TYPE_ZOMBIE, difficulty));
		}
		else if(pos == 2)
		{
			EnemyArray.add(new Enemy(enemyCounter%40 , WORLD_HEIGHT + 10, Enemy.ENEMY_TYPE_ZOMBIE, difficulty));
		}
		else
		{
			EnemyArray.add(new Enemy(WORLD_WIDTH + 10, enemyCounter%22, Enemy.ENEMY_TYPE_ZOMBIE, difficulty));
		}
		enemyCounter += 8;
	}
	
	// Add an enemy to the world
	private void addBoss(){
		int pos  = rand.nextInt(4);
		
		if(pos == 0)
		{
			EnemyArray.add(new Enemy(enemyCounter%40, -20, Enemy.ENEMY_TYPE_BOSS, difficulty+1));
		}
		else if(pos == 1)
		{
			EnemyArray.add(new Enemy(-2, enemyCounter%22, Enemy.ENEMY_TYPE_BOSS, difficulty+1));
		}
		else if(pos == 2)
		{
			EnemyArray.add(new Enemy(enemyCounter%40 , WORLD_HEIGHT + 27, Enemy.ENEMY_TYPE_BOSS, difficulty+1));
		}
		else
		{
			EnemyArray.add(new Enemy(WORLD_WIDTH + 13, enemyCounter%22, Enemy.ENEMY_TYPE_BOSS, difficulty+1));
		}
		enemyCounter += 8;
	}
	
	// Add a bullet to the world
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
		// Max of 3 powerups at the same time
		if(PowerUpArray.size() < 3)
		{
			PowerUpArray.add(new PowerUp(xPos, yPos, type));
		}
	}
}

