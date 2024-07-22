package net.tiagofar78.blockbattles.block;

import org.bukkit.Location;
import org.bukkit.Material;

import net.tiagofar78.blockbattles.BBGame;
import net.tiagofar78.blockbattles.BBPlayer;
import net.tiagofar78.blockbattles.managers.ConfigManager;

public class OakTrapdoorBlock extends Block {
    
    public Block createNewInstance() {
        return new OakTrapdoorBlock();
    }

    @Override
    public Material getMaterial() {
        return Material.OAK_TRAPDOOR;
    }

    @Override
    public void executePlacement(BBGame game, BBPlayer placer, BBPlayer otherPlayer, Location location) {
        ConfigManager config = ConfigManager.getInstance();
        otherPlayer.damage(config.getOakTrapdoorDamage());
        
        game.changeTurn();
    }

}
