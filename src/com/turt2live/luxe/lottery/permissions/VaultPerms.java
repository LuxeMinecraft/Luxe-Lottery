package com.turt2live.luxe.lottery.permissions;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.turt2live.luxe.lottery.Lottery;

public class VaultPerms {

	private Permission perms;

	public VaultPerms(){
		Plugin plugin = Lottery.getInstance().getServer().getPluginManager().getPlugin("Vault");
		if(plugin != null){
			RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
			perms = rsp.getProvider();
			if(perms == null){
				Lottery.getInstance().getLogger().severe("This plugin requires Vault (and a compatible economy plugin) to work!");
				Lottery.getInstance().getServer().getPluginManager().disablePlugin(Lottery.getInstance());
				return;
			}
		}
	}

	public boolean has(Player player, String node, World world){
		if(perms == null){
			return Lottery.getInstance().hasPermission(player, node);
		}
		if(perms.hasSuperPermsCompat()){
			return Lottery.getInstance().hasPermission(player, node);
		}
		try{
			return perms.playerHas(world, player.getName(), node);
		}catch(UnsupportedOperationException e){
			return Lottery.getInstance().hasPermission(player, node);
		}
	}

	public boolean has(CommandSender sender, String node){
		if(perms == null){
			return sender.hasPermission(node);
		}
		if(perms.hasSuperPermsCompat()){
			return sender.hasPermission(node);
		}
		try{
			return perms.has(sender, node);
		}catch(UnsupportedOperationException e){
			return sender.hasPermission(node);
		}
	}

}
