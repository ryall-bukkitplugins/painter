package me.ryall.flexiwool.listeners;

// Local
import me.ryall.flexiwool.Flexiwool;
import me.ryall.flexiwool.economy.EconomyInterface;

//Bukkit
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
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
            
            if (block != null && block.getType() == Material.WOOL)
            {
                ItemStack item = _event.getPlayer().getItemInHand();
                
                if (item.getTypeId() == ITEM_DYE)
                {
                    byte color = (byte)(15 - item.getDurability());
                    
                    if (block.getData() == color)
                        return;
                    
                    if (Flexiwool.get().getConfig().isEconomyEnabled())
                    {
                        EconomyInterface economy = Flexiwool.get().getEconomy().getInterface();
                        double price = Flexiwool.get().getConfig().getEconomyDyeCost();
                        
                        if (price > 0)
                        {
                            if (!economy.canAfford(player.getName(), price))
                            {
                                Flexiwool.get().getComms().error(player, "You need " + economy.formatCurrency(price) + " to dye this block.");
                                return;
                            }
                            
                            if (!economy.subtract(player.getName(), price))
                            {
                                Flexiwool.get().getComms().error(player, "Failed to charge your account.");
                                return;
                            }
                            
                            Flexiwool.get().getComms().message(player, "Charged " + economy.formatCurrency(price) + " to dye this block.");
                        }
                    }
                    
                    block.setData(color);
                    
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
}