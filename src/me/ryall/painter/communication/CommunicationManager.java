package me.ryall.painter.communication;

// Bukkit
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

//Local
import me.ryall.painter.Painter;

public class CommunicationManager
{
    public static String MESSAGE_HEADER = ChatColor.WHITE + "[" + ChatColor.GOLD + Painter.PLUGIN_NAME + ChatColor.WHITE + "] " + ChatColor.WHITE;
    
    public void message(Player _player, String _message)
    {
        _player.sendMessage(MESSAGE_HEADER + _message);
    }
    
    public void error(Player _player, String _message)
    {
        _player.sendMessage(MESSAGE_HEADER + ChatColor.RED + "Error: " + _message);
    }

    public void command(Player _player, String _command, String _description)
    {
        _player.sendMessage(MESSAGE_HEADER + ChatColor.GOLD + _command + ChatColor.WHITE + ": " + _description);
        
        //if (_description != null)
        //    _player.sendMessage(MESSAGE_HEADER + ChatColor.WHITE + _description);
    }
}
