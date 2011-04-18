package me.ryall.painter.economy;

// Bukkit
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

// Local
import me.ryall.painter.Painter;

public class EconomyManager
{
    private EconomyInterface economy;
    
    public EconomyInterface getInterface()
    {
        return economy;
    }    
    
    public void load()
    {
        if (economy == null) 
        {
            String adapter = Painter.get().getConfig().getEconomyAdapter();
            
            if (!adapter.isEmpty())
            {
                Plugin plugin = Painter.get().getServer().getPluginManager().getPlugin(adapter);
            
                if (plugin != null)
                {
                    if (adapter.toLowerCase().equals("iconomy"))
                        economy = new IConomyAdapter(plugin);
                    else if (adapter.toLowerCase().equals("mineconomy"))
                        economy = new MineConomyAdapter(plugin);
                    
                    if (economy != null)
                        Painter.get().logInfo("Attached to " + adapter);
                }
            }
        }
    }
    
    public double getPrice(Player _player, int _numBlocks)
    {
        if (Painter.get().getConfig().isEconomyEnabled())
        {
            return Painter.get().getConfig().getEconomyDyeCost() * _numBlocks;
        }
        
        return 0;
    }
    
    public boolean charge(Player _player, int _numBlocks) 
    {
        if (Painter.get().getConfig().isEconomyEnabled())
        {
            double price = getPrice(_player, _numBlocks);
            
            // Ignore invalid prices.
            if (price > 0)
            {
                if (!economy.canAfford(_player.getName(), price))
                {
                    Painter.get().getComms().error(_player, "You need " + economy.formatCurrency(price) + " to dye this block.");
                    return false;
                }
                
                if (!economy.subtract(_player.getName(), price))
                {
                    Painter.get().getComms().error(_player, "Failed to charge your account.");
                    return false;
                }
                
                Painter.get().getComms().message(_player, "Charged " + economy.formatCurrency(price) + " to dye this block.");
            }
        }
        
        return true;
    }

    public void refund(Player _player, double _price)
    {
        if (Painter.get().getConfig().isEconomyEnabled())
        {
            if (_price > 0)
            {
                if (!economy.add(_player.getName(), _price))
                {
                    Painter.get().getComms().error(_player, "Failed to refund your account.");
                    return;
                }
                
                Painter.get().getComms().message(_player, "Refunded " + economy.formatCurrency(_price) + ".");
            }
        }
    }
}
