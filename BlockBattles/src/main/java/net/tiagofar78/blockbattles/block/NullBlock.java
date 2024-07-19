package net.tiagofar78.blockbattles.block;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.tiagofar78.blockbattles.BBPlayer;
import net.tiagofar78.blockbattles.Board;
import net.tiagofar78.blockbattles.managers.MessagesManager;

public class NullBlock extends Block {

    @Override
    public Material getMaterial() {
        return null;
    }
    
    @Override
    public ItemStack toItemStack(MessagesManager messages) {
        return null;
    }

    @Override
    public void executePlacement(Board board, BBPlayer player1, BBPlayer player2) {
        // Empty
    }

}
