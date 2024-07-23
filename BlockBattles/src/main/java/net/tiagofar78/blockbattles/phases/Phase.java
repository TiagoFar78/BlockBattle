package net.tiagofar78.blockbattles.phases;

import net.tiagofar78.blockbattles.BBGame;

public abstract class Phase {
    
    public abstract Phase next();

    public abstract void start(BBGame game);
    
    public abstract boolean isClockWorking();
    
}
