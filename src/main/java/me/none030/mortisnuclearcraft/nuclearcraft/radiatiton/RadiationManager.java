package me.none030.mortisnuclearcraft.nuclearcraft.radiatiton;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.nuclearcraft.Manager;
import me.none030.mortisnuclearcraft.utils.NuclearType;
import me.none030.mortisnuclearcraft.utils.radiation.RadiationMode;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RadiationManager extends Manager {

    private final Radiation radiation;
    private final HashMap<UUID, Double> radiationByPlayer;
    private final HashMap<UUID, BossBar> bossBarByPlayer;
    private final HashMap<UUID, Boolean> toggleByPlayer;

    public RadiationManager(Radiation radiation) {
        super(NuclearType.RADIATION);
        this.radiation = radiation;
        this.radiationByPlayer = new HashMap<>();
        this.bossBarByPlayer = new HashMap<>();
        this.toggleByPlayer = new HashMap<>();
        MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
        plugin.getServer().getPluginManager().registerEvents(new RadiationListener(this), plugin);
        radiation.check(this);
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
    }

    public void setRadiation(Player player, double radiation) {
        double maxRadiation = this.radiation.getMaxRadiation();
        radiationByPlayer.put(player.getUniqueId(), Math.min(radiation, maxRadiation));
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
}
