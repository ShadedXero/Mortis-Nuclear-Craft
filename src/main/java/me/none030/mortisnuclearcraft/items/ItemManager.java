package me.none030.mortisnuclearcraft.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemManager {

    private final List<ItemStack> items;
    private final HashMap<String, ItemStack> itemById;

    public ItemManager() {
        this.items = new ArrayList<>();
        this.itemById = new HashMap<>();
    }

    public void giveItem(Player player, ItemStack item) {
        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItemNaturally(player.getLocation(), item);
        } else {
            player.getInventory().addItem(item);
        }
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public HashMap<String, ItemStack> getItemById() {
        return itemById;
    }
}
