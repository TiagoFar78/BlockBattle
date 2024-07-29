package net.tiagofar78.blockbattles.playerdata;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import net.tiagofar78.blockbattles.BlockBattles;

public class PlayerRepository {
    
    private static File repositoryFile = new File(BlockBattles.getBlockBattles().getDataFolder(), "playerdata.yml");
    private static YamlConfiguration repositoryYaml = YamlConfiguration.loadConfiguration(repositoryFile);
    
    public static YamlConfiguration getYaml() {
        return repositoryYaml;
    }
    
    public static void save() {
        try {
            repositoryYaml.save(repositoryFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
