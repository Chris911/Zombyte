package com.game.store;

import com.bag.lib.GameObject;

public class ItemButton extends GameObject{

	public Item item;
	public ItemButton(float x, float y, float width, float height, Item it) {
		super(x, y, width, height);
		item = it;
	}
		

}
