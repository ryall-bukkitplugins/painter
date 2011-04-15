package me.ryall.flexiwool.settings;

// Local
import me.ryall.flexiwool.Flexiwool;

// Bukkit
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

// Permissions
import com.nijikokun.bukkit.Permissions.Permissions;
import com.nijiko.permissions.PermissionHandler;

public class PermissionManager
{
    public static String PERMISSIONS_PREFIX = "flexiwool.";
    
    public PermissionHandler permissions;
    
    public void load()
    {
        if (permissions == null) 
        {
            Plugin plugin = Flexiwool.get().getServer().getPluginManager().getPlugin("Permissions");
            
            if (plugin != null)
            {
                Flexiwool.get().logInfo("Attached to Permissions");
                permissions = ((Permissions)plugin).getHandler();
            }
        }
    }
    
    protected boolean hasGlobalPermission(Player _player)
    {
        return (permissions == null && _player.isOp()) || 
            hasPermission(_player, PERMISSIONS_PREFIX + "*") || 
            hasPermission(_player, "*");
    }
    
    public boolean hasDyePermission(Player _player)
    {
        return hasGlobalPermission(_player) || 
            hasPermission(_player, PERMISSIONS_PREFIX + "dye");
    }
    
    public boolean hasFillPermission(Player _player)
    {
        return hasGlobalPermission(_player) || 
            hasPermission(_player, PERMISSIONS_PREFIX + "fill");
    }
    
    public boolean hasRollbackPermission(Player _player)
    {
        return hasGlobalPermission(_player) || 
            hasPermission(_player, PERMISSIONS_PREFIX + "rollback");
    }
    
    public boolean hasTransmutePermission(Player _player)
    {
        return hasGlobalPermission(_player) || 
            hasPermission(_player, PERMISSIONS_PREFIX + "transmute");
    }
    
    private boolean hasPermission(Player _player, String _permission)
    {
        if (permissions != null)
            return permissions.has(_player, _permission);
        
        return false;
    }
}
