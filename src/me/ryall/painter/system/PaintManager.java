package me.ryall.painter.system;

import java.util.ArrayList;

import me.ryall.painter.Painter;
import me.ryall.painter.system.History.Log;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class PaintManager
{
    public static BlockFace[] blockFaces = { BlockFace.UP, BlockFace.DOWN, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH };

    public boolean isPaintable(Block _block)
    {
        if (_block.getType() == Material.WOOL)
            return true;

        return Painter.get().getConfig().isTransmutable(_block);
    }

    public void set(Player _player, Block _block, byte _colour)
    {
        double price = Painter.get().getEconomy().getPrice(_player, _block, 1);

        // If we have economy enabled, we need to charge the user first.
        if (!Painter.get().getEconomy().charge(_player, price))
            return;

        // Log the change.
        History history = Painter.get().getHistory().get(_player);

        if (history != null)
        {
            Log log = history.createLog(_player.getWorld().getName(), price);
            paint(log, _block, _colour);
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

                // If the block matches and it hasn't already been added, add it
                // now.
                if (adjacentBlock.getType() == currentBlock.getType() && adjacentBlock.getData() == currentBlock.getData() && !blocks.contains(adjacentBlock))
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
        int blocksToCharge = Painter.get().getConfig().shouldFillChargePerBlock() ? blocksFound : 1;
        double price = Painter.get().getEconomy().getPrice(_player, _source, blocksToCharge);

        if (!Painter.get().getEconomy().charge(_player, price))
            return;

        // Log the change.
        History history = Painter.get().getHistory().get(_player);
        Log log = null;

        if (history != null)
            log = history.createLog(_player.getWorld().getName(), price);

        // Finally, let's do the fill.
        for (int i = 0; i < blocksFound; i++)
        {
            paint(log, blocks.get(i), _colour);
        }
    }

    private void paint(Log _log, Block _block, byte _colour)
    {
        if (_log != null)
            _log.addEntry(_block, _colour);

        _block.setType(Material.WOOL);
        _block.setData(_colour);
    }
}
