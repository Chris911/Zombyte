package com.game.jetpack;

import com.bag.lib.*;
import com.bag.lib.gl.*;
import com.bag.lib.impl.*;

public class Assets {
   
	// Main Menu
    public static Texture mainMenuItems;
    public static TextureRegion mainMenuBackground;
    
    // Game Interface
    
    // Game Objects
    public static Texture playerItems;
    public static TextureRegion player;
    
    public static Texture explosionItems;
    public static TextureRegion explo1;
    
    // Game Backgrounds
    public static Texture tileMapItems;
    public static TextureRegion blueTile;
    public static TextureRegion redTile;

    
    public static void load(GLGame game) {
       	
    	// Main Menu (UI)
    	mainMenuItems = new Texture(game, "mapPack1.png");
    	mainMenuBackground = new TextureRegion(mainMenuItems, 0, 0, 800, 480);
    	
    	// Game Screen 
    	tileMapItems = new Texture(game, "tilemap.png");
    	blueTile = new TextureRegion(tileMapItems, 0, 0, 64, 64);
    	redTile = new TextureRegion(tileMapItems, 64, 0, 64, 64);

    	// Game Objects
    	playerItems = new Texture(game, "pacman.png");
    	player = new TextureRegion(playerItems, 0, 0, 64, 64);

    }       
    
    public static void reload() {
    	tileMapItems.reload();
    	playerItems.reload();
    	mainMenuItems.reload();

    	//if(Settings.soundEnabled )
           // music.play();
    }
    
    public static void playSound(Sound sound) {
        //if(Settings.soundEnabled);
         //   sound.play(0.4f);
    }
}


