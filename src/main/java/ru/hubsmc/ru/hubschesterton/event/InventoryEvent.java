package ru.hubsmc.ru.hubschesterton.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import ru.hubsmc.ru.hubschesterton.HubsChesterton;
import ru.hubsmc.ru.hubschesterton.internal.ChestertonInventoryHolder;
import ru.hubsmc.ru.hubschesterton.internal.item.ChestertonItem;
import ru.hubsmc.ru.hubschesterton.internal.menu.ChestertonMenu;

public class InventoryEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof ChestertonInventoryHolder) {

            event.setCancelled(true); // First thing to do, if an exception is thrown at least the player doesn't take the item

            ChestertonMenu menu = ((ChestertonInventoryHolder) event.getInventory().getHolder()).getMenu();
            int slot = event.getRawSlot();

            if (slot >= 0 && slot < menu.size()) {

                ChestertonItem item = menu.getItem(slot);
                Player player = (Player) event.getWhoClicked();

                if (item != null && event.getInventory().getItem(slot) != null) {
                    // Closes the inventory and executes commands AFTER the event
                    Bukkit.getScheduler().scheduleSyncDelayedTask(HubsChesterton.getInstance(), () -> {
                        boolean close = item.onClick(player);
                        if (close) {
                            player.closeInventory();
                        }
                    });
                }
            }

        }
    }

}
