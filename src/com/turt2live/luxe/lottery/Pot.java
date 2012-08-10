package com.turt2live.luxe.lottery;

import java.util.List;

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
	}

}
