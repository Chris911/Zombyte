package com.game.jetpack;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.bag.lib.Game;
import com.bag.lib.Input.TouchEvent;
import com.bag.lib.Screen;
import com.bag.lib.gl.Camera2D;
import com.bag.lib.gl.SpriteBatcher;
import com.bag.lib.impl.GLGame;
import com.bag.lib.impl.GLScreen;
import com.bag.lib.math.OverlapTester;
import com.bag.lib.math.Vector2;

public class MainMenuScreen extends GLScreen {
    Camera2D guiCam;
    SpriteBatcher batcher;
    Vector2 touchPoint;
    AnimationHandler animationHandler;
    
    float sceneAlpha;
    float sceneAngle;
    boolean changeScreen;
    Screen screen;
    
    ArrayList<UIButton> buttonsAssets;
    UIButton playButton;
    UIButton highScoresButton;


    public MainMenuScreen(Game game) {
        super(game);
        
        // Main GL camera
        guiCam = new Camera2D(glGraphics, 800, 480);
        
        // Batcher that will draw every sprites
        batcher = new SpriteBatcher(glGraphics, 100);
        
        // Touch location vector
        touchPoint = new Vector2();
        
        // Pre-load assets here
        Assets.load((GLGame) game);
        
        // UI Buttons and the array with all of their assets
        playButton = new UIButton(500,200,200,200,Assets.blueTile,Assets.redTile);
        highScoresButton = new UIButton(200,200,200,200,Assets.blueTile,Assets.redTile);
        buttonsAssets = new ArrayList<UIButton>();
        buttonsAssets.add(playButton);
        buttonsAssets.add(highScoresButton);  
        
        animationHandler = new AnimationHandler(game, buttonsAssets);
        changeScreen = false;
        
        // Load previous game settings (sound enabled on/off)
        //Settings.load(game.getFileIO());
        
    }       

    @Override
    public void update(float deltaTime) {
    	
    	// Acquire all of touch events
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
        	
        	// Cycle through every touch events
            TouchEvent event = touchEvents.get(i);
            
            // Assign touch point after conversion to our World coordinates
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);
            
            if(event.type == TouchEvent.TOUCH_DOWN){
                if(OverlapTester.pointInRectangle(playButton.bounds, touchPoint)) {
                	playButton.state = UIButton.STATE_PRESSED;
                }	
            }
            
            // Detect touch on specific bounding rects
            if(event.type == TouchEvent.TOUCH_UP) { 
                if(playButton.state == UIButton.STATE_PRESSED) {
                	changeScreen = true;
                	screen = new GameScreen(game); 
                	playButton.state = UIButton.STATE_IDLE;
                }
            }
        }
        
        // Check if we are changing screen
        if(changeScreen) {
        	animationHandler.transitionToScreenWithRotateAnimation(screen);
        }
    }

    @Override
    // Draw method - draws the present assets
    public void present(float deltaTime) {
    	
    	// GL buffer flush
    	GL10 gl = glGraphics.getGL();
	    gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	    gl.glEnable(GL10.GL_TEXTURE_2D); 
	    guiCam.setViewportAndMatrices();
	    
	    gl.glEnable(GL10.GL_BLEND);
	    gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	    
        // Animate the menu screen and render the assets
	    animationHandler.renderAnimations(gl, batcher);
        
        gl.glDisable(GL10.GL_BLEND);
    }
    
    @Override
    public void pause() {        
        //Settings.save(game.getFileIO());
    }

    @Override
    public void resume() {        
    }       

    @Override
    public void dispose() {        
    }
}
