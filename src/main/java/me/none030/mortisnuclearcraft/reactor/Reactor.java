package me.none030.mortisnuclearcraft.reactor;

import me.none030.mortisnuclearcraft.structures.Structure;
import me.none030.mortisnuclearcraft.utils.Fuel;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Reactor {

    private final Structure structure;
    private final List<ReactorRecipe> recipes;
    private final HashMap<String, ReactorRecipe> recipeById;
    private final List<Fuel> fuels;

    public Reactor(Structure structure) {
        this.structure = structure;
        this.recipes = new ArrayList<>();
        this.recipeById = new HashMap<>();
        this.fuels = new ArrayList<>();
    }

    public int getFuelPower(ItemStack item) {
        if (item == null) {
            return 0;
        }
        int amount = item.getAmount();
        for (Fuel fuel : fuels) {
            if (!fuel.isFuel(item)) {
                continue;
            }
            return fuel.calculatePower(amount);
        }
        return 0;
    }

    public Fuel getFuel(ItemStack item) {
        if (item == null) {
            return null;
        }
        for (Fuel fuel : fuels) {
            if (fuel.isFuel(item)) {
                return fuel;
            }
        }
        return null;
    }

    public int getWater(Location core) {
        int water = 0;
        List<Block> blocks = new ArrayList<>(structure.getBlocksWithType(core, Material.WATER_CAULDRON));
        if (blocks.size() == 0) {
            return 0;
        }
        for (Block block : blocks) {
            Levelled data = (Levelled) block.getBlockData();
            water = water + data.getLevel();
        }
        return water;
    }

    public Structure getStructure() {
        return structure;
    }

    public List<ReactorRecipe> getRecipes() {
        return recipes;
    }

    public HashMap<String, ReactorRecipe> getRecipeById() {
        return recipeById;
    }

    public List<Fuel> getFuels() {
        return fuels;
    }
}
