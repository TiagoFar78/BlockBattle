package net.tiagofar78.blockbattles.block;

import org.bukkit.Location;

import net.tiagofar78.blockbattles.BBGame;
import net.tiagofar78.blockbattles.BBPlayer;

public interface Interactable {

    /**
     * 
     * @return if the interaction should be cancelled
     */
    boolean interact(BBGame game, BBPlayer interacter, BBPlayer other, Location location);
    
}
