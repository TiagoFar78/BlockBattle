package net.tiagofar78.blockbattles;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.tiagofar78.blockbattles.block.Block;
import net.tiagofar78.blockbattles.block.Interactable;
import net.tiagofar78.blockbattles.managers.ConfigManager;
import net.tiagofar78.blockbattles.managers.MessagesManager;
import net.tiagofar78.blockbattles.managers.SchematicsManager;
import net.tiagofar78.blockbattles.phases.Phase;
import net.tiagofar78.blockbattles.phases.TurnsPhase;
import net.tiagofar78.blockbattles.utils.SchedulerUtils;

public class BBGame {
    
    private int _index;
    private Location _referenceLoc;
    private boolean _isPlayer1Turn;
    private int _turnId = 0;
    private int _subTurnId = 0;
    private int _turnTimer;
    
    private Board _board;
    private BBPlayer _player1;
    private BBPlayer _player2;
    
    private Phase _phase;
    
    public BBGame(int index, Location referenceLocation, Player player1, Player player2) {
        _index = index;
        _referenceLoc = referenceLocation;
        _player1 = new BBPlayer(player1, this);
        _player2 = new BBPlayer(player2, this);
        _board = new Board();
        
        generateMap();
        
        ConfigManager config = ConfigManager.getInstance();
        addPlayer(_player1, config.getPlayer1SpawnPoint().add(referenceLocation));
        addPlayer(_player2, config.getPlayer2SpawnPoint().add(referenceLocation));
        
        startNextPhase(new TurnsPhase());
    }
    
    private void generateMap() {
        SchematicsManager.pasteMapSchematic(_referenceLoc);
    }
    
    public int getIndex() {
        return _index;
    }
    
    public void setIsPlayer1Turn(boolean value) {
        _isPlayer1Turn = value;
    }
    
    public BBPlayer getPlayer1() {
        return _player1;
    }
    
    public BBPlayer getPlayer2() {
        return _player2;
    }
    
    public Board getBoard() {
        return _board;
    }
    
    private void adapLocation(Location location) {
        location.subtract(_referenceLoc);
    }

//  #########################################
//  #                 Lobby                 #
//  #########################################
    
    public void addPlayer(BBPlayer player, Location spawnPoint) {
        player.reset();
        player.getBukkitPlayer().teleport(spawnPoint);
        player.updateInventory();
    }
    
    public void removePlayer(BBPlayer player) {
        Location exitLocation = ConfigManager.getInstance().getExitLocation();
        player.getBukkitPlayer().teleport(exitLocation);
    }
    
    public BBPlayer getPlayer(Player player) {
        if (_player1.getBukkitPlayer().equals(player)) {
            return _player1;
        }
        
        if (_player2.getBukkitPlayer().equals(player)) {
            return _player2;
        }
        
        return null;
    }

//  ########################################
//  #                Phases                #
//  ########################################

    public void startNextPhase() {
        startNextPhase(_phase.next());
    }

    public void startNextPhase(Phase phase) {
        _phase = phase;
        _phase.start(this);
    }

//  ########################################
//  #                 Play                 #
//  ########################################
    
    /**
     * 
     * @return if the placement should be cancelled
     */
    public boolean playerPlacedBlock(BBPlayer player, int slot, Location location) {        
        Block block = player.getItemAt(slot);
        if (block == null) {
            return false;
        }
        
        adapLocation(location);
        if (!_board.isValidLocation(location)) {
            return true;
        }
        
        if (!isPlayerTurn(player)) {
            return true;
        }
        
        BBPlayer otherPlayer = _isPlayer1Turn ? _player2 : _player1;
        block.executePlacement(this, player, otherPlayer, location);
        
        _board.setBlock(location, block);
        player.usedItemAt(slot);
        
        visuallyApplyResult();
        return false;
    }
    
    /**
     * 
     * @return if the interaction should be cancelled
     */
    public boolean playerInteractedWithBlock(BBPlayer player, Location location) {
        adapLocation(location);
        
        Block block = _board.getBlock(location);
        if (block == null) {
            return false;
        }
        
        if (!(block instanceof Interactable)) {
            return false;
        }
        
        if (!isPlayerTurn(player)) {
            return true;
        }
        
        BBPlayer otherPlayer = _isPlayer1Turn ? _player2 : _player1;
        boolean isCancelled = ((Interactable) block).interact(this, player, otherPlayer, location);
        visuallyApplyResult();
        
        return isCancelled;        
    }
    
    private void visuallyApplyResult() {        
        _player1.updateScoreboardHealthLines(this);
        _player2.updateScoreboardHealthLines(this);
        
        if (_player1.getHealth() <= 0) {
            startNextPhase();
        }
        else if (_player2.getHealth() <= 0) {
            startNextPhase();
        }
    }

//  ########################################
//  #                 Turn                 #
//  ########################################
    
    public int getTurnTimer() {
        return _turnTimer;
    }
    
    public int getTurnId() {
        return _turnId;
    }
    
    public boolean isPlayerTurn(BBPlayer player) {
        return _isPlayer1Turn == player.equals(_player1);
    }
    
    public void changeTurn() {
        _isPlayer1Turn = !_isPlayer1Turn;
        
        BBPlayer player = _isPlayer1Turn ? _player1 : _player2;
        _turnTimer = ConfigManager.getInstance().getTurnSeconds();
        _turnId++;
        _subTurnId = 0;
        
        runTurnTimer(player.getBukkitPlayer().getName(), _turnId, _subTurnId);
    }
    
    public void setTurnTimer(int turnTimer) {
        BBPlayer player = _isPlayer1Turn ? _player1 : _player2;
        _turnTimer = turnTimer;
        _subTurnId++;
        
        runTurnTimer(player.getBukkitPlayer().getName(), _turnId, _subTurnId);
    }
    
    private void runTurnTimer(String playerName, int id, int subId) {
        Player bukkitPlayer1 = _player1.getBukkitPlayer();
        Player bukkitPlayer2 = _player2.getBukkitPlayer();
        
        MessagesManager messages1 = MessagesManager.getInstanceByPlayer(bukkitPlayer1.getName());
        MessagesManager messages2 = MessagesManager.getInstanceByPlayer(bukkitPlayer2.getName());
        
        if (_turnTimer == 0) {
            if (isPlayerTurn(_player1)) {
                bukkitPlayer1.sendMessage(messages1.getLostTurnOwnMessage());
                bukkitPlayer2.sendMessage(messages2.getLostTurnOtherMessage(playerName));
            }
            else {
                bukkitPlayer1.sendMessage(messages1.getLostTurnOtherMessage(playerName));
                bukkitPlayer2.sendMessage(messages2.getLostTurnOwnMessage());
            }
            changeTurn();
            return;
        }
        
        sendActionBarMessage(bukkitPlayer1, messages1.getTurnDurationMessage(playerName, _turnTimer));
        sendActionBarMessage(bukkitPlayer2, messages2.getTurnDurationMessage(playerName, _turnTimer));
        
        Bukkit.getScheduler().runTaskLater(BlockBattles.getBlockBattles(), new Runnable() {
            
            @Override
            public void run() {
                if (_turnId == id && _subTurnId == subId) {
                    _turnTimer--;
                    runTurnTimer(playerName, id, subId);
                }
            }
            
        }, SchedulerUtils.TICKS_PER_SECOND);
    }
    
    private void sendActionBarMessage(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

}
