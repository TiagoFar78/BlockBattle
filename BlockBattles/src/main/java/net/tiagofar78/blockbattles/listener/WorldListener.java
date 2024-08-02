package net.tiagofar78.blockbattles.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

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
    public void onPlayerRegen(EntityRegainHealthEvent e) {
        e.setCancelled(isOnBBWorld(e.getEntity().getWorld().getName()));
    }
    
    @EventHandler
    public void onMobSpawn(EntitySpawnEvent e) {
        e.setCancelled(isOnBBWorld(e.getEntity().getWorld().getName()));
    }
    
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        boolean isCancelled = isOnBBWorld(e.getWorld().getName()) && e.toWeatherState();
        e.setCancelled(isCancelled);
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        e.setCancelled(isOnBBWorld(e.getBlock().getWorld().getName()));
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(isOnBBWorld(e.getWhoClicked().getWorld().getName()));
    }
    
    @EventHandler
    public void onDropItem(PlayerDropItemEvent e) {
        e.setCancelled(isOnBBWorld(e.getPlayer().getWorld().getName()));
    }
    
    @EventHandler
    public void onSwapHandItems(PlayerSwapHandItemsEvent e) {
        e.setCancelled(isOnBBWorld(e.getPlayer().getWorld().getName()));
    }
    
}
