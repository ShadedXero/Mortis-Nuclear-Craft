package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.bombs.itembomb;

import com.mortisdevelopment.mortisnuclearcraft.MortisNuclearCraft;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.bombs.Bomb;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
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

    public ItemBomb(String id, ItemStack item, int speed, int strength, int radius, long duration, double radiation, boolean vehicles, boolean drain, boolean fire, boolean blockDamage, boolean townyBlockDamage, boolean blockRegen, boolean townyBlockRegen, long regenTime, boolean townyPlayerDamage, boolean explosionsEnabledPlotsWilderness) {
        super(strength, radius, duration, radiation, vehicles, drain, fire, blockDamage, townyBlockDamage, blockRegen, townyBlockRegen, regenTime, townyPlayerDamage, explosionsEnabledPlotsWilderness);
        this.id = id;
        this.item = item;
        this.speed = speed;
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
                if (projectile.isDead() || projectile.isOnGround() || projectile.isInLava() || projectile.isInPowderedSnow() || projectile.isInWaterOrBubbleColumn()) {
                    projectile.remove();
                    explode(itemBombManager.getRadiationManager(), projectile.getLocation());
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

    public String getId() {
        return id;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getSpeed() {
        return speed;
    }
}
