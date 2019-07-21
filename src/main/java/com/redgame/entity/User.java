package com.redgame.entity;

public class User {
	
	private String name ;
	
	private String pwd;
	
	private int score;

	public User(String name, String pwd) {
		this.name = name;
		this.pwd = pwd;
	}

	public User() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
