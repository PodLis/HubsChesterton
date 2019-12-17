package ru.hubsmc.ru.hubschesterton;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import ru.hubsmc.ru.hubschesterton.event.InventoryEvent;
import ru.hubsmc.ru.hubschesterton.internal.ActionClickHandler;
import ru.hubsmc.ru.hubschesterton.internal.action.GiveItemAction;
import ru.hubsmc.ru.hubschesterton.internal.action.ItemAction;
import ru.hubsmc.ru.hubschesterton.internal.item.ChestertonItem;
import ru.hubsmc.ru.hubschesterton.internal.menu.ChestMenu;
import ru.hubsmc.ru.hubschesterton.internal.menu.ChestertonMenu;

import java.util.LinkedList;
import java.util.List;

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

            getServer().getPluginManager().registerEvents(new InventoryEvent(), this);

            Commands commands = new Commands();
            getCommand("chesterton").setExecutor(commands);
            getCommand("chesterton").setTabCompleter(commands);

            ChestMenu chestMenu = new ChestMenu("test", "Hello", 27);

            ChestertonItem apple = new ChestertonItem(Material.APPLE);
            List<ItemAction> appleActions = new LinkedList<>();
            appleActions.add(new GiveItemAction(new ItemStack(Material.APPLE, 3)));
            apple.setClickHandler(new ActionClickHandler(appleActions));

            ChestertonItem cake = new ChestertonItem(Material.CAKE);
            List<ItemAction> cakeActions = new LinkedList<>();
            cakeActions.add(new GiveItemAction(new ItemStack(Material.CAKE, 1)));
            cakeActions.add(new GiveItemAction(new ItemStack(Material.WOODEN_SHOVEL, 2)));
            cake.setClickHandler(new ActionClickHandler(cakeActions));

            chestMenu.setItem(3, apple);
            chestMenu.setItem(10, cake);

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
