/*******************************************************************************
 * Copyright (c) 2012 turt2live (Travis Ralston).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.luxe.lottery.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;


/**
 * A Menu
 * 
 * @author turt2live
 */
public class Menu {

	private List<Page> pages = new ArrayList<Page>();
	private Style style = MenuStyle.DEFAULT.getStyle();
	private String prefix = "";
	private boolean usePrefix = false;
	private String title = "Default";
	private ChatColor mainColor = ChatColor.DARK_GREEN;
	private ChatColor titleColor = ChatColor.GREEN;
	private ChatColor pageColor = ChatColor.GREEN;
	private UUID uid;

	/**
	 * Creates a new Menu
	 */
	protected Menu(){
		uid = UUID.randomUUID();
	}

	/**
	 * Adds a page to the menu
	 * 
	 * @param page the page
	 */
	protected void addPage(Page page) throws PageAlreadyExistsException{
		if(pages.contains(page)){
			throw new PageAlreadyExistsException();
		}
		for(Page ourPages : pages){
			if(ourPages.getPageNumber() == page.getPageNumber()){
				throw new PageAlreadyExistsException();
			}
		}
		pages.add(page);
	}

	/**
	 * Gets an UNMODIFIABLE list of pages in this menu
	 * 
	 * @return the list of pages
	 */
	protected List<Page> getPages(){
		return Collections.unmodifiableList(pages);
	}

	/**
	 * Sets the prefix of this menu
	 * 
	 * @param prefix the prefix
	 */
	protected void setPrefix(String prefix){
		this.prefix = prefix;
	}

	/**
	 * Sets whether or not to use the prefix
	 * 
	 * @param use true to use
	 */
	protected void setUsePrefix(boolean use){
		this.usePrefix = use;
	}

	/**
	 * Sets the title
	 * 
	 * @param title the title
	 */
	protected void setTitle(String title){
		this.title = title;
	}

	/**
	 * Sets the color options for this menu
	 * 
	 * @param m the main color
	 * @param t the title color
	 * @param p the page color
	 */
	protected void setColor(ChatColor m, ChatColor t, ChatColor p){
		this.mainColor = m;
		this.titleColor = t;
		this.pageColor = p;
	}

	/**
	 * Gets the UUID of this menu
	 * 
	 * @return the UUID
	 */
	public UUID getUUID(){
		return uid;
	}

	/**
	 * Gets the main color of this menu
	 * 
	 * @return the main color
	 */
	public ChatColor getMainColor(){
		return mainColor;
	}

	/**
	 * Gets the title color of this menu
	 * 
	 * @return the title color
	 */
	public ChatColor getTitleColor(){
		return titleColor;
	}

	/**
	 * Gets the page color of this menu
	 * 
	 * @return the page color
	 */
	public ChatColor getPageColor(){
		return pageColor;
	}

	/**
	 * Gets the title of this menu
	 * 
	 * @return the title
	 */
	public String getTitle(){
		return title;
	}

	/**
	 * Gets the prefix of this menu
	 * 
	 * @return the prefix
	 */
	public String getPrefix(){
		return prefix;
	}

	/**
	 * Determines if this menu should send the prefix
	 * 
	 * @return true if this menu is sending the prefix
	 */
	public boolean usePrefix(){
		return usePrefix;
	}

	/**
	 * Sorts the pages in the menu correctly
	 * 
	 * @throws InvalidMenuException thrown if there are missing pages
	 */
	public void updatePages() throws InvalidMenuException{
		int totalPages = pages.size();
		List<Page> sorted = new ArrayList<Page>();
		for(int i = 0; i < totalPages; i++){
			sorted.add(Page.NULL_PAGE);
		}
		boolean[] missing = new boolean[totalPages];
		for(int i = 0; i < missing.length; i++){
			missing[i] = true;
		}
		for(Page page : pages){
			int arrayIndex = page.getPageNumber() - 1;
			missing[arrayIndex] = false;
			sorted.set(arrayIndex, page);
		}
		for(boolean m : missing){
			if(m){
				throw new InvalidMenuException("Missing pages");
			}
		}
		pages = sorted;
	}

	/**
	 * Sets the style of this menu
	 * 
	 * @param style the style
	 */
	public void setStyle(Style style){
		this.style = style;
	}

	/**
	 * Sets the style of this menu
	 * 
	 * @param style the style
	 */
	public void setStyle(MenuStyle style){
		this.style = style.getStyle();
	}

	/**
	 * Gets the style of this menu
	 * 
	 * @return the style
	 */
	public Style getStyle(){
		return style;
	}

	/**
	 * Sends a target the menu
	 * 
	 * @param target the target
	 * @param pageNumber the page number to send
	 * @throws InvalidMenuException thrown if the menu is invalid
	 * @throws NoPageException thrown if the page number doesn't exist
	 * @throws InvalidPageException thrown if the page is invalid
	 */
	public void sendTo(CommandSender target, int pageNumber) throws InvalidMenuException, NoPageException, InvalidPageException{
		validate();
		Page page = getPage(pageNumber);
		page.validate();
		StringBuilder header = new StringBuilder();
		if(usePrefix){
			header.append(prefix);
			if(!prefix.endsWith(" ")){
				header.append(" ");
			}
		}
		header.append(style.getHeader(title, pageNumber, pages.size(), mainColor, titleColor, pageColor));
		StringBuilder footer = new StringBuilder();
		if(usePrefix){
			footer.append(prefix);
			if(!prefix.endsWith(" ")){
				footer.append(" ");
			}
		}
		footer.append(style.getFooter(title, pageNumber, pages.size(), mainColor, titleColor, pageColor));
		target.sendMessage(header.toString());
		page.sendTo(target);
		target.sendMessage(footer.toString());
	}

	public Page getPage(int pageNumber) throws NoPageException{
		return getPages().get(pageNumber - 1);
	}

	/**
	 * Validates the menu. If nothing is thrown there are no problems.
	 * 
	 * @throws InvalidMenuException thrown if something is wrong
	 */
	public void validate() throws InvalidMenuException{
		if(style == null || mainColor == null || titleColor == null || pageColor == null || title == null){
			throw new InvalidMenuException("Property null");
		}
		if(usePrefix && prefix == null){
			throw new InvalidMenuException("Prefix null and enabled");
		}
	}
}
