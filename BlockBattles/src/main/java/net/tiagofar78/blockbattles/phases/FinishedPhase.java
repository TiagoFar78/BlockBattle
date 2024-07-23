package net.tiagofar78.blockbattles.phases;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.tiagofar78.blockbattles.BBGame;
import net.tiagofar78.blockbattles.BBPlayer;
import net.tiagofar78.blockbattles.BlockBattles;
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
        
        double healthLeft = winner.getHealth();
        sendGameResultMessages(bukkitPlayerWinner, winnerName, loserName, healthLeft);
        sendGameResultMessages(bukkitPlayerLoser, winnerName, loserName, healthLeft);
        
        int finishedSeconds = ConfigManager.getInstance().getFinishedSeconds();
        Bukkit.getScheduler().runTaskLater(BlockBattles.getBlockBattles(), new Runnable() {
            
            @Override
            public void run() {
                game.startNextPhase();
            }
            
        }, finishedSeconds * SchedulerUtils.TICKS_PER_SECOND);
    }
    
    private void sendGameResultMessages(Player messageReceiver, String winnerName, String loserName, double healthLeft) {
        MessagesManager messages = MessagesManager.getInstanceByPlayer(messageReceiver.getName());
        
        String title = messages.getResultTitle(winnerName);
        String subtitle = messages.getResultSubtitle(healthLeft);
        messageReceiver.sendTitle(title, subtitle, DEFAULT_FADE_IN, DEFAULT_STAY, DEFAULT_FADE_OUT);
        
        messageReceiver.sendMessage(messages.getGameResultMessage(winnerName, loserName));
    }

    @Override
    public boolean isClockWorking() {
        return false;
    }

}
