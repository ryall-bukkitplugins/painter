package me.ryall.painter.system;

// Java
import java.util.ArrayList;

// Bukkit
import me.ryall.painter.Painter;

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
                World world = Painter.get().getServer().getWorld(worldName);
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
        int max = Painter.get().getConfig().getMaxHistory();
        
        if (max > 0)
        {
            logs = new Log[max];
            pointer = max - 1;
        }
    }
    
    public Log createLog(String _worldName, double _price)
    {
        if (logs != null)
        {
            Log log = new Log(_worldName, _price);
            
            pointer = ++pointer % logs.length;
            logs[pointer] = log;
            
            return log;
        }
        
        return null;
    }
    
    public void rollback()
    {
        if (logs != null)
        {
            Log log = logs[pointer];
            
            if (log != null)
            {
                logs[pointer] = null;
                pointer = (pointer + logs.length - 1) % logs.length;
            
                log.rollback();
            }
        }
    }

    public Log getLastLog()
    {
        return (logs != null) ? logs[pointer] : null;
    }
}
