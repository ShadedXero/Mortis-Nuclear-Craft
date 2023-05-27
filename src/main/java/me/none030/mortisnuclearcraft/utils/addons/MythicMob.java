package me.none030.mortisnuclearcraft.utils.addons;

import me.none030.mortisnuclearcraft.nuclearcraft.radiatiton.RadiationManager;
import me.none030.mortisnuclearcraft.utils.radiation.RadiationMode;
import me.none030.mortisnuclearcraft.utils.radiation.RadiationType;
import org.bukkit.entity.Player;

public class MythicMob {

    private final String id;
    private final String mob;
    private final RadiationType type;
    private final RadiationMode mode;
    private final double rad;

    public MythicMob(String id, String mob, RadiationType type, RadiationMode mode, double rad) {
        this.id = id;
        this.mob = mob;
        this.type = type;
        this.mode = mode;
        this.rad = rad;
    }

    public boolean isMythicMob(String id) {
        return mob.equals(id);
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

    public String getMob() {
        return mob;
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
}
