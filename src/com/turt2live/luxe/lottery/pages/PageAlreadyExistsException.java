package com.turt2live.luxe.lottery.pages;

/**
 * For when a page is already in a menu
 * 
 * @author turt2live
 */
public class PageAlreadyExistsException extends Exception {

	private static final long serialVersionUID = -5378808832326494655L;

	public PageAlreadyExistsException(){
		super("A page with that page number already exists");
	}

}
