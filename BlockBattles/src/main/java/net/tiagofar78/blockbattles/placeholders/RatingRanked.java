package net.tiagofar78.blockbattles.placeholders;

import org.bukkit.OfflinePlayer;

import net.tiagofar78.blockbattles.playerdata.PlayerRanking;

public class RatingRanked extends BBPAPIExpansion {
    
    @Override
    public String onRequest(OfflinePlayer player) {
        int points = PlayerRanking.get(player.getName());
        return Integer.toString(points);
    }

}
