package com.turt2live.luxe.lottery.pages;

/**
 * An enum to represent various menu styles
 * 
 * @author turt2live
 */
public enum MenuStyle{

	DEFAULT(new BasicStyle()),
	LAVA_CRAFT(new LavaCraftStyle());

	private Style style;

	private MenuStyle(Style s){
		this.style = s;
	}

	/**
	 * Gets the style object of this style
	 * 
	 * @return the style
	 */
	public Style getStyle(){
		return style;
	}
}
