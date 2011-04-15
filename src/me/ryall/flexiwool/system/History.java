package me.ryall.flexiwool.system;

// Java
import java.util.ArrayList;

// Bukkit
import me.ryall.flexiwool.Flexiwool;

// Bukkit
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class History
{
    // Logs for the history.
    public class Log
    {
        // Entries for the logs.
        public class Entry
        {
            Location location;
            byte colourFrom;
            byte colourTo;
        }
        
        ArrayList<Entry> entries;
        String worldName;
        double price;

        public Log(String _worldName, double _price)
        {
            entries = new ArrayList<Entry>();
            worldName = _worldName;
            price = _price;
        }
        
        public void addEntry(Block _block, byte _colour)
        {
            Entry entry = new Entry();
            
            entry.location = _block.getLocation();
            entry.colourFrom = _block.getData();
            entry.colourTo = _colour;
            
            entries.add(entry);
        }
        
        private void rollback()
        {
            for (Entry entry : entries)
            {
                World world = Flexiwool.get().getServer().getWorld(worldName);
                Block block = world.getBlockAt(entry.location);
                
                // Make sure we're not changing something we shouldn't be.
                if (block.getType() == Material.WOOL && block.getData() == entry.colourTo)
                {
                    block.setData(entry.colourFrom);
                }
            }
        }

        public double getPrice()
        {
            return price;
        }
    }
    
    Log[] logs;
    int pointer;
    
    public History()
    {
        logs = new Log[Flexiwool.get().getConfig().getMaxHistory()];
        pointer = 0;
    }
    
    public Log createLog(String _worldName, double _price)
    {
        Log log = new Log(_worldName, _price);
        
        logs[pointer] = log;
        pointer = ++pointer % logs.length;
        
        return log;
    }
    
    public void rollback()
    {
        Log log = logs[pointer];
        
        if (log != null)
        {
            logs[pointer] = null;
            pointer = (pointer + logs.length - 1) % logs.length;
        
            log.rollback();
        }
    }

    public Log getLastLog()
    {
        return logs[pointer];
    }
}
