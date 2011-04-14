package me.ryall.flexiwool.communication;

// Bukkit
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

//Local
import me.ryall.flexiwool.Flexiwool;

public class CommunicationManager
{
    public static String PLUGIN_HEADER = ChatColor.WHITE + "[" + ChatColor.GOLD + Flexiwool.PLUGIN_NAME + ChatColor.WHITE + "] ";
    public static String MESSAGE_HEADER = PLUGIN_HEADER + ChatColor.WHITE;
    public static String ERROR_HEADER = PLUGIN_HEADER + ChatColor.RED + "Error: ";
    
    public void message(Player _player, String _message)
    {
        _player.sendMessage(MESSAGE_HEADER + _message);
    }
    
    public void error(Player _player, String _message)
    {
        _player.sendMessage(ERROR_HEADER + _message);
    }
}
