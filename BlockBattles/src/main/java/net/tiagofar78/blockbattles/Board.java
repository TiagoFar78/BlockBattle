package net.tiagofar78.blockbattles;

import org.bukkit.Location;

import net.tiagofar78.blockbattles.block.Block;
import net.tiagofar78.blockbattles.managers.ConfigManager;

public class Board {
    
    private Block[][][] _board;
    
    public Board() {
        Location boardSize = ConfigManager.getInstance().getBoardSizeLocation();
        _board = new Block[boardSize.getBlockX()][boardSize.getBlockZ()][boardSize.getBlockY()];
    }
    
    public Block getBlock(Location location) {
        if (!isValidLocation(location)) {
            return null;
        }
        
        return _board[location.getBlockX()][location.getBlockZ()][location.getBlockZ()];
    }
    
    public void setBlock(Location location, Block block) {
        _board[location.getBlockX()][location.getBlockZ()][location.getBlockZ()] = block;
    }
    
    public boolean isValidLocation(Location location) {
        ConfigManager config = ConfigManager.getInstance();
        Location upperLocation = config.getBoardSizeLocation().add(-1, -1, -1);
        
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        
        return 0 <= x && x <= upperLocation.getBlockX() &&
                0 <= y && y <= upperLocation.getBlockY() &&
                0 <= z && z <= upperLocation.getBlockZ();
    }

}
