package me.none030.mortisnuclearcraft;

import me.none030.mortisnuclearcraft.managers.NuclearCraftManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.SQLException;

public final class MortisNuclearCraft extends JavaPlugin {

    private static MortisNuclearCraft Instance;
    private boolean mythicMobs;
    private boolean crackShot;
    private boolean weaponMechanics;
    private boolean brewery;
    private boolean qav;
    private NuclearCraftManager nuclearCraftManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Instance = this;
        mythicMobs = getServer().getPluginManager().getPlugin("MythicMobs") != null;
        crackShot = getServer().getPluginManager().getPlugin("CrackShot") != null;
        weaponMechanics = getServer().getPluginManager().getPlugin("WeaponMechanics") != null;
        brewery = getServer().getPluginManager().getPlugin("Brewery") != null;
        qav = getServer().getPluginManager().getPlugin("QualityArmoryVehicles") != null;
        nuclearCraftManager = new NuclearCraftManager();
    }

    public void onDisable() {
        Connection connection = nuclearCraftManager.getDataManager().getConnection();
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static MortisNuclearCraft getInstance() {
        return Instance;
    }

    public boolean hasMythicMobs() {
        return mythicMobs;
    }

    public boolean hasCrackShot() {
        return crackShot;
    }

    public boolean hasWeaponMechanics() {
        return weaponMechanics;
    }

    public boolean hasBrewery() {
        return brewery;
    }

    public boolean hasQAV() {
        return qav;
    }

    public NuclearCraftManager getNuclearCraftManager() {
        return nuclearCraftManager;
    }
}
