package com.turt2live.luxe.lottery.pages;

import org.bukkit.ChatColor;

/**
 * A style
 * 
 * @author turt2live
 */
public abstract class Style {

	private MenuStyle type;

	/**
	 * Creates a new style
	 * 
	 * @param style the style type
	 */
	public Style(MenuStyle style){
		this.type = style;
	}

	/**
	 * Gets the header of this style
	 * 
	 * @param title the title
	 * @param pageNumber the current page number
	 * @param maxPages the maximum pages
	 * @param mainColor the main color of the header
	 * @param titleColor the title's color
	 * @param pageColor the page color ("Page X/Y)
	 * @return the colored header
	 */
	public abstract String getHeader(String title, int pageNumber, int maxPages, ChatColor mainColor, ChatColor titleColor, ChatColor pageColor);

	/**
	 * Gets the footer of this style
	 * 
	 * @param title the title
	 * @param pageNumber the current page number
	 * @param maxPages the maximum pages
	 * @param mainColor the main color of the header
	 * @param titleColor the title's color
	 * @param pageColor the page color ("Page X/Y)
	 * @return the colored footer
	 */
	public abstract String getFooter(String title, int pageNumber, int maxPages, ChatColor mainColor, ChatColor titleColor, ChatColor pageColor);

	/**
	 * Gets the menu style of this style
	 * 
	 * @return the menu style
	 */
	public MenuStyle getType(){
		return type;
	}
}
