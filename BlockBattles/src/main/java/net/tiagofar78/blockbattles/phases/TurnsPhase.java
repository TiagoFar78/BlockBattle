package net.tiagofar78.blockbattles.phases;

import java.util.Random;

import net.tiagofar78.blockbattles.BBGame;

public class TurnsPhase extends Phase {
    
    @Override
    public Phase next() {
        return new DisabledPhase();
    }

    @Override
    public void start(BBGame game) {
        game.setIsPlayer1Turn(new Random().nextBoolean());
        game.changeTurn();
    }

}
