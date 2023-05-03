package me.none030.mortisnuclearcraft.utils.addons;

import me.none030.mortisnuclearcraft.nuclearcraft.bombs.Bomb;
import me.none030.mortisnuclearcraft.nuclearcraft.radiatiton.RadiationManager;
import me.none030.mortisnuclearcraft.utils.radiation.RadiationType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Grenade extends Bomb {

    private final String id;
    private final String weapon;
    private final RadiationType type;
    private final double rad;
    private final long duration;

    public Grenade(String id, String weapon, RadiationType type, double rad, int radius, long duration) {
        super(radius);
        this.id = id;
        this.weapon = weapon;
        this.type = type;
        this.rad = rad;
        this.duration = duration;
    }

    public boolean isGrenade(String id) {
        return weapon.equals(id);
    }

    public void explode(RadiationManager radiationManager, Location location) {
        radiate(radiationManager, location, duration, rad);
    }

    private void changeRadiation(RadiationManager radiationManager, Player player) {
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

    public double getRadiation() {
        return rad;
    }

    public long getDuration() {
        return duration;
    }
}
