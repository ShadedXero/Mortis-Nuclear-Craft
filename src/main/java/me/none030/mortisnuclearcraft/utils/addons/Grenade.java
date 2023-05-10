package me.none030.mortisnuclearcraft.utils.addons;

import me.none030.mortisnuclearcraft.nuclearcraft.bombs.Bomb;
import me.none030.mortisnuclearcraft.utils.radiation.RadiationType;

public class Grenade extends Bomb {

    private final String id;
    private final String weapon;
    private final RadiationType type;

    public Grenade(String id, String weapon, RadiationType type, double radiation, int radius, long duration) {
        super(0, radius, duration, radiation, false, false, false, false, false, false, false, 0);
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
