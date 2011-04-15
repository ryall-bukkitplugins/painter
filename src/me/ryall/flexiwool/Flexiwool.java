package me.ryall.flexiwool;

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
import me.ryall.flexiwool.communication.CommunicationManager;
import me.ryall.flexiwool.economy.EconomyManager;
import me.ryall.flexiwool.listeners.FlexiwoolPlayerListener;
import me.ryall.flexiwool.listeners.FlexiwoolPluginListener;
import me.ryall.flexiwool.settings.ConfigManager;
import me.ryall.flexiwool.settings.PermissionManager;
import me.ryall.flexiwool.system.HistoryManager;
import me.ryall.flexiwool.system.Painter;

public class Flexiwool extends JavaPlugin
{
    public static String PLUGIN_NAME = "Flexiwool";
    public static String LOG_HEADER = "[" + PLUGIN_NAME + "] ";
    private static Flexiwool instance = null;
    
    private Logger log;
    private FlexiwoolPluginListener pluginListener;
    private FlexiwoolPlayerListener playerListener;
    private ConfigManager configManager;
    private PermissionManager permissionManager;
    private EconomyManager economyManager;
    private CommunicationManager communicationManager;
    private HistoryManager historyManager;
    private Painter painter;
    
    public static Flexiwool get()
    {
        return instance;
    }
    
    public void onEnable()
    {
        instance = this;
        log = Logger.getLogger("Minecraft");
        pluginListener = new FlexiwoolPluginListener();
        playerListener = new FlexiwoolPlayerListener();
        
        configManager = new ConfigManager();
        permissionManager = new PermissionManager();
        economyManager = new EconomyManager();
        communicationManager = new CommunicationManager();
        historyManager = new HistoryManager();
        painter = new Painter();
        
        registerEvents();
        
        getConfig().load();
        
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
        if (_label.equals("flexiwool") || _label.equals("fw"))
        {
            if (_sender instanceof Player)
            {
                Player player = (Player)_sender;
            
                if (_args.length == 1)
                {
                    if (_args[0].equals("rollback") || _args[0].equals("rb") || _args[0].equals("undo"))
                    {
                        historyManager.rollback(player);
                        return true;
                    }
                }
                
                if (configManager.isHistoryEnabled())
                    communicationManager.command(player, "/fw <rollback|rb|undo>", "Roll-back your last change.");
            }
            else
                logError("Flexiwool commands can only be executed in-game.");
            
            return true;
        }
        
        return false;
    }
    
    public ConfigManager getConfig()
    {
        return configManager;
    }
    
    public PermissionManager getPermissions()
    {
        return permissionManager;
    }
    
    public EconomyManager getEconomy()
    {
        return economyManager;
    }
    
    public CommunicationManager getComms()
    {
        return communicationManager;
    }
    
    public HistoryManager getHistory()
    {
        return historyManager;
    }
    
    public Painter getPainter()
    {
        return painter;
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
