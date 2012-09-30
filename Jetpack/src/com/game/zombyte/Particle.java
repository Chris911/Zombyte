package com.game.zombyte;

public class Particle {

	public static final int STATE_ALIVE = 0;	// particle is alive
	public static final int STATE_DEAD = 1;		// particle is dead

	public static final int MAX_DIMENSION		= 5;	// the maximum width or height
	public static final int MAX_SPEED			= 10;	// maximum speed (per update)

	public int state;			// particle is alive or dead
	private float width;		// width of the particle
	protected float height;		// height of the particle
	public float x, y;			// horizontal and vertical position
	private double xv, yv;		// vertical and horizontal velocity
	public	float alpha;
	
	public Particle(int x, int y, float size) {
		this.x = x;
		this.y = y;
		this.state = Particle.STATE_ALIVE;
		this.width = size;
		this.height = this.width;
		this.xv = (rndDbl(0, MAX_SPEED * 2) - MAX_SPEED);
		this.yv = (rndDbl(0, MAX_SPEED * 2) - MAX_SPEED);
		this.alpha = 1.0f;
		
		if (xv * xv + yv * yv > MAX_SPEED * MAX_SPEED) {
			xv *= 0.7;
			yv *= 0.7;
		}
	}
	
	public void update(float deltaTime) {
		if (this.state != STATE_DEAD) {
			this.x += this.xv*deltaTime;
			this.y += this.yv*deltaTime;

			alpha -= 0.1; 
			
			if (this.alpha <= 0) {	// reached the end if its life
				this.state = STATE_DEAD;
			}
		}
	}
	
	// Return an integer that ranges from min inclusive to max inclusive.
		static int rndInt(int min, int max) {
			return (int) (min + Math.random() * (max - min + 1));
		}

		static double rndDbl(double min, double max) {
			return min + (max - min) * Math.random();
		}
}
