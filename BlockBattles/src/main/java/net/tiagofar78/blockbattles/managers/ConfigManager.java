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
    private int _finishedSeconds;
    private int _startingHealth;
    private int _startingHandSize;
    private int _rankedStolenPoints;
    
    private String _defaultLanguage;
    private List<String> _availableLanguages;
    
    private String _worldName;
    private Location _referenceLocation;
    private Location _boardSizeLocation;
    private Location _player1SpawnPoint;
    private Location _player2SpawnPoint;
    private Location _exitLocation;
    
    private double _jackOLanternDamageDay;
    private double _jackOLanternDamageNight;
    private double _chestDamage;
    private double _oakDoorDamage;
    private int _oakDoorMaxInteractions;
    private int _oakDoorSecondsToInteract;
    private double _oakFenceGateDamage;
    private int _oakFenceGateMaxInteractions;
    private int _oakFenceGateSecondsToInteract;
    private double _oakTrapdoorDamage;
    private double _torchDamage;
    private double _redstoneLampDamage;
    private double _flowerPotDamage;
    private double _pointedDripstoneDamage;
    private double _waxedExposedCutCopperStairsDamage;
    private double _dayLightDetectorDamage;
    
    private ConfigManager() {
        YamlConfiguration config = BBResources.getYamlConfiguration();
        
        _maxGames = config.getInt("MaxSimultaneousGames");
        _turnSeconds = config.getInt("TurnSeconds");
        _finishedSeconds = config.getInt("FinishedSeconds");
        _startingHealth = config.getInt("StartingHealth");
        _startingHandSize = config.getInt("StartingHandSize");
        _rankedStolenPoints = config.getInt("RankedStolenPoints");
        
        _defaultLanguage = config.getString("DefaultLanguage");
        _availableLanguages = config.getStringList("AvailableLanguages");
        
        _worldName = config.getString("WorldName");
        String locationsPath = "Locations.";
        World bbWorld = Bukkit.getWorld(_worldName);
        _referenceLocation = createLocation(bbWorld, config, locationsPath + "Reference");
        _boardSizeLocation = createLocation(bbWorld, config, locationsPath + "BoardSize");
        _player1SpawnPoint = createLocation(bbWorld, config, locationsPath + "Player1SpawnPoint");
        _player2SpawnPoint = createLocation(bbWorld, config, locationsPath + "Player2SpawnPoint");
        World exitWorld = Bukkit.getWorld(config.getString(locationsPath + "Exit.World"));
        _exitLocation = createLocation(exitWorld, config, locationsPath + "Exit");
        
        String itemsPath = "Items.";
        _jackOLanternDamageDay = config.getDouble(itemsPath + "JackOLantern.DamageDay");
        _jackOLanternDamageNight = config.getDouble(itemsPath + "JackOLantern.DamageNight");
        _chestDamage = config.getDouble(itemsPath + "Chest.Damage");
        _oakDoorDamage = config.getDouble(itemsPath + "OakDoor.Damage");
        _oakDoorMaxInteractions = config.getInt(itemsPath + "OakDoor.MaxInteractions");
        _oakDoorSecondsToInteract = config.getInt(itemsPath + "OakDoor.SecondsToInteract");
        _oakFenceGateDamage = config.getDouble(itemsPath + "OakFenceGate.Damage");
        _oakFenceGateMaxInteractions = config.getInt(itemsPath + "OakFenceGate.MaxInteractions");
        _oakFenceGateSecondsToInteract = config.getInt(itemsPath + "OakFenceGate.SecondsToInteract");
        _oakTrapdoorDamage = config.getDouble(itemsPath + "OakTrapdoor.Damage");
        _torchDamage = config.getDouble(itemsPath + "Torch.Damage");
        _redstoneLampDamage = config.getDouble(itemsPath + "RedstoneLamp.Damage");
        _flowerPotDamage = config.getDouble(itemsPath + "FlowerPot.Damage");
        _pointedDripstoneDamage = config.getDouble(itemsPath + "PointedDripstone.Damage");
        _waxedExposedCutCopperStairsDamage = config.getDouble(itemsPath + "WaxedExposedCutCopperStairs.Damage");
        _dayLightDetectorDamage = config.getDouble(itemsPath + "DayLightDetector.Damage");
    }
    
    private Location createLocation(World world, YamlConfiguration config, String path) {
        double x = config.getDouble(path + ".X");
        double y = config.getDouble(path + ".Y");
        double z = config.getDouble(path + ".Z");
        float yaw = (float) config.getDouble(path + ".Yaw");
        float pitch = (float) config.getDouble(path + ".Pitch");
        
        return new Location(world, x, y, z, yaw, pitch);
    }
    
    public int getMaxGames() {
        return _maxGames;
    }
    
    public int getTurnSeconds() {
        return _turnSeconds;
    }
    
    public int getFinishedSeconds() {
        return _finishedSeconds;
    }
    
    public int getStartingHealth() {
        return _startingHealth;
    }
    
    public int getStartingHandSize() {
        return _startingHandSize;
    }
    
    public int getRankedStolenPoints() {
        return _rankedStolenPoints;
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
    
    public Location getBoardSizeLocation() {
        return createLocationCopy(_boardSizeLocation);
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
    
    public double getJackOLanternDamageDay() {
        return _jackOLanternDamageDay;
    }
    
    public double getJackOLanternDamageNight() {
        return _jackOLanternDamageNight;
    }
    
    public double getChestDamage() {
        return _chestDamage;
    }
    
    public double getOakDoorDamage() {
        return _oakDoorDamage;
    }
    
    public int getOakDoorMaxInteractions() {
        return _oakDoorMaxInteractions;
    }
    
    public int getOakDoorSecondsToInteract() {
        return _oakDoorSecondsToInteract;
    }
    
    public double getOakFenceGateDamage() {
        return _oakFenceGateDamage;
    }
    
    public int getOakFenceGateMaxInteractions() {
        return _oakFenceGateMaxInteractions;
    }
    
    public int getOakFenceGateSecondsToInteract() {
        return _oakFenceGateSecondsToInteract;
    }
    
    public double getOakTrapdoorDamage() {
        return _oakTrapdoorDamage;
    }
    
    public double getTorchDamage() {
        return _torchDamage;
    }
    
    public double getRedstoneLampDamage() {
        return _redstoneLampDamage;
    }
    
    public double getFlowerPotDamage() {
        return _flowerPotDamage;
    }
    
    public double getPointedDripstoneDamage() {
        return _pointedDripstoneDamage;
    }
    
    public double getWaxedExposedCutCopperStairsDamage() {
        return _waxedExposedCutCopperStairsDamage;
    }
    
    public double getDayLightDetectorDamage() {
        return _dayLightDetectorDamage;
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
