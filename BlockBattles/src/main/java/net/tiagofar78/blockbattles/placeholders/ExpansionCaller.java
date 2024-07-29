package net.tiagofar78.blockbattles.placeholders;

import org.bukkit.OfflinePlayer;

public class ExpansionCaller extends BBPAPIExpansion {
    
    private BBPAPIExpansion[] expansions = { new RatingRanked(), new Wins(), new Losses() };
    
    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        for (BBPAPIExpansion expansion : expansions) {
            if (identifier.equals(expansion.getClass().getSimpleName())) {
                return expansion.onRequest(player);
            }
        }
        
        return null;
    }

    @Override
    public String onRequest(OfflinePlayer player) {
        return null;
    }

}
