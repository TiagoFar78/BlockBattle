package net.tiagofar78.blockbattles.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class QueueManager {

    private static List<Player> queue = new ArrayList<>();
    
    /**
     * 
     * @return  0 if joined successfully<br>
     *          -1 if is already in queue 
     */
    public static int joinQueue(Player player) {
        if (queue.contains(player)) {
            return -1;
        }
        
        queue.add(player);
        sendSignalToGamesManager();
        return 0;
    }

    /**
     * 
     * @return  0 if left successfully<br>
     *          -1 if is not in queue 
     */
    public static int leaveQueue(Player player) {
        if (!queue.contains(player)) {
            return -1;
        }
        
        queue.remove(player);
        return 0;
    }
    
    private static void sendSignalToGamesManager() {
        if (queue.size() != 2) {
            return;
        }
        
        int returnCode = GamesManager.startGame(queue.get(0), queue.get(1));
        if (returnCode == 0) {
            queue.remove(0);
            queue.remove(0);
        }
        
    }
    
}
