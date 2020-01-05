package ru.hubsmc.ru.hubschesterton.internal.menu;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import ru.hubsmc.ru.hubschesterton.PluginUtils;
import ru.hubsmc.ru.hubschesterton.internal.ActionClickHandler;
import ru.hubsmc.ru.hubschesterton.internal.action.ReturnItemAction;
import ru.hubsmc.ru.hubschesterton.internal.item.ChestertonItem;
import ru.hubsmc.ru.hubschesterton.internal.item.PlayerHeadItem;
import ru.hubsmc.ru.hubschesterton.internal.item.ShopItem;

import java.util.Collections;
import java.util.Map;

public class ShopQuantityMenu extends ChestertonMenu{

    private ShopItem item;
    private PlayerHeadItem returnButton;

    public ShopQuantityMenu(ShopItem shopItem) {
        super("&6&lПокупка предмета", 54);
        this.item = shopItem;
        returnButton = new PlayerHeadItem();
        returnButton.setName(PluginUtils.getConfigurationString("buttons.return.name"));
        returnButton.setClickHandler(new ActionClickHandler(Collections.singletonList(new ReturnItemAction(this))));
    }

    @Override
    protected void setItemsToInventory(Inventory inventory, Player player) {
        item.prepareToQuantityMenu();

        if (item.getMaterial().equals(Material.ENCHANTED_BOOK)) {
            for (Map.Entry<Enchantment, Integer> entry : item.getStoredEnchantments().entrySet()) {
                for (int i = 1; i <= entry.getKey().getMaxLevel(); i++) {
                    inventory.setItem(9 + i, item.createShopEnchantedStack(player, i));
                }
            }
        } else {
            int maxIt = ((int) (Math.log10(item.getMaterial().getMaxStackSize()) / Math.log10(2))) + 1;
            for (int i = 1; i <= maxIt; i++) {
                inventory.setItem(9 + i, item.createShopStack(player, (int) Math.pow(2, i - 1)));
            }
        }

        returnButton.setBase64(PluginUtils.getConfigurationString("buttons.return.base"));
        inventory.setItem(34, returnButton.createItemStack(player));
        inventory.setItem(35, returnButton.createItemStack(player));
    }

    @Override
    public ChestertonItem getItem(int slot) {
        item.prepareToBuyAction();

        switch (slot) {
            case 10:
                return item.changeData(1);
            case 11:
                return item.changeData(2);
            case 12:
                return item.changeData(4);
            case 13:
                return item.changeData(8);
            case 14:
                return item.changeData(16);
            case 15:
                return item.changeData(32);
            case 16:
                return item.changeData(64);
            case 34:
            case 35:
                return returnButton;
        }

        return null;
    }

}
