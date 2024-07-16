package net.tiagofar78.blockbattles;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.tiagofar78.blockbattles.managers.ConfigManager;
import net.tiagofar78.blockbattles.managers.MessagesManager;
import net.tiagofar78.blockbattles.managers.SchematicsManager;
import net.tiagofar78.blockbattles.utils.SchedulerUtils;

public class BBGame {
    
    private Location _referenceLoc;
    private Player _player1;
    private Player _player2;
    private boolean _isPlayer1Turn;
    private int _turnId = 0;
    
    public BBGame(Location referenceLocation, Player player1, Player player2) {
        _referenceLoc = referenceLocation;
        _player1 = player1;
        _player2 = player2;
        
        generateMap();
        
        ConfigManager config = ConfigManager.getInstance();
        sendPlayerToGame(_player1, config.getPlayer1SpawnPoint().add(referenceLocation));
        sendPlayerToGame(_player2, config.getPlayer2SpawnPoint().add(referenceLocation));
        
        _isPlayer1Turn = new Random().nextBoolean();
        changeTurn();
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

//  ########################################
//  #                 Turn                 #
//  ########################################
    
    public boolean isPlayerTurn(Player player) {
        return _isPlayer1Turn == player.equals(_player1);
    }
    
    public void changeTurn() {
        _isPlayer1Turn = !_isPlayer1Turn;
        
        Player player = _isPlayer1Turn ? _player1 : _player2;
        runTurnTimer(player.getName());
    }
    
    private void runTurnTimer(String playerName) {
        int turnSeconds = ConfigManager.getInstance().getTurnSeconds();
        _turnId++;
        
        runTurnTimer(playerName, turnSeconds, _turnId);
    }
    
    private void runTurnTimer(String playerName, int secondsLeft, int id) {
        if (_turnId != id) {
            return;
        }
        
        MessagesManager messages1 = MessagesManager.getInstanceByPlayer(_player1.getName());
        MessagesManager messages2 = MessagesManager.getInstanceByPlayer(_player2.getName());
        
        if (secondsLeft == 0) {
            if (isPlayerTurn(_player1)) {
                _player1.sendMessage(messages1.getLostTurnOwnMessage());
                _player2.sendMessage(messages2.getLostTurnOtherMessage(playerName));
            }
            else {
                _player1.sendMessage(messages1.getLostTurnOtherMessage(playerName));
                _player2.sendMessage(messages2.getLostTurnOwnMessage());
            }
            changeTurn();
            return;
        }
        
        sendActionBarMessage(_player1, messages1.getTurnDurationMessage(playerName, secondsLeft));
        sendActionBarMessage(_player2, messages2.getTurnDurationMessage(playerName, secondsLeft));
        
        Bukkit.getScheduler().runTaskLater(BlockBattles.getBlockBattles(), new Runnable() {
            
            @Override
            public void run() {
                runTurnTimer(playerName, secondsLeft - 1, id);
            }
            
        }, SchedulerUtils.TICKS_PER_SECOND);
    }
    
    private void sendActionBarMessage(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

}
