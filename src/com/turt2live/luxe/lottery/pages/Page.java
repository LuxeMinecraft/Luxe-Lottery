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

import java.util.UUID;

import org.bukkit.command.CommandSender;


/**
 * A Page
 * 
 * @author turt2live
 */
public class Page {

	protected static Page NULL_PAGE = new Page();
	private Line[] lines = new Line[PageFactory.MAX_LINES];
	private int pageNumber = 1;
	private UUID uid;

	/**
	 * Creates a new page
	 */
	protected Page(){
		uid = UUID.randomUUID();
	}

	/**
	 * Sets the lines of this page
	 * 
	 * @param lines the lines
	 */
	protected void setLines(Line[] lines){
		this.lines = lines;
	}

	/**
	 * Sets the page number of this page
	 * 
	 * @param pageNumber the page number
	 * @throws IndexOutOfBoundsException thrown if the line number does not exist
	 */
	protected void setPageNumber(int pageNumber){
		this.pageNumber = pageNumber;
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
	 * Sets a line in this page, this will overwrite if needed
	 * 
	 * @param line the line index (not array index, so 1 = first line)
	 * @param contents the line itself
	 * @throws IndexOutOfBoundsException thrown if the line number does not exist
	 */
	public void setLine(int line, String contents) throws IndexOutOfBoundsException{
		if(line <= 0 || line > PageFactory.MAX_LINES){
			throw new IndexOutOfBoundsException("Line does not exist");
		}
		lines[line - 1] = new SimpleLine(contents);
	}

	/**
	 * Gets the line from this page
	 * 
	 * @param line the line index (not array index, so 1 = first line)
	 * @return the line
	 * @throws IndexOutOfBoundsException thrown if the line number does not exist
	 */
	public Line getLine(int line) throws IndexOutOfBoundsException{
		if(line <= 0 || line > PageFactory.MAX_LINES){
			throw new IndexOutOfBoundsException("Line does not exist");
		}
		return lines[line - 1];
	}

	/**
	 * Gets the page number of this page
	 * 
	 * @return the page number
	 */
	public int getPageNumber(){
		return pageNumber;
	}

	/**
	 * Sends this page to the target
	 * 
	 * @param target the target
	 * @throws InvalidPageException thrown if the page is invalid
	 */
	public void sendTo(CommandSender target) throws InvalidPageException{
		validate();
		for(int i = 0; i < lines.length; i++){
			if(lines[i] != null){
				lines[i].format();
				target.sendMessage(lines[i].getLine());
			}
		}
	}

	/**
	 * Validates the page
	 * 
	 * @throws InvalidPageException thrown if invalid
	 */
	public void validate() throws InvalidPageException{
		if(pageNumber <= 0){
			throw new InvalidPageException(pageNumber + " is not a valid page number");
		}
		if(lines.length < PageFactory.MAX_LINES || lines.length > PageFactory.MAX_LINES){
			throw new InvalidPageException("Invalid number of lines");
		}
	}
}
