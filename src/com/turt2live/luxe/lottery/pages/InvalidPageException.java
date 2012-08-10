package com.turt2live.luxe.lottery.pages;

/**
 * Thrown if a page is invalid
 * 
 * @author turt2live
 */
public class InvalidPageException extends Exception {

	private static final long serialVersionUID = -1498187124264523341L;

	public InvalidPageException(String reason){
		super(reason);
	}

}
