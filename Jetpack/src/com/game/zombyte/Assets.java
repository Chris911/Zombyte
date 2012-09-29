package com.game.zombyte;

import com.bag.lib.Sound;
import com.bag.lib.gl.Animation;
import com.bag.lib.gl.Font;
import com.bag.lib.gl.Texture;
import com.bag.lib.gl.TextureRegion;
import com.bag.lib.impl.GLGame;

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
    public static TextureRegion shotgun;
    public static TextureRegion rocket;
    public static TextureRegion rocketBullet;
    public static TextureRegion rifle;
    
    // Explosion
    public static Texture explosionMap;
    public static TextureRegion explosionFr1;
    public static TextureRegion explosionFr2;
    public static TextureRegion explosionFr3;
    public static TextureRegion explosionFr4;
    public static TextureRegion explosionFr5;
    public static TextureRegion explosionFr6;
    public static TextureRegion explosionFr7;
    public static TextureRegion explosionFr8;
    public static TextureRegion explosionFr9;
    public static TextureRegion explosionFr10;
    public static TextureRegion explosionFr11;
    public static TextureRegion explosionFr12;
    public static TextureRegion explosionFr13;
    public static TextureRegion explosionFr14;
    public static TextureRegion explosionFr15;
    public static TextureRegion explosionFr16;
    
    // Animations
    public static Animation		zombieMoveAnimation;
    
    // Font
    public static Texture 		fontTex;  
    public static Font 			font;   
    
    // LevelObjects
    public static TextureRegion		tree;
    public static Animation		explosionAnimation;

    
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
    	
    	// Weapons - Add them to to sprites map?
    	shotgun    = new TextureRegion(spritesMap,   384, 256, 56, 56);
    	rocket     = new TextureRegion(spritesMap,   256, 384, 64, 64);
    	rocketBullet = new TextureRegion(spritesMap, 256, 256, 8, 8);
    	rifle 	   = new TextureRegion(spritesMap,   256, 128, 32, 32);
    	
    	// Explosion
    	explosionMap  = new Texture(game, "explosion.png");
    	explosionFr1  = new TextureRegion(explosionMap, 0, 0, 48, 56);
    	explosionFr2  = new TextureRegion(explosionMap, 48, 0, 48, 56);
    	explosionFr3  = new TextureRegion(explosionMap, 96, 0, 56, 56);
    	explosionFr4  = new TextureRegion(explosionMap, 152, 0, 48, 56);
    	explosionFr5  = new TextureRegion(explosionMap, 0, 56, 48, 56);
    	explosionFr6  = new TextureRegion(explosionMap, 48, 56, 48, 56);
    	explosionFr7  = new TextureRegion(explosionMap, 96, 56, 56, 56);
    	explosionFr8  = new TextureRegion(explosionMap, 152, 56, 48, 56);
    	explosionFr9  = new TextureRegion(explosionMap, 0, 102, 48, 56);
    	explosionFr10 = new TextureRegion(explosionMap, 48, 102, 48, 56);
    	explosionFr11 = new TextureRegion(explosionMap, 96, 102, 56, 56);
    	explosionFr12 = new TextureRegion(explosionMap, 152, 102, 48, 56);
    	explosionFr13 = new TextureRegion(explosionMap, 0, 158, 48, 56);
    	explosionFr14 = new TextureRegion(explosionMap, 48, 158, 48, 56);
    	explosionFr15 = new TextureRegion(explosionMap, 96, 158, 56, 56);
    	explosionFr16 = new TextureRegion(explosionMap, 152, 158, 48, 56);
    	
    	// Animation
    	zombieMoveAnimation = new Animation(0.2f, zombieLeft, zombieIdle, zombieRight );
    	
    	// Font
    	fontTex = new Texture(game, "font3.png");
        font = new Font(fontTex, 0, 0, 16, 16, 20);
        
        // LevelObjects
        tree = new TextureRegion(spritesMap, 128, 0, 128, 128);
    	explosionAnimation  = new Animation(0.07f, explosionFr1,explosionFr2,explosionFr3,explosionFr4,
								    			   explosionFr5,explosionFr6,explosionFr7,explosionFr8,
								    			   explosionFr9,explosionFr10,explosionFr11,explosionFr12,
								    			   explosionFr13,explosionFr14,explosionFr15,explosionFr16);
    	
    }       
    
    public static void reload() {
    	tileMapItems.reload();
    	playerItems.reload();
    	mainMenuItems.reload();
    	spritesMap.reload();

    	//if(Settings.soundEnabled )
           // music.play();
    }
    
    public static void playSound(Sound sound) {
        //if(Settings.soundEnabled);
         //   sound.play(0.4f);
    }
}

