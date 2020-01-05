package ru.hubsmc.ru.hubschesterton.internal.parser;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.hubsmc.ru.hubschesterton.PluginUtils;
import ru.hubsmc.ru.hubschesterton.internal.item.ShopItem;
import ru.hubsmc.ru.hubschesterton.internal.menu.ChestMenu;
import ru.hubsmc.ru.hubschesterton.internal.menu.ShopCategoryMenu;

import static ru.hubsmc.ru.hubschesterton.PluginUtils.*;

public class MenuParser {

    public static ChestMenu parseChestMenu(String fileName) {
        Configuration configuration = YamlConfiguration.loadConfiguration(PluginUtils.getFile(fileName));
        ChestMenu chestMenu;
        if (configuration.getString("title") == null) {
            chestMenu = new ChestMenu("TITLE NOT FOUND", 54);
        } else {
            chestMenu = new ChestMenu(configuration.getString("title"), 54);
        }

        for (String slot : configuration.getKeys(false)) {
            if (slot.equals("title")) {
                continue;
            }
            int iSlot = Integer.parseInt(slot);
            if (iSlot >= 0 && iSlot < 54) {
                chestMenu.setItem(iSlot, ItemParser.parseCustomItem(configuration.getConfigurationSection(slot), chestMenu));
            }
        }

        return chestMenu;
    }

    public static ShopCategoryMenu parseCategoryMenu(String categoryName, String title) {
        int n = 1;
        String tFileName = "shop-" + categoryName + "-" + n;
        while (fileExists(tFileName)) {
            n++;
            tFileName = "shop-" + categoryName + "-" + n;
        }

        return parseCategoryMenu(categoryName, title, 1, n-1);
    }

    public static ShopCategoryMenu parseCategoryMenu(String categoryName, String title, int page, int maxPage) {
        if (page > maxPage || page < 1) {
            return null;
        }
        String fileName = "shop-" + categoryName + "-" + page;

        Configuration configuration = YamlConfiguration.loadConfiguration(PluginUtils.getFile(fileName));
        ShopCategoryMenu shopCategoryMenu = new ShopCategoryMenu(categoryName, title, page, maxPage);

        for (String slot : configuration.getKeys(false)) {
            int iSlot = Integer.parseInt(slot);
            if (iSlot >= 0 && iSlot < 54) {

                ShopItem shopItem = ItemParser.parseShopItem(configuration.getConfigurationSection(slot));

                shopItem.prepareToCategoryMenu(shopCategoryMenu);

                shopCategoryMenu.setItem(iSlot, shopItem);

            }
        }

        return shopCategoryMenu;
    }

}
