package net.tiagofar78.blockbattles.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import net.tiagofar78.blockbattles.dataobjects.PlayersAndType;

public class QueueManager {

    private static List<Player> normalQueue = new ArrayList<>();
    private static List<Player> rankedQueue = new ArrayList<>();
    
    /**
     * 
     * @return  0 if joined successfully<br>
     *          -1 if is already in queue 
     */
    public static int joinNormalQueue(Player player) {
        return joinQueue(normalQueue, rankedQueue, player, false);
    }
    
    /**
     * 
     * @return  0 if joined successfully<br>
     *          -1 if is already in queue<br>
     */
    public static int joinRankedQueue(Player player) {
        return joinQueue(rankedQueue, normalQueue, player, true);
    }
    
    private static int joinQueue(List<Player> queue, List<Player> otherQueue, Player player, boolean isRanked) {
        if (queue.contains(player) || otherQueue.contains(player)) {
            return -1;
        }
        
        queue.add(player);
        sendSignalToGamesManager(queue, isRanked);
        return 0;
    }

    /**
     * 
     * @return  0 if left successfully<br>
     *          -1 if is not in queue 
     */
    public static int leaveQueue(Player player) {
        if (normalQueue.contains(player)) {
            normalQueue.remove(player);
            return 0;
        }
        
        if (rankedQueue.contains(player)) {
            rankedQueue.remove(player);
            return 0;
        }
        
        return -1;
    }
    
    public static PlayersAndType getNextGamePlayerAndType() {
        PlayersAndType normalQueueNextGame = getNextGamePlayerAndType(normalQueue, false);
        if (normalQueueNextGame != null) {
            return normalQueueNextGame;
        }
        
        return getNextGamePlayerAndType(rankedQueue, true);
    }
    
    private static PlayersAndType getNextGamePlayerAndType(List<Player> queue, boolean isRanked) {
        if (queue.size() < 2) {
            return null;
        }
        
        PlayersAndType playersAndType = new PlayersAndType(queue.get(0), queue.get(1), isRanked);
        
        queue.remove(0);
        queue.remove(0);
        
        return playersAndType;
    }
    
    private static void sendSignalToGamesManager(List<Player> queue, boolean isRanked) {
        if (queue.size() != 2) {
            return;
        }
        
        int returnCode = GamesManager.startGame(queue.get(0), queue.get(1), isRanked);
        if (returnCode == 0) {
            queue.remove(0);
            queue.remove(0);
        }
        
    }
    
}
