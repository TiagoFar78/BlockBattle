package net.tiagofar78.blockbattles.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import net.tiagofar78.blockbattles.dataobjects.BBGamePlayer;
import net.tiagofar78.blockbattles.managers.ConfigManager;
import net.tiagofar78.blockbattles.managers.GamesManager;

public class PlayerListener implements Listener {
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        ConfigManager config = ConfigManager.getInstance();
        
        Location location = e.getBlock().getLocation();
        if (!location.getWorld().getName().equals(config.getWorldName())) {
            return;
        }
        
        Player player = e.getPlayer();
        BBGamePlayer gameAndPlayer = GamesManager.findGameAndPlayer(player);
        if (gameAndPlayer != null) {
            int itemSlot = player.getInventory().getHeldItemSlot();
            boolean cancel = gameAndPlayer.getGame().playerPlacedBlock(gameAndPlayer.getPlayer(), itemSlot, location);
            e.setCancelled(cancel);
        }
    }

}
