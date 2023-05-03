package me.none030.mortisnuclearcraft.nuclearcraft.drops;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Random;

public class Drop {

    private final String id;
    private final ItemStack item;
    private final HashMap<Material, Double> blocks;
    private final HashMap<EntityType, Double> mobs;

    public Drop(String id, ItemStack item, HashMap<Material, Double> blocks, HashMap<EntityType, Double> mobs) {
        this.id = id;
        this.item = item;
        this.blocks = blocks;
        this.mobs = mobs;
    }

    public void drop(Location loc, Material block) {
        if (blocks == null) {
            return;
        }
        Double chance = blocks.get(block);
        if (chance == null) {
            return;
        }
        if (!chance(chance)) {
            return;
        }
        loc.getWorld().dropItemNaturally(loc, item);
    }

    public void drop(Location loc, EntityType mob) {
        if (mobs == null) {
            return;
        }
        Double chance = mobs.get(mob);
        if (chance == null) {
            return;
        }
        if (!chance(chance)) {
            return;
        }
        loc.getWorld().dropItemNaturally(loc, item);
    }

    private boolean chance(double chance) {
        Random random = new Random();
        if (chance < 0.001) {
            chance += 1;
        }
        if (chance < 1) {
            chance += chance * 100;
        }
        double number = random.nextDouble(0, 100);
        return number <= chance;
    }

    public String getId() {
        return id;
    }

    public ItemStack getItem() {
        return item;
    }

    public HashMap<Material, Double> getBlocks() {
        return blocks;
    }

    public HashMap<EntityType, Double> getMobs() {
        return mobs;
    }
}
