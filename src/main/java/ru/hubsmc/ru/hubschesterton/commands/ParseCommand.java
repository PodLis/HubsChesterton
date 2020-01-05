package ru.hubsmc.ru.hubschesterton.commands;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import ru.hubsmc.ru.hubschesterton.Permissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static ru.hubsmc.ru.hubschesterton.PluginUtils.getFile;
import static ru.hubsmc.ru.hubschesterton.util.MessageUtils.*;
import static ru.hubsmc.ru.hubschesterton.util.ServerUtils.logConsole;

public class ParseCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            if (command.getName().equalsIgnoreCase("chestpars")) {

                if (!(sender instanceof Player)) {
                    sendMustBePlayerMessage(sender);
                    return true;
                }

                Player player = (Player) sender;

                if (!player.hasPermission(Permissions.PARSE.getPermission())) {
                    sendNoPermMessage(player, label);
                    return true;
                }

                if (args.length == 0) {
                    sendNotEnoughArgsMessage(player, label);
                    return true;
                }

                Block block = player.getTargetBlock(null, 3);

                if (!block.getType().equals(Material.CHEST) && !block.getType().equals(Material.TRAPPED_CHEST)) {
                    sendPrefixMessage(player, "It is not a chest!");
                    return true;
                }

                if (block.getType().equals(Material.TRAPPED_CHEST)) {
                    sendPrefixMessage(player, "OMG! It's a TRAP!");
                }

                sendPrefixMessage(player, "Start parsing...");

                StringBuilder fileName = new StringBuilder();
                for (String arg : args) {
                    fileName.append(arg).append("-");
                }
                fileName.deleteCharAt(fileName.length() - 1);
                File dataFile = getFile(fileName.toString());
                FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

                Chest chest = (Chest) block.getState();
                InventoryHolder holder = chest.getInventory().getHolder();
                Inventory inventory = holder.getInventory();

                for (int i = 0; i < inventory.getSize(); i++) {
                    ItemStack itemStack = inventory.getItem(i);
                    if (itemStack != null && !itemStack.getType().isAir()) {

                        // Type
                        Material material = itemStack.getType();
                        data.set(i + ".type", material.name());

                        // Enchantments
                        for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
                            data.set(i + ".enchantments." + enchantment.getName(), itemStack.getEnchantmentLevel(enchantment));
                        }

                        // Potion effects
                        if (material.equals(Material.POTION) || material.equals(Material.SPLASH_POTION) ||
                                material.equals(Material.LINGERING_POTION) || material.equals(Material.TIPPED_ARROW)) {

                            List<String> customEffects = new ArrayList<>();
                            PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();

                            if (potionMeta.hasCustomEffects()) {

                                // Custom effects
                                for (PotionEffect effect : (potionMeta).getCustomEffects()) {
                                    customEffects.add(effect.getType().getName() + ":" +
                                            effect.getAmplifier() + ":" +
                                            effect.getDuration() + ":" +
                                            effect.hasParticles());
                                }
                                if (customEffects.size() > 0) {
                                    data.set(i + ".custom-potion.name", potionMeta.getDisplayName());
                                    data.set(i + ".custom-potion.color", potionMeta.getColor().toString());
                                    data.set(i + ".custom-potion.effects", customEffects);
                                }

                            } else {
                                // Normal effects
                                data.set(i + ".potion.effect", potionMeta.getBasePotionData().getType().name());
                                data.set(i + ".potion.extended", potionMeta.getBasePotionData().isExtended());
                                data.set(i + ".potion.upgraded", potionMeta.getBasePotionData().isUpgraded());
                            }

                        }

                        // Enchanted book
                        if (material.equals(Material.ENCHANTED_BOOK)) {
                            EnchantmentStorageMeta storageMeta = (EnchantmentStorageMeta) itemStack.getItemMeta();
                            for (Enchantment enchantment : storageMeta.getStoredEnchants().keySet()) {
                                data.set(i + ".storage." + enchantment.getName(), storageMeta.getStoredEnchantLevel(enchantment));
                            }
                        }

                    }
                }
                data.save(dataFile);

                sendPrefixMessage(player, "Parse success!");

                return true;

            }
        } catch (Throwable e) {
            e.printStackTrace();
            logConsole(Level.WARNING, "Some troubles with chestpars.");
        }
        return true;
    }

}
