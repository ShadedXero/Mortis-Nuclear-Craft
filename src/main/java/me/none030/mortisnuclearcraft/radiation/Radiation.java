package me.none030.mortisnuclearcraft.radiation;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.utils.radiation.RadiationMode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Radiation {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final RadiationManager radiationManager;
    private final double radiation;
    private final double maxRadiation;
    private final RadiationDisplay display;
    private final List<RadiationMob> mobs;
    private final List<RadiationPill> pills;
    private final List<RadiationEffect> effects;

    public Radiation(RadiationManager radiationManager, double radiation, double maxRadiation, RadiationDisplay display) {
        this.radiationManager = radiationManager;
        this.radiation = radiation;
        this.maxRadiation = maxRadiation;
        this.display = display;
        this.mobs = new ArrayList<>();
        this.pills = new ArrayList<>();
        this.effects = new ArrayList<>();
        check();
    }

    public RadiationPill getPill(ItemStack item) {
        for (RadiationPill pill : pills) {
            if (pill.isPill(item)) {
                return pill;
            }
        }
        return null;
    }

    private void check() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    checkNearbyMobs(player);
                    for (RadiationEffect effect : effects) {
                        if (effect.hasAbove(player)) {
                            effect.applyEffect(player);
                        }else {
                            effect.removeEffect(player);
                        }
                    }
                    radiationManager.removeRadiation(player, radiation);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void checkNearbyMobs(Player player) {
        List<LivingEntity> entities = new ArrayList<>(player.getLocation().getNearbyLivingEntities(3));
        for (RadiationMob mob : mobs) {
            if (!mob.getMode().equals(RadiationMode.PROXIMITY)) {
                continue;
            }
            for (LivingEntity entity : entities) {
                if (mob.isMob(entity)) {
                    mob.changeRadiation(player);
                }
            }
        }
    }

    public double getRadiation() {
        return radiation;
    }

    public double getMaxRadiation() {
        return maxRadiation;
    }

    public RadiationDisplay getDisplay() {
        return display;
    }

    public List<RadiationMob> getMobs() {
        return mobs;
    }

    public List<RadiationPill> getPills() {
        return pills;
    }

    public List<RadiationEffect> getEffects() {
        return effects;
    }
}
