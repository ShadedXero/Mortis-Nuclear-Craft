package me.none030.mortisnuclearcraft.nuclearcraft.reactor;

import me.none030.mortisnuclearcraft.structures.Structure;
import me.none030.mortisnuclearcraft.utils.Fuel;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ReactorRecipe {

    private final String id;
    private final ItemStack input;
    private final ItemStack output;
    private final ItemStack waste;
    private final boolean specificFuel;
    private final List<ItemStack> fuels;
    private final long duration;
    private final int fuelPower;
    private final int water;

    public ReactorRecipe(String id, ItemStack input, ItemStack output, ItemStack waste, boolean specificFuel, List<ItemStack> fuels, long duration, int fuelPower, int water) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.waste = waste;
        this.specificFuel = specificFuel;
        this.fuels = fuels;
        this.duration = duration;
        this.fuelPower = fuelPower;
        this.water = water;
    }

    public boolean isRecipe(ItemStack input, ItemStack output, ItemStack waste) {
        if (input == null) {
            return false;
        }
        if (!this.input.isSimilar(input)) {
            return false;
        }
        if (input.getAmount() < this.input.getAmount()) {
            return false;
        }
        if (output != null) {
            if (!output.isSimilar(this.output)) {
                return false;
            }
            if (output.getAmount() >= output.getMaxStackSize()) {
                return false;
            }
            int amount = output.getAmount() + this.output.getAmount();
            return amount < this.output.getMaxStackSize();
        }
        if (waste != null) {
            if (!waste.isSimilar(this.waste)) {
                return false;
            }
            if (waste.getAmount() >= waste.getMaxStackSize()) {
                return false;
            }
            int amount = waste.getAmount() + this.waste.getAmount();
            return amount < this.waste.getMaxStackSize();
        }
        return true;
    }

    public boolean hasEnoughFuel(int power) {
        return power >= fuelPower;
    }

    public boolean hasEnoughWater(int water) {
        return water >= this.water;
    }

    public void endProcess(ReactorData data) {
        ItemStack output = data.getOutput();
        if (output == null) {
            data.setOutput(this.output.clone());
        }else {
            output.setAmount(output.getAmount() + this.output.getAmount());
            data.setOutput(output);
        }
        ItemStack waste = data.getWaste();
        if (waste == null) {
            data.setWaste(this.waste.clone());
        }else {
            waste.setAmount(waste.getAmount() + this.waste.getAmount());
            data.setWaste(waste);
        }
        data.setProcess(null);
        data.setTimer(-1);
    }

    public void process(ReactorManager reactorManager, ReactorData data, Structure structure) {
        if (!isRecipe(data.getInput(), data.getOutput(), data.getWaste())) {
            return;
        }
        if (specificFuel) {
            Fuel fuel = reactorManager.getReactor().getFuel(data.getFuel());
            if (fuel == null) {
                return;
            }
            boolean isFuel = false;
            for (ItemStack item : getFuels()) {
                if (fuel.isFuel(item)) {
                    isFuel = true;
                }
            }
            if (!isFuel) {
                return;
            }
        }
        int power = reactorManager.getReactor().getFuelPower(data.getFuel());
        if (!hasEnoughFuel(power)) {
            return;
        }
        int water = reactorManager.getReactor().getWater(data.getStructureId(), data.getCore());
        if (!hasEnoughWater(water)) {
            structure.destroy(data.getCore());
            reactorManager.getReactor().explode(reactorManager.getRadiationManager(), data.getCore());
            data.empty();
            reactorManager.delete(data);
            return;
        }
        int inputAmount = Math.max(data.getInput().getAmount(), this.input.getAmount()) - Math.min(data.getInput().getAmount(), this.input.getAmount());
        if (inputAmount <= 0) {
            data.setInput( null);
        }else {
            ItemStack input = data.getInput();
            input.setAmount(inputAmount);
            data.setInput(input);
        }
        Fuel fuelItem = reactorManager.getReactor().getFuel(data.getFuel());
        int fuelAmount = Math.max(fuelItem.getAmount(fuelPower), data.getFuel().getAmount()) - Math.min(fuelItem.getAmount(fuelPower), data.getFuel().getAmount());
        if (fuelAmount <= 0) {
            data.setFuel(null);
        }else {
            ItemStack fuel = data.getFuel();
            fuel.setAmount(fuelAmount);
            data.setFuel(fuel);
        }
        removeWater(data, structure, this.water);
        data.setProcess(id);
        data.setTimer(duration);
    }

    private void removeWater(ReactorData reactorData, Structure structure, int water) {
        List<Block> blocks = new ArrayList<>(structure.getSpecificBlocks(reactorData.getCore(), Material.WATER_CAULDRON));
        if (blocks.size() == 0) {
            return;
        }
        for (Block block : blocks) {
            if (water <= 0) {
                return;
            }
            Levelled data = (Levelled) block.getBlockData();
            int level = data.getLevel();
            if (level == 0) {
                continue;
            }
            if (water >= level) {
                block.setBlockData(Material.CAULDRON.createBlockData());
                water = water - level;
            }else {
                data.setLevel(level - water);
                block.setBlockData(data);
                water = 0;
            }
        }
    }

    public String getId() {
        return id;
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }

    public ItemStack getWaste() {
        return waste;
    }

    public boolean isSpecificFuel() {
        return specificFuel;
    }

    public List<ItemStack> getFuels() {
        return fuels;
    }

    public long getDuration() {
        return duration;
    }

    public int getFuelPower() {
        return fuelPower;
    }

    public int getWater() {
        return water;
    }
}
