package me.sablednah.impact;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class ImpactCommandExecutor implements CommandExecutor {
	public Impact plugin;

	public ImpactCommandExecutor(Impact instance) {
		this.plugin=instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("impact")){
			//command
		}
		return false; 
	}
}