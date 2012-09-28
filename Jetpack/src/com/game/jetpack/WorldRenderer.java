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
        
        gl.glDisable(GL10.GL_BLEND);
    }
    
    private void renderPlayer() {
    	batcher.beginBatch(Assets.playerItems);
    	
    	// Assign correct camera position to follow the player.  Don't overlap out of bounds
    		cam.position.x = world.player.position.x;
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
}


