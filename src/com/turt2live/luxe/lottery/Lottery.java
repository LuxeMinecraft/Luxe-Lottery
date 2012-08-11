package com.turt2live.luxe.lottery;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.feildmaster.lib.configuration.PluginWrapper;
import com.turt2live.luxe.lottery.economy.VaultEcon;
import com.turt2live.luxe.lottery.pages.LuxeLotteryLine;
import com.turt2live.luxe.lottery.pages.Pagination;
import com.turt2live.luxe.lottery.permissions.VaultPerms;

public class Lottery extends PluginWrapper implements Listener {

	private static Lottery instance;

	public static Lottery getInstance(){
		return instance;
	}

	private VaultPerms perms;
	private VaultEcon econ;
	private Pot pot;

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
			if(econ.isNull()){ // We require economy
				getLogger().severe("This plugin requires Vault (and an economy) to work!");
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
		}else{
			getLogger().severe("This plugin requires Vault to work!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		// Setup lottery function
		pot = new Pot();
		pot.add(getConfig().getDouble("lottery.current-pot", 0));

		// Commands setup
		LotteryCommands commands = new LotteryCommands();
		getCommand("lottery").setExecutor(commands);
		getCommand("lotteryadmin").setExecutor(commands);

		// Spam console
		getLogger().info("Loaded! Plugin by turt2live");
	}

	@Override
	public void onDisable(){
		pot.save();
		getLogger().info("Disabled! Plugin by turt2live");
	}

	public Pot getActivePot(){
		return pot;
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

	public static String formatMoney(double amount){
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
		return (currencyFormatter.format(amount).replace('$', ' ') + " Luxe").trim();
	}

	public int getActiveTickets(String owner){
		return getConfig().getInt("tickets." + owner + ".amount", 0);
	}

	public boolean isRecentWinner(String name){
		reloadConfig();
		List<String> recent = getConfig().getStringList("winners");
		if(recent != null){
			for(String r : recent){
				String[] parts = r.split(" ");
				if(parts[0].equalsIgnoreCase(name)){
					return true;
				}
			}
		}
		return false;
	}

	public void showRecentWinners(CommandSender sender){
		reloadConfig();
		List<String> raw = getConfig().getStringList("winners");
		if(raw == null || raw.size() <= 0){
			sender.sendMessage(prefix() + ChatColor.AQUA + "No one has recently won! Be the first, buy a ticket with /lottery buy");
		}else{
			List<LuxeLotteryLine> lines = new ArrayList<LuxeLotteryLine>();
			for(String r : raw){
				lines.add(new LuxeLotteryLine(r));
			}
			Pagination page = new Pagination(lines);
			page.generate("Recent Winners", prefix(), ChatColor.DARK_AQUA, ChatColor.AQUA, ChatColor.GRAY);
			page.showTo(sender, 1);
		}
	}

	public double getWaitingPrize(String name){
		return getConfig().getDouble("prizes." + name, 0);
	}

	public void clearWaitingPrize(String name){
		getConfig().set("prizes." + name, null);
		saveConfig();
		reloadConfig();
	}

}
