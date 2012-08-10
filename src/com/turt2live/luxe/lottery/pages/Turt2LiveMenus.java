package com.turt2live.luxe.lottery.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;


/**
 * Plugin version of menus
 * 
 * @author turt2live
 */
public class Turt2LiveMenus extends JavaPlugin {

	private static Turt2LiveMenus instance;
	private List<Menu> menus = new ArrayList<Menu>();

	@Override
	public void onEnable(){
		instance = this;
		getLogger().info("Enabled. (By turt2live)");
	}

	@Override
	public void onDisable(){
		instance = null;
		getLogger().info("Disabled (By turt2live)");
	}

	/**
	 * Adds a menu to the registered menus list
	 * 
	 * @param menu the menu
	 * @throws InvalidMenuException thrown if the menu is invalid
	 */
	public void registerMenu(Menu menu) throws InvalidMenuException{
		menu.validate();
		menus.add(menu);
	}

	/**
	 * Removes a menu from the registered menus list
	 * 
	 * @param menu the menu
	 */
	public void unregisterMenu(Menu menu){
		menus.remove(menu);
	}

	/**
	 * Gets a menu
	 * 
	 * @param uid the UUID of the menu
	 * @return the menu, or null if no menu
	 */
	public Menu getMenu(UUID uid){
		for(Menu menu : menus){
			if(menu.getUUID().compareTo(uid) == 0){
				return menu;
			}
		}
		return null;
	}

	/**
	 * Gets a list of menus matching a title
	 * 
	 * @param title the title
	 * @param strict true to enable case sensitve searching
	 * @return the list of menus
	 */
	public List<Menu> getMenus(String title, boolean strict){
		List<Menu> menus = new ArrayList<Menu>();
		for(Menu menu : this.menus){
			if(strict){
				if(menu.getTitle().equals(title)){
					menus.add(menu);
				}
			}else{
				if(menu.getTitle().equalsIgnoreCase(title)){
					menus.add(menu);
				}
			}
		}
		return menus;
	}

	/**
	 * Gets the instance of the plugin
	 * 
	 * @return the instance
	 */
	public static Turt2LiveMenus getInstance(){
		return instance;
	}
}
