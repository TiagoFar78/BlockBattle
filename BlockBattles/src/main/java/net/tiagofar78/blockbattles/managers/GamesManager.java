package net.tiagofar78.blockbattles.managers;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.tiagofar78.blockbattles.BBGame;
import net.tiagofar78.blockbattles.BBPlayer;
import net.tiagofar78.blockbattles.dataobjects.BBGamePlayer;

public class GamesManager {
    
    private final static int GAMES_DISTANCE = 1000;
    
    private static BBGame[] games = new BBGame[ConfigManager.getInstance().getMaxGames()];
    
    /**
     * @return  0 if started successfully<br>
     *          -1 if could not start game
     */
    public static int startGame(Player player1, Player player2) {
        ConfigManager config = ConfigManager.getInstance();
        
        for (int i = 0; i < config.getMaxGames(); i++) {
            if (games[i] == null) {
                Location referenceLocation = getReferenceLocation(config, i);
                
                games[i] = new BBGame(i, referenceLocation, player1, player2);
                return 0;
            }
        }
        
        return -1;
    }
    
    public static void removeGame(int index) {
        games[index] = null;
        
        List<Player> sparePlayers = QueueManager.getSparePlayers();
        if (sparePlayers != null) {
            startGame(sparePlayers.get(0), sparePlayers.get(1));
        }
    }
    
    public static BBGamePlayer findGameAndPlayer(Player player) {
        for (int i = 0; i < games.length; i++) {
            if (games[i] == null) {
                continue;
            }
            
            BBPlayer bbPlayer = games[i].getPlayer(player);
            if (bbPlayer != null) {
                return new BBGamePlayer(games[i], bbPlayer);
            }
        }
        
        return null;
    }
    
    public static void generateMaps() {
        ConfigManager config = ConfigManager.getInstance();

        for (int i = 0; i < config.getMaxGames(); i++) {
            SchematicsManager.pasteMapSchematic(getReferenceLocation(config, i));
        }
    }
    
    private static Location getReferenceLocation(ConfigManager config, int index) {
        return config.getReferenceLocation().add(index * GAMES_DISTANCE, 0, 0);
    }

}
