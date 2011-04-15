package me.ryall.flexiwool.listeners;

// Local
import me.ryall.flexiwool.Flexiwool;

//Bukkit
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class FlexiwoolPlayerListener extends PlayerListener
{
    public static int ITEM_DYE = 351;
    
    public void onPlayerInteract(PlayerInteractEvent _event)
    {
        Player player = _event.getPlayer();
        
        if (Flexiwool.get().getPermissions().hasDyePermission(player))
        {
            Block block = _event.getClickedBlock();
            
            // If we've hit a wool block.
            if (block != null && block.getType() == Material.WOOL)
            {
                ItemStack item = _event.getPlayer().getItemInHand();
                
                // If we've hit the wool block with a dye.
                if (item.getTypeId() == ITEM_DYE)
                {
                    byte colour = (byte)(15 - item.getDurability());
                    
                    // Don't colour blocks that are already set to the colour we want to set.
                    if (block.getData() == colour)
                        return;
                    
                    // Left click to change a single block.
                    if (_event.getAction() == Action.LEFT_CLICK_BLOCK)
                    {
                        Flexiwool.get().getPainter().set(player, block, colour);
                    }
                    // Right click to fill.
                    else if (_event.getAction() == Action.RIGHT_CLICK_BLOCK && 
                            Flexiwool.get().getPermissions().hasFillPermission(player))
                    {
                        Flexiwool.get().getPainter().fill(player, block, colour);
                    }

                    // Consume the dye if we have the option enabled.
                    if (Flexiwool.get().getConfig().shouldConsumeDye())
                    {
                        int remaining = item.getAmount();
                        
                        if (remaining > 1)
                            item.setAmount(remaining - 1);
                        else
                            player.getInventory().remove(item);
                    }
                }
            }
        } 
        else
        {
            Flexiwool.get().getComms().error(player, "You don't have permission to dye blocks.");
        }
    }
    
    public void onPlayerQuit(PlayerQuitEvent _event)
    {
        Flexiwool.get().getHistory().delete(_event.getPlayer());
    }
}
