package me.none030.mortisnuclearcraft.reactor;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.data.ReactorData;
import me.none030.mortisnuclearcraft.utils.Fuel;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ReactorRecipe {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final ReactorManager reactorManager;
    private final String id;
    private final ItemStack input;
    private final ItemStack output;
    private final long duration;
    private final int fuelPower;
    private final int water;

    public ReactorRecipe(ReactorManager reactorManager, String id, ItemStack input, ItemStack output, long duration, int fuelPower, int water) {
        this.reactorManager = reactorManager;
        this.id = id;
        this.input = input;
        this.output = output;
        this.duration = duration;
        this.fuelPower = fuelPower;
        this.water = water;
    }

    public boolean isRecipe(ItemStack input, ItemStack output) {
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
            plugin.getLogger().info("Recipe Worked");
            if (!output.isSimilar(this.output)) {
                return false;
            }
            plugin.getLogger().info("Recipe Worked 2");
            if (output.getAmount() >= output.getMaxStackSize()) {
                return false;
            }
            plugin.getLogger().info("Recipe Worked 3");
            int amount = output.getAmount() + this.output.getAmount();
            return amount < this.output.getMaxStackSize();
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
            data.setOutput(reactorManager, this.output);
        }else {
            output.setAmount(output.getAmount() + this.output.getAmount());
            data.setOutput(reactorManager, output);
        }
        data.setProcess(reactorManager, null);
        data.setTimer(reactorManager, -1);
        data.update(data);
    }

    public void process(ReactorData data) {
        plugin.getLogger().info("Process Worked");
        if (!isRecipe(data.getInput(), data.getOutput())) {
            return;
        }
        plugin.getLogger().info("Process Worked 2");
        int power = reactorManager.getReactor().getFuelPower(data.getFuel());
        if (!hasEnoughFuel(power)) {
            return;
        }
        plugin.getLogger().info("Process Worked 3");
        int water = reactorManager.getReactor().getWater(data.getCore());
        if (!hasEnoughWater(water)) {
            data.explode(reactorManager);
            reactorManager.deleteReactor(data.getCore());
            data.close();
            return;
        }
        plugin.getLogger().info("Process Worked 4");
        int inputAmount = Math.max(data.getInput().getAmount(), this.input.getAmount()) - Math.min(data.getInput().getAmount(), this.input.getAmount());
        if (inputAmount <= 0) {
            data.setInput(reactorManager, null);
        }else {
            ItemStack input = data.getInput();
            input.setAmount(inputAmount);
            data.setInput(reactorManager, input);
        }
        Fuel fuelItem = reactorManager.getReactor().getFuel(data.getFuel());
        int fuelAmount = Math.max(fuelItem.getAmount(fuelPower), data.getFuel().getAmount()) - Math.min(fuelItem.getAmount(fuelPower), data.getFuel().getAmount());
        if (fuelAmount <= 0) {
            data.setFuel(reactorManager, null);
        }else {
            ItemStack fuel = data.getFuel();
            fuel.setAmount(fuelAmount);
            data.setFuel(reactorManager, fuel);
        }
        removeWater(data.getCore(), this.water);
        data.setProcess(reactorManager, id);
        data.setTimer(reactorManager, duration);
        data.update(data);
    }

    private void removeWater(Location core, int water) {
        List<Block> blocks = new ArrayList<>(reactorManager.getReactor().getStructure().getBlocksWithType(core, Material.WATER_CAULDRON));
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
            if (water > level) {
                block.setType(Material.CAULDRON);
                water = water - level;
            }else {
                if (water == 3) {
                    block.setType(Material.CAULDRON);
                }else {
                    data.setLevel(level - water);
                }
                water = 0;
            }
        }
    }

    public ReactorManager getReactorManager() {
        return reactorManager;
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
