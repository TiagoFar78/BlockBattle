package net.tiagofar78.blockbattles.block;

import org.bukkit.Location;
import org.bukkit.Material;

import net.tiagofar78.blockbattles.BBGame;
import net.tiagofar78.blockbattles.BBPlayer;
import net.tiagofar78.blockbattles.managers.ConfigManager;

public class DayLightDetectorBlock extends Block {

    @Override
    public Block createNewInstance() {
        return new DayLightDetectorBlock();
    }

    @Override
    public Material getMaterial() {
        return Material.DAYLIGHT_DETECTOR;
    }

    @Override
    public void executePlacement(BBGame game, BBPlayer placer, BBPlayer otherPlayer, Location location) {
        double damage = ConfigManager.getInstance().getDayLightDetectorDamage();
        otherPlayer.registerDamageFromDayPeriod(damage);
        
        game.changeTurn();
    }
    
}
