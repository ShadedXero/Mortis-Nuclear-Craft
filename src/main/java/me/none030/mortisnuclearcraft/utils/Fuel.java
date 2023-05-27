package me.none030.mortisnuclearcraft.utils;

import org.bukkit.inventory.ItemStack;

public class Fuel {

    private final ItemStack fuel;
    private final int power;

    public Fuel(ItemStack fuel, int power) {
        this.fuel = fuel;
        this.power = power;
    }

    public boolean isFuel(ItemStack item) {
        return fuel.isSimilar(item);
    }

    public int calculatePower(int amount) {
        return power * amount;
    }

    public int getAmount(int power) {
        return (int) Math.round((double) power / this.power);
    }

    public ItemStack getFuel() {
        return fuel;
    }

    public int getPower() {
        return power;
    }
}
