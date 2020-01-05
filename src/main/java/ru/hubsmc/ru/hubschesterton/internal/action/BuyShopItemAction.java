package ru.hubsmc.ru.hubschesterton.internal.action;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import ru.hubsmc.ru.hubschesterton.PluginUtils;
import ru.hubsmc.ru.hubschesterton.internal.item.ShopItem;
import ru.hubsmc.ru.hubschesterton.util.MessageUtils;

import static ru.hubsmc.hubsvalues.api.API.takeDollars;

public class BuyShopItemAction extends ItemAction {

    private ShopItem item;
    private int price;

    public BuyShopItemAction(ShopItem item, int price) {
        this.item = item;
        this.price = price;
    }

    @Override
    public void execute(Player player) {
        if (item.getMaterial().equals(Material.ENCHANTED_BOOK)) {
            int level = ((int) (Math.log10(item.getData()) / Math.log10(2))) + 1;
            if (takeDollars(player, PluginUtils.getEnchantedBookPrice(item.getStoredEnchantments(), level)) == 0) {
                MessageUtils.sendPrefixMessage(player, "Недостаточно долларов!");
                return;
            }
            player.getInventory().addItem(item.createBuyEnchantedStack(player, level));
        } else {
            if (takeDollars(player, price * item.getData()) == 0) {
                MessageUtils.sendPrefixMessage(player, "Недостаточно долларов!");
                return;
            }
            player.getInventory().addItem(item.createItemStack(player, item.getData()));
        }
    }

}
