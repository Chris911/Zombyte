package com.game.jetpack;

import javax.microedition.khronos.opengles.GL10;

import com.bag.lib.gl.Camera2D;
import com.bag.lib.gl.SpriteBatcher;
import com.bag.lib.impl.GLGraphics;

public class WorldRenderer {
    static final float FRUSTUM_WIDTH = 20;
    static final float FRUSTUM_HEIGHT = 11;
	
    GLGraphics 		glGraphics;
    World 			world;
    Camera2D 		cam;
    SpriteBatcher 	batcher;    
    
    // Constructor of the world renderer
    // Draws every game objects in the world
    public WorldRenderer(GLGraphics glGraphics, SpriteBatcher batcher, World world) {
        this.glGraphics = glGraphics;
        this.world = world;
        this.cam = new Camera2D(glGraphics, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
        this.batcher = batcher;
        this.cam.zoom = 1.0f;
    }

    public void render() {
        cam.setViewportAndMatrices();
        renderBackground();
        renderObjects();
        renderExplosions();
    }
    
    public void renderBackground() {
    
//       batcher.beginBatch(Assets.tileMapItems); 
//       TextureRegion asset = null;
//       // Tiles map rendering!
//       for(int i = 0; i < World.WORLD_WIDTH; i++){
//        	for(int j = 0; j < World.WORLD_HEIGHT; j++){
//        		
//        		if(world.level[i][j] == 1)
//        			asset = Assets.redTile;       
//        		else if(world.level[i][j] == 2)
//        			asset = Assets.blueTile;     
//        		
//        		batcher.drawSprite(i, j , 1.0f, 1.0f, asset);
//        	}
//       }
//        
//        batcher.endBatch();
    	
    	batcher.beginBatch(Assets.mainMenuItems);
    	batcher.drawSprite(World.WORLD_WIDTH/2, World.WORLD_HEIGHT/2, World.WORLD_WIDTH, World.WORLD_HEIGHT, Assets.mainMenuBackground);
    	batcher.endBatch();
    }
    
    public void renderObjects() {
        GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_BLEND);
        gl.glEnable(GL10.GL_LINE_SMOOTH);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
    
        gl.glColor4f(1, 1, 1, 1);
        
        renderPlayer();
        renderEnemies();
        renderAmmo();
        renderPowerUp();
        
        gl.glDisable(GL10.GL_BLEND);
    }
    
    private void renderPlayer() {
    	batcher.beginBatch(Assets.playerItems);
    	
    	// Assign correct camera position to follow the player.  Don't overlap out of bounds
		if(world.player.position.x < World.WORLD_WIDTH * 0.75 && world.player.position.x > World.WORLD_WIDTH * 0.25)
			cam.position.x = world.player.position.x;
		if(world.player.position.y < World.WORLD_HEIGHT * 0.75 && world.player.position.y > World.WORLD_HEIGHT * 0.25)
			cam.position.y = world.player.position.y;
       
    	// Draw the player sprite
        batcher.drawSprite(world.player.position.x, world.player.position.y , Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT, Assets.player);

        batcher.endBatch();
    }
    
    private void renderExplosions() {
      
        GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_BLEND);
        gl.glClear(GL10.GL_DEPTH_BUFFER_BIT);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	  	try {

			batcher.beginBatch(Assets.tileMapItems);

	        for(int j = 0; j < world.explosion.particles.size(); j++) {
	        	
	            Particle par = world.explosion.particles.get(j);
	            gl.glColor4f(1, 1, 1, par.alpha);
	      	  	batcher.drawSprite(par.x, par.y , 0.5f, 0.5f, Assets.redTile);
	      	  	
	        }
        
	        //gl.glDisable(GL10.GL_BLEND);
	        batcher.endBatch();
	        
		} catch (Exception e) {}
	    gl.glColor4f(1, 1, 1, 1);
    }
    
    private void renderEnemies() {
    	try {
	    	batcher.beginBatch(Assets.spritesMap);
	    	
	        int len = world.EnemyArray.size();
	        for(int i = 0; i < len; i++) {
	            Enemy enemy = world.EnemyArray.get(i);  
	            
	            if(enemy.type == Enemy.ENEMY_TYPE_ZOMBIE)
	            {
	            	//TextureRegion keyFrame = Assets.enemyMove.getKeyFrame(enemy.stateTime, Animation.ANIMATION_LOOPING);
	            	batcher.drawSprite(enemy.position.x, enemy.position.y, 1.4f, 1.4f,(enemy.rotationAngle - 90)*180/3.146f, Assets.zombieIdle);
	            	
	            } 
	            else if ( enemy.type == Enemy.ENEMY_TYPE_BOSS ) 
	            {
	            	//TextureRegion keyFrame = Assets.enemyMove.getKeyFrame(enemy.stateTime, Animation.ANIMATION_LOOPING);
	            	batcher.drawSprite(enemy.position.x, enemy.position.y, 5, 5, (enemy.rotationAngle - 90)*180/3.146f, Assets.zombieIdle);
	            }
	        }
	       
	        batcher.endBatch();
    	}
    	catch(Exception e) { }
    }
    
    private void renderAmmo() {
    	try {
    		batcher.beginBatch(Assets.spritesMap);
        	
            int len = world.bulletArray.size();
            for(int i = 0; i < len; i++) {
                Bullet bullet = world.bulletArray.get(i);         
                     
               if(world.player.weapon.getType() == Weapon.WEAPON_PISTOL)        		  
                	batcher.drawSprite(bullet.position.x, bullet.position.y, 
                					   Bullet.BASIC_HEIGHT, Bullet.BASIC_WIDTH, 
                					   bullet.rotationAngle + 90, Assets.bullet);
                
                else if(world.player.weapon.getType() == Weapon.WEAPON_SHOTGUN)
                	batcher.drawSprite(bullet.position.x, bullet.position.y, 
                					   Bullet.BASIC_HEIGHT, Bullet.BASIC_WIDTH, 
                				       bullet.rotationAngle + 90, Assets.bulletRed);
               
                else if(world.player.weapon.getType() == Weapon.WEAPON_RIFLE)
                {
                	batcher.drawSprite(bullet.position.x, bullet.position.y, 
     					   Bullet.BASIC_HEIGHT, Bullet.BASIC_WIDTH, 
     				       bullet.rotationAngle + 90, Assets.bulletYellow);
                }
               
                else if(world.player.weapon.getType() == Weapon.WEAPON_ROCKET)
                {
                	// WE NEED TO DEAL WITH THIS - CHRIS
                	batcher.endBatch();
                	batcher.beginBatch(Assets.weaponsMap);
                	batcher.drawSprite(bullet.position.x, bullet.position.y, 
     					   Bullet.BASIC_HEIGHT, Bullet.BASIC_WIDTH, 
     				       bullet.rotationAngle + 90, Assets.rocketBullet);
                	batcher.endBatch();
                	batcher.beginBatch(Assets.spritesMap);
                }
            }
            
            batcher.endBatch();
            
		} catch (Exception e) {}
    }
    
    private void renderPowerUp() {
    	try {
	    	batcher.beginBatch(Assets.weaponsMap);
	    	
	        int len = world.PowerUpArray.size();
	        for(int i = 0; i < len; i++) {
	            PowerUp powerup = world.PowerUpArray.get(i);  
	            
	            if(powerup.type == PowerUp.POWERUP_TYPE_SHOTGUN)
	            {
	            	//TextureRegion keyFrame = Assets.enemyMove.getKeyFrame(enemy.stateTime, Animation.ANIMATION_LOOPING);
	            	batcher.drawSprite(powerup.position.x, powerup.position.y, 3.0f, 3.0f,(powerup.rotationAngle - 90)*180/3.146f, Assets.shotgun);
	            	
	            } 
	            else if (powerup.type == PowerUp.POWERUP_TYPE_ROCKET) 
	            {
	            	//TextureRegion keyFrame = Assets.enemyMove.getKeyFrame(enemy.stateTime, Animation.ANIMATION_LOOPING);
	            	batcher.drawSprite(powerup.position.x, powerup.position.y, 3.0f, 3.0f, (powerup.rotationAngle - 90)*180/3.146f, Assets.rocket);
	            }
	            else if (powerup.type == PowerUp.POWERUP_TYPE_RIFLE) 
	            {
	            	//TextureRegion keyFrame = Assets.enemyMove.getKeyFrame(enemy.stateTime, Animation.ANIMATION_LOOPING);
	            	batcher.drawSprite(powerup.position.x, powerup.position.y, 3.0f, 3.0f, (powerup.rotationAngle - 90)*180/3.146f, Assets.rifle);
	            }
	        }
	       
	        batcher.endBatch();
    	}
    	catch(Exception e) { }
    }
}


