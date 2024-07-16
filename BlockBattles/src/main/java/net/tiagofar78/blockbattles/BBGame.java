package net.tiagofar78.blockbattles;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.tiagofar78.blockbattles.managers.ConfigManager;
import net.tiagofar78.blockbattles.managers.SchematicsManager;

public class BBGame {
    
    private Location _referenceLoc;
    private Player _player1;
    private Player _player2;
    
    public BBGame(Location referenceLocation, Player player1, Player player2) {
        _referenceLoc = referenceLocation;
        _player1 = player1;
        _player2 = player2;
        
        generateMap();
        
        ConfigManager config = ConfigManager.getInstance();
        sendPlayerToGame(_player1, config.getPlayer1SpawnPoint().add(referenceLocation));
        sendPlayerToGame(_player2, config.getPlayer2SpawnPoint().add(referenceLocation));
    }
    
    private void generateMap() {
        SchematicsManager.pasteMapSchematic(_referenceLoc);
    }
    
    private void sendPlayerToGame(Player player, Location spawnPoint) {
        PlayerInventory inv = player.getInventory();
        inv.clear();
        inv.setArmorContents(new ItemStack[4]);
        inv.setItemInOffHand(null);
        
        player.setGameMode(GameMode.SURVIVAL);
        player.setFlying(false);
        player.setAllowFlight(false);
        player.teleport(spawnPoint);        
    }

}
