package com.game.jetpack;

import com.bag.lib.GameObject;
import com.bag.lib.gl.TextureRegion;


public class UIButton extends GameObject{

	public static int STATE_IDLE 	= 0;
	public static int STATE_PRESSED	= 1;
	
	public TextureRegion idleState;
	public TextureRegion pressedState;
	
	public float R_width;
	public float R_height;
	
	public int state;
	
	public UIButton(float x, float y, float width, float height, TextureRegion idle, TextureRegion pressed) {
		super(x, y, width, height);
		
		idleState = idle;
		pressedState = pressed;
		state = STATE_IDLE;
		
		R_width = width;
		R_height = height;
	}

}
