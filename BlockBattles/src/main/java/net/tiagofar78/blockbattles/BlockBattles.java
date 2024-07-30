package net.tiagofar78.blockbattles;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.tiagofar78.blockbattles.commands.LeaveQueueCommand;
import net.tiagofar78.blockbattles.commands.LoadMapsCommand;
import net.tiagofar78.blockbattles.commands.QueueCommand;
import net.tiagofar78.blockbattles.listener.PlayerListener;
import net.tiagofar78.blockbattles.listener.WorldListener;
import net.tiagofar78.blockbattles.managers.ConfigManager;
import net.tiagofar78.blockbattles.managers.MessagesManager;
import net.tiagofar78.blockbattles.managers.SchematicsManager;
import net.tiagofar78.blockbattles.placeholders.ExpansionCaller;

public class BlockBattles extends JavaPlugin {
    
    public static final String ADMIN_PERMISSION = "BlockBattles.Admin"; 
    
    @Override
    public void onEnable() {
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
        
        getCommand("queue").setExecutor(new QueueCommand());
        getCommand("leavequeue").setExecutor(new LeaveQueueCommand());
        getCommand("bbloadmaps").setExecutor(new LoadMapsCommand());
        
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new WorldListener(), this);

        loadResourcesAndManagers();
        
        setWorldDifficulty();
        
        if (pm.isPluginEnabled("PlaceholderAPI")) {
            new ExpansionCaller().register();
        }
    }

    public static BlockBattles getBlockBattles() {
        return (BlockBattles) Bukkit.getServer().getPluginManager().getPlugin("TF_BlockBattles");
    }

    private void loadResourcesAndManagers() {
        ConfigManager.load();
        MessagesManager.load();
        SchematicsManager.load();
    }
    
    private void setWorldDifficulty() {
        ConfigManager config = ConfigManager.getInstance();
        Bukkit.getWorld(config.getWorldName()).setDifficulty(Difficulty.HARD);
    }

}
