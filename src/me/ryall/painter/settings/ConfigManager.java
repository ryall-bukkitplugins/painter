package me.ryall.painter.settings;

import java.util.HashMap;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.config.Configuration;
import me.ryall.painter.Painter;

public class ConfigManager
{
    private Configuration config;
    private HashMap<String, Double> transmutations;
    
    public void load() 
    {
        config = Painter.get().getConfiguration();
        config.load();
        
        // Load all the transmutations.
        transmutations = new HashMap<String, Double>();
        
        List<String> keys = config.getKeys("Transmutation.Blocks");
        
        if (keys != null && keys.size() > 0)
        {
            for (String key : keys)
            {
                transmutations.put(key, config.getDouble("Transmutation.Blocks." + key, 0));
            }
        }
    }
    
    public boolean shouldConsumeDye()
    {
        return config.getBoolean("Dye.Consume", true);
    }
    
    public boolean isEconomyEnabled()
    {
        return config.getBoolean("Economy.Enabled", false) && Painter.get().getEconomy().getInterface() != null;
    }
    
    public String getEconomyAdapter()
    {
        return config.getString("Economy.Adapter", "");
    }
    
    private double getEconomyDyeCost()
    {
        return config.getDouble("Economy.DyeCost", 1);
    }
    
    public int getMaxFill()
    {
        return config.getInt("Fill.Max", 1000);
    }
    
    public boolean shouldFillChargePerBlock()
    {
        return config.getBoolean("Fill.ChargePerBlock", true);
    }
    
    public boolean isHistoryEnabled()
    {
        return config.getBoolean("History.Enabled", true);
    }
    
    public int getMaxHistory()
    {
        return config.getInt("History.Max", 10);
    }
    
    public boolean shouldHistoryRefundOnRollback()
    {
        return config.getBoolean("History.RefundOnRollback", true);
    }
    
    public boolean shouldHistoryPersistOnLogout()
    {
        return config.getBoolean("History.PersistOnLogout", false);
    }

    public boolean isTransmutable(Block _block)
    {
        return _block != null && 
            (_block.getType() == Material.WOOL || transmutations.containsKey(_block.getType().toString()));
    }
    
    public boolean isTransmuteFillEnabled()
    {
        return config.getBoolean("Transmutation.EnableFill", false);
    }
    
    public double getCost(Block _block)
    {
        if (_block.getType() == Material.WOOL)
            return getEconomyDyeCost();

        return transmutations.get(_block.getType().toString());
    }
}
