package com.game.zombyte;

import java.util.ArrayList;

public class Explosion {

	public static final int STATE_ALIVE 	= 0;	// at least 1 particle is alive
	public static final int STATE_DEAD 		= 1;	// all particles are dead

	public ArrayList<Particle> particles;			// particles in the explosion
	protected int size;						// number of particles
	protected int state;						// whether it's still active or not
	
	public Explosion(int particleNr, int x, int y, float size) {
		this.state = STATE_ALIVE;
		this.particles = new ArrayList<Particle>();
	 	for (int i = 0; i < particleNr; i++) {
			Particle p = new Particle(x, y, size);
			this.particles.add(p);
		}
	 	size = particleNr;
	}
	
	public void update(float deltaTime) {
		if (this.state != STATE_DEAD) {
			for (int i = 0; i < this.particles.size(); i++) {
				Particle par = this.particles.get(i);
				if (par.state == STATE_ALIVE) {
					par.update(deltaTime);
					
					//if(this.particles.get(i).state == Particle.STATE_DEAD)
						//particles.remove(i);
				}
				else
					this.state = STATE_DEAD;
			}
			//if (isDead)
				//this.state = STATE_DEAD; 
		}
	}
}
