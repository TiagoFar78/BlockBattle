package net.tiagofar78.blockbattles.placeholders;

import org.bukkit.OfflinePlayer;

import net.tiagofar78.blockbattles.playerdata.PlayerWins;

public class Wins extends BBPAPIExpansion{

    @Override
    public String onRequest(OfflinePlayer player) {
        int wins = PlayerWins.get(player.getName());
        return Integer.toString(wins);
    }

}
