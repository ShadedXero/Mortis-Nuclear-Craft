package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.waste.types;

import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.radiatiton.RadiationManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class Waste{

    private final String id;
    private final double radiation;

    public Waste(String id, double radiation) {
        this.id = id;
        this.radiation = radiation;
    }

    public abstract void giveWaste(Player player);

    public abstract boolean isWaste(ItemStack item);

    public void giveRadiation(RadiationManager radiationManager, Player player) {
        radiationManager.addRadiation(player, radiation);
    }

    public String getId() {
        return id;
    }

    public double getRadiation() {
        return radiation;
    }
}
