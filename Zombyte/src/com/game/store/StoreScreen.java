package com.game.store;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.bag.lib.Game;
import com.bag.lib.Input.TouchEvent;
import com.bag.lib.gl.Camera2D;
import com.bag.lib.gl.SpriteBatcher;
import com.bag.lib.impl.GLScreen;
import com.bag.lib.math.Vector2;
import com.game.utilities.Settings;
import com.game.zombyte.Assets;
import com.game.zombyte.UIButton;

public class StoreScreen extends GLScreen {
   
	private Camera2D guiCam;
    private SpriteBatcher batcher;
    private Vector2 touchPoint;
    
    private boolean changeScreen;
    public static boolean screenLock = false;
    
    ArrayList<UIButton> buttonsAssets;
    
    ArrayList<Item> storeItems;
    ArrayList<ItemButton> onScreenItems;
    
    public final int itemButtonWidth = 40;
    public final int itemButtonHeight = 50;

    public StoreScreen(Game game) {
        super(game);
        
        // Main GL camera
        guiCam = new Camera2D(glGraphics, 800, 480);
        
        // Batcher that will draw every sprites
        batcher = new SpriteBatcher(glGraphics, 80);
        
        // Touch location vector
        touchPoint = new Vector2();
        
        // UI Buttons and the array with all of their assets        
        storeItems = new ArrayList<Item>();
        onScreenItems = new ArrayList<ItemButton>(); 

        changeScreen = false;
        
        Settings.load(game.getFileIO());
        
        // Init the store items
        addItemsToStoreList();
        displayItemsByCategory("c1");
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
//                if(OverlapTester.pointInRectangle(singlePlayButton.bounds, touchPoint)) {
//                	singlePlayButton.state = UIButton.STATE_PRESSED;
//                	screenLock = true;
//                }	
//                else if(OverlapTester.pointInRectangle(tutorialButton.bounds, touchPoint)) {
//                	tutorialButton.state = UIButton.STATE_PRESSED;
//                	screenLock = true;
//                }	
//                else if(OverlapTester.pointInRectangle(highScoresButton.bounds, touchPoint)) {
//                	highScoresButton.state = UIButton.STATE_PRESSED;
//                	screenLock = true;
//                }
            }
            
            // Detect touch on specific bounding rects
            if(event.type == TouchEvent.TOUCH_UP) {
            	
//                if(singlePlayButton.state == UIButton.STATE_PRESSED) {
//                	
//                	changeScreen = true;
//                	singlePlayButton.state = UIButton.STATE_IDLE;
//                } else if(tutorialButton.state == UIButton.STATE_PRESSED) {
//
//                } else if(highScoresButton.state == UIButton.STATE_PRESSED) {
//                	changeScreen = true;
//                }
            }
        }
        
        // Check if we are changing screen
        if(changeScreen) {
        }
    }

    @Override
    // Draw method - draws the present assets
    public void present(float deltaTime) {
	    
	    GL10 gl = glGraphics.getGL();
	    gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
	    gl.glEnable(GL10.GL_TEXTURE_2D); 
	    guiCam.setViewportAndMatrices();
	    
	    //gl.glEnable(GL10.GL_BLEND);
	    //gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	    
	    batcher.beginBatch(Assets.fontTex);
    	Assets.font.drawText(batcher, "ALLO QUEPASSA", 320, 350);
    	batcher.endBatch();
	    
	    // Render the items on screen
	    renderItems();
	    
        gl.glDisable(GL10.GL_BLEND);
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
    
    public static void releaseScreenLock (){
    	screenLock = false;
    }
    
    // Add items to the mega-store list (would be with DB)
    private void addItemsToStoreList()
    {
    	storeItems.add(new Item("Test1", "c1", 500, Assets.heartFull));
    	storeItems.add(new Item("Test2", "c1", 500, Assets.heartFull));
    	storeItems.add(new Item("Test3", "c3", 500, Assets.heartFull));
    	storeItems.add(new Item("Test4", "c1", 500, Assets.heartFull));
    	storeItems.add(new Item("Test5", "c1", 500, Assets.heartFull));
    }
    
    // Get all the items from a certain category
    private void displayItemsByCategory(String category)
    {
    	if(onScreenItems.size() > 0)
    		onScreenItems.clear();
    	
    	int j = 1;
    	int offsetX = 20;
    	int offsetY = 10;
    	
    	for(int i = 1; i <= storeItems.size(); i++)
    	{
    		Item it = storeItems.get(i-1);
    		if(it.category == category)
    		{
    			onScreenItems.add(new ItemButton(i*itemButtonWidth+offsetX, 
    											 j*itemButtonHeight + offsetY, 
    											 itemButtonWidth, itemButtonHeight,
    											 it));
    			if(onScreenItems.size() > 3)
    				j++;
    		}
    	}
    }   
    
    private void renderItems()
    {	    
    	
    	batcher.beginBatch(Assets.hearts);
    	for (int i = 0; i < onScreenItems.size(); i++) {
			ItemButton ib = onScreenItems.get(i);
			batcher.drawSprite(ib.position.x, ib.position.y, 100, 100, ib.item.activeAsset);
		}
        batcher.endBatch();

    }
}
