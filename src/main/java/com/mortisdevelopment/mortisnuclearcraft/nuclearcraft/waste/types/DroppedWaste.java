package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.waste.types;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DroppedWaste extends Waste{

    private final ItemStack item;
    private final int radius;

    public DroppedWaste(String id, double radiation, int radius, ItemStack item) {
        super(id, radiation);
        this.item = item;
        this.radius = radius;
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

    public int getRadius() {
        return radius;
    }
}
