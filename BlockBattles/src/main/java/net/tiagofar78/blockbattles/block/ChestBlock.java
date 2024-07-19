package net.tiagofar78.blockbattles.block;

import org.bukkit.Material;

import net.tiagofar78.blockbattles.BBPlayer;
import net.tiagofar78.blockbattles.Board;
import net.tiagofar78.blockbattles.managers.ConfigManager;

public class ChestBlock extends Block {

    @Override
    public Material getMaterial() {
        return Material.CHEST;
    }

    @Override
    public void executePlacement(Board board, BBPlayer player1, BBPlayer player2) {
        double damage = ConfigManager.getInstance().getChestDamage();
        player2.damage(damage);
    }

}
