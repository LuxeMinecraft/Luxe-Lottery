package com.turt2live.luxe.lottery;

public class Ticket {

	private String owner;
	private double cost;
	private Lottery plugin = Lottery.getInstance();

	public Ticket(String owner, double cost){
		this.owner = owner;
		this.cost = cost;
	}

	public String getOwner(){
		return owner;
	}

	public double getCost(){
		return cost;
	}

	public void save(){
		plugin.getConfig().set("tickets." + owner + ".amount", plugin.getConfig().getInt("tickets." + owner + ".amount", 0) + 1);
	}

}
