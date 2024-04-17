package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.radiatiton;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class RadiationWorld {

    private final World world;
    private final double radiation;

    public RadiationWorld(World world, double radiation) {
        this.world = world;
        this.radiation = radiation;
    }

    public void addRadiation(RadiationManager radiationManager, Player player) {
        radiationManager.addRadiation(player, radiation);
    }

    public boolean isInWorld(Player player) {
        return player.getWorld().equals(world);
    }

    public World getWorld() {
        return world;
    }

    public double getRadiation() {
        return radiation;
    }
}
