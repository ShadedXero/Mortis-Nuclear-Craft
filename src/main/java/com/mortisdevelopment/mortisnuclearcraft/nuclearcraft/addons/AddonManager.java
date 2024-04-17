package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.addons;

import com.mortisdevelopment.mortisnuclearcraft.MortisNuclearCraft;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.radiatiton.RadiationManager;

public class AddonManager {

    private final RadiationManager radiationManager;
    private final MythicMobsManager mythicMobs;
    private final CrackShotManager crackShot;
    private final WeaponMechanicsManager weaponMechanics;
    private final BreweryManager brewery;

    public AddonManager(RadiationManager radiationManager, boolean mythicMobs, boolean crackShot, boolean weaponMechanics, boolean brewery) {
        this.radiationManager = radiationManager;
        MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
        if (plugin.hasMythicMobs()) {
            if (mythicMobs) {
                this.mythicMobs = new MythicMobsManager(this);
                plugin.getLogger().info("Hooked into MythicMobs");
            }else {
                this.mythicMobs = null;
            }
        }else {
            this.mythicMobs = null;
        }
        if (plugin.hasCrackShot()) {
            if (crackShot) {
                this.crackShot = new CrackShotManager(this);
                plugin.getLogger().info("Hooked into CrackShot");
            }else {
                this.crackShot = null;
            }
        }else {
            this.crackShot = null;
        }
        if (plugin.hasWeaponMechanics()) {
            if (weaponMechanics) {
                this.weaponMechanics = new WeaponMechanicsManager(this);
                plugin.getLogger().info("Hooked into WeaponMechanics");
            }else {
                this.weaponMechanics = null;
            }
        }else {
            this.weaponMechanics = null;
        }
        if (plugin.hasBrewery()) {
            if (brewery) {
                this.brewery = new BreweryManager(this);
                plugin.getLogger().info("Hooked into Brewery");
            }else {
                this.brewery = null;
            }
        }else {
            this.brewery = null;
        }
    }

    public RadiationManager getRadiationManager() {
        return radiationManager;
    }

    public MythicMobsManager getMythicMobs() {
        return mythicMobs;
    }

    public CrackShotManager getCrackShot() {
        return crackShot;
    }

    public WeaponMechanicsManager getWeaponMechanics() {
        return weaponMechanics;
    }

    public BreweryManager getBrewery() {
        return brewery;
    }
}
