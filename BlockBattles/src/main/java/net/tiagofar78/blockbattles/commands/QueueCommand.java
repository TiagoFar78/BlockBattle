package net.tiagofar78.blockbattles.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.tiagofar78.blockbattles.managers.GamesManager;
import net.tiagofar78.blockbattles.managers.MessagesManager;
import net.tiagofar78.blockbattles.managers.QueueManager;

public class QueueCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by players");
            return false;
        }
        
        Player player = (Player) sender;
        MessagesManager messages = MessagesManager.getInstanceByPlayer(player.getName());
        
        if (GamesManager.findGameAndPlayer(player) != null) {
            player.sendMessage(messages.getCantExecuteInGameMessage());
            return false;
        }
        
        boolean isRanked = args.length > 0 && args[0].equals("-r");
        
        int returnCode = isRanked ? QueueManager.joinRankedQueue(player) : QueueManager.joinNormalQueue(player);
        if (returnCode == 0) {
            player.sendMessage(messages.getJoinedQueueMessage());
        }
        else if (returnCode == -1) {
            player.sendMessage(messages.getAlreadyInQueueMessage());
        }
        
        return false;
    }
    
}
