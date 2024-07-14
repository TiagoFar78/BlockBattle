package net.tiagofar78.blockbattles.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import net.tiagofar78.blockbattles.BBResources;

public class ConfigManager {
    
    private static ConfigManager instance = new ConfigManager();

    public static void load() {
        instance.checkIfValid();
    }
    
    public static ConfigManager getInstance() {
        return instance;
    }
    
    private String _defaultLanguage;
    private List<String> _availableLanguages;
    
    private ConfigManager() {
        YamlConfiguration config = BBResources.getYamlConfiguration();
        
        _defaultLanguage = config.getString("DefaultLanguage");
        _availableLanguages = config.getStringList("AvailableLanguages");
    }
    
    public String getDefaultLanguage() {
        return _defaultLanguage;
    }
    
    public List<String> getAvailableLanguages() {
        return new ArrayList<>(_availableLanguages);
    }

//  ########################################
//  #               Validity               #
//  ########################################
    
    private void checkIfValid() {
        
    }

}
