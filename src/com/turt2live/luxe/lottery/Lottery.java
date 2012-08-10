package com.turt2live.luxe.lottery;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.feildmaster.lib.configuration.PluginWrapper;
import com.turt2live.luxe.lottery.economy.VaultEcon;
import com.turt2live.luxe.lottery.permissions.VaultPerms;

public class Lottery extends PluginWrapper implements Listener {

	private static Lottery instance;

	public static Lottery getInstance(){
		return instance;
	}

	private VaultPerms perms;
	private VaultEcon econ;

	@Override
	public void onEnable(){
		instance = this;

		// Check configuration
		getConfig().loadDefaults(getResource("resources/config.yml"));
		if(!getConfig().fileExists() || !getConfig().checkDefaults()){
			getConfig().saveDefaults();
		}
		getConfig().load();

		// Load Vault (if we can)
		Plugin plugin = getServer().getPluginManager().getPlugin("Vault");
		if(plugin != null){
			perms = new VaultPerms();
			econ = new VaultEcon();
		}else{
			getLogger().severe("This plugin requires Vault to work!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		// Commands setup
		LotteryCommands commands = new LotteryCommands();
		// TODO

		// Spam console
		getLogger().info("Loaded! Plugin by turt2live");
	}

	@Override
	public void onDisable(){
		getLogger().info("Disabled! Plugin by turt2live");
	}

	public VaultEcon getVaultEcon(){
		return econ;
	}

	public VaultPerms getVaultPerms(){
		return perms;
	}

	public boolean hasPermission(CommandSender sender, String permission){
		if(perms != null){
			if(sender instanceof Player){
				return perms.has((Player) sender, permission, ((Player) sender).getWorld());
			}else{
				return perms.has(sender, permission);
			}
		}
		return sender.hasPermission(permission);
	}

	public static String prefix(){
		return ChatColor.translateAlternateColorCodes('&', Lottery.getInstance().getConfig().getString("general.prefix")).trim() + " ";
	}

}
