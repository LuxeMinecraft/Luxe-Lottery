package com.turt2live.luxe.lottery;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class LotteryCommands implements CommandExecutor {

	private Lottery plugin = Lottery.getInstance();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(command.getName().equalsIgnoreCase("lottery") && plugin.hasPermission(sender, "lottery.user")){
			if(args.length > 0){
				if(args[0].equalsIgnoreCase("buy")){
					int amount = 1;
					if(args.length > 1){
						try{
							amount = Integer.parseInt(args[1]);
						}catch(Exception e){
							amount = 1; // Just in case
							sender.sendMessage(Lottery.prefix() + ChatColor.RED + args[1] + " is not a number, so I assumed you meant 1");
						}
					}
					double ticketCost = plugin.getConfig().getDouble("general.cost-per-ticket");
					double cost = amount * ticketCost;
					if(plugin.isRecentWinner(sender.getName())){
						sender.sendMessage(Lottery.prefix() + ChatColor.RED + "You have won a lottery in the past 10 days.");
						sender.sendMessage(Lottery.prefix() + ChatColor.RED + "Don't be so greedy.");
						return true;
					}
					if(plugin.getActiveTickets(sender.getName()) >= plugin.getConfig().getInt("general.max-tickets")){
						sender.sendMessage(Lottery.prefix() + ChatColor.RED + "You have already bought the maximum number of tickets (" + plugin.getConfig().getInt("general.max-tickets") + ")");
						sender.sendMessage(Lottery.prefix() + ChatColor.RED + "Don't be so greedy.");
						return true;
					}
					if(amount < 1){
						sender.sendMessage(Lottery.prefix() + ChatColor.RED + "Negative Tickets? Yea...that makes sense.");
						return true;
					}
					if(!plugin.getVaultEcon().canAfford(sender.getName(), cost)){
						sender.sendMessage(Lottery.prefix() + ChatColor.RED + "You don't have enough!");
						return true;
					}
					List<Ticket> tickets = new ArrayList<Ticket>();
					for(int i = 0; i < amount; i++){
						tickets.add(new Ticket(sender.getName(), ticketCost));
					}
					plugin.getActivePot().addTickets(tickets);
					sender.sendMessage(Lottery.prefix() + ChatColor.GREEN + "You purchased " + amount + " ticket" + (amount > 1 || amount == 0 ? "s" : ""));
				}else if(args[0].equalsIgnoreCase("winners")){
					plugin.showRecentWinners(sender);
				}else if(args[0].equalsIgnoreCase("claim")){
					double amount = plugin.getWaitingPrize(sender.getName());
					if(amount > 0){
						String winnings = Lottery.formatMoney(amount);
						sender.sendMessage(Lottery.prefix() + ChatColor.GREEN + "You gained " + winnings + "!");
						plugin.getVaultEcon().deposit(sender.getName(), amount);
						plugin.clearWaitingPrize(sender.getName());
					}else{
						sender.sendMessage(Lottery.prefix() + ChatColor.RED + "You don't have any winnings to collect");
					}
				}else{
					showHelp(sender);
				}
			}else{
				String potAmount = Lottery.formatMoney(plugin.getActivePot().getAmount());
				int tickets = plugin.getActiveTickets(sender.getName());
				String costPer = Lottery.formatMoney(plugin.getConfig().getDouble("general.cost-per-ticket"));
				sender.sendMessage(Lottery.prefix() + ChatColor.DARK_GREEN + "There is currently " + ChatColor.GREEN + potAmount + ChatColor.DARK_GREEN + " in the pot");
				sender.sendMessage(Lottery.prefix() + ChatColor.DARK_GREEN + "You have " + ChatColor.GREEN + tickets + ChatColor.DARK_GREEN + " ticket" + (tickets == 0 || tickets > 1 ? "s" : ""));
				sender.sendMessage(Lottery.prefix() + ChatColor.DARK_GREEN + "Tickets currently cost " + ChatColor.GREEN + costPer + ChatColor.DARK_GREEN + " per ticket");
				sender.sendMessage(Lottery.prefix() + ChatColor.DARK_GREEN + "Type /lottery help for more information.");
			}
			return true;
		}else if(command.getName().equalsIgnoreCase("lotteryadmin") && plugin.hasPermission(sender, "lottery.admin")){
			if(args.length > 0){
				if(args[0].equalsIgnoreCase("addtopot")){
					if(args.length > 1){
						try{
							double amount = Double.parseDouble(args[1]);
							plugin.getActivePot().add(amount);
							sender.sendMessage(Lottery.prefix() + ChatColor.GREEN + "Added " + Lottery.formatMoney(amount) + " to the pot");
						}catch(Exception e){ // Not a number
							sender.sendMessage(Lottery.prefix() + ChatColor.RED + "Incorrect syntax, please use " + ChatColor.DARK_RED + "/lotteryadmin addtopot <n>");
						}
					}else{
						sender.sendMessage(Lottery.prefix() + ChatColor.RED + "Incorrect syntax, please use " + ChatColor.DARK_RED + "/lotteryadmin addtopot <n>");
					}
				}else if(args[0].equalsIgnoreCase("removefrompot")){
					if(args.length > 1){
						try{
							double amount = Double.parseDouble(args[1]);
							plugin.getActivePot().remove(amount);
							sender.sendMessage(Lottery.prefix() + ChatColor.GREEN + "Removed " + Lottery.formatMoney(amount) + " from the pot");
						}catch(Exception e){ // Not a number
							sender.sendMessage(Lottery.prefix() + ChatColor.RED + "Incorrect syntax, please use " + ChatColor.DARK_RED + "/lotteryadmin removefrompot <n>");
						}
					}else{
						sender.sendMessage(Lottery.prefix() + ChatColor.RED + "Incorrect syntax, please use " + ChatColor.DARK_RED + "/lotteryadmin removefrompot <n>");
					}
				}else if(args[0].equalsIgnoreCase("draw")){
					// TODO
				}
			}else{
				showHelp(sender);
			}
			return true;
		}
		// No permission messages
		if(command.getName().equalsIgnoreCase("lottery") || command.getName().equalsIgnoreCase("lotteryadmin")){
			sender.sendMessage(Lottery.prefix() + ChatColor.DARK_RED + "You don't have permission");
			return true;
		}
		return false;
	}

	private void showHelp(CommandSender sender){
		sender.sendMessage(Lottery.prefix() + ChatColor.DARK_AQUA + "Lottery Help");
		sender.sendMessage(Lottery.prefix() + ChatColor.DARK_GREEN + "/lottery" + ChatColor.GREEN + " Shows lottery info");
		sender.sendMessage(Lottery.prefix() + ChatColor.DARK_GREEN + "/lottery buy [n]" + ChatColor.GREEN + " Buy 1 or [n] tickets");
		sender.sendMessage(Lottery.prefix() + ChatColor.DARK_GREEN + "/lottery winners" + ChatColor.GREEN + " Shows previous winners");
		sender.sendMessage(Lottery.prefix() + ChatColor.DARK_GREEN + "/lottery claim" + ChatColor.GREEN + " Claims your recent winnings");
		if(plugin.hasPermission(sender, "lottery.admin")){
			sender.sendMessage(Lottery.prefix() + ChatColor.DARK_GREEN + "/lotteryadmin addtopot <n>" + ChatColor.GREEN + " Adds <n> to pot");
			sender.sendMessage(Lottery.prefix() + ChatColor.DARK_GREEN + "/lotteryadmin removefrompot <n>" + ChatColor.GREEN + " Removes <n> from pot");
			sender.sendMessage(Lottery.prefix() + ChatColor.DARK_GREEN + "/lotteryadmin draw" + ChatColor.GREEN + " Forces a draw");
		}
	}

}
