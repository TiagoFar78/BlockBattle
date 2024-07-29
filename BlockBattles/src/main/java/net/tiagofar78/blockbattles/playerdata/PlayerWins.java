package net.tiagofar78.blockbattles.playerdata;

public class PlayerWins {

    public static int get(String playerName) {
        return PlayerRepository.getYaml().getInt(path(playerName));
    }
    
    public static void add(String playerName) {
        PlayerRepository.getYaml().set(path(playerName), get(playerName) + 1);
        PlayerRepository.save();
    }
    
    private static String path(String playerName) {
        return playerName + ".Wins";
    }

}
