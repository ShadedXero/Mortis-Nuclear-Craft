package me.none030.mortisnuclearcraft.radiation;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.data.DataManager;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RadiationManager {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final DataManager dataManager;
    private Radiation radiation;
    private final HashMap<UUID, Double> radiationByPlayer;
    private final HashMap<UUID, BossBar> bossBarByPlayer;
    private final HashMap<UUID, Boolean> toggleByPlayer;

    public RadiationManager(DataManager dataManager) {
        this.dataManager = dataManager;
        this.radiationByPlayer = dataManager.getRadiationStorage().getRadiations();
        this.bossBarByPlayer = new HashMap<>();
        this.toggleByPlayer = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(new RadiationListener(this), plugin);
    }

    public void addRadiation(Player player, double radiation) {
        if (radiation <= 0) {
            return;
        }
        double playerRadiation = getRadiationByPlayer().get(player.getUniqueId());
        if (playerRadiation >= (this.radiation.getMaxRadiation() - this.radiation.getRadiation())) {
            return;
        }
        double amount = playerRadiation + radiation;
        getRadiationByPlayer().put(player.getUniqueId(), Math.min(amount, this.radiation.getMaxRadiation()));
    }

    public void removeRadiation(Player player, double radiation) {
        if (radiation <= 0) {
            return;
        }
        double playerRadiation = getRadiationByPlayer().get(player.getUniqueId());
        if (playerRadiation <= 0) {
            return;
        }
        double amount = playerRadiation - radiation;
        if (amount <= 0) {
            getRadiationByPlayer().put(player.getUniqueId(), 0.0);
        }else {
            getRadiationByPlayer().put(player.getUniqueId(), amount);
        }
    }

    public void setRadiation(Player player, double radiation) {
        double maxRadiation = this.radiation.getMaxRadiation();
        getRadiationByPlayer().put(player.getUniqueId(), Math.min(radiation, maxRadiation));
    }

    public double getRadiation(Player player) {
        Double radiation = getRadiationByPlayer().get(player.getUniqueId());
        if (radiation == null) {
            return -1;
        }
        return radiation;
    }

    public void updateRadiation(Player player) {
        Double radiation = getRadiationByPlayer().get(player.getUniqueId());
        if (radiation == null) {
            return;
        }
        dataManager.getRadiationStorage().storeRadiation(player.getUniqueId(), radiation);
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public Radiation getRadiation() {
        return radiation;
    }

    public void setRadiation(Radiation radiation) {
        this.radiation = radiation;
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
