package me.none030.mortisnuclearcraft.nuclearcraft.reactor;

import me.none030.mortisnuclearcraft.nuclearcraft.bombs.Bomb;
import me.none030.mortisnuclearcraft.structures.Structure;
import me.none030.mortisnuclearcraft.utils.Fuel;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.block.data.Levelled;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Reactor extends Bomb {

    private final List<Structure> structures;
    private final List<ReactorRecipe> recipes;
    private final HashMap<String, ReactorRecipe> recipeById;
    private final List<Fuel> fuels;
    private final int strength;
    private final long duration;
    private final double radiation;
    private final boolean vehicles;
    private final boolean drain;

    public Reactor(int radius, List<Structure> structures, int strength, long duration, double radiation, boolean vehicles, boolean drain) {
        super(radius);
        this.structures = structures;
        this.recipes = new ArrayList<>();
        this.recipeById = new HashMap<>();
        this.fuels = new ArrayList<>();
        this.strength = strength;
        this.duration = duration;
        this.radiation = radiation;
        this.vehicles = vehicles;
        this.drain = drain;
    }

    public void explode(ReactorManager reactorManager, Location loc) {
        explode(loc, strength, vehicles, drain);
        radiate(reactorManager.getRadiationManager(), loc, duration, radiation);
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

    public int getWater(String structureId, Location core) {
        Structure structure = getStructure(structureId);
        if (structure == null) {
            return 0;
        }
        int water = 0;
        List<Block> blocks = new ArrayList<>(structure.getSpecificBlocks(core, Material.WATER_CAULDRON));
        if (blocks.size() == 0) {
            return 0;
        }
        for (Block block : blocks) {
            Levelled data = (Levelled) block.getBlockData();
            water = water + data.getLevel();
        }
        return water;
    }

    public Structure getStructure(Location location) {
        for (Structure structure : structures) {
            if (!structure.isStructure(location, List.of(Material.CAULDRON, Material.WATER_CAULDRON))) {
                continue;
            }
            return structure;
        }
        return null;
    }

    public Structure getStructure(String structureId) {
        for (Structure structure : structures) {
            if (structure.getId().equalsIgnoreCase(structureId)) {
                return structure;
            }
        }
        return null;
    }

    public void checkFuel(ReactorData data, Structure structure) {
        List<Hopper> hoppers = structure.getInHoppers(data.getCore());
        for (Hopper hopper : hoppers) {
            if (isFull(data.getFuel())) {
                return;
            }else {
                addFuel(hopper.getInventory(), data);
            }
        }
    }

    public void checkInHoppers(ReactorData data, Structure structure) {
        List<Hopper> hoppers = structure.getInHoppers(data.getCore());
        for (Hopper hopper : hoppers) {
            if (isFull(data.getInput())) {
                return;
            }else {
                addItem(hopper.getInventory(), data);
            }
        }
    }

    public void checkOutHoppers(ReactorData data, Structure structure) {
        List<Hopper> hoppers = structure.getOutHoppers(data.getCore());
        for (Hopper hopper : hoppers) {
            if (data.getOutput() == null && data.getWaste() == null) {
                return;
            }else {
                removeItems(hopper.getInventory(), data);
            }
        }
    }

    public void addFuel(Inventory inv, ReactorData data) {
        if (inv.isEmpty()) {
            return;
        }
        ItemStack fuelItem = data.getFuel();
        if (fuelItem == null || fuelItem.getType().isAir()) {
            for (ItemStack item : inv.getContents()) {
                if (item == null || item.getType().isAir()) {
                    continue;
                }
                Fuel fuel = getFuel(item);
                if (fuel == null) {
                    continue;
                }
                inv.removeItem(item);
                data.setFuel(item);
                return;
            }
        }else {
            if (isFull(fuelItem)) {
                return;
            }
            Fuel fuel = getFuel(fuelItem);
            if (fuel == null) {
                return;
            }
            for (ItemStack item : inv.getContents()) {
                if (item == null || item.getType().isAir()|| !item.isSimilar(fuelItem)) {
                    continue;
                }
                int itemAmount = item.getAmount();
                int fuelItemAmount = fuelItem.getAmount();
                int maxAmount = fuelItem.getMaxStackSize();
                int space = maxAmount - fuelItemAmount;
                if (itemAmount > space) {
                    item.setAmount(item.getAmount() - space);
                    ItemStack cloned = item.clone();
                    cloned.setAmount(maxAmount);
                    data.setFuel(cloned);
                }else {
                    inv.removeItem(item);
                    item.setAmount(fuelItemAmount + itemAmount);
                    data.setFuel(item);
                }
                return;
            }
        }
    }

    public void removeItems(Inventory inv, ReactorData data) {
        if (inv.firstEmpty() == -1) {
            return;
        }
        ItemStack output = data.getOutput();
        if (output != null) {
            inv.addItem(output);
            data.setOutput(null);
        }
        if (inv.firstEmpty() == -1) {
            return;
        }
        ItemStack waste = data.getWaste();
        if (waste != null) {
            inv.addItem(waste);
            data.setWaste(null);
        }
    }

    public void addItem(Inventory inv, ReactorData data) {
        if (isFull(data.getInput())) {
            return;
        }
        ItemStack input = data.getInput();
        for (ItemStack item : inv.getContents()) {
            if (item == null || item.getType().isAir()) {
                continue;
            }
            if (input != null) {
                int space = input.getMaxStackSize() - input.getAmount();
                if (!item.isSimilar(input)) {
                    continue;
                }
                if (isFull(data.getInput())) {
                    return;
                }
                int amount = item.getAmount();
                if (amount > space) {
                    item.setAmount(amount - space);
                    input.setAmount(input.getMaxStackSize());
                    data.setInput(input);
                } else {
                    inv.removeItem(item);
                    input.setAmount(input.getAmount() + amount);
                    data.setInput(input);
                }
            }else {
                inv.removeItem(item);
                data.setInput(item);
            }
        }
    }

    private boolean isFull(ItemStack item) {
        if (item == null) {
            return false;
        }
        return item.getAmount() >= item.getMaxStackSize();
    }

    public List<Structure> getStructures() {
        return structures;
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

    public int getStrength() {
        return strength;
    }

    public long getDuration() {
        return duration;
    }

    public double getRadiation() {
        return radiation;
    }

    public boolean isVehicles() {
        return vehicles;
    }

    public boolean isDrain() {
        return drain;
    }
}
