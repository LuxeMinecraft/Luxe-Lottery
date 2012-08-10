package com.turt2live.luxe.lottery.pages;

/**
 * Thrown when a menu is invalid
 * 
 * @author turt2live
 */
public class InvalidMenuException extends Exception {

	private static final long serialVersionUID = -441877001276015544L;

	public InvalidMenuException(String reason){
		super(reason);
	}

}
