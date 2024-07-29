package net.tiagofar78.blockbattles.placeholders;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.tiagofar78.blockbattles.BlockBattles;

public abstract class BBPAPIExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", BlockBattles.getBlockBattles().getDescription().getAuthors());
    }

    @Override
    public @NotNull String getIdentifier() {
        return BlockBattles.class.getSimpleName();
    }

    @Override
    public @NotNull String getVersion() {
        return BlockBattles.getBlockBattles().getDescription().getVersion();
    }
    
    public abstract String onRequest(OfflinePlayer player);

}
