package net.tiagofar78.blockbattles.phases;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.tiagofar78.blockbattles.BBGame;
import net.tiagofar78.blockbattles.BBPlayer;
import net.tiagofar78.blockbattles.BlockBattles;
import net.tiagofar78.blockbattles.Ranking;
import net.tiagofar78.blockbattles.managers.ConfigManager;
import net.tiagofar78.blockbattles.managers.MessagesManager;
import net.tiagofar78.blockbattles.utils.SchedulerUtils;

public class FinishedPhase extends Phase {
    
    private static final int DEFAULT_FADE_IN = 10;
    private static final int DEFAULT_STAY = 70;
    private static final int DEFAULT_FADE_OUT = 20;

    @Override
    public Phase next() {
        return new DisabledPhase();
    }

    @Override
    public void start(BBGame game) {
        ConfigManager config = ConfigManager.getInstance();
        
        BBPlayer winner = game.getPlayer1();
        BBPlayer loser = game.getPlayer2();
        if (winner.getHealth() == 0) {
            BBPlayer temp = winner;
            winner = loser;
            loser = temp;
        }
        
        Player bukkitPlayerWinner = winner.getBukkitPlayer();
        Player bukkitPlayerLoser = loser.getBukkitPlayer();
        
        String winnerName = bukkitPlayerWinner.getName();
        String loserName = bukkitPlayerLoser.getName();
        
        if (game.isRanked()) {
            int rankedPoints = config.getRankedStolenPoints();
            
            int lostPoints = Ranking.take(loserName, rankedPoints);
            int wonPoints = Ranking.give(winnerName, rankedPoints);
             
            SubtitleFunction winnerF = (messages, points) -> messages.getRankedWinnerResultSubtitle(points);
            sendGameResultMessages(bukkitPlayerWinner, winnerName, loserName, wonPoints, winnerF);
            SubtitleFunction loserF = (messages, points) -> messages.getRankedLoserResultSubtitle(points);
            sendGameResultMessages(bukkitPlayerLoser, winnerName, loserName, lostPoints, loserF);
        }
        else {
            double healthLeft = winner.getHealth();
            sendGameResultMessages(bukkitPlayerWinner, winnerName, loserName, healthLeft);
            sendGameResultMessages(bukkitPlayerLoser, winnerName, loserName, healthLeft);
        }
        
        int finishedSeconds = config.getFinishedSeconds();
        Bukkit.getScheduler().runTaskLater(BlockBattles.getBlockBattles(), new Runnable() {
            
            @Override
            public void run() {
                game.startNextPhase();
            }
            
        }, finishedSeconds * SchedulerUtils.TICKS_PER_SECOND);
    }
    
    private void sendGameResultMessages(Player receiver, String winnerName, String loserName, double healthLeft) {
        MessagesManager messages = MessagesManager.getInstanceByPlayer(receiver.getName());
        
        String title = messages.getResultTitle(winnerName);
        String subtitle = messages.getResultSubtitle(healthLeft);
        receiver.sendTitle(title, subtitle, DEFAULT_FADE_IN, DEFAULT_STAY, DEFAULT_FADE_OUT);
        
        receiver.sendMessage(messages.getGameResultMessage(winnerName, loserName));
    }
    
    private void sendGameResultMessages(Player receiver, String winnerName, String loserName, int points, SubtitleFunction f) {
        MessagesManager messages = MessagesManager.getInstanceByPlayer(receiver.getName());
        
        String title = messages.getResultTitle(winnerName);
        String subtitle = f.getSubtitle(messages, points);
        receiver.sendTitle(title, subtitle, DEFAULT_FADE_IN, DEFAULT_STAY, DEFAULT_FADE_OUT);
        
        receiver.sendMessage(messages.getGameResultMessage(winnerName, loserName));
    }

    @Override
    public boolean isClockWorking() {
        return false;
    }
    
    private interface SubtitleFunction {
        
        String getSubtitle(MessagesManager messages, int poits);
        
    }

}
