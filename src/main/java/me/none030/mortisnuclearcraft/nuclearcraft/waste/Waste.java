package me.none030.mortisnuclearcraft.nuclearcraft.waste;

import me.none030.mortisnuclearcraft.nuclearcraft.radiatiton.RadiationManager;
import me.none030.mortisnuclearcraft.utils.waste.WasteType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Waste {

    private final String id;
    private final ItemStack item;
    private final WasteType type;
    private final double radiation;
    private final int radius;

    public Waste(String id, ItemStack item, WasteType type, double radiation, int radius) {
        this.id = id;
        this.item = item;
        this.type = type;
        this.radiation = radiation;
        this.radius = radius;
        createWaste();
    }

    private void createWaste() {
        WasteData data = new WasteData(item.getItemMeta());
        data.create(id);
        item.setItemMeta(data.getMeta());
    }

    public void giveWaste(Player player) {
        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItemNaturally(player.getLocation(), item);
        } else {
            player.getInventory().addItem(item);
        }
    }

    public void createWasteBlock(Block block) {
        WasteBlockData data = new WasteBlockData(block);
        data.create(id);
    }

    public void giveRadiation(RadiationManager radiationManager, Player player) {
        radiationManager.addRadiation(player, radiation);
    }


    public ItemStack getItem() {
        return item;
    }

    public WasteType getType() {
        return type;
    }

    public double getRadiation() {
        return radiation;
    }

    public int getRadius() {
        return radius;
    }
}
