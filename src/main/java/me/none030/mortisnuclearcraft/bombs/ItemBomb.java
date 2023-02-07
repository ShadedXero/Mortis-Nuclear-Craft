package me.none030.mortisnuclearcraft.bombs;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.utils.bomb.VehicleRemover;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ItemBomb {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final BombManager bombManager;
    private final String id;
    private final ItemStack item;
    private final int speed;
    private final int strength;
    private final int radius;
    private final int duration;
    private final double radiation;
    private final boolean vehicles;

    public ItemBomb(BombManager bombManager, String id, ItemStack item, int speed, int strength, int radius, int duration, double radiation, boolean vehicles) {
        this.bombManager = bombManager;
        this.id = id;
        this.item = item;
        this.speed = speed;
        this.strength = strength;
        this.radius = radius;
        this.duration = duration;
        this.radiation = radiation;
        this.vehicles = vehicles;
        createBomb();
    }

    private void createBomb() {
        ItemMeta meta = item.getItemMeta();
        NamespacedKey nuclearCraft = new NamespacedKey(plugin, "NuclearCraft");
        meta.getPersistentDataContainer().set(nuclearCraft, PersistentDataType.STRING, "BOMB");
        NamespacedKey nuclearCraftId = new NamespacedKey(plugin, "NuclearCraftId");
        meta.getPersistentDataContainer().set(nuclearCraftId, PersistentDataType.STRING, id);
        item.setItemMeta(meta);
    }

    public void launch(Player player) {
        Projectile projectile = player.launchProjectile(Arrow.class, player.getLocation().getDirection().multiply(speed));
        new BukkitRunnable() {
            @Override
            public void run() {
                Firework firework = projectile.getWorld().spawn(projectile.getLocation(), Firework.class);
                FireworkMeta meta = firework.getFireworkMeta();
                meta.displayName(Component.text("BOMB"));
                meta.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.RED, Color.ORANGE, Color.YELLOW, Color.GRAY).withFlicker().build());
                firework.setFireworkMeta(meta);
                firework.setCustomNameVisible(false);
                firework.detonate();
                if (projectile.isOnGround() || projectile.isInLava() || projectile.isInPowderedSnow() || projectile.isInWaterOrBubbleColumn()) {
                    explode(projectile.getLocation());
                    projectile.remove();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
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

    public void giveBomb(Player player) {
        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItemNaturally(player.getLocation(), item);
        } else {
            player.getInventory().addItem(item);
        }
    }

    private void explode(Location loc) {
        if (vehicles) {
            destroyVehicles(loc);
        }
        drain(loc, radius);
        loc.getWorld().createExplosion(loc, strength, true, true);
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

    public ItemStack getItem() {
        return item;
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

    public int getSpeed() {
        return speed;
    }
}
