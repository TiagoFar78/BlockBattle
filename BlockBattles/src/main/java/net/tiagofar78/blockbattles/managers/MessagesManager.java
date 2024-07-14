package net.tiagofar78.blockbattles.managers;

import java.util.Hashtable;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import net.tiagofar78.blockbattles.BBResources;

public class MessagesManager {

    public static void load() {
        // Just needs to enter this class to initialize static values
    }

    private static Hashtable<String, MessagesManager> instance = initializeLanguageMessages();

    private static Hashtable<String, MessagesManager> initializeLanguageMessages() {
        ConfigManager config = ConfigManager.getInstance();

        Hashtable<String, MessagesManager> languagesMessages = new Hashtable<>();

        List<String> availableLanguages = config.getAvailableLanguages();
        String defaultLanguage = config.getDefaultLanguage();

        if (availableLanguages.size() == 0) {
            availableLanguages.add(defaultLanguage);
        }

        for (String language : availableLanguages) {
            languagesMessages.put(language, new MessagesManager(language));
        }

        return languagesMessages;
    }

    public static MessagesManager getInstance(String language) {
        if (language == null) {
            language = ConfigManager.getInstance().getDefaultLanguage();
        }

        return instance.get(language);
    }

    public static MessagesManager getInstanceByPlayer(String playerName) {
        String language = MessagesManager.getPlayerLanguage(playerName);
        if (language == null) {
            language = ConfigManager.getInstance().getDefaultLanguage();
        }

        return MessagesManager.getInstance(language);
    }

    private static String getPlayerLanguage(String playerName) {
        return ConfigManager.getInstance().getDefaultLanguage();
    }

//  ########################################
//  #               Warnings               #
//  ########################################
    
    private String _joinedQueueMessage;
    private String _leftQueueMessage;

//  ########################################
//  #                Errors                #
//  ########################################
    
    private String _alreadyInQueueMessage;
    private String _notInQueueMessage;
    
    private MessagesManager(String language) {
        YamlConfiguration messages = BBResources.getYamlLanguage(language);
        
        String warningPath = "Messages.Warnings.";
        _joinedQueueMessage = createMessage(messages.getString(warningPath + "JoinedQueue"));
        _leftQueueMessage = createMessage(messages.getString(warningPath + "LeftQueue"));
        
        String errorPath = "Messages.Errors.";
        _alreadyInQueueMessage = createMessage(messages.getString(errorPath + "AlreadyInQueue"));
        _notInQueueMessage = createMessage(messages.getString(errorPath + "NotInQueue"));
    }

    private String createMessage(String rawMessage) {
        return rawMessage.replace("&", "§");
    }

//  ########################################
//  #               Warnings               #
//  ########################################
    
    public String getJoinedQueueMessage() {
        return _joinedQueueMessage;
    }
    
    public String getLeftQueueMessage() {
        return _leftQueueMessage;
    }

//  ########################################
//  #                Errors                #
//  ########################################
    
    public String getAlreadyInQueueMessage() {
        return _alreadyInQueueMessage;
    }
    
    public String getNotInQueueMessage() {
        return _notInQueueMessage;
    }

}
