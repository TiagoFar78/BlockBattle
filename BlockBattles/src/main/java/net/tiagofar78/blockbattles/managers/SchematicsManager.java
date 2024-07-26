package net.tiagofar78.blockbattles.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockState;

import net.tiagofar78.blockbattles.BBResources;

public class SchematicsManager {
    
    private static SchematicsManager instance;
    
    public static void load() {
        instance = new SchematicsManager();
    }
    
    private Clipboard map;
    
    private SchematicsManager() {
        map = createClipboard("Map");
    }
    
    private Clipboard createClipboard(String name) {
        File file = BBResources.schematicFile(name);
        ClipboardFormat format = ClipboardFormats.findByFile(file);

        try {
            ClipboardReader reader = format.getReader(new FileInputStream(file));
            return reader.read();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void pasteMapSchematic(Location loc) {
        pasteSchematic(instance.map, loc);
    }
    
    private static void pasteSchematic(Clipboard clipboard, Location loc) {
        try {
            ConfigManager config = ConfigManager.getInstance();
            
            World world = BukkitAdapter.adapt(Bukkit.getWorld(config.getWorldName()));
            EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(world).build();
            
            BlockVector3 block = BlockVector3.at(loc.getX(), loc.getY(), loc.getZ());
            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession)
                    .to(block).ignoreAirBlocks(false).build();
            
            Operations.complete(operation);
            editSession.commit();
            editSession.close();
        } catch (WorldEditException e) {
            e.printStackTrace();
        }
    }
    
    public static void clear(Location loc1, Location loc2) {
        String worldName = ConfigManager.getInstance().getWorldName();
        World world = BukkitAdapter.adapt(Bukkit.getWorld(worldName));
        BlockVector3 min = BlockVector3.at(loc1.getX(), loc1.getY(), loc1.getZ());
        BlockVector3 max = BlockVector3.at(loc2.getX(), loc2.getY(), loc2.getZ());
        CuboidRegion selection = new CuboidRegion(world, min, max);

        BlockState air = BukkitAdapter.adapt(Material.AIR.createBlockData());
        EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(world).build();
        try {
            editSession.setBlocks(selection, air);
            editSession.commit();
            editSession.close();
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }
    }

}
