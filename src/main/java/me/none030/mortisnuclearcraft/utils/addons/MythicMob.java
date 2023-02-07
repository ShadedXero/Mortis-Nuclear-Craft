package me.none030.mortisnuclearcraft.utils.addons;

import me.none030.mortisnuclearcraft.radiation.RadiationManager;
import me.none030.mortisnuclearcraft.utils.radiation.RadiationMode;
import me.none030.mortisnuclearcraft.utils.radiation.RadiationType;
import org.bukkit.entity.Player;

public class MythicMob {

    private final RadiationManager radiationManager;
    private final String id;
    private final String mob;
    private final RadiationType type;
    private final RadiationMode mode;
    private final double rad;

    public MythicMob(RadiationManager radiationManager, String id, String mob, RadiationType type, RadiationMode mode, double rad) {
        this.radiationManager = radiationManager;
        this.id = id;
        this.mob = mob;
        this.type = type;
        this.mode = mode;
        this.rad = rad;
    }

    public boolean isMythicMob(String id) {
        return mob.equals(id);
    }

    public void changeRadiation(Player player) {
        if (type.equals(RadiationType.INCREASE)) {
            radiationManager.addRadiation(player, rad);
        } else {
            radiationManager.removeRadiation(player, rad);
        }
    }

    public RadiationManager getRadiation() {
        return radiationManager;
    }

    public String getId() {
        return id;
    }

    public String getMob() {
        return mob;
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
}
