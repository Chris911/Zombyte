package com.game.utilities;

import java.util.TimerTask;

import com.game.zombyte.World;

public class NukeTimer extends TimerTask {
	World world;
	
	public NukeTimer(World world) {
		this.world = world;
	}
	
	@Override
	public void run() {
		world.waitingForNukeToFinish = false;
	}
}
