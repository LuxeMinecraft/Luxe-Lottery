package com.turt2live.luxe.lottery.pages;

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

import org.bukkit.ChatColor;

/**
 * Basic menu style
 * 
 * @author turt2live
 */
public class LavaCraftStyle extends Style {

	private String header = "{C2}%s {C1}({C3}Page %d/%d{C1}):";
	private String footer = "{C2}-- {C3}Page %d/%d{C2} --";

	/**
	 * Creates a new Basic Style
	 */
	public LavaCraftStyle(){
		super(MenuStyle.LAVA_CRAFT);
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
		tfooter = String.format(tfooter, /*title,*/pageNumber, maxPages);
		return tfooter;
	}

}
