package net.tiagofar78.blockbattles.dataobjects;

import net.tiagofar78.blockbattles.BBGame;
import net.tiagofar78.blockbattles.BBPlayer;

public class BBGamePlayer {
    
    private BBGame _game;
    private BBPlayer _player;
    
    public BBGamePlayer(BBGame game, BBPlayer player) {
        _game = game;
        _player = player;
    }
    
    public BBGame getGame() {
        return _game;
    }
    
    public BBPlayer getPlayer() {
        return _player;
    }

}
