package com.game.jetpack;

import java.util.List;
import javax.microedition.khronos.opengles.GL10;

import android.util.Log;
import com.bag.lib.Game;
import com.bag.lib.Input.TouchEvent;
import com.bag.lib.gl.Camera2D;
import com.bag.lib.gl.FPSCounter;
import com.bag.lib.gl.SpriteBatcher;
import com.bag.lib.impl.GLScreen;
import com.bag.lib.math.OverlapTester;
import com.bag.lib.math.Rectangle;
import com.bag.lib.math.Vector2;

import com.game.jetpack.World.WorldListener;

@SuppressWarnings("unused")
public class GameScreen extends GLScreen {
	
	// States 
    static final int GAME_READY 	= 0;    
    static final int GAME_RUNNING 	= 1;
    static final int GAME_PAUSED 	= 2;
    static final int GAME_LEVEL_END = 3;
    static final int GAME_OVER 		= 4;
    
    // Dpad's positions
    static final int JOYSTICK_SIZE	= 128;
    
    // Screen size
    static final int SCREEN_WIDTH	= 800;
    static final int SCREEN_HEIGHT	= 480;

    int 			state;
    Camera2D 		guiCam;
    Vector2 		touchPoint;
    float 			velocity;
    float 			angle;
    SpriteBatcher 	batcher;  
    
    World 			world;
    WorldListener 	worldListener;
    WorldRenderer 	renderer;
    
    float 			startTime;
    float 			elapsedTime;
    FPSCounter 		fpsCounter;
    
	boolean 		gameOverTouch = false;
	
	// Move joystick
    Joystick		moveJoystick;
	boolean 		moveJoystickFirstTouch 	= true;
	boolean 		moveStickIsMoving 		= false;
	
	// Action joystick
    Joystick		actionJoystick;
	boolean 		actionJoystickFirstTouch = true;
	boolean 		actionStickIsMoving 	 = false;
	

    
    public GameScreen(Game game) {
        super(game);
        
        // Set state to Ready (ex : waiting touch to begin)
        state = GAME_READY;
        
        // Create camera with screen coordinates as viewport (to cover the whole screen)
        guiCam = new Camera2D(glGraphics, SCREEN_WIDTH, SCREEN_HEIGHT);
        
        // Prepare the first touch point
        touchPoint = new Vector2();
        
        // Create a sprite batcher capable of holding 5000 sprites
        batcher = new SpriteBatcher(glGraphics, 5000);
        
        // Create a worldListener, to trigger events on the world
        worldListener = new WorldListener() {
			
			public int getTime() {
				return (int)elapsedTime;
			}
        };
        
        // Create a world Instance
        world = new World(worldListener);
        
        // Create a renderer
        renderer = new WorldRenderer(glGraphics, batcher, world);
        
        // FPS Counter to help
        fpsCounter = new FPSCounter();
        
        // Variables
        velocity = 0;
        startTime = System.currentTimeMillis();
        elapsedTime = 0;

        moveJoystick 	= new Joystick(0, 0, JOYSTICK_SIZE);
        actionJoystick 	= new Joystick(0, 0, JOYSTICK_SIZE);


    }

	@Override
	// Main update method : calls other update state methods
	public void update(float deltaTime) {
	    
		if(deltaTime > 0.1f)
	        deltaTime = 0.1f;
	    
	    switch(state) {
	    case GAME_READY:
	        updateReady();
	        break;
	    case GAME_RUNNING:
	        updateRunning(deltaTime);
	        break;
	    case GAME_PAUSED:
	        updatePaused();
	        break;
	    case GAME_OVER:
	        updateGameOver();
	        break;
	    }
	}
	
	// Update when state is READY
	private void updateReady() {
		// First touch 
	    //if(game.getInput().getTouchEvents().size() > 0) {
	        state = GAME_RUNNING;
	    //}
	}
	
	// Update when state is RUNNING
	private void updateRunning(float deltaTime) {
	    List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
	    int len = touchEvents.size();
	    for(int i = 0; i < len; i++) {
	        TouchEvent event = touchEvents.get(i);
	        touchPoint.set(event.x, event.y);
	        guiCam.touchToWorld(touchPoint);
	        	        
	        if(event.type == TouchEvent.TOUCH_DRAGGED ||event.type == TouchEvent.TOUCH_DOWN){     
	        	
	        	// Touched in the left part of the screen
	        	if(touchPoint.x < SCREEN_WIDTH/2){ // First 1/3 (starting from left)
	        		handlePlayerMoveJoystickEvents();
	        	} //else if (touchPoint.x < SCREEN_WIDTH/3*2 && touchPoint.x >= SCREEN_WIDTH/3 ) { // between 1/3 and 2/3
	        		
	        	 else { // last 1/3
	        		handlePlayerActionJoystickEvents();
	        	}
	        }
	        else if(event.type == TouchEvent.TOUCH_UP){
	        	world.player.state = Player.PLAYER_STATE_IDLE;
	        	moveJoystickFirstTouch = true;
	        	actionJoystickFirstTouch = true;	
	        } 
	    }    
	    elapsedTime += deltaTime;
	    world.update(deltaTime, velocity);
	}
	
	private void updatePaused() {
		// game.setScreen(new MainMenuScreen(game));
	}	
	
	private void updateGameOver() {
	    List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
	    int len = touchEvents.size();
	    for(int i = 0; i < len; i++) {      
	        TouchEvent event = touchEvents.get(i);
	        touchPoint.set(event.x, event.y);
	        guiCam.touchToWorld(touchPoint);
	    }
	}

	@Override
	public void present(float deltaTime) {
		
		deltaTime /= 2; // to adjust framerate

		GL10 gl = glGraphics.getGL();
	    
		// Render game objects
		for(int i = 0; i < 2; i++){
		    gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		    gl.glEnable(GL10.GL_TEXTURE_2D);
			renderer.render();
		}
	    
	    guiCam.setViewportAndMatrices();
	    gl.glEnable(GL10.GL_BLEND);
	    gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	    gl.glColor4f(1, 1, 1, 0.8f);
	    
	    //batcher.beginBatch(Assets.gameScreenItems);
	    
	    switch(state) {
	    case GAME_READY:
	        presentReady();
	        break;
	    case GAME_RUNNING:
	        presentRunning();
	        break;
	    case GAME_PAUSED:
	        presentPaused();
	        break;
	    case GAME_LEVEL_END:
	        presentLevelEnd();
	        break;
	    case GAME_OVER:
	        presentGameOver();
	        break;
	    }
	    //batcher.endBatch();
	    gl.glDisable(GL10.GL_BLEND);
	    //fpsCounter.logFrame();
		
	}
	
	private void presentReady() {
         // Draw here
		drawUI();
	}
	
	private void presentRunning() {
		// Draw here
		drawUI();
	}
	
	private void presentPaused() { 
		// Draw here
	}
	
	private void presentLevelEnd() {
		// Draw here
	}
	
	private void presentGameOver() {
		// Draw here
	}
	
	private void drawUI()
	{
		try{
		batcher.beginBatch(Assets.tileMapItems);
		// Draw Move Joystick 
		if(!moveJoystickFirstTouch){
			
			// Base
			batcher.drawSprite(moveJoystick.basePosition.x, moveJoystick.basePosition.y, moveJoystick.size*2, moveJoystick.size*2, Assets.redTile);
			// Stick
			batcher.drawSprite(moveJoystick.stickPosition.x, moveJoystick.stickPosition.y, 100, 100, Assets.blueTile);
			
		}
		
		if(!actionJoystickFirstTouch){
			
			// Base
			batcher.drawSprite(actionJoystick.basePosition.x, actionJoystick.basePosition.y, actionJoystick.size*2, actionJoystick.size*2, Assets.redTile);
			// Stick
			batcher.drawSprite(actionJoystick.stickPosition.x, actionJoystick.stickPosition.y, 100, 100, Assets.blueTile);
			
		}
		
		batcher.endBatch();
		} catch(Exception e){
			
		}
	}

    @Override
    public void pause() {
    	
    }

    @Override
    public void resume() {        
    }

    @Override
    public void dispose() {       
    }
    
    // Handle a full pass through the move joystick possible events
    public void handlePlayerMoveJoystickEvents() {
    	
    	// Place the Joystick's base at the first touch location
    	if(moveJoystickFirstTouch) {
    		moveJoystick.setBasePosition(touchPoint);
    		moveJoystickFirstTouch = false;
    	}
    	
    	// Test if the touchpoint is inside the X bounds of the joystick 
    	// and Move the stick position if so
    	if(moveJoystick.moveStick_X(touchPoint)){
    		moveStickIsMoving = true;
    	}
    	
    	// Same in Y
    	if(moveJoystick.moveStick_Y(touchPoint)){
    		moveStickIsMoving = true;
    	}
    	
    	// If X or Y stick position has moved, get the distance which acts as
    	// a speed delimiter and assign it as the player's velocity
    	if(moveStickIsMoving){
    		world.player.velocity.x = moveJoystick.getStickBaseDistance().x/5;
    		world.player.velocity.y = moveJoystick.getStickBaseDistance().y/10;
    		moveStickIsMoving = false;
    	}
    		
    	Log.d("1- Touch at: ","X: "+touchPoint.x+" Y:"+touchPoint.y);
    	Log.d("1- Dpad base at: ","X: "+moveJoystick.basePosition.x+" Y:"+moveJoystick.basePosition.y);
    	Log.d("1- Dpad stick at: ","X: "+moveJoystick.stickPosition.x+" Y:"+moveJoystick.stickPosition.y);

    	world.player.state = Player.PLAYER_STATE_MOVING;
    }
    
    // Handle a full pass through the action joystick possible events
    public void handlePlayerActionJoystickEvents() {
    	
    	// Place the Joystick's base at the first touch location
    	if(actionJoystickFirstTouch) {
    		actionJoystick.setBasePosition(touchPoint);
    		actionJoystickFirstTouch = false;
    	}
    	
    	// Test if the touchpoint is inside the X bounds of the joystick 
    	// and Move the stick position if so
    	if(actionJoystick.moveStick_X(touchPoint)){
    		actionStickIsMoving = true;
    	}
    	
    	// Same in Y
    	if(actionJoystick.moveStick_Y(touchPoint)){
    		actionStickIsMoving = true;
    	}
    	
    	// If X or Y stick position has moved, get the distance which acts as
    	// a speed delimiter and assign it as the player's velocity
    	if(actionStickIsMoving){
    		world.addBullet(actionJoystick.getAngle());
    		actionStickIsMoving = false;
    	}
    		
    	//Log.d("2- Touch at: ","X: "+touchPoint.x+" Y:"+touchPoint.y);
    	//Log.d("2- Dpad base at: ","X: "+actionJoystick.basePosition.x+" Y:"+actionJoystick.basePosition.y);
    	//Log.d("2- Dpad stick at: ","X: "+actionJoystick.stickPosition.x+" Y:"+actionJoystick.stickPosition.y);

    }
}


