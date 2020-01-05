package ru.hubsmc.ru.hubschesterton;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import ru.hubsmc.ru.hubschesterton.commands.ParseCommand;
import ru.hubsmc.ru.hubschesterton.commands.SellCommand;
import ru.hubsmc.ru.hubschesterton.commands.ShopCommand;
import ru.hubsmc.ru.hubschesterton.event.InventoryEvent;

import static ru.hubsmc.ru.hubschesterton.PluginUtils.loadConfiguration;
import static ru.hubsmc.ru.hubschesterton.util.ServerUtils.logConsole;

public final class HubsChesterton extends JavaPlugin {

    public static final String CHAT_PREFIX = ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "HC" + ChatColor.DARK_GREEN + "] " + ChatColor.GREEN;
    static HubsChesterton plugin;

    public static HubsChesterton getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        try {

            loadConfiguration();

            getServer().getPluginManager().registerEvents(new InventoryEvent(), this);

            Commands commands = new Commands();
            getCommand("chesterton").setExecutor(commands);
            getCommand("chesterton").setTabCompleter(commands);

            getCommand("chestpars").setExecutor(new ParseCommand());
            getCommand("shop").setExecutor(new ShopCommand());
            getCommand("sell").setExecutor(new SellCommand());

            logConsole("HubsChesterton успешно включен!");
        } catch (Throwable e) {
            e.printStackTrace();
            logConsole("Произошла ошибка при загрузке HubsChesterton!");
        }
    }

    @Override
    public void onDisable() {
        logConsole("HubsChesterton успешно отключен.");
    }

}
