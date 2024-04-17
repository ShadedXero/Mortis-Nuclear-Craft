package com.mortisdevelopment.mortisnuclearcraft.customblocks;

import com.jeff_media.customblockdata.CustomBlockData;
import com.mortisdevelopment.mortisnuclearcraft.MortisNuclearCraft;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class CustomBlock {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final String id;
    private final ItemStack item;
    private final List<String> keys;

    public CustomBlock(String id, ItemStack item, List<String> keys) {
        this.id = id;
        this.item = item;
        this.keys = keys;
    }

    public void giveCustomBlock(Player player) {
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(item.clone());
        }else {
            player.getWorld().dropItemNaturally(player.getLocation(), item.clone());
        }
    }

    public void dropCustomBlock(Location location) {
        location.getWorld().dropItemNaturally(location, item.clone());
    }

    public void createBlock(Block block) {
        if (block == null) {
            return;
        }
        CustomBlockData data = new CustomBlockData(block, plugin);
        for (String key : keys) {
            data.set(new NamespacedKey(plugin, key), PersistentDataType.STRING, key);
        }
    }

    public boolean isItem(ItemStack item) {
        return this.item.isSimilar(item);
    }

    public String getId() {
        return id;
    }

    public ItemStack getItem() {
        return item.clone();
    }

    public List<String> getKeys() {
        return keys;
    }
}
