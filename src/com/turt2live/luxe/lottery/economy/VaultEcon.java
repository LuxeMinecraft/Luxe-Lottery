package com.turt2live.luxe.lottery.economy;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.turt2live.luxe.lottery.Lottery;

public class VaultEcon {

	private Economy econ;

	public VaultEcon(){
		Plugin plugin = Lottery.getInstance().getServer().getPluginManager().getPlugin("Vault");
		if(plugin != null){
			RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
			econ = rsp.getProvider();
			if(econ == null){
				Lottery.getInstance().getLogger().severe("This plugin requires Vault (and a compatible economy plugin) to work!");
				Lottery.getInstance().getServer().getPluginManager().disablePlugin(Lottery.getInstance());
				return;
			}
		}
	}

	public void withdraw(String account, double cost){
		econ.withdrawPlayer(account, cost);
	}

	public void deposit(String account, double cost){
		econ.depositPlayer(account, cost);
	}

}
