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

        // If we've hit a paintable block.
        if (block != null && Painter.get().getPaintManager().isPaintable(block))
        {
            Player player = _event.getPlayer();
            ItemStack item = player.getItemInHand();

            // If we've hit the paintable block with a dye.
            if (item.getTypeId() == ITEM_DYE)
            {
                if (!Painter.get().getPermissionManager().hasDyePermission(player))
                {
                    Painter.get().getCommunicationManager().error(player, "You don't have permission to dye blocks.");
                    return;
                }

                // If transmutation is off, only allow dyeing of wool blocks.
                if (block.getType() != Material.WOOL && !Painter.get().getPermissionManager().hasTransmutePermission(player))
                {
                    Painter.get().getCommunicationManager().error(player, "You don't have permission to transmute blocks.");
                    return;
                }

                byte colour = (byte) (15 - item.getDurability());

                // Don't colour blocks that are already set to the colour we
                // want to set.
                if (block.getType() == Material.WOOL && block.getData() == colour)
                    return;

                // Left click to change a single block.
                if (_event.getAction() == Action.LEFT_CLICK_BLOCK)
                {
                    Painter.get().getPaintManager().set(player, block, colour);
                }
                // Right click to fill.
                else if (_event.getAction() == Action.RIGHT_CLICK_BLOCK && Painter.get().getPermissionManager().hasFillPermission(player))
                {
                    if (block.getType() != Material.WOOL && !Painter.get().getConfigManager().isTransmuteFillEnabled())
                    {
                        Painter.get().getCommunicationManager().error(player, "You cannot transmute and fill at the same time.");
                        return;
                    }

                    Painter.get().getPaintManager().fill(player, block, colour);
                }

                // Consume the dye if we have the option enabled.
                if (Painter.get().getConfigManager().shouldConsumeDye())
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

    public void onPlayerQuit(PlayerQuitEvent _event)
    {
        Painter.get().getHistoryManager().delete(_event.getPlayer());
    }
}
