package net.tiagofar78.blockbattles.block;

import org.bukkit.Location;
import org.bukkit.Material;

import net.tiagofar78.blockbattles.BBGame;
import net.tiagofar78.blockbattles.BBPlayer;
import net.tiagofar78.blockbattles.managers.ConfigManager;

public class WaxedExposedCutCopperStairsBlock extends Block {

    @Override
    public Material getMaterial() {
        return Material.WAXED_EXPOSED_CUT_COPPER_STAIRS;
    }

    @Override
    public void executePlacement(BBGame game, BBPlayer placer, BBPlayer otherPlayer, Location location) {
        double damage = ConfigManager.getInstance().getWaxedExposedCutCopperStairsDamage();
        otherPlayer.damage(damage);
        
        game.changeTurn();
    }
    
    

}
