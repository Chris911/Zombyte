package com.game.zombyte;

import java.util.List;
import javax.microedition.khronos.opengles.GL10;
import android.widget.Toast;

import com.bag.lib.Game;
import com.bag.lib.Input.TouchEvent;
import com.bag.lib.gl.Camera2D;
import com.bag.lib.gl.FPSCounter;
import com.bag.lib.gl.SpriteBatcher;
import com.bag.lib.impl.GLScreen;
import com.bag.lib.math.OverlapTester;
import com.bag.lib.math.Rectangle;
import com.bag.lib.math.Vector2;

import com.game.database.Highscore;
import com.game.database.HighscoreDataSource;
import com.game.database.HighscoreDatabaseHelper;
import com.game.zombyte.World.WorldListener;

@SuppressWarnings("unused")
public class GameScreen extends GLScreen {
	
	// States 
    static final int GAME_READY 	= 0;    
    static final int GAME_RUNNING 	= 1;
    static final int GAME_PAUSED 	= 2;
    static final int GAME_LEVEL_END = 3;
    static final int GAME_OVER 		= 4;
    
    // Dpad's positions
    static final int JOYSTICK_SIZE	= 90;
    
    // Screen size
    static final int SCREEN_WIDTH	= 800;
    static final int SCREEN_HEIGHT	= 480;

    int 			state;
    Camera2D 		guiCam;
    Vector2 		moveTouchPoint;
    Vector2 		actionTouchPoint;
    float 			velocity;
    float 			angle;
    float 			gameOverTime;
    float 			nextRoundTime;
    float			nextWeaponTime;
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
	
	boolean			shootTouchDown			= false;
    
    public GameScreen(Game game) {
        super(game);
        
        // Set state to Ready (ex : waiting touch to begin)
        state = GAME_READY;
        
        // Create camera with screen coordinates as viewport (to cover the whole screen)
        guiCam = new Camera2D(glGraphics, SCREEN_WIDTH, SCREEN_HEIGHT);
        
        // Prepare the first touch point
        moveTouchPoint = new Vector2();
        actionTouchPoint = new Vector2();

        // Create a sprite batcher capable of holding 5000 sprites
        batcher = new SpriteBatcher(glGraphics, 5000);
        
        // Create a worldListener, to trigger events on the world
        worldListener = new WorldListener() {
			
			public int getTime() {
				return (int)elapsedTime;
			}
			
			public void playBulletHit() {
				Assets.basicShoot.play(0.3f);
			}
			
			public void playRocketHit() {
				Assets.rocketShoot.play(0.3f);
			}
			
			public void playPlayerHit() {
				Assets.playerHit.play(0.05f);
			}
			
			public void powerUpHit() {
				Assets.powerUp.play(0.8f);
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
        gameOverTime = 0;
        nextRoundTime = 0;
        nextWeaponTime = 0;

        moveJoystick 	= new Joystick(0, 0, JOYSTICK_SIZE);
        moveJoystick.setBasePosition(new Vector2(100,75));
        actionJoystick 	= new Joystick(0, 0, JOYSTICK_SIZE);
        actionJoystick.setBasePosition(new Vector2(700,75));
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
	    case GAME_LEVEL_END:
	    	updateNextRound(deltaTime);
	        break;
	    case GAME_OVER:
	        updateGameOver(deltaTime);
	        break;
	    }
	}
	
	// Update when state is READY
	private void updateReady() {
		// First touch 
		Assets.intro.stop();
		Assets.gamemusic.play();
	    if(game.getInput().getTouchEvents().size() > 0) {
	        state = GAME_RUNNING;
	    }
	}
	
	// Update when state is RUNNING
	private void updateRunning(float deltaTime) {
	    List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
	    int len = touchEvents.size();
	    for(int i = 0; i < len; i++) { 
	        TouchEvent event = touchEvents.get(i);
	        	        
	        if(event.type == TouchEvent.TOUCH_DRAGGED ||event.type == TouchEvent.TOUCH_DOWN){     
	        	
	        	// Touched in the left part of the screen
	        	if(event.x < SCREEN_WIDTH/2 - 30){ // First 1/2 (starting from left)
	        		moveTouchPoint.set(event.x, event.y);
	    	        guiCam.touchToWorld(moveTouchPoint);
	        		handlePlayerMoveJoystickEvents();
	        	}	
	        	else if(event.x > SCREEN_WIDTH/2 + 30 && event.y >= 120) { // last 1/2
	        		shootTouchDown = true;
	        		actionTouchPoint.set(event.x, event.y);
	    	        guiCam.touchToWorld(actionTouchPoint);
	        	}
	        	else if(event.x <= SCREEN_WIDTH/2 + 30 && event.x >= SCREEN_WIDTH/2 - 30) {
	        		shootTouchDown = false;
	        	}

	        	
	        }
	        else if(event.type == TouchEvent.TOUCH_UP){
	        	world.player.state = Player.PLAYER_STATE_IDLE;
	        	moveJoystickFirstTouch = true;
	        	actionJoystickFirstTouch = true;
	        	
	        	if(event.x < SCREEN_WIDTH/2 - 30){ 
	                moveJoystick.resetStickPosition();
	        	}
	        	if(event.x > SCREEN_WIDTH/2 + 30 && event.y >= 120){ 
	        		shootTouchDown = false;
	                actionJoystick.resetStickPosition();
	        	}
	        	else if(event.x >= (3/4)*800 && event.y < 120){
	        		shootTouchDown = false;
	        		world.player.toggleWeapons();
	                actionJoystick.resetStickPosition();
	        	}
	        } 
	    }  
	    
	    if(shootTouchDown)
	    	handlePlayerActionJoystickEvents();
	    
	    elapsedTime += deltaTime;
	    world.update(deltaTime, velocity);
	    
	    if(world.state == World.WORLD_STATE_NEXT_LEVEL)
	    	this.state = GAME_LEVEL_END;
	    
	    if(world.state == World.WORLD_STATE_GAME_OVER)
	    	this.state = GAME_OVER;
	}
	
	private void updatePaused() {
		// game.setScreen(new MainMenuScreen(game));
	}	
	
	private void updateNextRound(float deltaTime) {
		// First touch 
		nextRoundTime+= deltaTime;
		
	    if(game.getInput().getTouchEvents().size() > 0 && nextRoundTime >= 2.0f) {
	        state = GAME_RUNNING;
	        world.state = World.WORLD_STATE_RUNNING;
			world.player.position.set(World.WORLD_WIDTH/2,World.WORLD_HEIGHT/2);
	        nextRoundTime = 0;
	    }
	}
	
	private void updateGameOver(float deltaTime) {
		gameOverTime+= deltaTime;
	    List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
	    int len = touchEvents.size();
	    for(int i = 0; i < len; i++) {      
	        TouchEvent event = touchEvents.get(i);
	        moveTouchPoint.set(event.x, event.y);
	        guiCam.touchToWorld(moveTouchPoint);
	        
	        if(event.type == TouchEvent.TOUCH_UP && gameOverTime > 2.0f) {
<<<<<<< HEAD
	        	if(world.score > 0)
	        	{
		        	HighscoreDataSource dbHelper = new HighscoreDataSource(ZombyteActivity.gameContext);
		        	dbHelper.open();
		        	dbHelper.createHighscore(new Highscore("GCA", world.score));
		        	dbHelper.close();
	        	}
=======

	    		Assets.gamemusic.stop();
	        	HighscoreDataSource dbHelper = new HighscoreDataSource(ZombyteActivity.gameContext);
	        	dbHelper.open();
	        	dbHelper.createHighscore(new Highscore("GCA", world.score));
	        	dbHelper.close();
>>>>>>> bb7c664022c093b914a508964a51c592bb052222
	        	
	        	game.setScreen(new MainMenuScreen(game));
	        }
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
	    gl.glColor4f(1, 1, 1, 0.6f);
	    
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
		GL10 gl = glGraphics.getGL();
		gl.glColor4f(1, 1, 1, 1);
	    batcher.beginBatch(Assets.fontTex);
	    Assets.font.drawText(batcher, "PRESS TO START!", 300, 300);
	    batcher.endBatch();
	}
	
	private void presentRunning() {
		// Draw here
		drawUI();

	}
	
	private void presentPaused() { 
		// Draw here
	}
	
	private void presentLevelEnd() {
		
		GL10 gl = glGraphics.getGL();
		gl.glColor4f(1, 1, 1, 1);
	    batcher.beginBatch(Assets.fontTex);
	    Assets.font.drawText(batcher, "PREPARE FOR ROUND "+world.round, 265, 300);
	    Assets.font.drawText(batcher, "CURRENT SCORE:"+world.score, 270, 320);
	    if(nextRoundTime > 1.5f)
	    	Assets.font.drawText(batcher, "TOUCH TO START!", 300, 150);

	    batcher.endBatch();
	}
	
	private void presentGameOver() {
		// Draw here
		GL10 gl = glGraphics.getGL();
		gl.glColor4f(1, 1, 1, 1);
	    batcher.beginBatch(Assets.fontTex);
	    Assets.font.drawText(batcher, "GAME OVER", 320, 300);
	    Assets.font.drawText(batcher, "FINAL SCORE:"+world.score, 290, 200);
	    batcher.endBatch();
	}
	
	private void drawUI()
	{
		try{
		batcher.beginBatch(Assets.joystickMap);
		
		// Base
		batcher.drawSprite(moveJoystick.basePosition.x, moveJoystick.basePosition.y, moveJoystick.size, moveJoystick.size, Assets.joystickBottom);
		// Stick
		batcher.drawSprite(moveJoystick.stickPosition.x, moveJoystick.stickPosition.y, JOYSTICK_SIZE, JOYSTICK_SIZE, Assets.joystickTop);
	
		// Base
		batcher.drawSprite(actionJoystick.basePosition.x, actionJoystick.basePosition.y, actionJoystick.size, actionJoystick.size, Assets.joystickBottom);
		// Stick
		batcher.drawSprite(actionJoystick.stickPosition.x, actionJoystick.stickPosition.y, JOYSTICK_SIZE, JOYSTICK_SIZE, Assets.joystickTop);
		
		batcher.endBatch();
	    
		GL10 gl = glGraphics.getGL();
		gl.glColor4f(1, 1, 1, 1);
		batcher.beginBatch(Assets.hubMap);
		batcher.drawSprite(128, 435, 256, 100, Assets.hubLeft);		
		batcher.drawSprite(672, 435, 256, 100, Assets.hubRight);		
		batcher.endBatch();
		
		batcher.beginBatch(Assets.hearts);
		
		if(world.player.life >= 2)
			batcher.drawSprite(65,  440, 32, 32, Assets.heartFull);
		else if(world.player.life >= 1)
			batcher.drawSprite(65,  440, 32, 32, Assets.heartHalf);
		else
			batcher.drawSprite(65,  440, 32, 32, Assets.heartEmpty);
		
		if(world.player.life >= 4)
			batcher.drawSprite(105,  440, 32, 32, Assets.heartFull);
		else if(world.player.life >= 3)
			batcher.drawSprite(105,  440, 32, 32, Assets.heartHalf);
		else
			batcher.drawSprite(105,  440, 32, 32, Assets.heartEmpty);
		
		if(world.player.life >= 6)
			batcher.drawSprite(145,  440, 32, 32, Assets.heartFull); 
		else if(world.player.life >= 5)
			batcher.drawSprite(145,  440, 32, 32, Assets.heartHalf);
		else
			batcher.drawSprite(145,  440, 32, 32, Assets.heartEmpty);

		batcher.endBatch();
		
	    batcher.beginBatch(Assets.fontTex);
	    Assets.font.drawText(batcher, "SCORE:" + world.score, 25, 465);
	    Assets.font.drawText(batcher, " x " + world.player.weapon.bulletsRemaining, 700, 450);
	    batcher.endBatch();
	     
//	    final float hubWeaponX = 675;
//	    final float hubWeaponY = 460;
	    final float hubWeaponX = 200;
	    final float hubWeaponY = 200;
	    final float hubWeaponRatio = 50.0f; 
	    batcher.beginBatch(Assets.spritesMap);
	    if(world.player.weapon.getType() == Weapon.WEAPON_PISTOL)
	    {
	    	batcher.drawSprite(650, 433, 75, 75, Assets.pistol);
	    }
	    else if(world.player.weapon.getType() == Weapon.WEAPON_SHOTGUN)
	    {
	    	batcher.drawSprite(670, 413, 100, 100, Assets.shotgun); 
	    }
	    else if(world.player.weapon.getType() == Weapon.WEAPON_ROCKET)
	    {
	    	batcher.drawSprite(675, 460, 100, 100, Assets.rocket);
	    }
	    else if(world.player.weapon.getType() == Weapon.WEAPON_RIFLE)
	    {
	    	batcher.drawSprite(660, 438, 65, 65, Assets.rifle);
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
    	
    	// Test if the touchpoint is inside the X bounds of the joystick 
    	// and Move the stick position if so
    	if(moveJoystick.moveStick_X(moveTouchPoint)){
    		moveStickIsMoving = true;
    	}
    	
    	// Same in Y
    	if(moveJoystick.moveStick_Y(moveTouchPoint)){
    		moveStickIsMoving = true;
    	}
    	
    	// If X or Y stick position has moved, get the distance which acts as
    	// a speed delimiter and assign it as the player's velocity
    	if(moveStickIsMoving){
    		world.player.velocity.x = moveJoystick.getStickBaseDistance().x/10;
    		world.player.velocity.y = moveJoystick.getStickBaseDistance().y/10;
    		world.player.rotationAngle = moveJoystick.getAngle();
    		moveStickIsMoving = false;
    	}

    	world.player.state = Player.PLAYER_STATE_MOVING;
    }
    
    // Handle a full pass through the action joystick possible events
    public void handlePlayerActionJoystickEvents() {
    	
    	// Test if the touchpoint is inside the X bounds of the joystick 
    	// and Move the stick position if so
    	if(actionJoystick.moveStick_X(actionTouchPoint)){
    		actionStickIsMoving = true;
    	}
    	
    	// Same in Y
    	if(actionJoystick.moveStick_Y(actionTouchPoint)){
    		actionStickIsMoving = true;
    	}
    	
    	// If X or Y stick position has moved, get the distance which acts as
    	// a speed delimiter and assign it as the player's velocity
    	if(actionStickIsMoving){
    		world.addBullet(actionJoystick.getAngle());
    		actionStickIsMoving = false;
    	}
    }
}


