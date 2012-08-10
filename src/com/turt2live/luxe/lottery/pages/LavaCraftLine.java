package com.turt2live.luxe.lottery.pages;

import org.bukkit.ChatColor;

public class LavaCraftLine extends Line {

	/**
	 * Creates a new LavaCraft Style line
	 */
	public LavaCraftLine(){
		super();
	}

	/**
	 * Creates a new LavaCraft Style line with a default value
	 * 
	 * @param line the line
	 */
	public LavaCraftLine(String line){
		super(line);
	}

	@Override
	public void format(){
		super.line = ChatColor.YELLOW + super.line;
	}

}
