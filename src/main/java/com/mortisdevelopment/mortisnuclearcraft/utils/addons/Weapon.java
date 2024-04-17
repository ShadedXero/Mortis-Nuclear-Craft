package com.mortisdevelopment.mortisnuclearcraft.utils.addons;

import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.radiatiton.RadiationManager;
import com.mortisdevelopment.mortisnuclearcraft.utils.radiation.RadiationMode;
import com.mortisdevelopment.mortisnuclearcraft.utils.radiation.RadiationType;
import org.bukkit.entity.Player;

public class Weapon {

    private final String id;
    private final String weapon;
    private final RadiationType type;
    private final RadiationMode mode;
    private final double rad;
    private final int radius;

    public Weapon(String id, String weapon, RadiationType type, RadiationMode mode, double rad, int radius) {
        this.id = id;
        this.weapon = weapon;
        this.type = type;
        this.mode = mode;
        this.rad = rad;
        this.radius = radius;
    }

    public boolean isWeapon(String id) {
        return weapon.equals(id);
    }

    public void changeRadiation(RadiationManager radiationManager, Player player) {
        if (type.equals(RadiationType.INCREASE)) {
            radiationManager.addRadiation(player, rad);
        } else {
            radiationManager.removeRadiation(player, rad);
        }
    }

    public String getId() {
        return id;
    }

    public String getWeapon() {
        return weapon;
    }

    public RadiationType getType() {
        return type;
    }

    public RadiationMode getMode() {
        return mode;
    }

    public double getRadiation() {
        return rad;
    }

    public int getRadius() {
        return radius;
    }
}
