package me.ryall.flexiwool.system;

// Java
import java.util.ArrayList;

// Bukkit
import me.ryall.flexiwool.Flexiwool;
import me.ryall.flexiwool.system.Historian.Log.Entry;

// Bukkit
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Historian
{
    // Logs for the historian to track.
    public class Log
    {
        public class Entry
        {
            Location location;
            byte colourFrom;
            byte colourTo;
        }
        
        ArrayList<Entry> entries;
        String worldName;
        double cost;

        public Log(String _worldName, double _cost)
        {
            entries = new ArrayList<Entry>();
            worldName = _worldName;
            cost = _cost;
        }
        
        public void addEntry(Block _block, byte _colourFrom, byte _colourTo)
        {
            Entry entry = new Entry();
            
            entry.location = _block.getLocation();
            entry.colourFrom = _colourFrom;
            entry.colourTo = _colourTo;
            
            entries.add(entry);
        }
        
        private boolean rollback()
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
            
            return true;
        }
    }
    
    Log[] logs;
    int pointer;
    
    public Historian()
    {
        logs = new Log[Flexiwool.get().getConfig().getMaxHistory()];
        pointer = 0;
    }
    
    public Log createLog(String _worldName, double _cost)
    {
        Log log = new Log(_worldName, _cost);
        
        logs[pointer] = log;
        pointer = ++pointer % logs.length;
        
        return log;
    }
    
    public boolean rollback()
    {
        Log log = logs[pointer];
        
        if (log != null)
        {
            logs[pointer] = null;
            pointer = (pointer + logs.length - 1) % logs.length;
        
            return log.rollback();
        }
        
        return false;
    }
}
