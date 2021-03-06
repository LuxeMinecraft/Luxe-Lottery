package com.turt2live.luxe.lottery;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import com.feildmaster.lib.configuration.PluginWrapper;
import com.turt2live.luxe.lottery.economy.VaultEcon;
import com.turt2live.luxe.lottery.pages.LuxeLotteryLine;
import com.turt2live.luxe.lottery.pages.Pagination;
import com.turt2live.luxe.lottery.permissions.VaultPerms;
import com.turt2live.luxe.lottery.timer.Countdown;
import com.turt2live.luxe.lottery.timer.CountdownLoop;

public class Lottery extends PluginWrapper implements Listener {

	private static Lottery instance;

	public static Lottery getInstance(){
		return instance;
	}

	private VaultPerms perms;
	private VaultEcon econ;
	private Pot pot;
	private Countdown countdown;
	private CountdownLoop cdLoop;

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

		// For login message
		getServer().getPluginManager().registerEvents(this, this);

		// Start countdown
		long runtimeRaw = getConfig().getLong("general.draw-every");
		long runtime = runtimeRaw * 60 * 60 * 20;
		long left = getConfig().getLong("lottery.time-left", runtime);
		countdown = new Countdown();
		cdLoop = new CountdownLoop(left, runtime, countdown);
		countdown.setListener(cdLoop);

		// Spam console
		getLogger().info("Loaded! Plugin by turt2live");
	}

	@Override
	public void onDisable(){
		pot.save();
		countdown.close();
		cdLoop.saveAndClose();
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
		long now = System.currentTimeMillis();
		Long expire = getConfig().getLong("winner." + name);
		if(expire == null || expire == 0 || expire >= now){
			// Remove winner
			getConfig().set("winner." + name, null);
			saveConfig();
			reloadConfig();
			return false;
		}
		return true;
	}

	public void showRecentWinners(CommandSender sender, int pageNumber){
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
			if(pageNumber < 1){
				pageNumber = 1;
			}else if(pageNumber > page.maxPages()){
				pageNumber = page.maxPages();
			}
			page.showTo(sender, pageNumber);
		}
	}

	public void addRecentWinner(String winner, double amount){
		// Only keep 10
		reloadConfig();
		long days10 = 24 * 60 * 60 * 1000; // Milliseconds
		long nowPlus10Days = System.currentTimeMillis() + days10;
		getConfig().set("winner." + winner, nowPlus10Days);
		List<String> raw = getConfig().getStringList("winners");
		if(raw == null){
			raw = new ArrayList<String>();
		}
		List<String> newList = new ArrayList<String>();
		newList.add(winner + " " + amount);
		for(int i = 0; i < (raw.size() >= 9 ? 9 : raw.size()); i++){
			newList.add(raw.get(i));
		}
		getConfig().set("winners", newList);
		saveConfig();
		reloadConfig();
	}

	public double getWaitingPrize(String name){
		return getConfig().getDouble("prizes." + name, 0);
	}

	public void clearWaitingPrize(String name){
		getConfig().set("prizes." + name, null);
		saveConfig();
		reloadConfig();
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		double waiting = getWaitingPrize(player.getName());
		if(waiting > 0){
			player.sendMessage(prefix() + ChatColor.GOLD + "You have " + ChatColor.YELLOW + formatMoney(waiting) + ChatColor.GOLD + " waiting for you!");
			player.sendMessage(prefix() + ChatColor.GREEN + "Type \"/lottery claim\" to collect it!");
		}
	}

}
