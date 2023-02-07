package me.none030.mortisnuclearcraft.utils.addons;

import me.none030.mortisnuclearcraft.radiation.RadiationManager;
import me.none030.mortisnuclearcraft.utils.radiation.RadiationMode;
import me.none030.mortisnuclearcraft.utils.radiation.RadiationType;
import org.bukkit.entity.Player;

public class Weapon {

    private final RadiationManager radiationManager;
    private final String id;
    private final String weapon;
    private final RadiationType type;
    private final RadiationMode mode;
    private final double rad;
    private final int radius;

    public Weapon(RadiationManager radiationManager, String id, String weapon, RadiationType type, RadiationMode mode, double rad, int radius) {
        this.radiationManager = radiationManager;
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

    public void changeRadiation(Player player) {
        if (type.equals(RadiationType.INCREASE)) {
            radiationManager.addRadiation(player, rad);
        } else {
            radiationManager.removeRadiation(player, rad);
        }
    }

    public String getId() {
        return id;
    }

    public RadiationManager getRadiation() {
        return radiationManager;
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

    public double getRad() {
        return rad;
    }

    public int getRadius() {
        return radius;
    }
}
