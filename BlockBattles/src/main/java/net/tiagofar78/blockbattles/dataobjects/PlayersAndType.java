package net.tiagofar78.blockbattles.dataobjects;

import org.bukkit.entity.Player;

public class PlayersAndType {

    private Player _player1;
    private Player _player2;
    private boolean _isRankedGame;
    
    public PlayersAndType(Player player1, Player player2, boolean isRankedGame) {
        _player1 = player1;
        _player2 = player2;
        _isRankedGame = isRankedGame;
    }
    
    public Player getPlayer1() {
        return _player1;
    }
    
    public Player getPlayer2() {
        return _player2;
    }
    
    public boolean isRankedGame() {
        return _isRankedGame;
    }
    
}
