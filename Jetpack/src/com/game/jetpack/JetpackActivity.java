package com.game.jetpack;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.bag.lib.Screen;
import com.bag.lib.impl.GLGame;

public class JetpackActivity extends GLGame {

	boolean firstTimeCreate = true;
    
    public Screen getStartScreen() {
        //return new MainMenuScreen(this);
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
}
