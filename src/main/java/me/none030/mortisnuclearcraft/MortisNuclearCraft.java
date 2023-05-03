package me.none030.mortisnuclearcraft;

import me.none030.mortisnuclearcraft.nuclearcraft.NuclearCraftManager;
import org.bukkit.plugin.java.JavaPlugin;

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
