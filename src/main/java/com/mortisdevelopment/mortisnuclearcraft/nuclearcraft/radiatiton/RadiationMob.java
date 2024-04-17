package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.radiatiton;

import com.mortisdevelopment.mortisnuclearcraft.utils.radiation.RadiationMode;
import com.mortisdevelopment.mortisnuclearcraft.utils.radiation.RadiationType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class RadiationMob {

    private final String id;
    private final EntityType mob;
    private final RadiationType type;
    private final RadiationMode mode;
    private final double rad;

    public RadiationMob(String id, EntityType mob, RadiationType type, RadiationMode mode, double rad) {
        this.id = id;
        this.mob = mob;
        this.type = type;
        this.mode = mode;
        this.rad = rad;
    }

    public boolean isMob(Entity entity) {
        return mob.equals(entity.getType());
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

    public EntityType getMob() {
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
