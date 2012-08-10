package com.turt2live.luxe.lottery.pages;

/**
 * For when a page doesn't exist in a menu, or the page number requested does not exist
 * @author turt2live
 */
public class NoPageException extends Exception {

	private static final long serialVersionUID = 6563405971785091001L;
	
	public NoPageException(){
		super("A page with that number doesn not exist.");
	}

}
