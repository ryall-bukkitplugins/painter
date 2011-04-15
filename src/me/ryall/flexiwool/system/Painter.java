package me.ryall.flexiwool.system;

// Java
import java.util.ArrayList;

// Local
import me.ryall.flexiwool.Flexiwool;

// Bukkit
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class Painter
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
        if (!Flexiwool.get().getEconomy().charge(_player, 1))
            return;
        
        // Change the colour of the wool block.
        _block.setData(_colour); 
    }
    
    public void fill(Player _player, Block _source, byte _colour)
    {
        // Find the blocks we should replace before doing anything.
        ArrayList<Block> blocks = new ArrayList<Block>();
        
        int blocksFound = 1;
        int blocksAllowed = Flexiwool.get().getConfig().getMaxFill();
        
        if (blocksAllowed <= 0)
            return;
        
        blocks.ensureCapacity(blocksAllowed);
        blocks.add(_source);
        
        for (int i = 0; i < blocksFound; i++)
        {
            Block currentBlock = blocks.get(i);
            
            for (int j = 1; j < blockFaces.length; j++)
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
        int blocksToCharge = Flexiwool.get().getConfig().shouldFillChargePerBlock() 
                                 ? blocksFound
                                 : 1;
        
        if (!Flexiwool.get().getEconomy().charge(_player, blocksToCharge))
            return;
        
        // Finally, let's do the fill.
        for (int i = 0; i < blocksFound; i++)
        {
            blocks.get(i).setData(_colour);
        }
    }
}
