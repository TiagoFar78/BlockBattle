package net.tiagofar78.blockbattles;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.tiagofar78.blockbattles.managers.ConfigManager;

public class BBPlayer {
    
    private final static int BUKKIT_MAX_HEALTH = 20;
    
    private Player _player;
    private double _health;
    
    public BBPlayer(Player player) {
        ConfigManager config = ConfigManager.getInstance();
        
        _player = player;
        _health = config.getStartingHealth();
    }
    
    public Player getBukkitPlayer() {
        return _player;
    }
    
    public double getHealth() {
        return _health;
    }
    
    public void updateHealth(double amount) {
        _health += amount;
        int maxBBPlayerHealth = ConfigManager.getInstance().getStartingHealth();
        _player.setHealth(_health * BUKKIT_MAX_HEALTH / maxBBPlayerHealth);
    }
    
    public void resetPlayer(Location spawnPoint) {
        PlayerInventory inv = _player.getInventory();
        inv.clear();
        inv.setArmorContents(new ItemStack[4]);
        inv.setItemInOffHand(null);
        
        _player.setGameMode(GameMode.SURVIVAL);
        _player.teleport(spawnPoint);   
    }
    
    @Override
    public boolean equals(Object o) {
        return ((BBPlayer) o).getBukkitPlayer().getName().equals(_player.getName());
    }

}
