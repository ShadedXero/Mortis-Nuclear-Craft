package com.mortisdevelopment.mortisnuclearcraft.utils.addons;

import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.bombs.Bomb;
import com.mortisdevelopment.mortisnuclearcraft.utils.radiation.RadiationType;

public class Grenade extends Bomb {

    private final String id;
    private final String weapon;
    private final RadiationType type;

    public Grenade(String id, String weapon, RadiationType type, double radiation, int radius, long duration) {
        super(0, radius, duration, radiation, false, false, false, false, false, false, false, 0, false, false);
        this.id = id;
        this.weapon = weapon;
        this.type = type;
    }

    public boolean isGrenade(String id) {
        return weapon.equals(id);
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
}
