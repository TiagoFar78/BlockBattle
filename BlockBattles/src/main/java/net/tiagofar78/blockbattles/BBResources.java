package net.tiagofar78.blockbattles;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

public class BBResources {

//  ########################################
//  #                Config                #
//  ########################################

    public static YamlConfiguration getYamlConfiguration() {
        return YamlConfiguration.loadConfiguration(configFile());
    }

    private static File configFile() {
        return new File(BlockBattles.getPrisonEscape().getDataFolder(), "config.yml");
    }

    public static void saveConfig(YamlConfiguration config) {
        File configFile = configFile();

        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//  ########################################
//  #               Language               #
//  ########################################

    public static YamlConfiguration getYamlLanguage(String language) {
        return YamlConfiguration.loadConfiguration(languageFile(language));
    }

    private static File languageFile(String language) {
        String parent = BlockBattles.getPrisonEscape().getDataFolder() + File.separator + "languages";
        return new File(parent, language + ".yml");
    }

//  ########################################
//  #              Schematics              #
//  ########################################

    public static File schematicFile(String name) {
        String parent = BlockBattles.getPrisonEscape().getDataFolder() + File.separator + "schematics";
        return new File(parent, name + ".schem");
    }    
    
}
