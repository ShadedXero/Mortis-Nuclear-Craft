package me.none030.mortisnuclearcraft.nuclearcraft.radiatiton;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.utils.radiation.RadiationMode;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Radiation {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final double radiation;
    private final double maxRadiation;
    private final RadiationDisplay display;
    private final List<RadiationMob> mobs;
    private final List<RadiationPill> pills;
    private final List<RadiationEffect> effects;

    public Radiation(double radiation, double maxRadiation, RadiationDisplay display) {
        this.radiation = radiation;
        this.maxRadiation = maxRadiation;
        this.display = display;
        this.mobs = new ArrayList<>();
        this.pills = new ArrayList<>();
        this.effects = new ArrayList<>();
    }

    public double getMinRadiationEffect() {
        RadiationEffect effect = effects.get(0);
        if (effect == null) {
            return maxRadiation;
        }
        for (RadiationEffect radiationEffect : effects) {
            if (radiationEffect.getRadiation() < effect.getRadiation()) {
                effect = radiationEffect;
            }
        }
        return effect.getRadiation();
    }

    public RadiationPill getPill(ItemStack item) {
        for (RadiationPill pill : pills) {
            if (pill.isPill(item)) {
                return pill;
            }
        }
        return null;
    }

    public void check(RadiationManager radiationManager) {
        display.check(radiationManager);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (RadiationEffect effect : effects) {
                        if (effect.hasAbove(radiationManager, player)) {
                            effect.applyEffect(player);
                        }else {
                            effect.removeEffect(player);
                        }
                    }
                    List<LivingEntity> entities = new ArrayList<>(player.getLocation().getNearbyLivingEntities(3));
                    for (RadiationMob mob : mobs) {
                        if (!mob.getMode().equals(RadiationMode.PROXIMITY)) {
                            continue;
                        }
                        for (LivingEntity entity : entities) {
                            if (mob.isMob(entity)) {
                                mob.changeRadiation(radiationManager, player);
                            }
                        }
                    }
                    radiationManager.removeRadiation(player, radiation);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
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
