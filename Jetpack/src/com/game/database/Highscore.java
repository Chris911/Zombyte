package com.game.database;

public class Highscore implements Comparable<Highscore> {
	
	private String name;
	private int score;
	public Highscore(String name, int score) {
		super();
		this.name = name;
		this.score = score;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	public String toString() {
		return name + "\t" + Integer.toString(score);
	}

	public int compareTo(Highscore another) {
		return another.score - this.score;
	}
}
