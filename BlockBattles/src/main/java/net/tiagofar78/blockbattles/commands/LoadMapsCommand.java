package net.tiagofar78.blockbattles.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.tiagofar78.blockbattles.BlockBattles;
import net.tiagofar78.blockbattles.managers.GamesManager;
import net.tiagofar78.blockbattles.managers.MessagesManager;

public class LoadMapsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MessagesManager messages = MessagesManager.getInstanceByPlayer(sender.getName());
        
        if (!sender.hasPermission(BlockBattles.ADMIN_PERMISSION)) {
            sender.sendMessage(messages.getNotAllowedMessage());
            return true;
        }
        
        GamesManager.generateMaps();
        sender.sendMessage(messages.getMapsLoadedMessage());
        
        return true;
    }

}
