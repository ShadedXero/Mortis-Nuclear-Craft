package me.none030.mortisnuclearcraft.bombs;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.data.BlockBombData;
import me.none030.mortisnuclearcraft.structures.Structure;
import me.none030.mortisnuclearcraft.utils.bomb.VehicleRemover;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class BlockBomb {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final BombManager bombManager;
    private final String id;
    private final String name;
    private final int strength;
    private final int radius;
    private final int duration;
    private final double radiation;
    private final boolean vehicles;
    private final Structure structure;

    public BlockBomb(BombManager bombManager, String id, String name, int strength, int radius, int duration, double radiation, boolean vehicles, Structure structure) {
        this.bombManager = bombManager;
        this.id = id;
        this.name = name;
        this.strength = strength;
        this.radius = radius;
        this.duration = duration;
        this.radiation = radiation;
        this.vehicles = vehicles;
        this.structure = structure;
    }

    public void explode(Location loc) {
        if (vehicles) {
            destroyVehicles(loc);
        }
        drain(loc, radius);
        loc.createExplosion(strength, true, true);
        final int[] count = {0};
        new BukkitRunnable() {
            @Override
            public void run() {
                count[0] = count[0]++;
                if (count[0] >= duration) {
                    cancel();
                }
                List<LivingEntity> entities = new ArrayList<>(loc.getNearbyLivingEntities(radius));
                for (int i = 0; i < entities.size(); i++) {
                    LivingEntity entity = entities.get(i);
                    if (!(entity instanceof Player)) {
                        entity.setHealth(0);
                        entities.remove(entity);
                        continue;
                    }
                    Player player = (Player) entity;
                    bombManager.getRadiationManager().addRadiation(player, radiation);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
        Bukkit.broadcast(Component.text(BombMessages.BOMB_EXPLODE.replace("%world%", loc.getWorld().getName()).replace("%x%", String.valueOf(loc.getBlockX())).replace("%y%", String.valueOf(loc.getBlockY())).replace("%z%", String.valueOf(loc.getBlockZ()))));
    }

    private void drain(Location loc, int radius) {
        for (double x = loc.getX() - radius; x <= loc.getX() + radius; x++) {
            for (double y = loc.getY() - radius; y <= loc.getY() + radius; y++) {
                for (double z = loc.getZ() - radius; z <= loc.getZ() + radius; z++) {
                    Location location = new Location(loc.getWorld(), x, y, z);
                    Block block = location.getBlock();
                    if (block.getType().equals(Material.SEAGRASS) || block.getType().equals(Material.TALL_SEAGRASS) || block.getType().equals(Material.KELP) || block.getType().equals(Material.KELP_PLANT) || block.getType().equals(Material.WATER)) {
                        block.setType(Material.AIR, false);
                    }
                }
            }
        }
    }

    private void destroyVehicles(Location location) {
        if (!plugin.hasQAV()) {
            return;
        }
        VehicleRemover remover = new VehicleRemover(location, radius);
        remover.removeVehicles();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStrength() {
        return strength;
    }

    public int getRadius() {
        return radius;
    }

    public int getDuration() {
        return duration;
    }

    public double getRadiation() {
        return radiation;
    }

    public boolean isVehicles() {
        return vehicles;
    }

    public Structure getStructure() {
        return structure;
    }
}
