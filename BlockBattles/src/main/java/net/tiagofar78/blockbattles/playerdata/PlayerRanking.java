package net.tiagofar78.blockbattles.playerdata;

public class PlayerRanking {

    public static int get(String playerName) {
        return PlayerRepository.getYaml().getInt(path(playerName));
    }
    
    public static int give(String playerName, int amount) {
        PlayerRepository.getYaml().set(path(playerName), get(playerName) + amount);
        PlayerRepository.save();
        
        return amount;
    }
    
    public static int take(String playerName, int amount) {
        int current = get(playerName);
        if (current - amount < 0) {
            amount = current;
        }
        
        PlayerRepository.getYaml().set(path(playerName), current - amount);
        PlayerRepository.save();
        
        return amount;
    }
    
    private static String path(String playerName) {
        return playerName + ".Rating";
    }
    
}
