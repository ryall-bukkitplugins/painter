package me.ryall.painter.system;

// Java
import java.util.HashMap;

// Local
import me.ryall.painter.Painter;
import me.ryall.painter.system.History.Log;

// Bukkit
import org.bukkit.entity.Player;

public class HistoryManager
{
    private HashMap<String, History> histories;

    public HistoryManager()
    {
        histories = new HashMap<String, History>();
    }

    public History getHistory(Player _player)
    {
        if (Painter.get().getConfigManager().isHistoryEnabled())
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
        if (!Painter.get().getConfigManager().isHistoryEnabled())
            return;

        if (Painter.get().getPermissionManager().hasRollbackPermission(_player))
        {
            History history = getHistory(_player);

            if (history != null)
            {
                Log log = history.getLastLog();

                if (log != null)
                {
                    history.rollback();

                    if (Painter.get().getConfigManager().shouldHistoryRefundOnRollback())
                        Painter.get().getEconomyManager().refund(_player, log.getPrice());

                    Painter.get().getCommunicationManager().message(_player, "Your changes were rolled-back.");
                } else
                    Painter.get().getCommunicationManager().error(_player, "You don't have any changes to roll-back.");
            } else
                Painter.get().getCommunicationManager().error(_player, "Could not access your history.");
        } else
            Painter.get().getCommunicationManager().error(_player, "You don't have permission to roll-back changes.");
    }

    public void delete(Player _player)
    {
        if (!Painter.get().getConfigManager().shouldHistoryPersistOnLogout())
        {
            if (histories.containsKey(_player.getName()))
                histories.remove(_player.getName());
        }
    }
}
