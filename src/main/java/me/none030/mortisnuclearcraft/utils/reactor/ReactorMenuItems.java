package me.none030.mortisnuclearcraft.utils.reactor;

import org.bukkit.inventory.ItemStack;

public class ReactorMenuItems {

    private final String title;
    private final ItemStack filter;
    private final ItemStack input;
    private final ItemStack output;
    private final ItemStack fuel;
    private final ItemStack fuelCalculator;
    private final ItemStack waterCalculator;
    private final ItemStack manualMode;
    private final ItemStack redstoneMode;
    private final ItemStack animation1;
    private final ItemStack animation2;

    public ReactorMenuItems(String title, ItemStack filter, ItemStack input, ItemStack output, ItemStack fuel, ItemStack fuelCalculator, ItemStack waterCalculator, ItemStack manualMode, ItemStack redstoneMode, ItemStack animation1, ItemStack animation2) {
        this.title = title;
        this.filter = filter;
        this.input = input;
        this.output = output;
        this.fuel = fuel;
        this.fuelCalculator = fuelCalculator;
        this.waterCalculator = waterCalculator;
        this.manualMode = manualMode;
        this.redstoneMode = redstoneMode;
        this.animation1 = animation1;
        this.animation2 = animation2;
    }

    public String getTitle() {
        return title;
    }

    public ItemStack getFilter() {
        return filter.clone();
    }

    public ItemStack getInput() {
        return input.clone();
    }

    public ItemStack getOutput() {
        return output.clone();
    }

    public ItemStack getFuel() {
        return fuel.clone();
    }

    public ItemStack getFuelCalculator() {
        return fuelCalculator.clone();
    }

    public ItemStack getWaterCalculator() {
        return waterCalculator.clone();
    }

    public ItemStack getManualMode() {
        return manualMode.clone();
    }

    public ItemStack getRedstoneMode() {
        return redstoneMode.clone();
    }

    public ItemStack getAnimation1() {
        return animation1.clone();
    }

    public ItemStack getAnimation2() {
        return animation2.clone();
    }
}
