package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.centrifuge;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class OutputStorage implements InventoryHolder {

    private final CentrifugeData data;
    private final Inventory menu;

    public OutputStorage(CentrifugeData data) {
        this.data = data;
        this.menu = getMenu();
    }

    private Inventory getMenu() {
        Inventory menu = data.getOutput(this);
        if (menu != null) {
            return menu;
        } else {
            Inventory inv = Bukkit.createInventory(this, 9);
            data.setOutput(inv);
            return inv;
        }
    }

    public boolean canAdd() {
        int size = 0;
        for (ItemStack item : menu.getContents()) {
            if (item == null || item.getType().isAir()) {
                continue;
            }
            size++;
        }
        return size < 6;
    }

    public ItemStack[] getOutputs() {
        ItemStack[] items = new ItemStack[6];
        for (int i = 0; i < 6; i++) {
            ItemStack item = menu.getItem(i);
            if (item == null || item.getType().isAir()) {
                items[i] = null;
                continue;
            }
            items[i] = item;
        }
        return items;
    }

    public ItemStack getOutput(int index) {
        if (index < 0 || index >= 6) {
            return null;
        }
        ItemStack item = menu.getItem(index);
        if (item == null || item.getType().isAir()) {
            return null;
        }
        return item;
    }

    public CentrifugeData getData() {
        return data;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return menu;
    }
}
