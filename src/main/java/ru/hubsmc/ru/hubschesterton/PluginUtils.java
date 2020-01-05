package ru.hubsmc.ru.hubschesterton;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import ru.hubsmc.ru.hubschesterton.internal.ChestertonInventoryHolder;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import static ru.hubsmc.ru.hubschesterton.util.ServerUtils.logConsole;

public class PluginUtils {

    private static final int GREAT_PRICE = 1000000;

    private static FileConfiguration configuration;
    private static File configFile;
    private static File pricesFile;
    private static Map<Material, Integer> pricesMap, sellMap;
    private static Map<Enchantment, Integer> enchantmentsPriceMap;
    private static Map<PotionType, Integer> potionsPriceMap;

    public static String getVersion() {
        return HubsChesterton.plugin.getDescription().getVersion();
    }

    public static void reloadConfig() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getInventory().getHolder() instanceof ChestertonInventoryHolder) {
                player.closeInventory();
            }
        }
        loadConfiguration();
    }

    public static File getFile(String name) {
        return new File(getMenuFolder(), name + ".yml");
    }

    public static boolean fileExists(String name) {
        return (new File(getMenuFolder(), name + ".yml")).exists();
    }

    private static File getFolder() {
        File folder = HubsChesterton.plugin.getDataFolder();
        if (!folder.exists() && folder.mkdir()) {
            logConsole("Folder recreated");
        }
        return folder;
    }

    private static File getMenuFolder() {
        File folder = new File(HubsChesterton.plugin.getDataFolder(), "menu");
        if (!folder.exists() && folder.mkdir()) {
            logConsole("Menu folder recreated");
        }
        return folder;
    }

    static void loadConfiguration() {
        try {
            if (configFile == null) {
                configFile = new File(getFolder(), "config.yml");
            }
            if (pricesFile == null) {
                pricesFile = new File(getFolder(), "prices.yml");
            }

            if (configFile.exists()) {
                configuration = YamlConfiguration.loadConfiguration(configFile);
                configuration.load(configFile);
                HubsChesterton.plugin.reloadConfig();
            } else {
                HubsChesterton.plugin.saveResource("config.yml", false);
                configuration = YamlConfiguration.loadConfiguration(configFile);
                logConsole("The 'config.yml' file successfully created!");
            }

            if (!pricesFile.exists()) {
                HubsChesterton.plugin.saveResource("prices.yml", false);
                logConsole("The 'prices.yml' file successfully created!");
            }

            pricesMap = new HashMap<>();
            FileConfiguration priceConfiguration = YamlConfiguration.loadConfiguration(pricesFile);
            for (String key : priceConfiguration.getKeys(false)) {
                int price = priceConfiguration.getInt(key);
                pricesMap.put(Material.getMaterial(key.toUpperCase()), price > 0 ? price : GREAT_PRICE);
            }

            enchantmentsPriceMap = new HashMap<>();
            for (String key : configuration.getConfigurationSection("shop.enchantments").getKeys(false)) {
                enchantmentsPriceMap.put(Enchantment.getByName(key), configuration.getInt("shop.enchantments." + key));
            }

            potionsPriceMap = new HashMap<>();
            for (String key : configuration.getConfigurationSection("shop.potions").getKeys(false)) {
                potionsPriceMap.put(PotionType.valueOf(key), configuration.getInt("shop.potions." + key));
            }

            sellMap = new HashMap<>();
            for (String key : configuration.getConfigurationSection("shop.sell-cost").getKeys(false)) {
                sellMap.put(Material.getMaterial(key.toUpperCase()), configuration.getInt("shop.sell-cost." + key));
            }

        } catch (Throwable e) {
            e.printStackTrace();
            logConsole(Level.WARNING, "There was a file error.");
        }

    }

    public static String getConfigurationString(String path) {
        return configuration.getString(path);
    }

    public static List<String> getConfigurationStringList(String path) {
        return configuration.getStringList(path);
    }


    public static int getItemPrice(Material material) {
        if (pricesMap.containsKey(material)) {
            return pricesMap.get(material);
        }
        return GREAT_PRICE;
    }

    public static int getEnchantmentPrice(Enchantment enchantment) {
        if (enchantmentsPriceMap.containsKey(enchantment)) {
            return enchantmentsPriceMap.get(enchantment);
        }
        return GREAT_PRICE;
    }

    public static int getEffectPrice(PotionType potionType) {
        if (potionsPriceMap.containsKey(potionType)) {
            return potionsPriceMap.get(potionType);
        }
        return GREAT_PRICE;
    }

    public static int getBrewPrice(Material material) {
        return getItemPrice(material) + configuration.getInt("shop.brew-addition");
    }


    public static int getPotionPrice(PotionData potionData, Material material) {
        int startPrice = getEffectPrice(potionData.getType()) + getItemPrice(Material.GLASS_BOTTLE);

        if (potionData.isExtended()) {
            startPrice += getBrewPrice(Material.REDSTONE);
        }
        if (potionData.isUpgraded()) {
            startPrice += getBrewPrice(Material.GLOWSTONE_DUST);
        }

        switch (material) {
            case POTION:
                return startPrice;
            case LINGERING_POTION:
                return startPrice + getBrewPrice(Material.DRAGON_BREATH);
            case SPLASH_POTION:
                return startPrice + getBrewPrice(Material.GUNPOWDER);
            default:
                return GREAT_PRICE;
        }
    }

    public static int getArrowPrice(PotionData potionData) {
        return getPotionPrice(potionData, Material.LINGERING_POTION) / 8 + getItemPrice(Material.ARROW);
    }

    public static int getEnchantedBookPrice(Map<Enchantment, Integer> enchantments) {
        int startPrice = getItemPrice(Material.BOOK), tPrice;
        for (Map.Entry<Enchantment, Integer> enchantment : enchantments.entrySet()) {
            tPrice = getEnchantmentPrice(enchantment.getKey());
            for (int i = 2; i <= enchantment.getValue(); i++) {
                tPrice += tPrice + (Math.pow(2, i-1) + i - 2)*configuration.getInt("shop.exp-cost");
            }
            startPrice += tPrice;
        }
        return startPrice;
    }

    public static int getEnchantedBookPrice(Map<Enchantment, Integer> enchantments, int level) {
        int startPrice = getItemPrice(Material.BOOK), tPrice;
        for (Map.Entry<Enchantment, Integer> enchantment : enchantments.entrySet()) {
            tPrice = getEnchantmentPrice(enchantment.getKey());
            for (int i = 2; i <= level; i++) {
                tPrice += tPrice + (Math.pow(2, i - 1) + i - 2) * configuration.getInt("shop.exp-cost");
            }
            startPrice += tPrice;
        }
        return startPrice;
    }

    public static int getSellPrice(Material material) {
        if (sellMap.containsKey(material)) {
            return sellMap.get(material);
        }
        return 0;
    }

    public static boolean isSold(Material material) {
        return sellMap.containsKey(material);
    }

    public static Set<Material> getSellSet() {
        return sellMap.keySet();
    }

}
