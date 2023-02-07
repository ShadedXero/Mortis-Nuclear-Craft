package me.none030.mortisnuclearcraft.addons;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;

public class AddonManager {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final MythicMobsAddon mythicMobs;
    private final CrackShotAddon crackShot;
    private final WeaponMechanicsAddon weaponMechanics;
    private final BreweryAddon brewery;

    public AddonManager(boolean mythicMobs, boolean crackShot, boolean weaponMechanics, boolean brewery) {
        if (plugin.hasMythicMobs()) {
            if (mythicMobs) {
                this.mythicMobs = new MythicMobsAddon(true);
                plugin.getServer().getPluginManager().registerEvents(this.mythicMobs, plugin);
                plugin.getLogger().info("Hooked into MythicMobs");
            }else {
                this.mythicMobs = null;
            }
        }else {
            this.mythicMobs = null;
        }
        if (plugin.hasCrackShot()) {
            if (crackShot) {
                this.crackShot = new CrackShotAddon(true);
                plugin.getServer().getPluginManager().registerEvents(this.crackShot, plugin);
                plugin.getLogger().info("Hooked into CrackShot");
            }else {
                this.crackShot= null;
            }
        }else {
            this.crackShot = null;
        }
        if (plugin.hasWeaponMechanics()) {
            if (weaponMechanics) {
                this.weaponMechanics = new WeaponMechanicsAddon(true);
                plugin.getServer().getPluginManager().registerEvents(this.weaponMechanics, plugin);
                plugin.getLogger().info("Hooked into WeaponMechanics");
            }else {
                this.weaponMechanics = null;
            }
        }else {
            this.weaponMechanics = null;
        }
        if (plugin.hasBrewery()) {
            if (brewery) {
                this.brewery = new BreweryAddon(true);
                plugin.getServer().getPluginManager().registerEvents(this.brewery, plugin);
                plugin.getLogger().info("Hooked into Brewery");
            }else {
                this.brewery = null;
            }
        }else {
            this.brewery = null;
        }
    }

    public MythicMobsAddon getMythicMobs() {
        return mythicMobs;
    }

    public CrackShotAddon getCrackShot() {
        return crackShot;
    }

    public WeaponMechanicsAddon getWeaponMechanics() {
        return weaponMechanics;
    }

    public BreweryAddon getBrewery() {
        return brewery;
    }
}
