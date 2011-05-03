package me.ryall.painter.economy;

// Bukkit
import org.bukkit.plugin.Plugin;

// iConomy
import com.spikensbror.bukkit.mineconomy.MineConomy;

public class MineConomyAdapter extends EconomyInterface
{
    private MineConomy plugin;

    public String getName()
    {
        return "MineConomy";
    }

    public MineConomyAdapter(Plugin _plugin)
    {
        plugin = (MineConomy) _plugin;
    }

    public double getBalance(String _playerName)
    {
        return plugin.getBank().getTotal(_playerName);
    }

    public boolean canAfford(String _playerName, double _amount)
    {
        return plugin.getBank().getTotal(_playerName) >= _amount;
    }

    public boolean add(String _playerName, double _amount)
    {
        return plugin.getBank().add(_playerName, _amount);
    }

    public boolean subtract(String _playerName, double _amount)
    {
        return plugin.getBank().subtract(_playerName, _amount);
    }

    public boolean transfer(String _playerFrom, String _playerTo, double _amount)
    {
        return plugin.getBank().transfer(_playerFrom, _playerTo, _amount);
    }

    public String formatCurrency(double _amount)
    {
        return "$" + getCurrencyString(_amount);
    }
}
