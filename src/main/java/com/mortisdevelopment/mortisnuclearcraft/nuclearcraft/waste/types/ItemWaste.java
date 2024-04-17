package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.waste.types;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemWaste extends Waste {

    private final ItemStack item;

    public ItemWaste(String id, double radiation, ItemStack item) {
        super(id, radiation);
        this.item = item;
    }

    @Override
    public void giveWaste(Player player) {
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(item.clone());
        }else {
            player.getWorld().dropItemNaturally(player.getLocation(), item.clone());
        }
    }

    @Override
    public boolean isWaste(ItemStack item) {
        return this.item.isSimilar(item);
    }

    public ItemStack getItem() {
        return item.clone();
    }
}
