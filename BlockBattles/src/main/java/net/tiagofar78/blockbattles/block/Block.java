package net.tiagofar78.blockbattles.block;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.tiagofar78.blockbattles.BBPlayer;
import net.tiagofar78.blockbattles.Board;
import net.tiagofar78.blockbattles.managers.MessagesManager;

public abstract class Block {

    public abstract Material getMaterial();

    public String getConfigName() {
        String subClassName = this.getClass().getSimpleName();
        int itemWordLength = "Item".length();

        return subClassName.substring(0, subClassName.length() - itemWordLength);
    }

    public String getDisplayName(MessagesManager messages) {
        return messages.getItemName(getConfigName());
    }

    public List<String> getLore(MessagesManager messages) {
        return messages.getItemLore(getConfigName());
    }

    public ItemStack toItemStack(MessagesManager messages) {
        ItemStack item = new ItemStack(getMaterial());
        ItemMeta meta = item.getItemMeta();
        setName(messages, meta);
        setLore(messages, meta);
        item.setItemMeta(meta);
        
        return item;
    }
    
    private void setName(MessagesManager messages, ItemMeta meta) {
        String name = getDisplayName(messages);
        if (name != null) {
            meta.setDisplayName(name);
        }
    }
    
    private void setLore(MessagesManager messages, ItemMeta meta) {
        List<String> lore = getLore(messages);
        if (lore != null) {
            meta.setLore(lore);
        }
    }
    
    public abstract void executePlacement(Board board, BBPlayer player1, BBPlayer player2);

}
