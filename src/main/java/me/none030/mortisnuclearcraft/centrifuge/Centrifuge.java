package me.none030.mortisnuclearcraft.centrifuge;

import me.none030.mortisnuclearcraft.structures.Structure;
import me.none030.mortisnuclearcraft.utils.Fuel;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Centrifuge {

    private final Structure structure;
    private final List<CentrifugeRecipe> recipes;
    private final HashMap<String, CentrifugeRecipe> recipeById;
    private final List<Fuel> fuels;

    public Centrifuge(Structure structure) {
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

    public Structure getStructure() {
        return structure;
    }

    public List<CentrifugeRecipe> getRecipes() {
        return recipes;
    }

    public HashMap<String, CentrifugeRecipe> getRecipeById() {
        return recipeById;
    }

    public List<Fuel> getFuels() {
        return fuels;
    }
}
