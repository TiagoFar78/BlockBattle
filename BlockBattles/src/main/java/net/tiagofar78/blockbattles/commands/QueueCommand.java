package net.tiagofar78.blockbattles.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.tiagofar78.blockbattles.managers.QueueManager;

public class QueueCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by players");
        }
        
        Player player = (Player) sender;
        int returnCode = QueueManager.joinQueue(player);
        
        if (returnCode == 0) {
            // TODO send success message
        }
        else {
            // TODO send already in queue message
        }
        
        return false;
    }
    
}
