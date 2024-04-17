package com.mortisdevelopment.mortisnuclearcraft.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemEditor {

    private final ItemStack item;

    public ItemEditor(ItemStack item) {
        this.item = item;
    }

    public void setCustomModelData(int customModelData) {
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(customModelData);
        item.setItemMeta(meta);
    }

    public int getCustomModelData() {
        ItemMeta meta = item.getItemMeta();
        return meta.getCustomModelData();
    }

    public void setName(String name) {
        ItemMeta meta = item.getItemMeta();
        MessageUtils editor = new MessageUtils(name);
        editor.color();
        meta.displayName(Component.text(editor.getMessage()));
        item.setItemMeta(meta);
    }

    public String getName() {
        ItemMeta meta = item.getItemMeta();
        Component name = meta.displayName();
        if (name == null) {
            return null;
        }
        return LegacyComponentSerializer.legacyAmpersand().serialize(name);
    }

    public void setLore(List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        List<Component> components = new ArrayList<>();
        for (String line : lore) {
            MessageUtils editor = new MessageUtils(line);
            editor.color();
            components.add(Component.text(editor.getMessage()));
        }
        meta.lore(components);
        item.setItemMeta(meta);
    }

    public void addLore(String line) {
        ItemMeta meta = item.getItemMeta();
        List<Component> components = meta.lore();
        if (components == null || components.size() == 0) {
            components = new ArrayList<>();
        }
        MessageUtils editor = new MessageUtils(line);
        editor.color();
        components.add(Component.text(editor.getMessage()));
        meta.lore(components);
        item.setItemMeta(meta);
    }

    public void removeLore(String line) {
        ItemMeta meta = item.getItemMeta();
        List<Component> components = meta.lore();
        if (components == null || components.size() == 0) {
            return;
        }
        components.remove(Component.text(line));
        meta.lore(components);
        item.setItemMeta(meta);
    }

    public void removeLore(Component line) {
        ItemMeta meta = item.getItemMeta();
        List<Component> components = meta.lore();
        if (components == null || components.size() == 0) {
            return;
        }
        components.remove(line);
        meta.lore(components);
        item.setItemMeta(meta);
    }

    public void removeLore(int line) {
        ItemMeta meta = item.getItemMeta();
        List<Component> components = meta.lore();
        if (components == null || components.size() == 0) {
            return;
        }
        components.remove(line);
        meta.lore(components);
        item.setItemMeta(meta);
    }

    public List<String> getLore() {
        ItemMeta meta = item.getItemMeta();
        List<Component> components = meta.lore();
        if (components == null || components.size() == 0) {
            return null;
        }
        List<String> lore = new ArrayList<>();
        for (Component component : components) {
            String line = LegacyComponentSerializer.legacyAmpersand().serialize(component);
            lore.add(line);
        }
        return lore;
    }

    public void addEnchants(HashMap<Enchantment, Integer> enchantments) {
        ItemMeta meta = item.getItemMeta();
        for (Enchantment enchant : enchantments.keySet()) {
            int level = enchantments.get(enchant);
            meta.addEnchant(enchant, level, true);
        }
        item.setItemMeta(meta);
    }

    public void addEnchant(Enchantment enchantment, int level) {
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(enchantment, level, true);
        item.setItemMeta(meta);
    }

    public void removeEnchant(Enchantment enchantment) {
        ItemMeta meta = item.getItemMeta();
        meta.removeEnchant(enchantment);
        item.setItemMeta(meta);
    }

    public Map<Enchantment, Integer> getEnchants() {
        ItemMeta meta = item.getItemMeta();
        return meta.getEnchants();
    }

    public void addFlags(List<ItemFlag> flags) {
        ItemMeta meta = item.getItemMeta();
        for (ItemFlag flag : flags) {
            meta.addItemFlags(flag);
        }
        item.setItemMeta(meta);
    }

    public void addFlag(ItemFlag flag) {
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(flag);
        item.setItemMeta(meta);
    }

    public void removeFlag(ItemFlag flag) {
        ItemMeta meta = item.getItemMeta();
        meta.removeItemFlags(flag);
        item.setItemMeta(meta);
    }

    public List<ItemFlag> getFlags() {
        ItemMeta meta = item.getItemMeta();
        List<ItemFlag> flags = new ArrayList<>(meta.getItemFlags());
        if (flags.size() == 0) {
            return null;
        }
        return flags;
    }

    public void setPlaceholder(String placeholder, String value) {
        ItemMeta meta = item.getItemMeta();
        Component name = meta.displayName();
        if (name != null) {
            MessageUtils editor = new MessageUtils(LegacyComponentSerializer.legacyAmpersand().serialize(name).replace(placeholder, value));
            editor.color();
            meta.displayName(Component.text(editor.getMessage()));
        }
        List<Component> lore = meta.lore();
        if (lore != null) {
            for (int i = 0; i < lore.size(); i++) {
                Component component = lore.get(i);
                MessageUtils editor = new MessageUtils(LegacyComponentSerializer.legacyAmpersand().serialize(component).replace(placeholder, value));
                editor.color();
                lore.set(i, Component.text(editor.getMessage()));
            }
            meta.lore(lore);
        }
        item.setItemMeta(meta);
    }

    public ItemStack getItem() {
        return item;
    }
}
