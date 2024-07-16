package net.tiagofar78.blockbattles.managers;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.tiagofar78.blockbattles.BBGame;

public class GamesManager {
    
    private final static int GAMES_DISTANCE = 1000;
    
    private static BBGame[] games = new BBGame[ConfigManager.getInstance().getMaxGames()];
    
    /**
     * @return  0 if started successfully<br>
     *          -1 if could not start game
     */
    public static int startGame(Player player1, Player player2) {
        for (int i = 0; i < games.length; i++) {
            if (games[i] == null) {
                ConfigManager config = ConfigManager.getInstance();
                
                Location referenceLocation = config.getReferenceLocation().add(i * GAMES_DISTANCE, 0, 0);
                
                games[i] = new BBGame(referenceLocation, player1, player2);
                return 0;
            }
        }
        
        return -1;
    }

}
