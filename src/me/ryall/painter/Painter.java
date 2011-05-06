package me.ryall.painter;

// Java
import java.util.logging.Logger;

// Bukkit
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

// Local
import me.ryall.painter.communication.CommunicationManager;
import me.ryall.painter.economy.EconomyManager;
import me.ryall.painter.listeners.PainterPlayerListener;
import me.ryall.painter.listeners.PainterPluginListener;
import me.ryall.painter.settings.ConfigManager;
import me.ryall.painter.settings.PermissionManager;
import me.ryall.painter.system.HistoryManager;
import me.ryall.painter.system.PaintManager;

public class Painter extends JavaPlugin
{
    public static String PLUGIN_NAME = "Painter";
    public static String LOG_HEADER = "[" + PLUGIN_NAME + "] ";
    private static Painter instance = null;

    private Logger log;
    private PainterPluginListener pluginListener;
    private PainterPlayerListener playerListener;
    private ConfigManager configManager;
    private PermissionManager permissionManager;
    private EconomyManager economyManager;
    private CommunicationManager communicationManager;
    private HistoryManager historyManager;
    private PaintManager paintManager;

    public static Painter get()
    {
        return instance;
    }

    public void onEnable()
    {
        instance = this;
        log = Logger.getLogger("Minecraft");
        pluginListener = new PainterPluginListener();
        playerListener = new PainterPlayerListener();

        configManager = new ConfigManager();
        permissionManager = new PermissionManager();
        economyManager = new EconomyManager();
        communicationManager = new CommunicationManager();
        historyManager = new HistoryManager();
        paintManager = new PaintManager();

        registerEvents();

        getConfigManager().load();

        logInfo("Started");
    }

    public void onDisable()
    {
        logInfo("Stopped");
    }

    public void registerEvents()
    {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvent(Event.Type.PLUGIN_ENABLE, pluginListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Event.Priority.Normal, this);
    }

    public boolean onCommand(CommandSender _sender, Command _command, String _label, String[] _args)
    {
        if (_label.equals("painter") || _label.equals("paint"))
        {
            if (_sender instanceof Player)
            {
                Player player = (Player) _sender;

                if (_args.length == 1)
                {
                    if (_args[0].equals("rollback") || _args[0].equals("rb") || _args[0].equals("undo"))
                    {
                        historyManager.rollback(player);
                        return true;
                    }
                }

                if (configManager.isHistoryEnabled())
                    communicationManager.command(player, "/paint <rollback|rb|undo>", "Roll-back your last change.");
            } else
                logError("Commands can only be executed in-game.");

            return true;
        }

        return false;
    }

    public ConfigManager getConfigManager()
    {
        return configManager;
    }

    public PermissionManager getPermissionManager()
    {
        return permissionManager;
    }

    public EconomyManager getEconomyManager()
    {
        return economyManager;
    }

    public CommunicationManager getCommunicationManager()
    {
        return communicationManager;
    }

    public HistoryManager getHistoryManager()
    {
        return historyManager;
    }

    public PaintManager getPaintManager()
    {
        return paintManager;
    }

    public void logInfo(String _message)
    {
        log.info(LOG_HEADER + _message);
    }

    public void logError(String _message)
    {
        log.severe(LOG_HEADER + _message);
    }
}
