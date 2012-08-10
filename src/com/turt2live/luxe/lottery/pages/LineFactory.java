/*******************************************************************************
 * Copyright (c) 2012 turt2live (Travis Ralston).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.luxe.lottery.pages;

/**
 * Builds a line
 * 
 * @author turt2live
 */
public class LineFactory {

	private String line;

	/**
	 * Creates a new line factory with no default line
	 */
	public LineFactory(){}

	/**
	 * Creates a new line factory with a default line
	 * 
	 * @param line the line
	 */
	public LineFactory(String line){
		this.line = line;
	}

	/**
	 * Sets the line of this line factory
	 * 
	 * @param line the line
	 * @return the factory
	 */
	public LineFactory setLine(String line){
		this.line = line;
		return this;
	}

	/**
	 * Returns a SimpleLine object of the line factory (compiled)
	 * 
	 * @return the SimpleLine object
	 */
	public Line getLine(){
		return new SimpleLine(line);
	}

	/**
	 * Generate lines from an array
	 * 
	 * @param lines the lines
	 * @return the lines objects
	 */
	public static Line[] generateLines(String... lines){
		Line[] ret = new Line[lines.length];
		for(int i = 0; i < lines.length; i++){
			ret[i] = new SimpleLine(lines[i]);
		}
		return ret;
	}

}
