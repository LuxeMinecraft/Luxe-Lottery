package com.turt2live.luxe.lottery;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Central place for the actual lottery.<br>
 * This contains all the pot functions, drawing functions,
 * and ticket functions.
 * 
 * @author turt2live
 */
public class Pot {

	private double amount = 0;
	private Lottery plugin = Lottery.getInstance();

	public void addTicket(Ticket ticket){
		add(ticket.getCost());
		ticket.save();
	}

	public void addTickets(List<Ticket> tickets){
		for(Ticket ticket : tickets){
			add(ticket.getCost());
			ticket.save();
		}
	}

	public void add(double amount){
		this.amount += amount;
	}

	public void remove(double amount){
		this.amount -= amount;
	}

	public void empty(){
		amount = 0;
	}

	public void save(){
		plugin.getConfig().set("lottery.current-pot", amount);
		plugin.saveConfig();
		plugin.reloadConfig();
	}

	public String draw(){
		plugin.reloadConfig();
		Set<String> participants = plugin.getConfig().getConfigurationSection("tickets").getKeys(false);
		if(participants == null || participants.size() < 0){
			return null;
		}else{
			// We need to generate a list of tickets
			List<Ticket> tickets = new ArrayList<Ticket>();
			for(String name : participants){
				int amount = plugin.getConfig().getInt("tickets." + name + ".amount");
				for(int i = 0; i < amount; i++){
					tickets.add(new Ticket(name, 0)); // Cost is not needed
				}
			}
			// Draw a random number
			Random random = new Random(System.currentTimeMillis() * tickets.size());
			int index = random.nextInt(tickets.size());
			return tickets.get(index).getOwner();
		}
	}

	public void reset(){
		empty();
		plugin.getConfig().set("tickets", null);
		plugin.saveConfig();
		plugin.reloadConfig();
	}

	public double getAmount(){
		return amount;
	}

}
