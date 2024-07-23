package net.tiagofar78.blockbattles.phases;

import java.util.Random;

import net.tiagofar78.blockbattles.BBGame;

public class TurnsPhase extends Phase {
    
    @Override
    public Phase next() {
        return new FinishedPhase();
    }

    @Override
    public void start(BBGame game) {
        game.setIsPlayer1Turn(new Random().nextBoolean());
        game.changeTurn();
        
        game.getPlayer1().setScoreboardData(game);
        game.getPlayer2().setScoreboardData(game);
    }

    @Override
    public boolean isClockWorking() {
        return true;
    }

}
