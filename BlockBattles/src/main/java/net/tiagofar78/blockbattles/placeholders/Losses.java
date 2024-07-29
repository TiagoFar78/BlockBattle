package net.tiagofar78.blockbattles.placeholders;

import org.bukkit.OfflinePlayer;

import net.tiagofar78.blockbattles.playerdata.PlayerLosses;

public class Losses extends BBPAPIExpansion {

    @Override
    public String onRequest(OfflinePlayer player) {
        int losses = PlayerLosses.get(player.getName());
        return Integer.toString(losses);
    }

}
