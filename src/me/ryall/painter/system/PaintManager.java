package me.ryall.painter.system;

// Java
import java.util.ArrayList;

// Local
import me.ryall.painter.Painter;
import me.ryall.painter.system.History.Log;

// Bukkit
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class PaintManager
{
    public static BlockFace[] blockFaces = 
    {
        BlockFace.UP, BlockFace.DOWN, 
        BlockFace.EAST, BlockFace.WEST, 
        BlockFace.NORTH, BlockFace.SOUTH
    };
    
    public void set(Player _player, Block _block, byte _colour)
    {
        // If we have economy enabled, we need to charge the user first.
        if (!Painter.get().getEconomy().charge(_player, 1))
            return;
        
        // Log the change.
        History history = Painter.get().getHistory().get(_player);
        
        if (history != null)
        {
            Log log = history.createLog(_player.getWorld().getName(), Painter.get().getEconomy().getPrice(_player, 1));
            
            if (log != null)
                log.addEntry(_block, _colour);
        }
        
        // Change the colour of the wool block.
        _block.setData(_colour); 
    }
    
    public void fill(Player _player, Block _source, byte _colour)
    {
        // Find the blocks we should replace before doing anything.
        ArrayList<Block> blocks = new ArrayList<Block>();
        
        int blocksFound = 1;
        int blocksAllowed = Painter.get().getConfig().getMaxFill();
        
        if (blocksAllowed <= 0)
            return;
        
        blocks.ensureCapacity(blocksAllowed);
        blocks.add(_source);
        
        for (int i = 0; i < blocksFound; i++)
        {
            Block currentBlock = blocks.get(i);
            
            for (int j = 0; j < blockFaces.length; j++)
            {
                // Get the adjacent block.
                Block adjacentBlock = currentBlock.getFace(blockFaces[j]);
                
                // If the block matches and it hasn't already been added, add it now.
                if (adjacentBlock.getType() == Material.WOOL && 
                        adjacentBlock.getData() == currentBlock.getData() &&
                        !blocks.contains(adjacentBlock))
                {
                    if (blocksFound < blocksAllowed)
                    {
                        blocksFound++;
                        blocks.add(adjacentBlock);
                    }
                }
            }
            
         // Determine if we are full up.
         if (blocksFound >= blocksAllowed)
             break;
        }
        
        // Charge for the transaction.
        int blocksToCharge = Painter.get().getConfig().shouldFillChargePerBlock() 
                                 ? blocksFound
                                 : 1;
        
        if (!Painter.get().getEconomy().charge(_player, blocksToCharge))
            return;
        
        // Log the change.
        History history = Painter.get().getHistory().get(_player);
        Log log = null;
        
        if (history != null)
            log = history.createLog(_player.getWorld().getName(), Painter.get().getEconomy().getPrice(_player, blocksToCharge));
        
        // Finally, let's do the fill.
        for (int i = 0; i < blocksFound; i++)
        {
            Block changeBlock = blocks.get(i);
            
            if (log != null)
                log.addEntry(changeBlock, _colour);
            
            changeBlock.setData(_colour);
        }
    }
}
