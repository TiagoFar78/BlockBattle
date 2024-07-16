package net.tiagofar78.blockbattles;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.tiagofar78.blockbattles.commands.LeaveQueueCommand;
import net.tiagofar78.blockbattles.commands.QueueCommand;
import net.tiagofar78.blockbattles.managers.ConfigManager;
import net.tiagofar78.blockbattles.managers.MessagesManager;
import net.tiagofar78.blockbattles.managers.SchematicsManager;

public class BlockBattles extends JavaPlugin {
    
    @Override
    public void onEnable() {
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
        
        getCommand("queue").setExecutor(new QueueCommand());
        getCommand("leavequeue").setExecutor(new LeaveQueueCommand());

        loadResourcesAndManagers();
    }

    public static BlockBattles getBlockBattles() {
        return (BlockBattles) Bukkit.getServer().getPluginManager().getPlugin("TF_BlockBattles");
    }

    private void loadResourcesAndManagers() {
        ConfigManager.load();
        MessagesManager.load();
        SchematicsManager.load();
    }

}
