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
import java.util.List;

import org.bukkit.ChatColor;


/**
 * Used to create menus
 * 
 * @author turt2live
 */
public class MenuFactory {

	private List<Page> pages = new ArrayList<Page>();
	private Style style = MenuStyle.DEFAULT.getStyle();
	private String title = "Default";
	private String prefix = "";
	private ChatColor mainColor = ChatColor.DARK_GREEN;
	private ChatColor titleColor = ChatColor.GREEN;
	private ChatColor pageColor = ChatColor.GREEN;
	private boolean usePrefix = false;

	/**
	 * Creates a new MenuFactory with a style and predefined pages
	 * 
	 * @param style the style
	 * @param pages the pages
	 */
	public MenuFactory(Style style, Page... pages){
		this.style = style;
		for(Page page : pages){
			this.pages.add(page);
		}
	}

	/**
	 * Creates a new MenuFactory with a style and no pages
	 * 
	 * @param style the style
	 */
	public MenuFactory(Style style){
		this.style = style;
	}

	/**
	 * Creates a new Menu Factory with the default style and no pages
	 */
	public MenuFactory(){}

	/**
	 * Sets the style of the factory
	 * 
	 * @param style the style
	 * @return the factory
	 */
	public MenuFactory setStyle(Style style){
		this.style = style;
		return this;
	}

	/**
	 * Sets the style of the factory
	 * 
	 * @param style the style
	 * @return the factory
	 */
	public MenuFactory setStyle(MenuStyle style){
		this.style = style.getStyle();
		return this;
	}

	/**
	 * Adds a page to the factory
	 * 
	 * @param page the page
	 * @return the factory
	 * @throws PageAlreadyExistsException thrown if the page already exists
	 */
	public MenuFactory addPage(Page page) throws PageAlreadyExistsException{
		if(pages.contains(page)){
			throw new PageAlreadyExistsException();
		}
		for(Page ppage : pages){
			if(ppage.getPageNumber() == page.getPageNumber()){
				throw new PageAlreadyExistsException();
			}
		}
		pages.add(page);
		return this;
	}

	/**
	 * Removes a page from the factory
	 * 
	 * @param page the page
	 * @return the factory
	 */
	public MenuFactory removePage(Page page){
		pages.remove(page);
		return this;
	}

	/**
	 * Sets the title of the menu
	 * 
	 * @param title the title
	 * @return the factory
	 */
	public MenuFactory setTitle(String title){
		this.title = title;
		return this;
	}

	/**
	 * Sets the color options for the menu
	 * 
	 * @param mainColor the main color
	 * @param titleColor the title color
	 * @param pageColor the page color
	 * @return the factory
	 */
	public MenuFactory setColors(ChatColor mainColor, ChatColor titleColor, ChatColor pageColor){
		this.mainColor = mainColor;
		this.titleColor = titleColor;
		this.pageColor = pageColor;
		return this;
	}

	/**
	 * Sets the prefix of the menu
	 * 
	 * @param prefix the prefix
	 * @param use true to use the prefix
	 * @return the factory
	 */
	public MenuFactory setPrefix(String prefix, boolean use){
		this.prefix = prefix;
		this.usePrefix = use;
		return this;
	}

	/**
	 * Gets a page from the factory
	 * 
	 * @param pageNumber the page number
	 * @return the page
	 * @throws NoPageIndexException thrown if no page with that number exists
	 */
	public Page getPage(int pageNumber) throws NoPageException{
		for(Page page : pages){
			if(page.getPageNumber() == pageNumber){
				return page;
			}
		}
		throw new NoPageException();
	}

	/**
	 * Gets the resulting menu of the factory
	 * 
	 * @return the Menu
	 * @throws NoPagesException thrown if no pages set
	 * @throws InvalidMenuException thrown if the menu is missing something (like style)
	 */
	public Menu getMenu() throws NoPagesException, InvalidMenuException{
		if(style == null){
			throw new InvalidMenuException("No style set");
		}
		if(pages.size() <= 0){
			throw new NoPagesException();
		}
		Menu menu = new Menu();
		menu.setStyle(style);
		for(Page page : pages){
			try{
				menu.addPage(page);
			}catch(PageAlreadyExistsException e){
				throw new InvalidMenuException("Duplicate pages in menu");
			}
		}
		menu.updatePages();
		menu.setTitle(title);
		menu.setColor(mainColor, titleColor, pageColor);
		menu.setPrefix(prefix);
		menu.setUsePrefix(usePrefix);
		return menu;
	}

	/**
	 * Imports settings from the menu<br>
	 * <b>This will import all pages and disregard the ones that already have a page number in this factory.</b><br>
	 * <i>(Eg: if page 1 already is in the factory, page 1 from the menu is ignored)</i>
	 * 
	 * @param menu the menu to import
	 * @return the factory
	 * @throws InvalidMenuException thrown if the menu is missing a style.
	 */
	public MenuFactory importMenu(Menu menu) throws InvalidMenuException{
		menu.validate();
		setStyle(menu.getStyle());
		if(menu.getPages() != null){
			for(Page page : menu.getPages()){
				try{
					addPage(page);
				}catch(PageAlreadyExistsException e){} // Ignore this, it disregards the duplicate
			}
		}
		setPrefix(menu.getPrefix(), menu.usePrefix());
		setColors(menu.getMainColor(), menu.getTitleColor(), menu.getPageColor());
		setTitle(menu.getTitle());
		return this;
	}
}
