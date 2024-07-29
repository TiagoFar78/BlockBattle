package net.tiagofar78.blockbattles;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

public class Ranking {
    
    private static File rankingsFile = new File(BlockBattles.getBlockBattles().getDataFolder(), "rankings.yml");
    private static YamlConfiguration rankingsYaml = YamlConfiguration.loadConfiguration(rankingsFile);

    public static int get(String playerName) {
        return rankingsYaml.getInt(playerName);
    }
    
    public static int give(String playerName, int amount) {
        rankingsYaml.set(playerName, get(playerName) + amount);
        save();
        
        return amount;
    }
    
    public static int take(String playerName, int amount) {
        int current = get(playerName);
        if (current - amount < 0) {
            amount = current;
        }
        
        rankingsYaml.set(playerName, current - amount);
        save();
        
        return amount;
    }
    
    private static void save() {
        try {
            rankingsYaml.save(rankingsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
