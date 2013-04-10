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
	
	public static int WEAPON_AMMO_SHOTGUN = 12;
	public static int WEAPON_AMMO_ROCKET = 8;
	public static int WEAPON_AMMO_RIFLE = 50;
	
	
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
			this.bulletsRemaining = WEAPON_AMMO_SHOTGUN;
			this.damage = 12;
			this.fireRate = 2.2f;
			this.bulletSpeed = 22;
		}
		else if(type == WEAPON_RIFLE)
		{
			this.bulletsRemaining = WEAPON_AMMO_RIFLE; 
			this.damage = 10.0f;
			this.fireRate = 0.1f;
			this.bulletSpeed = 30;
		}
		else if(type == WEAPON_ROCKET)
		{
			this.bulletsRemaining = WEAPON_AMMO_ROCKET;
			this.damage = 70.0f;
			this.fireRate = 2.5f;
			this.bulletSpeed = 38;
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
	
	public static int getBulletsOfType(int type)
	{
		if(type == WEAPON_RIFLE){
			return WEAPON_AMMO_RIFLE/2;
		} 
		else 	if(type == WEAPON_ROCKET){
			return WEAPON_AMMO_ROCKET/2;
		} 
		else 	if(type == WEAPON_SHOTGUN){
			return WEAPON_AMMO_SHOTGUN/2;
		}
		return 1;
	}
	
}
