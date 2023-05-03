package me.none030.mortisnuclearcraft.nuclearcraft.bombs.itembomb;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.nuclearcraft.bombs.Bomb;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemBomb extends Bomb {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final String id;
    private final ItemStack item;
    private final int speed;
    private final int strength;
    private final int radius;
    private final int duration;
    private final double radiation;
    private final boolean vehicles;
    private final boolean drain;

    public ItemBomb(String id, ItemStack item, int speed, int strength, int radius, int duration, double radiation, boolean vehicles, boolean drain) {
        super(radius);
        this.id = id;
        this.item = item;
        this.speed = speed;
        this.strength = strength;
        this.radius = radius;
        this.duration = duration;
        this.radiation = radiation;
        this.vehicles = vehicles;
        this.drain = drain;
        createBomb();
    }

    private void createBomb() {
        ItemBombData data = new ItemBombData(item.getItemMeta());
        data.create(id);
        item.setItemMeta(data.getMeta());
    }

    public void launch(ItemBombManager itemBombManager, Player player) {
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
                    explode(itemBombManager, projectile.getLocation());
                    projectile.remove();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }



    public void giveBomb(Player player) {
        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItemNaturally(player.getLocation(), item);
        } else {
            player.getInventory().addItem(item);
        }
    }

    private void explode(ItemBombManager itemBombManager, Location loc) {
        explode(loc, strength, vehicles, drain);
        radiate(itemBombManager.getRadiationManager(), loc, duration, radiation);
    }

    public String getId() {
        return id;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getSpeed() {
        return speed;
    }

    public int getStrength() {
        return strength;
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

    public boolean isDrain() {
        return drain;
    }

    @Override
    public int getRadius() {
        return radius;
    }
}
