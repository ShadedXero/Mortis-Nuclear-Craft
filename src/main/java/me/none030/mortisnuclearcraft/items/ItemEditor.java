package me.none030.mortisnuclearcraft.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static me.none030.mortisnuclearcraft.utils.MessageUtils.colorMessage;

public class ItemEditor {

    private final ItemStack item;

    public ItemEditor(ItemStack item) {
        this.item = item;
    }

    public void setPlaceholder(String placeholder, String value) {
        ItemMeta meta = item.getItemMeta();
        Component name = meta.displayName();
        if (name != null) {
            meta.displayName(Component.text(colorMessage(LegacyComponentSerializer.legacyAmpersand().serialize(name).replace(placeholder, value))));
        }
        List<Component> lore = meta.lore();
        if (lore != null) {
            lore.replaceAll(component -> Component.text(colorMessage(LegacyComponentSerializer.legacyAmpersand().serialize(component).replace(placeholder, value))));
            meta.lore(lore);
        }
        item.setItemMeta(meta);
    }

    public ItemStack getItem() {
        return item;
    }
}
