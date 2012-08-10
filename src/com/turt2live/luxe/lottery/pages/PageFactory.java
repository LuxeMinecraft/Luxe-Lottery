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


/**
 * Creates a page
 * 
 * @author turt2live
 */
public class PageFactory {

	public static final int MAX_LINES = 8;
	private Line[] lines = new Line[MAX_LINES];
	private int pageNumber = 1;

	/**
	 * Creates a new page factory with a page number and some lines
	 * 
	 * @param pageNumber the page number
	 * @param lines the lines
	 * @throws InvalidPageException thrown if the passed in lines exceed (or less than) MAX_LINES
	 * @throws IndexOutOfBoundsException thrown if the page number is <1
	 */
	public PageFactory(int pageNumber, String[] lines) throws InvalidPageException, IndexOutOfBoundsException{
		if(pageNumber < 1){
			throw new IndexOutOfBoundsException("Page number too small");
		}
		if(lines.length > this.lines.length){
			throw new InvalidPageException("Too many lines");
		}
		if(lines.length < this.lines.length){
			throw new InvalidPageException("Too little lines");
		}
		this.pageNumber = pageNumber;
		this.lines = LineFactory.generateLines(lines);
	}

	/**
	 * Creates a new page factory with a page number and some lines
	 * 
	 * @param pageNumber the page number
	 * @param lines the lines
	 * @throws InvalidPageException thrown if the passed in lines exceed (or less than) MAX_LINES
	 * @throws IndexOutOfBoundsException thrown if the page number is <1
	 */
	public PageFactory(int pageNumber, Line[] lines) throws InvalidPageException, IndexOutOfBoundsException{
		if(pageNumber < 1){
			throw new IndexOutOfBoundsException("Page number too small");
		}
		if(lines.length > this.lines.length){
			throw new InvalidPageException("Too many lines");
		}
		if(lines.length < this.lines.length){
			throw new InvalidPageException("Too little lines");
		}
		this.pageNumber = pageNumber;
		this.lines = lines;
	}

	/**
	 * Creates a new page factory with a page number and no lines
	 * 
	 * @param pageNumber the page number
	 * @throws IndexOutOfBoundsException thrown if the page number is <1
	 */
	public PageFactory(int pageNumber) throws IndexOutOfBoundsException{
		if(pageNumber < 1){
			throw new IndexOutOfBoundsException("Page number too small");
		}
		this.pageNumber = pageNumber;
	}

	/**
	 * Creates a new page factory with a default page number (1) and no lines
	 */
	public PageFactory(){}

	/**
	 * Sets the page number of the page
	 * 
	 * @param pageNumber the page number
	 * @return the factory
	 * @throws IndexOutOfBoundsException thrown if the page number is <1
	 */
	public PageFactory setPageNumber(int pageNumber) throws IndexOutOfBoundsException{
		if(pageNumber < 1){
			throw new IndexOutOfBoundsException("Page number too small");
		}
		this.pageNumber = pageNumber;
		return this;
	}

	/**
	 * Sets a line in the page<br>
	 * This will throw an index out of bounds exception if you use
	 * 
	 * @param line the line index (not array index, so 1 = first line)
	 * @param contents the actual line
	 * @return the factory
	 * @throws IndexOutOfBoundsException thrown if the line number does not exist
	 */
	public PageFactory setLine(int line, String contents) throws IndexOutOfBoundsException{
		if(line <= 0 || line > MAX_LINES){
			throw new IndexOutOfBoundsException("Line does not exist");
		}
		lines[line - 1] = new SimpleLine(contents);
		return this;
	}

	/**
	 * Sets a line in the page<br>
	 * This will throw an index out of bounds exception if you use
	 * 
	 * @param line the line index (not array index, so 1 = first line)
	 * @param contents the actual line
	 * @return the factory
	 * @throws IndexOutOfBoundsException thrown if the line number does not exist
	 */
	public PageFactory setLine(int line, Line contents){
		if(line <= 0 || line > MAX_LINES){
			throw new IndexOutOfBoundsException("Line does not exist");
		}
		lines[line - 1] = contents;
		return this;
	}

	/**
	 * Generates the page
	 * 
	 * @return the page
	 * @throws InvalidPageException thrown if the page cannot be validated
	 */
	public Page getPage() throws InvalidPageException{
		Page page = new Page();
		page.setLines(lines);
		page.setPageNumber(pageNumber);
		page.validate();
		return page;
	}

	/**
	 * Imports a page into this factory
	 * 
	 * @param page the page
	 * @return the factory
	 * @throws InvalidPageException thrown if validation on the page cannot be completed
	 */
	public PageFactory importPage(Page page) throws InvalidPageException{
		page.validate();
		this.pageNumber = page.getPageNumber();
		for(int line = 0; line < lines.length; line++){
			this.lines[line] = page.getLine(line + 1);
		}
		return this;
	}

}
