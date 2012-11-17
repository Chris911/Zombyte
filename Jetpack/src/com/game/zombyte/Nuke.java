package com.game.zombyte;

import java.util.Timer;

import com.game.utilities.NukeTimer;


public class Nuke extends SpecialWeapon {

	private final static int DEFAULT_NUMBER_OF_NUKES = 2;
	private final static String NUKE_NAME = "Nuke";
	private final static long NUKE_TIMER_TIME = 5000L;
	
	public Nuke() {
		super(DEFAULT_NUMBER_OF_NUKES, NUKE_NAME);
	}

	@Override
	void activate(World world) {
		if(this.numberRemaining > 0)
		{
			world.waitingForNukeToFinish = true;
			Timer nukeTimer = new Timer();
			nukeTimer.schedule(new NukeTimer(world), NUKE_TIMER_TIME);
			world.listener.playNukeExplosion();
			world.vib.vibrate(2000);
			for(Enemy enemy : world.EnemyArray)
			{
				world.rocketExplosionArray.add(new RocketExplosion(enemy.position.x, enemy.position.y));
				enemy.life -= 10000;
			}
			//this.numberRemaining--;
		}
	}

}
