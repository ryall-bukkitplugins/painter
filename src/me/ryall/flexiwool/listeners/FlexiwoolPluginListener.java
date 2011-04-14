package me.ryall.flexiwool.listeners;

// Bukkit
import me.ryall.flexiwool.Flexiwool;

import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;

public class FlexiwoolPluginListener extends ServerListener
{
    public void onPluginEnable(PluginEnableEvent _event) 
    {
        Flexiwool.get().getPermissions().load();
        Flexiwool.get().getEconomy().load();
    }
}
