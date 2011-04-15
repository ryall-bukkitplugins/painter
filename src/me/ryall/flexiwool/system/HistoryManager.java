package me.ryall.flexiwool.system;

// Java
import java.util.HashMap;

// Local
import me.ryall.flexiwool.Flexiwool;
import me.ryall.flexiwool.system.History.Log;

// Bukkit
import org.bukkit.entity.Player;

public class HistoryManager
{
    private HashMap<String, History> histories;
    
    public HistoryManager()
    {
        histories = new HashMap<String, History>();
    }
    
    public History get(Player _player)
    {
        if (Flexiwool.get().getConfig().isHistoryEnabled())
        {
            History history = histories.get(_player.getName());
            
            if (history == null)
            {
                history = new History();
                histories.put(_player.getName(), history);
            }
            
            return history;
        }
        
        return null;
    }
    
    public void rollback(Player _player)
    {
        History history = get(_player);
        
        if (history != null)
        {
            Log log = history.getLastLog();
            
            if (log != null)
            {
                history.rollback();
            
                if (Flexiwool.get().getConfig().shouldHistoryRefundOnRollback())
                    Flexiwool.get().getEconomy().refund(_player, log.getPrice());
            }
        }
    }
    
    public void delete(Player _player)
    {
        if (!Flexiwool.get().getConfig().shouldHistoryPersistOnLogout())
        {
            if (histories.containsKey(_player))
                histories.remove(_player);
        }
    }
}
