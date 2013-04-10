package com.game.utilities;

import com.bag.lib.gl.TextureRegion;

public class ComboBox {
	
    public int scoreMultiplier = 1;
    public int killCombo = 0;
    public TextureRegion tex;
    
    public ComboBox(){
    	
    }
    
    public void update(float deltaTime){
		if(killCombo !=0 && killCombo == 10 && scoreMultiplier < 4){
			scoreMultiplier ++;
			killCombo = 0;
		}
    }
    
    public void playerHit(){
    	killCombo = 0;
    	scoreMultiplier = 1;
    }
    
    public void enemyKilled(){
        killCombo++;
    }
}
