package me.ryall.painter.listeners;

// Bukkit
import me.ryall.painter.Painter;

import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;

public class PainterPluginListener extends ServerListener
{
    public void onPluginEnable(PluginEnableEvent _event)
    {
        Painter.get().getPermissions().load();
        Painter.get().getEconomy().load();
    }
}
