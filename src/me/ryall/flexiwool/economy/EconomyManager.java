package me.ryall.flexiwool.economy;

// Bukkit
import org.bukkit.plugin.Plugin;

// Local
import me.ryall.flexiwool.Flexiwool;

public class EconomyManager
{
    private EconomyInterface economy;
    
    public void load()
    {
        if (economy == null) 
        {
            String adapter = Flexiwool.get().getConfig().getEconomyAdapter();
            
            if (!adapter.isEmpty())
            {
                Plugin plugin = Flexiwool.get().getServer().getPluginManager().getPlugin(adapter);
            
                if (plugin != null)
                {
                    if (adapter.toLowerCase().equals("iconomy"))
                        economy = new IConomyAdapter(plugin);
                    else if (adapter.toLowerCase().equals("mineconomy"))
                        economy = new MineConomyAdapter(plugin);
                    
                    if (economy != null)
                        Flexiwool.get().logInfo("Attached to " + adapter);
                }
            }
        }
    }
    
    public EconomyInterface getInterface()
    {
        return economy;
    }
}
