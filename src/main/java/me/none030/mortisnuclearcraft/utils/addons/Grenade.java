package me.none030.mortisnuclearcraft.utils.addons;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.radiation.RadiationManager;
import me.none030.mortisnuclearcraft.utils.radiation.RadiationType;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Grenade {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final RadiationManager radiationManager;
    private final String id;
    private final String weapon;
    private final RadiationType type;
    private final double rad;
    private final int radius;
    private final long duration;

    public Grenade(RadiationManager radiationManager, String id, String weapon, RadiationType type, double rad, int radius, long duration) {
        this.radiationManager = radiationManager;
        this.id = id;
        this.weapon = weapon;
        this.type = type;
        this.rad = rad;
        this.radius = radius;
        this.duration = duration;
    }

    public boolean isGrenade(String id) {
        return weapon.equals(id);
    }

    public void explode(Location location) {
        final int[] count = {0};
        new BukkitRunnable() {
            @Override
            public void run() {
                count[0] = count[0]++;
                if (count[0] >= duration) {
                    cancel();
                }
                List<LivingEntity> entities = new ArrayList<>(location.getNearbyLivingEntities(radius));
                for (int i = 0; i < entities.size(); i++) {
                    LivingEntity entity = entities.get(i);
                    if (!(entity instanceof Player)) {
                        entity.setHealth(0);
                        entities.remove(entity);
                        continue;
                    }
                    Player player = (Player) entity;
                    changeRadiation(player);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void changeRadiation(Player player) {
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

    public double getRad() {
        return rad;
    }

    public int getRadius() {
        return radius;
    }

    public long getDuration() {
        return duration;
    }
}
