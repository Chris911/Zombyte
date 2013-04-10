package com.game.zombyte;

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
import com.game.store.Account;
import com.game.store.StoreScreen;
import com.game.utilities.Settings;

public class MainMenuScreen extends GLScreen {
    private Camera2D guiCam;
    private SpriteBatcher batcher;
    private Vector2 touchPoint;
    private MenuRenderer menuRenderer;
    
    private boolean changeScreen;
    public static boolean screenLock = false;

    private Screen screen;
    
    ArrayList<UIButton> buttonsAssets;
    UIButton singlePlayButton;
    //UIButton coopPlayButton;
    UIButton highScoresButton;
    UIButton tutorialButton;

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
        final int btnHeight = 90;
        final int btnWidth = 240;
        singlePlayButton = new UIButton(450, 350, btnWidth, btnHeight, Assets.menuStartBtn, Assets.menuStartBtn); 
        //coopPlayButton 	 = new UIButton(450, 285, btnWidth, btnHeight, Assets.menuCoopBtn, Assets.menuCoopBtn);
        highScoresButton = new UIButton(450, 225, btnWidth, btnHeight, Assets.menuSettingsBtn, Assets.menuSettingsBtn);
        tutorialButton   = new UIButton(450, 100, btnWidth, btnHeight, Assets.menuTutorialBtn, Assets.menuTutorialBtn);
        
        buttonsAssets = new ArrayList<UIButton>(); 
        buttonsAssets.add(singlePlayButton);
        buttonsAssets.add(highScoresButton);
        //buttonsAssets.add(coopPlayButton);
        buttonsAssets.add(tutorialButton);

        
        menuRenderer = new MenuRenderer(game, buttonsAssets);
        changeScreen = false;
        
        Assets.intro.play();
        // Load previous game settings (sound enabled on/off)
        Settings.load(game.getFileIO());
        
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
            
            if(event.type == TouchEvent.TOUCH_DOWN && !screenLock){
                if(OverlapTester.pointInRectangle(singlePlayButton.bounds, touchPoint)) {
                	singlePlayButton.state = UIButton.STATE_PRESSED;
                	screenLock = true;
                }	
                else if(OverlapTester.pointInRectangle(tutorialButton.bounds, touchPoint)) {
//                	tutorialButton.state = UIButton.STATE_PRESSED;
//                	screenLock = true;
                	//ZombyteActivity.startNewActivity();
                	
                }	
                else if(OverlapTester.pointInRectangle(highScoresButton.bounds, touchPoint)) {
                	highScoresButton.state = UIButton.STATE_PRESSED;
                	screenLock = true;
                }
            }
            
            // Detect touch on specific bounding rects
            if(event.type == TouchEvent.TOUCH_UP) {
            	
                if(singlePlayButton.state == UIButton.STATE_PRESSED) {
                	changeScreen = true;
                	screen = new GameScreen(game); 
                	singlePlayButton.state = UIButton.STATE_IDLE;
                } else if(tutorialButton.state == UIButton.STATE_PRESSED) {
                	changeScreen = true;
                	screen = new StoreScreen(game); 
                	tutorialButton.state = UIButton.STATE_IDLE;
                	
                } else if(highScoresButton.state == UIButton.STATE_PRESSED) {
                	changeScreen = true;
                	screen = new HighscoreScreen(game); 
                	highScoresButton.state = UIButton.STATE_IDLE;
                }
            }
        }
        
        // Check if we are changing screen
        if(changeScreen) {
        	menuRenderer.transitionToScreenWithZoomInAnimation(screen, deltaTime);
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
	    menuRenderer.renderAnimations(gl, batcher);
        
	    batcher.beginBatch(Assets.fontTex);
	    Assets.font.drawText(batcher, "Money:"+Account.getMoney(), 500,40); 
	    Assets.font.drawText(batcher, "Tokens:"+Account.getTokens(), 500,25); 
	    Assets.font.drawText(batcher, "Grade-F Productions", 500,10); 
	    batcher.endBatch();
	    
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
    
    public static void releaseScreenLock (){
    	screenLock = false;
    }
}
