package com.turt2live.luxe.lottery.pages;

/**
 * Represents a line
 * 
 * @author turt2live
 */
public abstract class Line {

	protected String line;

	/**
	 * Creates a new Line with no value
	 */
	public Line(){}

	/**
	 * Creates a new Line with a default value
	 * 
	 * @param line the line
	 */
	public Line(String line){
		this.line = line;
	}

	/**
	 * Fetches the line
	 * 
	 * @return the line
	 */
	public String getLine(){
		return line;
	}

	/**
	 * Sets the line
	 * 
	 * @param line the line
	 */
	public void setLine(String line){
		this.line = line;
	}

	/**
	 * Called by the page generator before adding the line
	 */
	public abstract void format();

}
