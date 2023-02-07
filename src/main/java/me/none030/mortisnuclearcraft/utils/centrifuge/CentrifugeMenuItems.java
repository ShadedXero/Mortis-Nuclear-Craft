package me.none030.mortisnuclearcraft.utils.centrifuge;

import org.bukkit.inventory.ItemStack;

public class CentrifugeMenuItems {

    private final String title;
    private final ItemStack filter;
    private final ItemStack input1;
    private final ItemStack input2;
    private final ItemStack output1;
    private final ItemStack output2;
    private final ItemStack fuelCalculator;
    private final ItemStack fuel;
    private final ItemStack manualMode;
    private final ItemStack redstoneMode;
    private final ItemStack animation1;
    private final ItemStack animation2;

    public CentrifugeMenuItems(String title, ItemStack filter, ItemStack input1, ItemStack input2, ItemStack output1, ItemStack output2, ItemStack fuelCalculator, ItemStack fuel, ItemStack manualMode, ItemStack redstoneMode, ItemStack animation1, ItemStack animation2) {
        this.title = title;
        this.filter = filter;
        this.input1 = input1;
        this.input2 = input2;
        this.output1 = output1;
        this.output2 = output2;
        this.fuelCalculator = fuelCalculator;
        this.fuel = fuel;
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

    public ItemStack getInput1() {
        return input1.clone();
    }

    public ItemStack getInput2() {
        return input2.clone();
    }

    public ItemStack getOutput1() {
        return output1.clone();
    }

    public ItemStack getOutput2() {
        return output2.clone();
    }

    public ItemStack getFuelCalculator() {
        return fuelCalculator.clone();
    }

    public ItemStack getFuel() {
        return fuel.clone();
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
