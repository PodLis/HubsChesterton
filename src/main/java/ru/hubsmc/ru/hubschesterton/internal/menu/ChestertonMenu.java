package ru.hubsmc.ru.hubschesterton.internal.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import ru.hubsmc.ru.hubschesterton.internal.ChestertonInventoryHolder;
import ru.hubsmc.ru.hubschesterton.internal.item.ChestertonItem;

import static ru.hubsmc.ru.hubschesterton.internal.MenuUtils.registerMenu;

public abstract class ChestertonMenu {

    private ChestertonItem[] items;
    private String name;
    private String title;

    public ChestertonMenu(String name, String title, int slots) {
        items = new ChestertonItem[slots];
        this.name = name;
        this.title = title;
        registerMenu(this, name);
    }

    public void setItem(int slot, ChestertonItem item) {
        if (slot >= 0 && slot < items.length) {
            items[slot] = item;
        }
    }

    public ChestertonItem getItem(int slot) {
        if (slot >= 0 && slot < items.length) {
            return items[slot];
        }
        return null;
    }

    public int size() {
        return items.length;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory(new ChestertonInventoryHolder(this), items.length, title);

        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                inventory.setItem(i, items[i].createItemStack(player));
            }
        }

        player.openInventory(inventory);
    }

}
