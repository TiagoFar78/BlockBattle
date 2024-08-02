package net.tiagofar78.blockbattles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import net.tiagofar78.blockbattles.block.Block;
import net.tiagofar78.blockbattles.block.ChestBlock;
import net.tiagofar78.blockbattles.block.FlowerPotBlock;
import net.tiagofar78.blockbattles.block.JackOLanternBlock;
import net.tiagofar78.blockbattles.block.OakDoorBlock;
import net.tiagofar78.blockbattles.block.OakFenceGateBlock;
import net.tiagofar78.blockbattles.block.OakTrapdoorBlock;
import net.tiagofar78.blockbattles.block.PointedDripstoneBlock;
import net.tiagofar78.blockbattles.block.RedstoneLampBlock;
import net.tiagofar78.blockbattles.block.TorchBlock;
import net.tiagofar78.blockbattles.block.WaxedExposedCutCopperStairsBlock;
import net.tiagofar78.blockbattles.managers.ConfigManager;
import net.tiagofar78.blockbattles.managers.MessagesManager;

public class BBPlayer {
    
    public final static int BUKKIT_MAX_HEALTH = 20;
    public final static int BUKKIT_MAX_FOOD_LEVEL = 20;
    private final static int SCOREBOARD_HEALTH_LINE_1 = 1;
    private final static int SCOREBOARD_HEALTH_LINE_2 = 2;
    private final static int INVENTORY_STARTING_SLOT = 0;
    
    private Player _player;
    private double _health;
    private ScoreboardData _sbData;
    private List<Block> _rotation;
    private List<Block> _hand;
    private List<Block> _availableCombos;
    
    private List<Block> _deck;
    
    private double _damageTakenFromDayPeriodChange = 0;
    
    public BBPlayer(Player player, BBGame game) {
        ConfigManager config = ConfigManager.getInstance();
        
        _player = player;
        _health = config.getStartingHealth();
        
        _deck = createDummyDeck(); // TODO in the future, right now this is just for testing
        _rotation = initializeRotation();
        _hand = initializeHand();
    }
    
    public Player getBukkitPlayer() {
        return _player;
    }
    
    public double getHealth() {
        return _health;
    }
    
    public void damage(double amount) {
        _health -= amount;
        if (_health <= 0) {
            _health = 0;
            return;
        }
        
        int maxBBPlayerHealth = ConfigManager.getInstance().getStartingHealth();
        _player.setHealth(_health * (double) BUKKIT_MAX_HEALTH / (double) maxBBPlayerHealth);
    }
    
    public void kill() {
        _health = 0;
    }
    
    public void dayPeriodChanged() {
        damage(_damageTakenFromDayPeriodChange);
    }
    
    public void registerDamageFromDayPeriod(double damage) {
        _damageTakenFromDayPeriodChange += damage;
    }

//  #########################################
//  #                 Combo                 #
//  #########################################
    
    public List<Block> getAvailableCombos() {
        return _availableCombos;
    }
    
    public void clearCombo() {
        _availableCombos = null;
    }
    
    public void setAvailableCombos(List<Block> combos) {
        _availableCombos = combos;
    }
    
    @Override
    public boolean equals(Object o) {
        return ((BBPlayer) o).getBukkitPlayer().getName().equals(_player.getName());
    }

//  #########################################
//  #               Inventory               #
//  #########################################
    
    private List<Block> initializeRotation() {
        Random random = new Random();
        
        int n = _deck.size();
        List<Block> rotation = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int randomIndex = random.nextInt(_deck.size());
            rotation.add(_deck.get(randomIndex));
            _deck.remove(randomIndex);
        }
        
        return rotation;
    }
    
    private List<Block> initializeHand() {
        int handSize = ConfigManager.getInstance().getStartingHandSize();
        List<Block> hand = new ArrayList<>();
        
        for (int i = 0; i < handSize; i++) {
            hand.add(_rotation.get(0));
            _rotation.remove(0);
        }
        
        return hand;
    }
    
    public void usedItemAt(int slot) {
        int handIndex = toHandIndex(slot);
        
        _rotation.add(_hand.get(handIndex).createNewInstance());
        
        _hand.set(handIndex, _rotation.get(0));
        _rotation.remove(0);
        updateInventory();
    }
    
    public Block getItemAt(int slot) {
        int invIndex = toHandIndex(slot);
        if (invIndex == -1) {
            return null;
        }
        
        return _hand.get(invIndex);
    }
    
    private int toHandIndex(int slot) {
        int index = slot - INVENTORY_STARTING_SLOT;
        if (index < 0 || index >= _hand.size()) {
            return -1;
        }
        
        return index;
    }
    
    public void updateInventory() {
        MessagesManager messages = MessagesManager.getInstanceByPlayer(_player.getName());
        Inventory playerInv = _player.getInventory();
        for (int i = 0; i < _hand.size(); i++) {
            playerInv.setItem(INVENTORY_STARTING_SLOT + i, _hand.get(i).toItemStack(messages));
        }
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
    
    private List<Block> createDummyDeck() {
        List<Block> deck = new ArrayList<>();
        
        deck.add(new ChestBlock());
        deck.add(new OakDoorBlock());
        deck.add(new OakFenceGateBlock());
        deck.add(new OakTrapdoorBlock());
        deck.add(new TorchBlock());
        deck.add(new RedstoneLampBlock());
        deck.add(new FlowerPotBlock());
        deck.add(new PointedDripstoneBlock());
        deck.add(new WaxedExposedCutCopperStairsBlock());
        deck.add(new JackOLanternBlock());
        
        return deck;
    }

}
