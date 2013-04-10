package com.game.zombyte;

import com.bag.lib.Sound;
import com.bag.lib.gl.Animation;
import com.bag.lib.gl.Font;
import com.bag.lib.gl.Texture;
import com.bag.lib.gl.TextureRegion;
import com.bag.lib.impl.GLGame;
import com.bag.lib.Music;

public class Assets {
   
	// Main Menu
	public static Texture menuItems;
	public static TextureRegion menuBackground;
	
	// Map Background
    public static Texture mapItems1;
    public static TextureRegion MapBackground1;

    public static Texture mapItems2;
    public static TextureRegion MapBackground2;
    
    
    // HUB
    public static Texture hubMap;
    public static TextureRegion hubLeft;
    public static TextureRegion hubRight;
    
    // HUB - HEARTS
    public static Texture hearts;
    public static TextureRegion heartFull;
    public static TextureRegion heartHalf;
    public static TextureRegion heartEmpty;
    
    // Main Menu Buttons
    public static Texture mainMenuButtons;
    public static TextureRegion menuStartBtn;
    public static TextureRegion menuCoopBtn;
    public static TextureRegion menuTutorialBtn;
    public static TextureRegion menuSettingsBtn;
    
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
    
    // Player
    public static Texture players;
    public static TextureRegion playerIdle;
    public static TextureRegion playerLeft;
    public static TextureRegion playerRight;
    
    // Weapons
    public static TextureRegion shotgun;
    public static TextureRegion rocket;
    public static TextureRegion rocketBullet;
    public static TextureRegion rifle;
    public static TextureRegion pistol;
    
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
    
    // Joystick
    public static Texture 		joystickMap;
    public static TextureRegion joystickBottom;
    public static TextureRegion joystickTop;
    
    // Animations
    public static Animation		zombieMoveAnimation;
    public static Animation		playerAnimation;
    
    // Font
    public static Texture 		fontTex;  
    public static Font 			font;   
    
    // LevelObjects
    public static TextureRegion	rock;
    public static TextureRegion	tree;
    public static Animation		explosionAnimation;
    
    // Sounds
	public static Sound basicShoot;
	public static Sound rocketShoot;
	public static Sound playerHit;
	public static Sound powerUp;
	public static Sound nukeExplosion;
	
	public static Music intro;
	public static Music gamemusic;
 
    public static void load(GLGame game) {
       	
    	// Map Background
    	mapItems1 = new Texture(game, "map1.png");
    	MapBackground1 = new TextureRegion(mapItems1, 0, 0, 1024, 512);
    	mapItems2 = new Texture(game, "map2.png");
    	MapBackground2 = new TextureRegion(mapItems2, 0, 0, 1024, 512);
    	
    	// Main Menu (UI)
    	menuItems = new Texture(game, "menuBg.png"); 
    	menuBackground = new TextureRegion(menuItems, 0, 0, 800, 480);
    	
    	// HUB
    	hubMap = new Texture(game, "hubs.png");
    	hubLeft = new TextureRegion(hubMap, 0, 0, 256, 64);
    	hubRight = new TextureRegion(hubMap, 256, 0, 256, 64);
    	
    	// HUB - Hearts
    	hearts = new Texture(game, "hearts.png");
    	heartFull  = new TextureRegion(hearts, 0, 0, 32, 32);
    	heartHalf  = new TextureRegion(hearts, 32, 0, 32, 32);
    	heartEmpty = new TextureRegion(hearts, 64, 0, 32, 32);
    	 
    	// Main Menu Buttons
    	mainMenuButtons = new Texture(game, "menuButtons.png");
    	menuStartBtn 	= new TextureRegion(mainMenuButtons, 0, 0, 120, 45);
    	menuCoopBtn 	= new TextureRegion(mainMenuButtons, 128, 0, 120, 45); 
    	menuTutorialBtn = new TextureRegion(mainMenuButtons, 0, 128, 120, 45);
    	menuSettingsBtn = new TextureRegion(mainMenuButtons, 128, 128, 120, 45);
    	
    	// Game Screen 
    	tileMapItems = new Texture(game, "tilemap.png");
    	blueTile = new TextureRegion(tileMapItems, 0, 0, 64, 64);
    	redTile = new TextureRegion(tileMapItems, 64, 0, 64, 64);
    	
    	// Sprites
    	spritesMap = new Texture(game, "sprites.png");
    	zombieIdle  = new TextureRegion(spritesMap, 0, 0, 128, 128);
    	zombieLeft  = new TextureRegion(spritesMap, 0, 128, 128, 128);
    	zombieRight = new TextureRegion(spritesMap, 0, 256, 128, 128);
    	
    	// Player
    	players = new Texture(game, "playerSprite.png");
    	playerIdle  = new TextureRegion(players, 128, 128, 128, 128);
    	playerLeft  = new TextureRegion(players, 0, 0, 128, 128);
    	playerRight = new TextureRegion(players, 0, 128, 128, 128);
    	
    	bullet     = new TextureRegion(spritesMap, 128, 128, 128, 128);
    	bulletRed  = new TextureRegion(spritesMap, 128, 256, 128, 128);
    	bulletYellow  = new TextureRegion(spritesMap, 128, 384, 128, 128);
    	
    	// Weapons - Add them to to sprites map?
    	shotgun    = new TextureRegion(spritesMap,   384, 256, 56, 56);
    	rocket     = new TextureRegion(spritesMap,   256, 384, 64, 64);
    	rocketBullet = new TextureRegion(spritesMap, 256, 256, 8, 8);
    	rifle 	   = new TextureRegion(spritesMap,   256, 128, 32, 32);
    	pistol 	   = new TextureRegion(spritesMap,   384, 128, 32, 32);
    	
    	// Joystick
    	joystickMap = new Texture(game, "joystick.png");
    	joystickBottom = new TextureRegion(joystickMap, 0, 0, 64, 64);
    	joystickTop    = new TextureRegion(joystickMap, 64, 0, 64, 64);
    	
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
    	playerAnimation     = new Animation(0.2f, playerLeft, playerIdle, playerRight );
    	
    	// Font
    	fontTex = new Texture(game, "font3.png");
        font = new Font(fontTex, 0, 0, 16, 16, 20);
        
        // LevelObjects
        rock = new TextureRegion(spritesMap, 256, 0, 128, 128);
        tree = new TextureRegion(spritesMap, 128, 0, 128, 128);
    	explosionAnimation  = new Animation(0.07f, explosionFr1,explosionFr2,explosionFr3,explosionFr4,
								    			   explosionFr5,explosionFr6,explosionFr7,explosionFr8,
								    			   explosionFr9,explosionFr10,explosionFr11,explosionFr12,
								    			   explosionFr13,explosionFr14,explosionFr15,explosionFr16);
    	
    	// Sounds
        basicShoot = game.getAudio().newSound("basicshot.ogg");
        rocketShoot = game.getAudio().newSound("explo.ogg");
        playerHit = game.getAudio().newSound("rocketshot.ogg");
        powerUp = game.getAudio().newSound("powerup.ogg");
        nukeExplosion = game.getAudio().newSound("NukeExplosion.ogg");
        
        intro = game.getAudio().newMusic("intro.ogg");
        intro.setLooping(false);
        intro.setVolume(0.5f);
        
        gamemusic = game.getAudio().newMusic("game.ogg");
        gamemusic.setLooping(true);
        gamemusic.setVolume(0.8f);
    }       
    
    public static void reload() {
    	tileMapItems.reload();
    	mapItems1.reload();
    	mapItems2.reload();
    	spritesMap.reload();
    	menuItems.reload();
    	mainMenuButtons.reload();
    	fontTex.reload();
    	hubMap.reload();
    	players.reload();
    	joystickMap.reload();
    	hearts.reload();
    	explosionMap.reload();
    	//if(Settings.soundEnabled )
           // music.play();
    }
    
    public static void playSound(Sound sound) {
        //if(Settings.soundEnabled);
         //   sound.play(0.4f);
    }
}