package net.tiagofar78.blockbattles.phases;

import net.tiagofar78.blockbattles.BBGame;
import net.tiagofar78.blockbattles.managers.GamesManager;

public class DisabledPhase extends Phase {

    @Override
    public Phase next() {
        return null;
    }

    @Override
    public void start(BBGame game) {
        game.removePlayer(game.getPlayer1());
        game.removePlayer(game.getPlayer2());
        
        GamesManager.removeGame(game.getIndex());
    }

    @Override
    public boolean isClockWorking() {
        return false;
    }

}
