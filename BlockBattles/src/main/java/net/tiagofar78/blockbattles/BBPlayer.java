package net.tiagofar78.blockbattles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.tiagofar78.blockbattles.managers.ConfigManager;
import net.tiagofar78.blockbattles.managers.MessagesManager;

public class BBPlayer {
    
    private final static int BUKKIT_MAX_HEALTH = 20;
    private final static int SCOREBOARD_HEALTH_LINE_1 = 1;
    private final static int SCOREBOARD_HEALTH_LINE_2 = 2;
    
    private Player _player;
    private double _health;
    private ScoreboardData _sbData;
    
    public BBPlayer(Player player, BBGame game) {
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
        if (_health < 0) {
            _health = 0;
        }
        
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

//  ########################################
//  #              Scoreboard              #
//  ########################################
    
    public void setScoreboardData(BBGame game) {
        MessagesManager messages = MessagesManager.getInstanceByPlayer(_player.getName());
        
        _sbData = new ScoreboardData();
        
        String title = messages.getScoreboardTitle();
        List<String> sideBarContents = createSideBarContents(game, messages);
        _sbData.createSideBar(title, sideBarContents);
        
        _player.setScoreboard(_sbData.getScoreboard());
    }
    
    private List<String> createSideBarContents(BBGame game, MessagesManager messages) {
        List<String> sideBar = new ArrayList<>();
        
        int emptyLines = 2;
        for (int i = 0; i < emptyLines; i++) {
            sideBar.add("§" + i);
        }
        
        sideBar.add(SCOREBOARD_HEALTH_LINE_1, buildHealthLine(messages, game.getPlayer1()));
        sideBar.add(SCOREBOARD_HEALTH_LINE_2, buildHealthLine(messages, game.getPlayer2()));
        
        sideBar.add(messages.getServerShopLine());
        
        return sideBar;
    }
    
    private String buildHealthLine(MessagesManager messages, BBPlayer player) {
        double health = player.getHealth();
        return messages.getPlayerHealthLine(player.getBukkitPlayer().getName(), getColor(health), health);
    }
    
    private String getColor(double health) {
        double maxHealth = ConfigManager.getInstance().getStartingHealth();
        String[] colors = { "§a", "§e", "§c" };
        double[] steps = { 0.67, 0.33, 0 };
        
        for (int i = 0; i < steps.length; i++) {
            if (health >= steps[i] * maxHealth) {
                return colors[i];
            }
        }
        
        throw new IllegalArgumentException("Health is lower than 0");
    }
    
    public void updateScoreboardHealthLines(BBGame game) {
        MessagesManager messages = MessagesManager.getInstanceByPlayer(_player.getName());
        
        _sbData.updateLine(SCOREBOARD_HEALTH_LINE_1, buildHealthLine(messages, game.getPlayer1()));
        _sbData.updateLine(SCOREBOARD_HEALTH_LINE_2, buildHealthLine(messages, game.getPlayer2()));
    }

}
