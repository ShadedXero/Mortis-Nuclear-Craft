package me.none030.mortisnuclearcraft.nuclearcraft.centrifuge;

import me.none030.mortishoppers.data.HopperData;
import me.none030.mortishoppers.utils.HopperMode;
import me.none030.mortishoppers.utils.HopperStatus;
import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.structures.Structure;
import me.none030.mortisnuclearcraft.utils.Fuel;
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

    public void checkOutHoppers(CentrifugeData data, Structure structure) {
        List<Hopper> hoppers = structure.getOutHoppers(data.getCore());
        for (Hopper hopper : hoppers) {
            if (data.getOutput1() == null && data.getOutput2() == null) {
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
        HopperData hopperData = new HopperData(location);
        removeOutput1(inv, data, hopperData);
        removeOutput2(inv, data, hopperData);
    }

    private void removeOutput1(Inventory inv, CentrifugeData data, HopperData hopperData) {
        if (inv.firstEmpty() == -1) {
            return;
        }
        ItemStack output1 = data.getOutput1();
        if (output1 != null) {
            if (hopperData.getStatus().equals(HopperStatus.ENABLED)) {
                if (hopperData.getMode().equals(HopperMode.WHITELIST)) {
                    if (!hopperData.canGoThrough(output1)) {
                        return;
                    }
                } else {
                    if (hopperData.canGoThrough(output1)) {
                        return;
                    }
                }
            }
            inv.addItem(output1);
            data.setOutput1(null);
        }
    }

    private void removeOutput2(Inventory inv, CentrifugeData data, HopperData hopperData) {
        if (inv.firstEmpty() == -1) {
            return;
        }
        ItemStack output2 = data.getOutput2();
        if (output2 != null) {
            if (plugin.hasHopper()) {
                if (hopperData.getStatus().equals(HopperStatus.ENABLED)) {
                    if (hopperData.getMode().equals(HopperMode.WHITELIST)) {
                        if (!hopperData.canGoThrough(output2)) {
                            return;
                        }
                    } else {
                        if (hopperData.canGoThrough(output2)) {
                            return;
                        }
                    }
                }
            }
            inv.addItem(output2);
            data.setOutput2(null);
        }
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
