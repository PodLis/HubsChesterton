package ru.hubsmc.ru.hubschesterton.internal.special;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.hubsmc.ru.hubschesterton.PluginUtils;
import ru.hubsmc.ru.hubschesterton.internal.ClickHandler;
import ru.hubsmc.ru.hubschesterton.internal.item.ChestertonItem;
import ru.hubsmc.ru.hubschesterton.internal.item.PlayerHeadItem;
import ru.hubsmc.ru.hubschesterton.util.StringUtils;

import static ru.hubsmc.hubsvalues.api.API.addDollars;

public class SellBucket implements PartialHolderInNeed {

    private String title;
    private PlayerHeadItem item;
    private Inventory inventory;

    public SellBucket(String title) {
        this.title = title;
        this.inventory = null;
        item = new PlayerHeadItem();
        item.setName(PluginUtils.getConfigurationString("buttons.sell.name"));
        item.setLore(PluginUtils.getConfigurationStringList("buttons.sell.lore"));
        item.setBase64(PluginUtils.getConfigurationString("buttons.sell.base"));
        item.setClickHandler(new ClickHandler() {
            @Override
            public boolean onClick(Player player) {
                if (inventory != null) {
                    for (int i = 0; i < 53; i++) {
                        ItemStack itemStack = inventory.getItem(i);
                        if (itemStack != null) {
                            if (PluginUtils.isSold(itemStack.getType())) {
                                addDollars(player, PluginUtils.getSellPrice(itemStack.getType()) * itemStack.getAmount());
                            } else {
                                player.getInventory().addItem(itemStack);
                            }
                            inventory.clear(i);
                        }
                    }
                }
                return false;
            }
        });
    }

    public void open(Player player) {
        inventory = Bukkit.createInventory(new PartialInventoryHolder(this), 54, StringUtils.replaceColor(title));
        inventory.setItem(53, item.createItemStack(player));
        player.openInventory(inventory);
    }

    @Override
    public ChestertonItem getItem() {
        return item;
    }
}
