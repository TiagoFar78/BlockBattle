package net.tiagofar78.blockbattles.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import net.tiagofar78.blockbattles.managers.ConfigManager;

public class WorldListener implements Listener {
    
    private boolean isOnBBWorld(String worldName) {
        ConfigManager config = ConfigManager.getInstance();
        return worldName.equals(config.getWorldName());
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        e.setCancelled(isOnBBWorld(e.getEntity().getWorld().getName()));
    }
    
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        e.setCancelled(isOnBBWorld(e.getEntity().getWorld().getName()));
    }
    
    @EventHandler
    public void onMobSpawn(EntitySpawnEvent e) {
        e.setCancelled(isOnBBWorld(e.getEntity().getWorld().getName()));
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        e.setCancelled(isOnBBWorld(e.getBlock().getWorld().getName()));
    }
    
}
