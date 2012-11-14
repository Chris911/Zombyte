package com.game.zombyte;

import javax.microedition.khronos.opengles.GL10;

import com.bag.lib.gl.Animation;
import com.bag.lib.gl.Camera2D;
import com.bag.lib.gl.SpriteBatcher;
import com.bag.lib.gl.TextureRegion;
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

    	if( world.round < 6 || world.round > 11) {
    		batcher.beginBatch(Assets.mapItems1);
    		batcher.drawSprite(World.WORLD_WIDTH/2, World.WORLD_HEIGHT/2, World.WORLD_WIDTH, World.WORLD_HEIGHT, Assets.MapBackground1);
    		batcher.endBatch();
    	} else {
    		batcher.beginBatch(Assets.mapItems2);
    		batcher.drawSprite(World.WORLD_WIDTH/2, World.WORLD_HEIGHT/2, World.WORLD_WIDTH, World.WORLD_HEIGHT, Assets.MapBackground2);
    		batcher.endBatch();
    	}
    }
    
    public void renderObjects() {
        GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_BLEND);
        gl.glEnable(GL10.GL_LINE_SMOOTH);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
    
        gl.glColor4f(1, 1, 1, 1);
        
        if(world.player.life > 0)
        	renderPlayer();
        
        renderExplosions();
        renderRocketExplosion();
        renderEnemies();
        renderAmmo();
        renderPowerUp();
        renderLevelObjects();
        
        gl.glDisable(GL10.GL_BLEND);
    }
    
    private void renderPlayer() {
    	try{
    	batcher.beginBatch(Assets.players);
    	
    	Player player = world.player;
    	
    	// Assign camera position to follow the player.  Don't overlap out of bounds
		if(world.player.position.x < World.WORLD_WIDTH * 0.75 && world.player.position.x > World.WORLD_WIDTH * 0.25)
			cam.position.x = world.player.position.x;
		if(world.player.position.y < World.WORLD_HEIGHT * 0.75 && world.player.position.y > World.WORLD_HEIGHT * 0.25)
			cam.position.y = world.player.position.y;
       
    	// Draw the player sprite
		if(player.state == Player.PLAYER_STATE_IDLE)
		{
			batcher.drawSprite(world.player.position.x, world.player.position.y , Player.PLAYER_WIDTH, 
					Player.PLAYER_HEIGHT, (world.player.rotationAngle - 90), Assets.playerIdle);
		}
		else if(player.state == Player.PLAYER_STATE_MOVING)
		{
			TextureRegion keyFrame = Assets.playerAnimation.getKeyFrame(world.player.stateTime, Animation.ANIMATION_LOOPING);
        	batcher.drawSprite(player.position.x, player.position.y, Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT, player.rotationAngle-90, keyFrame);
		}

        batcher.endBatch();
    	} catch (Exception e){
    	}
    }
    
    private void renderExplosions() {
      
        GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_BLEND);
        gl.glClear(GL10.GL_DEPTH_BUFFER_BIT);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	  	try {

			batcher.beginBatch(Assets.tileMapItems);

	        for(int i = 0; i < world.explosionArray.size(); i++) {
	        	Explosion exp = world.explosionArray.get(i);
		        for(int j = 0; j < exp.particles.size(); j++) {
		            Particle par = exp.particles.get(j);
		            gl.glColor4f(1, 1, 1, par.alpha);
		      	  	batcher.drawSprite(par.x, par.y , 0.5f, 0.5f, Assets.redTile); 	  	
		        }
	        }
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
	            	if(enemy.state != Enemy.ENEMY_STATE_RETARDED){
	            	TextureRegion keyFrame = Assets.zombieMoveAnimation.getKeyFrame(enemy.stateTime, Animation.ANIMATION_LOOPING);
	            	batcher.drawSprite(enemy.position.x, enemy.position.y, 1.4f, 1.4f,(enemy.rotationAngle - 90)*180/3.146f, keyFrame);
	            	}
	            	else {
		            	TextureRegion keyFrame = Assets.zombieMoveAnimation.getKeyFrame(enemy.stateTime, Animation.ANIMATION_LOOPING);
		            	batcher.drawSprite(enemy.position.x, enemy.position.y, 1.4f, 1.4f,(enemy.randomAngleX - 90)*180/3.146f, keyFrame);
	            	}
	            } 
	            else if ( enemy.type == Enemy.ENEMY_TYPE_BOSS ) 
	            {
	            	TextureRegion keyFrame = Assets.zombieMoveAnimation.getKeyFrame(enemy.stateTime, Animation.ANIMATION_LOOPING);
	            	batcher.drawSprite(enemy.position.x, enemy.position.y, enemy.bounds.width, enemy.bounds.height,
	            			(enemy.rotationAngle - 90)*180/3.146f, keyFrame);
	            }
	        }
	        batcher.endBatch();
    	}
    	catch(Exception e) { }
    }
    
    private void renderRocketExplosion() {
    	try {
	    	batcher.beginBatch(Assets.explosionMap);
	    	
	        int len = world.rocketExplosionArray.size();
	        for(int i = 0; i < len; i++) {
	            RocketExplosion exp = world.rocketExplosionArray.get(i);  
            	
	            TextureRegion keyFrame = Assets.explosionAnimation.getKeyFrame(exp.stateTime, Animation.ANIMATION_LOOPING);
            	batcher.drawSprite(exp.position.x, exp.position.y, 1.5f, 1.5f, keyFrame);
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
                	batcher.drawSprite(bullet.position.x, bullet.position.y, 
     					   0.5f, 0.5f, 
     				       bullet.rotationAngle + 90, Assets.rocketBullet);
                }
            }
            
            batcher.endBatch();
            
		} catch (Exception e) {}
    }
     
    private void renderPowerUp() {
    	try {
	    	batcher.beginBatch(Assets.spritesMap);
	    	
	        int len = world.PowerUpArray.size();
	        for(int i = 0; i < len; i++) {
	            PowerUp powerup = world.PowerUpArray.get(i);   
	            
	            if(powerup.type == PowerUp.POWERUP_TYPE_SHOTGUN)
	            {
	            	//TextureRegion keyFrame = Assets.enemyMove.getKeyFrame(enemy.stateTime, Animation.ANIMATION_LOOPING);
	            	batcher.drawSprite(powerup.position.x, powerup.position.y, (float)(PowerUp.BASIC_WIDTH*1.5), (float)(PowerUp.BASIC_HEIGHT*1.5),
	            					  (powerup.rotationAngle - 90)*180/3.146f, Assets.shotgun);
	            	
	            } 
	            else if (powerup.type == PowerUp.POWERUP_TYPE_ROCKET) 
	            {
	            	//TextureRegion keyFrame = Assets.enemyMove.getKeyFrame(enemy.stateTime, Animation.ANIMATION_LOOPING);
	            	batcher.drawSprite(powerup.position.x, powerup.position.y, (float)(PowerUp.BASIC_WIDTH*1.5), (float)(PowerUp.BASIC_HEIGHT*1.5), 
	            					  (powerup.rotationAngle - 90)*180/3.146f, Assets.rocket);
	            }
	            else if (powerup.type == PowerUp.POWERUP_TYPE_RIFLE) 
	            {
	            	//TextureRegion keyFrame = Assets.enemyMove.getKeyFrame(enemy.stateTime, Animation.ANIMATION_LOOPING);
	            	batcher.drawSprite(powerup.position.x, powerup.position.y, PowerUp.BASIC_WIDTH, PowerUp.BASIC_HEIGHT, 
	            			          (powerup.rotationAngle - 90)*180/3.146f, Assets.rifle);
	            }
	            else if (powerup.type == PowerUp.POWERUP_TYPE_LIFE) 
	            {
	            	batcher.beginBatch(Assets.hearts);
	            	//TextureRegion keyFrame = Assets.enemyMove.getKeyFrame(enemy.stateTime, Animation.ANIMATION_LOOPING);
	            	batcher.drawSprite(powerup.position.x, powerup.position.y, PowerUp.BASIC_WIDTH, PowerUp.BASIC_HEIGHT, 
	            			          (powerup.rotationAngle - 90)*180/3.146f, Assets.heartFull);
	            	batcher.endBatch();
	            }
	        }
	       
	        batcher.endBatch();
    	}
    	catch(Exception e) { }
    }
    
    private float levelObjectsAlpha = 1.0f;
    private int alphaCounter = 0;
    private void renderLevelObjects()
    {
    	try {
            GL10 gl = glGraphics.getGL();
            gl.glEnable(GL10.GL_BLEND);
            gl.glEnable(GL10.GL_LINE_SMOOTH);
            gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
            
	    	batcher.beginBatch(Assets.spritesMap);
	    	
	        int len = world.levelObjectsArray.size();
	        for(int i = 0; i < len; i++) {
	            LevelObject lev = world.levelObjectsArray.get(i);
	            if(lev.alphaLevel == 1.0f)
	            	alphaCounter++;
	        }
	        
	        if(alphaCounter != len)
	        	levelObjectsAlpha = 0.6f;
	        else
	        	levelObjectsAlpha = 1.0f;
	        
	        gl.glColor4f(1, 1, 1, levelObjectsAlpha);
	        for(int i = 0; i < len; i++) {
	            LevelObject lev = world.levelObjectsArray.get(i);
	            batcher.drawSprite(lev.position.x, lev.position.y, lev.size, lev.size ,lev.asset);
	        }
	        batcher.endBatch();
	        alphaCounter = 0;
    	}
    	catch(Exception e) { }	
    }
}


