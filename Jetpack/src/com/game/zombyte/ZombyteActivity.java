package com.game.zombyte;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

<<<<<<< HEAD:Jetpack/src/com/game/zombyte/JetpackActivity.java
import android.view.KeyEvent;
=======
import android.content.Context;
>>>>>>> d2c87c01aeeafb765babf62b09433f2d772a6f13:Jetpack/src/com/game/zombyte/ZombyteActivity.java

import com.bag.lib.Screen;
import com.bag.lib.impl.GLGame;

public class ZombyteActivity extends GLGame {

	boolean firstTimeCreate = true;
    public static Context gameContext;
	
    public Screen getStartScreen() {
        //return new MainMenuScreen(this);
    	this.gameContext = this.getBaseContext();
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
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Assets.intro.stop();
            Assets.gamemusic.stop();
        	moveTaskToBack(true);


            return true;
        }

        return false;
    }
}
