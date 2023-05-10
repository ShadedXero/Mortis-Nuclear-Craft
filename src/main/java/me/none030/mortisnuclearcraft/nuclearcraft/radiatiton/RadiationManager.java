package me.none030.mortisnuclearcraft.nuclearcraft.radiatiton;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.nuclearcraft.Manager;
import me.none030.mortisnuclearcraft.utils.NuclearType;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.*;

public class RadiationManager extends Manager {

    private final Radiation radiation;
    private final HashMap<UUID, Double> radiationByPlayer;
    private final HashMap<UUID, BossBar> bossBarByPlayer;
    private final HashMap<UUID, Boolean> toggleByPlayer;
    private final HashMap<UUID, Set<RadiationEffect>> affectedPlayers;

    public RadiationManager(Radiation radiation) {
        super(NuclearType.RADIATION);
        this.radiation = radiation;
        this.radiationByPlayer = new HashMap<>();
        this.bossBarByPlayer = new HashMap<>();
        this.toggleByPlayer = new HashMap<>();
        this.affectedPlayers = new HashMap<>();
        MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
        plugin.getServer().getPluginManager().registerEvents(new RadiationListener(this), plugin);
        radiation.check(this);
    }

    public void preReload() {
        for (BossBar bossBar : bossBarByPlayer.values()) {
            bossBar.removeAll();
        }
    }

    public void addRadiation(Player player, double radiation) {
        if (radiation <= 0) {
            return;
        }
        Double playerRadiation = radiationByPlayer.get(player.getUniqueId());
        if (playerRadiation == null) {
            radiationByPlayer.put(player.getUniqueId(), 0.0);
            playerRadiation = 0.0;
        }
        double amount = playerRadiation + radiation;
        radiationByPlayer.put(player.getUniqueId(), Math.min(amount, this.radiation.getMaxRadiation()));
        for (RadiationEffect effect : getRadiation().getEffects()) {
            if (!effect.hasAbove(this, player)) {
                continue;
            }
            Set<RadiationEffect> effects = affectedPlayers.get(player.getUniqueId());
            if (effects == null) {
                effects = new HashSet<>();
            }
            if (effects.contains(effect)) {
                continue;
            }
            effect.applyEffect(player);
            effects.add(effect);
            affectedPlayers.put(player.getUniqueId(), effects);
        }
    }

    public void removeRadiation(Player player, double radiation) {
        if (radiation <= 0) {
            return;
        }
        Double playerRadiation = radiationByPlayer.get(player.getUniqueId());
        if (playerRadiation == null) {
            radiationByPlayer.put(player.getUniqueId(), 0.0);
            playerRadiation = 0.0;
        }
        if (playerRadiation <= 0) {
            return;
        }
        double amount = playerRadiation - radiation;
        radiationByPlayer.put(player.getUniqueId(), Math.max(amount, 0.0));
        for (RadiationEffect effect : getRadiation().getEffects()) {
            if (effect.hasAbove(this, player)) {
                continue;
            }
            Set<RadiationEffect> effects = affectedPlayers.get(player.getUniqueId());
            if (effects == null) {
                effects = new HashSet<>();
            }
            if (!effects.contains(effect)) {
                continue;
            }
            effect.removeEffect(player);
            effects.remove(effect);
            affectedPlayers.put(player.getUniqueId(), effects);
        }
    }

    public void setRadiation(Player player, double radiation) {
        double maxRadiation = this.radiation.getMaxRadiation();
        radiationByPlayer.put(player.getUniqueId(), Math.min(radiation, maxRadiation));
        for (RadiationEffect effect : getRadiation().getEffects()) {
            if (effect.hasAbove(this, player)) {
                Set<RadiationEffect> effects = affectedPlayers.get(player.getUniqueId());
                if (effects == null) {
                    effects = new HashSet<>();
                }
                if (effects.contains(effect)) {
                    continue;
                }
                effect.applyEffect(player);
                effects.add(effect);
                affectedPlayers.put(player.getUniqueId(), effects);
            }else {
                Set<RadiationEffect> effects = affectedPlayers.get(player.getUniqueId());
                if (effects == null) {
                    effects = new HashSet<>();
                }
                if (!effects.contains(effect)) {
                    continue;
                }
                effect.removeEffect(player);
                effects.remove(effect);
                affectedPlayers.put(player.getUniqueId(), effects);
            }
        }
    }

    public double getRadiation(Player player) {
        Double radiation = radiationByPlayer.get(player.getUniqueId());
        if (radiation == null) {
            radiationByPlayer.put(player.getUniqueId(), 0.0);
            return 0.0;
        }
        return radiation;
    }


    public Radiation getRadiation() {
        return radiation;
    }

    public HashMap<UUID, Double> getRadiationByPlayer() {
        return radiationByPlayer;
    }

    public HashMap<UUID, BossBar> getBossBarByPlayer() {
        return bossBarByPlayer;
    }

    public HashMap<UUID, Boolean> getToggleByPlayer() {
        return toggleByPlayer;
    }

    public HashMap<UUID, Set<RadiationEffect>> getAffectedPlayers() {
        return affectedPlayers;
    }
}
