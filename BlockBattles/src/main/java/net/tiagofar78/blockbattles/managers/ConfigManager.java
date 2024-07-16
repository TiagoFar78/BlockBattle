package net.tiagofar78.blockbattles.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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
    
    private int _maxGames;
    private int _turnSeconds;
    
    private String _defaultLanguage;
    private List<String> _availableLanguages;
    
    private String _worldName;
    private Location _referenceLocation;
    private Location _player1SpawnPoint;
    private Location _player2SpawnPoint;
    private Location _exitLocation;
    
    private ConfigManager() {
        YamlConfiguration config = BBResources.getYamlConfiguration();
        
        _maxGames = config.getInt("MaxSimultaneousGames");
        _turnSeconds = config.getInt("TurnSeconds");
        
        _defaultLanguage = config.getString("DefaultLanguage");
        _availableLanguages = config.getStringList("AvailableLanguages");
        
        _worldName = config.getString("WorldName");
        String locationsPath = "Locations.";
        World bbWorld = Bukkit.getWorld(_worldName);
        _referenceLocation = createLocation(bbWorld, config, locationsPath + "Reference");
        _player1SpawnPoint = createLocation(bbWorld, config, locationsPath + "Player1SpawnPoint");
        _player2SpawnPoint = createLocation(bbWorld, config, locationsPath + "Player2SpawnPoint");
        World exitWorld = Bukkit.getWorld(config.getString(locationsPath + "Exit.World"));
        _exitLocation = createLocation(exitWorld, config, locationsPath + "Exit");
    }
    
    private Location createLocation(World world, YamlConfiguration config, String path) {
        double x = config.getDouble(path + ".X");
        double y = config.getDouble(path + ".Y");
        double z = config.getDouble(path + ".Z");
        
        return new Location(world, x, y, z);
    }
    
    public int getMaxGames() {
        return _maxGames;
    }
    
    public int getTurnSeconds() {
        return _turnSeconds;
    }
    
    public String getDefaultLanguage() {
        return _defaultLanguage;
    }
    
    public List<String> getAvailableLanguages() {
        return new ArrayList<>(_availableLanguages);
    }
    
    public String getWorldName() {
        return _worldName;
    }
    
    public Location getReferenceLocation() {
        return createLocationCopy(_referenceLocation);
    }
    
    public Location getPlayer1SpawnPoint() {
        return createLocationCopy(_player1SpawnPoint);
    }
    
    public Location getPlayer2SpawnPoint() {
        return createLocationCopy(_player2SpawnPoint);
    }
    
    public Location getExitLocation() {
        return createLocationCopy(_exitLocation);
    }

//  ########################################
//  #                 Copy                 #
//  ########################################
    
    private Location createLocationCopy(Location location) {
        return location.clone();
    }

//  ########################################
//  #               Validity               #
//  ########################################
    
    private void checkIfValid() {
        
    }

}
