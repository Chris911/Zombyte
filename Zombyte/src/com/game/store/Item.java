package com.game.store;
import com.bag.lib.gl.TextureRegion;
public class Item {
	
	public String name;
	public String category;
	public int price;
	public TextureRegion activeAsset;

	public Item(String n, String c, int p, TextureRegion a){
		
		this.name = n;
		this.category = c;
		this.price = p;
		this.activeAsset = a;
	}
	
}
