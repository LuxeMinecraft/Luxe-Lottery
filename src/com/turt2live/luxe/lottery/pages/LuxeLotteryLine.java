package com.turt2live.luxe.lottery.pages;

import org.bukkit.ChatColor;

import com.turt2live.luxe.lottery.Lottery;

public class LuxeLotteryLine extends Line {

	public LuxeLotteryLine(String raw){
		this.line = raw;
	}

	@Override
	public void format(){
		String parts[] = line.split(" "); // format: <name> <amount>
		if(parts.length < 2){
			this.line = ChatColor.RED + "Invalid Winner";
		}
		String winnings = "UNKNOWN";
		try{
			double a = Double.parseDouble(parts[1]);
			winnings = Lottery.formatMoney(a);
		}catch(Exception e){}
		this.line = ChatColor.DARK_GREEN + parts[0] + ChatColor.GREEN + " won " + ChatColor.DARK_GREEN + winnings;
	}

}
