package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.centrifuge;

import com.mortisdevelopment.mortisnuclearcraft.structures.Structure;
import me.none030.mortishoppers.data.HopperData;
import me.none030.mortishoppers.utils.HopperMode;
import me.none030.mortishoppers.utils.HopperStatus;
import com.mortisdevelopment.mortisnuclearcraft.MortisNuclearCraft;
import com.mortisdevelopment.mortisnuclearcraft.utils.Fuel;
import org.bukkit.Location;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Centrifuge {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final List<Structure> structures;
    private final List<CentrifugeRecipe> recipes;
    private final HashMap<String, CentrifugeRecipe> recipeById;
    private final List<Fuel> fuels;

    public Centrifuge(List<Structure> structures) {
        this.structures = structures;
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

    public Structure getStructure(Location location) {
        for (Structure structure : structures) {
            if (!structure.isStructure(location)) {
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

    public void checkInHoppers(CentrifugeData data, Structure structure) {
        List<Hopper> hoppers = structure.getInHoppers(data.getCore());
        for (Hopper hopper : hoppers) {
            if (isFull(data.getInput1()) && isFull(data.getInput2())) {
                return;
            }else {
                addItems(hopper.getInventory(), data);
            }
        }
    }

    public void checkFuel(CentrifugeData data, Structure structure) {
        List<Hopper> hoppers = structure.getInHoppers(data.getCore());
        for (Hopper hopper : hoppers) {
            if (isFull(data.getFuel())) {
                return;
            }else {
                addFuel(hopper.getInventory(), data);
            }
        }
    }

    public void addItems(Inventory inv, CentrifugeData data) {
        if (inv.isEmpty()) {
            return;
        }
        addInput1(inv, data);
        addInput2(inv, data);
    }

    private void addInput1(Inventory inv, CentrifugeData data) {
        if (isFull(data.getInput1())) {
            return;
        }
        ItemStack input1 = data.getInput1();
        for (ItemStack item : inv.getContents()) {
            if (item == null || item.getType().isAir()) {
                continue;
            }
            if (input1 != null) {
                int space = input1.getMaxStackSize() - input1.getAmount();
                if (!item.isSimilar(input1)) {
                    continue;
                }
                if (isFull(data.getInput1())) {
                    return;
                }
                int amount = item.getAmount();
                if (amount > space) {
                    item.setAmount(amount - space);
                    input1.setAmount(input1.getMaxStackSize());
                    data.setInput1(input1);
                } else {
                    inv.removeItem(item);
                    input1.setAmount(input1.getAmount() + amount);
                    data.setInput1(input1);
                }
            }else {
                data.setInput1(item.clone());
                inv.removeItem(item);
            }
        }
    }

    private void addInput2(Inventory inv, CentrifugeData data) {
        if (isFull(data.getInput2())) {
            return;
        }
        ItemStack input2 = data.getInput2();
        for (ItemStack item : inv.getContents()) {
            if (item == null || item.getType().isAir()) {
                continue;
            }
            if (input2 != null) {
                int space = input2.getMaxStackSize() - input2.getAmount();
                if (!item.isSimilar(input2)) {
                    continue;
                }
                if (isFull(data.getInput2())) {
                    return;
                }
                int amount = item.getAmount();
                if (amount > space) {
                    item.setAmount(amount - space);
                    input2.setAmount(input2.getMaxStackSize());
                    data.setInput2(input2);
                } else {
                    inv.removeItem(item);
                    input2.setAmount(input2.getAmount() + amount);
                    data.setInput2(input2);
                }
            }else {
                data.setInput2(item.clone());
                inv.removeItem(item);
            }
        }
    }

    public void addFuel(Inventory inv, CentrifugeData data) {
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

    private boolean isFull(ItemStack item) {
        if (item == null) {
            return false;
        }
        return item.getAmount() >= item.getMaxStackSize();
    }

    public void checkOutHoppers(CentrifugeData data, Structure structure) {
        List<Hopper> hoppers = structure.getOutHoppers(data.getCore());
        for (Hopper hopper : hoppers) {
            if (new OutputStorage(data).getInventory().isEmpty()) {
                return;
            }else {
                removeItems(hopper.getInventory(), data);
            }
        }
    }

    public void removeItems(Inventory inv, CentrifugeData data) {
        Location location = inv.getLocation();
        if (location == null) {
            return;
        }
        removeOutputs(inv, data, location);
    }

    private void removeOutputs(Inventory inv, CentrifugeData data, Location location) {
        if (inv.firstEmpty() == -1) {
            return;
        }
        OutputStorage storage = new OutputStorage(data);
        for (ItemStack output : storage.getOutputs()) {
            if (inv.firstEmpty() == -1) {
                data.setOutput(storage.getInventory());
                return;
            }
            if (output == null || output.getType().isAir()) {
                continue;
            }
            if (plugin.hasHopper()) {
                HopperData hopperData = new HopperData(location);
                if (hopperData.getStatus().equals(HopperStatus.ENABLED)) {
                    if (hopperData.getMode().equals(HopperMode.WHITELIST)) {
                        if (!hopperData.canGoThrough(output)) {
                            continue;
                        }
                    } else {
                        if (hopperData.canGoThrough(output)) {
                            continue;
                        }
                    }
                }
            }
            inv.addItem(output);
            storage.getInventory().removeItem(output);
        }
        data.setOutput(storage.getInventory());
    }

    public List<Structure> getStructures() {
        return structures;
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
