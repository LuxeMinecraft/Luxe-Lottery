package com.turt2live.luxe.lottery.timer;

import com.turt2live.luxe.lottery.Lottery;

public class CountdownLoop extends CountdownListener {

	private long firstRunTime;
	private long normalRunTime;
	private long startTime;
	private boolean running = true;
	private int tid = -1;
	private Lottery plugin = Lottery.getInstance();
	private Countdown countdown;

	public CountdownLoop(long remainingTime, long normalTime, Countdown countdown){
		this.firstRunTime = remainingTime;
		this.normalRunTime = normalTime;
		this.countdown = countdown;
		tid = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, countdown, firstRunTime);
		if(tid == -1){
			plugin.getLogger().severe("Lottery countdown failed");
		}
		startTime = System.currentTimeMillis();
	}

	@Override
	public void onDraw(){
		plugin.getServer().getScheduler().cancelTask(tid);
		if(running){
			tid = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, countdown, normalRunTime);
			if(tid == -1){
				plugin.getLogger().severe("Lottery countdown failed");
			}
			startTime = System.currentTimeMillis();
		}
	}

	public void saveAndClose(){
		plugin.getServer().getScheduler().cancelTask(tid);
		running = false;
		long timeLeft = System.currentTimeMillis() - startTime;
		timeLeft = timeLeft / 1000; // Seconds
		timeLeft = timeLeft * 20; // Ticks
		timeLeft = normalRunTime - timeLeft; // Actual
		if(timeLeft <= 0){
			timeLeft = normalRunTime;
		}
		plugin.getConfig().set("lottery.time-left", timeLeft);
		plugin.saveConfig();
		plugin.reloadConfig();
	}

}
