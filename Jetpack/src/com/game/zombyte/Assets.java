package com.game.zombyte;

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

    // Sprites
    public static Texture 		spritesMap;
    public static TextureRegion zombieIdle;
    public static TextureRegion zombieLeft;
    public static TextureRegion zombieRight;
    public static TextureRegion bullet;
    public static TextureRegion bulletRed;
    public static TextureRegion bulletYellow;
    
    // Weapons
    public static Texture 		weaponsMap;
    public static TextureRegion shotgun;
    public static TextureRegion rocket;
    public static TextureRegion rocketBullet;
    public static TextureRegion rifle;
    
    // Animations
    public static Animation		zombieMoveAnimation;
    
    // Font
    public static Texture 		fontTex;  
    public static Font 			font;   
    
    // LevelObjects
    public static TextureRegion		tree;

    
    public static void load(GLGame game) {
       	
    	// Main Menu (UI)
    	mainMenuItems = new Texture(game, "bg.png");
    	mainMenuBackground = new TextureRegion(mainMenuItems, 0, 0, 1024, 512);
    	
    	// Game Screen 
    	tileMapItems = new Texture(game, "tilemap.png");
    	blueTile = new TextureRegion(tileMapItems, 0, 0, 64, 64);
    	redTile = new TextureRegion(tileMapItems, 64, 0, 64, 64);

    	// Game Objects
    	playerItems = new Texture(game, "pacman.png");
    	player = new TextureRegion(playerItems, 0, 0, 64, 64);
    	
    	// Sprites
    	spritesMap = new Texture(game, "sprites.png");
    	zombieIdle = new TextureRegion(spritesMap, 0, 0, 128, 128);
    	zombieLeft = new TextureRegion(spritesMap, 0, 128, 128, 128);
    	zombieRight = new TextureRegion(spritesMap, 0, 256, 128, 128);
    	bullet     = new TextureRegion(spritesMap, 128, 128, 128, 128);
    	bulletRed  = new TextureRegion(spritesMap, 128, 256, 128, 128);
    	bulletYellow  = new TextureRegion(spritesMap, 128, 384, 128, 128);
    	
    	//Weapons - Add them to to sprites map?
    	weaponsMap = new Texture(game, "weapons.png");
    	shotgun    = new TextureRegion(weaponsMap, 0, 0, 64, 64);
    	rocket     = new TextureRegion(weaponsMap, 64, 0, 64, 64);
    	rocketBullet = new TextureRegion(weaponsMap, 0, 64, 64, 64);
    	rifle 	   = new TextureRegion(weaponsMap, 64, 64, 64, 64);
    	
    	// Animation
    	zombieMoveAnimation = new Animation(0.2f, zombieLeft, zombieIdle, zombieRight );
    	
    	// Font
    	fontTex = new Texture(game, "font3.png");
        font = new Font(fontTex, 0, 0, 16, 16, 20);
        
        // LevelObjects
        tree = new TextureRegion(spritesMap, 128, 0, 128, 128);
    }       
    
    public static void reload() {
    	tileMapItems.reload();
    	playerItems.reload();
    	mainMenuItems.reload();
    	spritesMap.reload();
    	weaponsMap.reload();

    	//if(Settings.soundEnabled )
           // music.play();
    }
    
    public static void playSound(Sound sound) {
        //if(Settings.soundEnabled);
         //   sound.play(0.4f);
    }
}


