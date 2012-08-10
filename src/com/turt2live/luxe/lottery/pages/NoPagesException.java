package com.turt2live.luxe.lottery.pages;

/**
 * Thrown if the menu has no pages
 * 
 * @author turt2live
 */
public class NoPagesException extends Exception {

	private static final long serialVersionUID = -4429857123598309216L;

	public NoPagesException(){
		super("Menu has no pages");
	}
}
