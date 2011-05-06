package me.ryall.painter.economy;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

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
            String adapter = Painter.get().getConfigManager().getEconomyAdapter();

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

    public double getPrice(Player _player, Block _block, int _numBlocks)
    {
        if (Painter.get().getConfigManager().isEconomyEnabled())
        {
            return Painter.get().getConfigManager().getCost(_block) * _numBlocks;
        }

        return 0;
    }

    public boolean charge(Player _player, double _price)
    {
        if (Painter.get().getConfigManager().isEconomyEnabled())
        {
            // Ignore invalid prices.
            if (_price > 0)
            {
                if (!economy.canAfford(_player.getName(), _price))
                {
                    Painter.get().getCommunicationManager().error(_player, "You need " + economy.formatCurrency(_price) + " to dye this block.");
                    return false;
                }

                if (!economy.subtract(_player.getName(), _price))
                {
                    Painter.get().getCommunicationManager().error(_player, "Failed to charge your account.");
                    return false;
                }

                Painter.get().getCommunicationManager().message(_player, "Charged " + economy.formatCurrency(_price) + " to dye this block.");
            }
        }

        return true;
    }

    public void refund(Player _player, double _price)
    {
        if (Painter.get().getConfigManager().isEconomyEnabled())
        {
            if (_price > 0)
            {
                if (!economy.add(_player.getName(), _price))
                {
                    Painter.get().getCommunicationManager().error(_player, "Failed to refund your account.");
                    return;
                }

                Painter.get().getCommunicationManager().message(_player, "Refunded " + economy.formatCurrency(_price) + ".");
            }
        }
    }
}
