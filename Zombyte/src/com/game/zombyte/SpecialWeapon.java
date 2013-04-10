package com.game.zombyte;

public abstract class SpecialWeapon {
	public int numberRemaining;
	public String name;
	
	public SpecialWeapon(int numberRemaining, String name) {
		super();
		this.numberRemaining = numberRemaining;
		this.name = name;
	}

	abstract void activate(World world);
}
