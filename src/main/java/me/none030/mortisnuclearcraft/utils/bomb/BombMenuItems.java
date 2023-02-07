package me.none030.mortisnuclearcraft.utils.bomb;

import org.bukkit.inventory.ItemStack;

public class BombMenuItems {

    private final ItemStack filter;
    private final ItemStack redstoneMode;
    private final ItemStack manualMode;
    private final ItemStack timer;
    private final ItemStack noTimer;
    private final ItemStack timerSetter;
    private final ItemStack timerSet;

    public BombMenuItems(ItemStack filter, ItemStack redstoneMode, ItemStack manualMode, ItemStack timer, ItemStack noTimer, ItemStack timerSetter, ItemStack timerSet) {
        this.filter = filter;
        this.redstoneMode = redstoneMode;
        this.manualMode = manualMode;
        this.timer = timer;
        this.noTimer = noTimer;
        this.timerSetter = timerSetter;
        this.timerSet = timerSet;
    }

    public ItemStack getFilter() {
        return filter.clone();
    }

    public ItemStack getRedstoneMode() {
        return redstoneMode.clone();
    }

    public ItemStack getManualMode() {
        return manualMode.clone();
    }

    public ItemStack getTimer() {
        return timer.clone();
    }

    public ItemStack getNoTimer() {
        return noTimer.clone();
    }

    public ItemStack getTimerSetter() {
        return timerSetter.clone();
    }

    public ItemStack getTimerSet() {
        return timerSet.clone();
    }
}
