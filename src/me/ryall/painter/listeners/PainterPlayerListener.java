package me.ryall.painter.listeners;

// Local
import me.ryall.painter.Painter;

//Bukkit
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PainterPlayerListener extends PlayerListener
{
    public static int ITEM_DYE = 351;
    
    public void onPlayerInteract(PlayerInteractEvent _event)
    {
        Block block = _event.getClickedBlock();
            
        // If we've hit a wool block.
        if (block != null && block.getType() == Material.WOOL)
        {
            Player player = _event.getPlayer();
            ItemStack item = player.getItemInHand();
            
            // If we've hit the wool block with a dye.
            if (item.getTypeId() == ITEM_DYE)
            {
                if (Painter.get().getPermissions().hasDyePermission(player))
                {
                    byte colour = (byte)(15 - item.getDurability());
                    
                    // Don't colour blocks that are already set to the colour we want to set.
                    if (block.getData() == colour)
                        return;
                    
                    // Left click to change a single block.
                    if (_event.getAction() == Action.LEFT_CLICK_BLOCK)
                    {
                        Painter.get().getPaint().set(player, block, colour);
                    }
                    // Right click to fill.
                    else if (_event.getAction() == Action.RIGHT_CLICK_BLOCK && 
                            Painter.get().getPermissions().hasFillPermission(player))
                    {
                        Painter.get().getPaint().fill(player, block, colour);
                    }
    
                    // Consume the dye if we have the option enabled.
                    if (Painter.get().getConfig().shouldConsumeDye())
                    {
                        int remaining = item.getAmount();
                        
                        if (remaining > 1)
                            item.setAmount(remaining - 1);
                        else
                            player.getInventory().remove(item);
                    }
                }
                else
                    Painter.get().getComms().error(player, "You don't have permission to dye blocks.");
            }
        }
    }
    
    public void onPlayerQuit(PlayerQuitEvent _event)
    {
        Painter.get().getHistory().delete(_event.getPlayer());
    }
}
