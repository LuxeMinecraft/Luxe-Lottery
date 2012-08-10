package com.turt2live.luxe.lottery.pages;

import org.bukkit.ChatColor;

/**
 * Basic menu style
 * 
 * @author turt2live
 */
public class BasicStyle extends Style {

	private String header = "{C1}=======[ {C2}%s {C1}| {C3}Page %i/%i {C1}]=======";
	private String footer = header;

	/**
	 * Creates a new Basic Style
	 */
	public BasicStyle(){
		super(MenuStyle.DEFAULT);
	}

	@Override
	public String getHeader(String title, int pageNumber, int maxPages, ChatColor mainColor, ChatColor titleColor, ChatColor pageColor){
		String theader = header;
		theader = theader.replaceAll("\\{C1\\}", mainColor + "");
		theader = theader.replaceAll("\\{C2\\}", titleColor + "");
		theader = theader.replaceAll("\\{C3\\}", pageColor + "");
		theader = String.format(theader, title, pageNumber, maxPages);
		return theader;
	}

	@Override
	public String getFooter(String title, int pageNumber, int maxPages, ChatColor mainColor, ChatColor titleColor, ChatColor pageColor){
		String tfooter = footer;
		tfooter = tfooter.replaceAll("\\{C1\\}", mainColor + "");
		tfooter = tfooter.replaceAll("\\{C2\\}", titleColor + "");
		tfooter = tfooter.replaceAll("\\{C3\\}", pageColor + "");
		tfooter = String.format(tfooter, title, pageNumber, maxPages);
		return tfooter;
	}

}
