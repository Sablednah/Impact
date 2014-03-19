package me.sablednah.impact;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ImpactCommandExecutor implements CommandExecutor {
    
    public Impact plugin;
    
    public ImpactCommandExecutor(Impact instance) {
        this.plugin = instance;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("impact")) {
            if (args.length > 0 && args[0].toLowerCase().equals("reload")) {
                Boolean doReload = false;
                
                if (sender instanceof Player) {
                    if (sender.hasPermission("impact.reload")) {
                        doReload = true;
                    } else {
                        sender.sendMessage("You do not have permission to reload.");
                        return true;
                    }
                } else {
                    doReload = true;
                }
                
                if (doReload) {
                    Impact.logger.info("Reloading config...");
                    plugin.loadConfiguration(false);
                    return true;
                }
            }
        }
        return false;
    }
}