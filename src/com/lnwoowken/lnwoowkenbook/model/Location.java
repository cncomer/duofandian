package com.lnwoowken.lnwoowkenbook.model;

public class Location {
	private int toTheLeftOf;
	private int toTheRightOf;
	private int below;
	private int above;
	public Location(int toTheLeftOf,int above,int toTheRightOf,int below){
		this.toTheLeftOf = toTheLeftOf;
		this.toTheRightOf = toTheRightOf;
		this.above = above;
		this.below = below;
		
	}
	public int getToTheLeftOf() {
		return toTheLeftOf;
	}
	public void setToTheLeftOf(int toTheLeftOf) {
		this.toTheLeftOf = toTheLeftOf;
	}
	public int getToTheRightOf() {
		return toTheRightOf;
	}
	public void setToTheRightOf(int toTheRightOf) {
		this.toTheRightOf = toTheRightOf;
	}
	public int getBelow() {
		return below;
	}
	public void setBelow(int below) {
		this.below = below;
	}
	public int getAbove() {
		return above;
	}
	public void setAbove(int above) {
		this.above = above;
	}

}
