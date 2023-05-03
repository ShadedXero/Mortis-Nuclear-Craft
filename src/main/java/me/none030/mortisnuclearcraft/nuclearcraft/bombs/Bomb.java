package me.none030.mortisnuclearcraft.nuclearcraft.bombs;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.nuclearcraft.radiatiton.RadiationManager;
import me.none030.mortisnuclearcraft.utils.MessageUtils;
import me.none030.mortisnuclearcraft.utils.bomb.VehicleRemover;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class Bomb {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final DecimalFormat formatter = new DecimalFormat("#.#");
    private final int radius;

    protected Bomb(int radius) {
        this.radius = radius;
    }

    public void radiate(RadiationManager radiationManager, Location location, long duration, double radiation) {
        new BukkitRunnable() {
            long seconds;
            @Override
            public void run() {
                seconds++;
                if (seconds > duration) {
                    cancel();
                }
                List<LivingEntity> entities = new ArrayList<>(location.getNearbyLivingEntities(getRadius()));
                for (int i = 0; i < entities.size(); i++) {
                    LivingEntity entity = entities.get(i);
                    if (!(entity instanceof Player)) {
                        entity.setHealth(0);
                        entities.remove(entity);
                        continue;
                    }
                    Player player = (Player) entity;
                    radiationManager.addRadiation(player, radiation);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
        MessageUtils editor = new MessageUtils(radiationManager.getMessage("BOMB_EXPLODE"));
        editor.replace("%world%", location.getWorld().getName());
        editor.replace("%x%", formatter.format(location.getX()));
        editor.replace("%y%", formatter.format(location.getY()));
        editor.replace("%z%", formatter.format(location.getZ()));
        Bukkit.broadcast(Component.text(editor.getMessage()));
    }

    public void explode(Location location, int strength, boolean vehicles, boolean drain) {
        if (drain) {
            drain(location);
        }
        if (vehicles) {
            destroyVehicles(location);
        }
        location.getWorld().createExplosion(location, strength, true, true);
    }

    private void drain(Location loc) {
        for (double x = loc.getX() - getRadius(); x <= loc.getX() + getRadius(); x++) {
            for (double y = loc.getY() - getRadius(); y <= loc.getY() + getRadius(); y++) {
                for (double z = loc.getZ() - getRadius(); z <= loc.getZ() + getRadius(); z++) {
                    Location location = new Location(loc.getWorld(), x, y, z);
                    Block block = location.getBlock();
                    if (block.getType().equals(Material.SEAGRASS) || block.getType().equals(Material.TALL_SEAGRASS) || block.getType().equals(Material.KELP) || block.getType().equals(Material.KELP_PLANT) || block.getType().equals(Material.WATER)) {
                        block.setBlockData(Material.AIR.createBlockData());
                    }
                }
            }
        }
    }

    private void destroyVehicles(Location loc) {
        if (!plugin.hasQAV()) {
            return;
        }
        VehicleRemover remover = new VehicleRemover(loc, getRadius());
        remover.removeVehicles();
    }

    public int getRadius() {
        return radius;
    }
}
