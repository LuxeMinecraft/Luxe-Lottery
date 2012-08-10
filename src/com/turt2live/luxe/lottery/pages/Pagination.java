package com.turt2live.luxe.lottery.pages;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;


/**
 * Creates an easy to use "pagination"
 * 
 * @author turt2live
 */
public class Pagination {

	private List<? extends Line> lines = new ArrayList<Line>();
	private Menu menu;
	private int pages = 0;
	/**
	 * Menu style, must be set before generate is called
	 */
	protected Style style = MenuStyle.DEFAULT.getStyle();

	/**
	 * Creates the pagination with the lines defined
	 * 
	 * @param lines the lines
	 */
	public Pagination(List<? extends Line> lines){
		this.lines = lines;
	}

	/**
	 * Generates the pagination, this only has to be called once.
	 * 
	 * @param title the title
	 * @param prefix the prefix (null for none)
	 * @param mainColor the main color (see MenuFactory)
	 * @param titleColor the title color (see MenuFactory)
	 * @param pageColor the page color (see MenuFactory)
	 */
	public void generate(String title, String prefix, ChatColor mainColor, ChatColor titleColor, ChatColor pageColor){
		MenuFactory menufactory = new MenuFactory();
		menufactory.setPrefix(ChatColor.translateAlternateColorCodes('&', prefix), false);
		menufactory.setTitle(title);
		menufactory.setColors(mainColor, titleColor, pageColor);
		menufactory.setStyle(style);

		int page = 1;
		int line = 1;

		PageFactory factory = new PageFactory();
		factory.setPageNumber(page);
		for(Line message : lines){
			if(line > 8){
				try{
					menufactory.addPage(factory.getPage());
				}catch(PageAlreadyExistsException e){
					e.printStackTrace();
				}catch(InvalidPageException e){
					e.printStackTrace();
				}
				factory = new PageFactory();
				page++;
				factory.setPageNumber(page);
				line = 1;
			}
			factory.setLine(line, message);
			line++;
		}
		pages = page;
		try{
			menufactory.addPage(factory.getPage());
		}catch(PageAlreadyExistsException e){
			e.printStackTrace();
		}catch(InvalidPageException e){
			e.printStackTrace();
		}

		try{
			this.menu = menufactory.getMenu();
		}catch(NoPagesException e){
			e.printStackTrace();
		}catch(InvalidMenuException e){
			e.printStackTrace();
		}
	}

	/**
	 * Shows a page to the desired target.
	 * 
	 * @param target the target
	 * @param page the page (starts at 1, ends at maxPages())
	 */
	public void showTo(CommandSender target, int page){
		try{
			menu.sendTo(target, page);
		}catch(InvalidMenuException e){
			e.printStackTrace();
		}catch(NoPageException e){
			e.printStackTrace();
		}catch(InvalidPageException e){
			e.printStackTrace();
		}
	}

	/**
	 * Maximum number of pages this pagination has
	 * 
	 * @return the number of pages
	 */
	public int maxPages(){
		return pages;
	}

}
