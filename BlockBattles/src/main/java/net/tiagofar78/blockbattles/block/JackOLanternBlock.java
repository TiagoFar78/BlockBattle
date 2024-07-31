package net.tiagofar78.blockbattles.block;

import org.bukkit.Location;
import org.bukkit.Material;

import net.tiagofar78.blockbattles.BBGame;
import net.tiagofar78.blockbattles.BBPlayer;
import net.tiagofar78.blockbattles.DayPeriod;
import net.tiagofar78.blockbattles.managers.ConfigManager;

public class JackOLanternBlock extends Block {

    @Override
    public Block createNewInstance() {
        return new JackOLanternBlock();
    }

    @Override
    public Material getMaterial() {
        return Material.JACK_O_LANTERN;
    }

    @Override
    public void executePlacement(BBGame game, BBPlayer placer, BBPlayer otherPlayer, Location location) {
        ConfigManager config = ConfigManager.getInstance();
        
        double damage = game.getDayPeriod() == DayPeriod.DAY ? config.getJackOLanternDamageDay() : config.getJackOLanternDamageNight();
        otherPlayer.damage(damage);
        
        game.changeTurn();
    }

}
