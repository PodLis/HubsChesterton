package ru.hubsmc.ru.hubschesterton.internal.parser;

import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import ru.hubsmc.ru.hubschesterton.internal.action.ItemAction;
import ru.hubsmc.ru.hubschesterton.internal.action.OpenCategoryItemAction;
import ru.hubsmc.ru.hubschesterton.internal.action.OpenMenuItemAction;
import ru.hubsmc.ru.hubschesterton.internal.action.ReturnItemAction;
import ru.hubsmc.ru.hubschesterton.internal.menu.ChestMenu;

import java.util.HashMap;
import java.util.Map;

public class SubParser {

    static PotionData parsePotionData(ConfigurationSection section) {
        return new PotionData(PotionType.valueOf(section.getString("effect")), section.getBoolean("extended"), section.getBoolean("upgraded"));
    }

    static Map<Enchantment, Integer> parseEnchantments(ConfigurationSection section) {
        Map<Enchantment, Integer> enchantments = new HashMap<>();
        for (String enchantment : section.getKeys(false)) {
            enchantments.put(Enchantment.getByName(enchantment), section.getInt(enchantment));
        }
        return enchantments;
    }

    static Color parseColor(String string) {
        String[] strings = string.split(":");
        switch (strings.length) {
            case 1:
                return Color.fromRGB(Integer.parseInt(strings[0]));
            case 3:
                return Color.fromRGB(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
            default:
                return Color.BLACK;
        }
    }

    static ItemAction parseAction(String string, ChestMenu menu) {
        String[] strings = string.split(":");
        switch (strings[0]) {
            case "open":
                if (strings.length > 1)
                    return new OpenMenuItemAction(menu, strings[1]);
            case "category":
                if (strings.length > 1)
                    return new OpenCategoryItemAction(menu, strings[1]);
            default:
                return new ReturnItemAction(menu);
        }
    }

}
