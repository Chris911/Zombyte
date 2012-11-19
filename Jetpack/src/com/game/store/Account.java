package com.game.store;

public class Account {
	private static int tokens;
	private static int money;
	
	
	public static int getMoney()
	{
		return money;
	}
	
	public static int getTokens()
	{
		return tokens;
	}
	
	public static void setMoney(int mon)
	{
		money = mon;
	}
	
	public static void setTokens(int tok)
	{
		tokens = tok;
	}
	
	public static void addMoney(int mon)
	{
		money += mon;
	}
	
	public static void addTokens(int tok)
	{
		tokens += tok;
	}
}
