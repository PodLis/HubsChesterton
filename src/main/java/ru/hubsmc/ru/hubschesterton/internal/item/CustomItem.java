package ru.hubsmc.ru.hubschesterton.internal.item;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.List;

import static ru.hubsmc.ru.hubschesterton.util.StringUtils.replaceColor;

public class CustomItem extends ChestertonItem {

    private String name;
    private List<String> lore;
    private boolean enchanted;
    private Color color;

    public CustomItem(Material material) {
        super(material);
        this.enchanted = false;
    }

    public void setName(String name) {
        this.name = replaceColor(name);
    }

    public String getName() {
        return name;
    }

    public void setLore(List<String> lore) {
        this.lore = replaceColor(lore);
    }

    public void setEnchanted(boolean enchanted) {
        this.enchanted = enchanted;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    protected ItemStack setItemData(ItemStack itemStack, Player player) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return itemStack;
        }

        if (name != null) {
            meta.setDisplayName(name);
        }

        if (lore != null) {
            meta.setLore(lore);
        }

        meta.addItemFlags(
                ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_POTION_EFFECTS
        );

        if (meta instanceof PotionMeta && color != null) {

            PotionMeta potionMeta = (PotionMeta) meta;
            potionMeta.setColor(color);
            itemStack.setItemMeta(potionMeta);

        } else if (meta instanceof LeatherArmorMeta && color != null) {

            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;
            leatherArmorMeta.setColor(color);
            itemStack.setItemMeta(leatherArmorMeta);

        } else {

            itemStack.setItemMeta(meta);

        }

        if (enchanted) {
            itemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
        }

        return itemStack;
    }
}
