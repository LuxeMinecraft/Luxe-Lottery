package com.turt2live.luxe.lottery.timer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.turt2live.luxe.lottery.Lottery;

public class Countdown implements Runnable {

	private CountdownListener listener;
	private Lottery plugin = Lottery.getInstance();
	private boolean running = true;

	public void setListener(CountdownListener listener){
		this.listener = listener;
	}

	@Override
	public void run(){
		if(!running)
			return;
		String winningName = plugin.getActivePot().draw();
		if(winningName != null){
			double amount = plugin.getActivePot().getAmount();
			plugin.getActivePot().setWinner(winningName, amount);
			plugin.getActivePot().reset();
			Bukkit.broadcastMessage(Lottery.prefix() + ChatColor.GOLD + winningName + " has won " + Lottery.formatMoney(amount));
		}
		if(listener != null){
			listener.onDraw();
		}
	}

	public void close(){
		running = false;
	}

}
