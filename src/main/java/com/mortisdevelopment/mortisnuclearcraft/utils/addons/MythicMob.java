package com.mortisdevelopment.mortisnuclearcraft.utils.addons;

import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.radiatiton.RadiationManager;
import com.mortisdevelopment.mortisnuclearcraft.utils.radiation.RadiationMode;
import com.mortisdevelopment.mortisnuclearcraft.utils.radiation.RadiationType;
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
