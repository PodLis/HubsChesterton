package ru.hubsmc.ru.hubschesterton.internal.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.hubsmc.ru.hubschesterton.PluginUtils;
import ru.hubsmc.ru.hubschesterton.internal.ActionClickHandler;
import ru.hubsmc.ru.hubschesterton.internal.action.BuyShopItemAction;
import ru.hubsmc.ru.hubschesterton.internal.action.OpenMenuItemAction;
import ru.hubsmc.ru.hubschesterton.internal.menu.ShopCategoryMenu;
import ru.hubsmc.ru.hubschesterton.internal.menu.ShopQuantityMenu;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ShopItem extends ExtendedItem {

    private int price;
    private int data;

    public ShopItem(Material material, int price) {
        super(material);
        this.price = price;
        setNeedToRefresh(true);
    }

    public void prepareToCategoryMenu(ShopCategoryMenu shopCategoryMenu) {
        setLore(new LinkedList<>());
        addLore("&e" + price + "$");
        addLore("&aКликните, чтобы перейти к покупке");
        setLoreIsUpdated(false);

        setClickHandler(new ActionClickHandler(Collections.singletonList(new OpenMenuItemAction(shopCategoryMenu, new ShopQuantityMenu(this)))));
    }

    public void prepareToQuantityMenu() {
        setLore(new LinkedList<>());
        addLore("&e" + price + "$");
        setLoreIsUpdated(false);

        setClickHandler(new ActionClickHandler( Collections.singletonList(new BuyShopItemAction(this, price)) ));
    }

    public void prepareToBuyAction() {
        setLore(new LinkedList<>());
        setLoreIsUpdated(false);
    }

    public ItemStack createShopStack(Player player, int amount) {
        setLore(new LinkedList<>());
        addLore("&e" + (price * amount) + "$");
        setLoreIsUpdated(false);

        return createItemStack(player, amount);
    }

    private void setEnchantmentLevel(int level) {
        Map<Enchantment, Integer> storedEnchantments = getStoredEnchantments();
        for (Map.Entry<Enchantment, Integer> entry : storedEnchantments.entrySet()) {
            storedEnchantments.replace(entry.getKey(), level);
        }
        setStoredEnchantments(storedEnchantments);
    }

    public ItemStack createShopEnchantedStack(Player player, int level) {
        setEnchantmentLevel(level);
        setLore(new LinkedList<>());
        addLore("&e" + PluginUtils.getEnchantedBookPrice(storedEnchantments) + "$");
        setLoreIsUpdated(false);
        return createItemStack(player);
    }

    public ItemStack createBuyEnchantedStack(Player player, int level) {
        setEnchantmentLevel(level);
        setLore(new LinkedList<>());
        setLoreIsUpdated(false);
        return createItemStack(player);
    }

    public ShopItem changeData(int data) {
        this.data = data;
        return this;
    }

    public int getData() {
        return data;
    }

}
