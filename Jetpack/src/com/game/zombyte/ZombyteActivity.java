package com.game.zombyte;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.view.KeyEvent;
import android.content.Context;

import com.bag.lib.Screen;
import com.bag.lib.impl.GLGame;

public class ZombyteActivity extends GLGame {

	boolean firstTimeCreate = true;
    public static Context gameContext;
	
    public Screen getStartScreen() {
        //return new MainMenuScreen(this);
    	gameContext = this.getBaseContext();
        return new MainMenuScreen(this);
    }
    
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {         
        super.onSurfaceCreated(gl, config);
        if(firstTimeCreate) {
        	
            //Settings.load(getFileIO());
            Assets.load(this);
            firstTimeCreate = false;            
        } else {
            Assets.reload();
        }
    }     
    
    @Override
    public void onPause() {
        super.onPause();
        //if(Settings.soundEnabled)
            //Assets.music.pause();
    }
    
    @Override
    public void onStop() {
        super.onStop();
        Assets.intro.stop();
        Assets.gamemusic.stop();
    	moveTaskToBack(true);
    }
    
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            
        	//moveTaskToBack(true);
            return true;
            
        } else if (keyCode == KeyEvent.KEYCODE_HOME) {

        	//moveTaskToBack(true);
            return true;
        }

        return false;
    }
}
