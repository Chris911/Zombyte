package com.game.zombyte;

public class Weapon {
	public int bulletsRemaining;
	private float damage;
	private float fireRate;
	private int type;
	private float bulletSpeed;
	
	public static int WEAPON_PISTOL  = 0;
	public static int WEAPON_SHOTGUN = 1;
	public static int WEAPON_RIFLE   = 2;
	public static int WEAPON_ROCKET  = 3;
	
	public Weapon(int type) {
		super();
		this.type = type;
		
		assignWeaponProperties(type);
	}

	private void assignWeaponProperties(int type)
	{
		if(type == WEAPON_PISTOL)
		{
			this.bulletsRemaining = 1;
			this.damage = 8;
			this.fireRate = 0.7f;
			this.bulletSpeed = 26;
		}
		else if(type == WEAPON_SHOTGUN)
		{
			this.bulletsRemaining = 25;
			this.damage = 12;
			this.fireRate = 2.2f;
			this.bulletSpeed = 14;
		}
		else if(type == WEAPON_RIFLE)
		{
			this.bulletsRemaining = 50; 
			this.damage = 10.0f;
			this.fireRate = 0.1f;
			this.bulletSpeed = 30;
		}
		else if(type == WEAPON_ROCKET)
		{
			this.bulletsRemaining = 10;
			this.damage = 60.0f;
			this.fireRate = 2.5f;
			this.bulletSpeed = 35;
		}
	}
	
	public void fire() {
		if(type != WEAPON_PISTOL) 
		{
			bulletsRemaining--;
		}
		
		if(bulletsRemaining <= 0)
		{
			setType(WEAPON_PISTOL);
		}
	}

	public float getDamage() {
		return damage;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public float getFireRate() {
		return fireRate;
	}

	public void setFireRate(float fireRate) {
		this.fireRate = fireRate;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
		
		assignWeaponProperties(type);
	}

	public float getBulletSpeed() {
		return bulletSpeed;
	}

	public void setBulletSpeed(float bulletSpeed) {
		this.bulletSpeed = bulletSpeed;
	}
	
}
