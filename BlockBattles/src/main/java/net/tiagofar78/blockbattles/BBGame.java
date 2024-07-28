package net.tiagofar78.blockbattles;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

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

    private final static int MIDDAY_TICKS = 6000;
    
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
        
        ConfigManager config = ConfigManager.getInstance();
        
        SchematicsManager.clear(_referenceLoc, config.getBoardSizeLocation().add(_referenceLoc).add(-1, -1, -1));
        
        addPlayer(_player1, config.getPlayer1SpawnPoint().add(referenceLocation));
        addPlayer(_player2, config.getPlayer2SpawnPoint().add(referenceLocation));
        
        startNextPhase(new TurnsPhase());
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
        Player bukkitPlayer = player.getBukkitPlayer();
        PlayerInventory inv = bukkitPlayer.getInventory();
        inv.clear();
        inv.setArmorContents(new ItemStack[4]);
        inv.setItemInOffHand(null);
        
        bukkitPlayer.setHealth(BBPlayer.BUKKIT_MAX_HEALTH);
        bukkitPlayer.setFoodLevel(BBPlayer.BUKKIT_MAX_FOOD_LEVEL);
        bukkitPlayer.setGameMode(GameMode.SURVIVAL);
        bukkitPlayer.setPlayerTime(MIDDAY_TICKS, false);
        bukkitPlayer.setPlayerWeather(WeatherType.CLEAR);
        
        bukkitPlayer.teleport(spawnPoint);
        player.updateInventory();
    }
    
    public void removePlayer(BBPlayer player) {
        Location exitLocation = ConfigManager.getInstance().getExitLocation();
        
        Player bukkitPlayer = player.getBukkitPlayer();
        bukkitPlayer.teleport(exitLocation);
        bukkitPlayer.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        bukkitPlayer.setHealth(BBPlayer.BUKKIT_MAX_HEALTH);
        bukkitPlayer.getInventory().clear();
        bukkitPlayer.resetPlayerTime();
        bukkitPlayer.resetPlayerWeather();
    }
    
    public void playerLeft(BBPlayer player) {
        removePlayer(player);
        player.kill();
        
        if (_phase.isClockWorking()) {
            visuallyApplyResult();
        }
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
        
        if (!_phase.isClockWorking()) {
            return true;
        }
        
        adapLocation(location);
        if (!_board.isValidLocation(location)) {
            return true;
        }
        
        if (!isPlayerTurn(player)) {
            return true;
        }
        
        List<Block> availableCombos = player.getAvailableCombos();
        if (availableCombos != null && !availableCombos.contains(block)) {
            return true;
        }
        
        BBPlayer otherPlayer = _isPlayer1Turn ? _player2 : _player1;
        block.executePlacement(this, player, otherPlayer, location);
        player.setAvailableCombos(block.getAvailableCombos());
        
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
        
        if (!_phase.isClockWorking()) {
            return true;
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
        player.clearCombo();
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
        if (!_phase.isClockWorking()) {
            return;
        }
        
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
