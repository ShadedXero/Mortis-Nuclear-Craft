package me.none030.mortisnuclearcraft.config;

import me.none030.mortisnuclearcraft.nuclearcraft.addons.AddonManager;
import me.none030.mortisnuclearcraft.utils.addons.*;
import me.none030.mortisnuclearcraft.utils.radiation.RadiationMode;
import me.none030.mortisnuclearcraft.utils.radiation.RadiationType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class AddonsConfig extends Config {

    public AddonsConfig(ConfigManager configManager) {
        super("addons.yml", configManager);
    }

    @Override
    public void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection mythicMobsSection = config.getConfigurationSection("mythic-mobs");
        if (mythicMobsSection == null) {
            getPlugin().getLogger().severe("'mythic-mobs' section could not be found in addons.yml");
            getPlugin().getLogger().severe("Please add the 'mythic-mobs' section back or regenerate the addons.yml file");
            return;
        }
        boolean mythicMobs = mythicMobsSection.getBoolean("enabled");
        ConfigurationSection crackShotSection = config.getConfigurationSection("crack-shot");
        if (crackShotSection == null) {
            getPlugin().getLogger().severe("'crack-shot' section could not be found in addons.yml");
            getPlugin().getLogger().severe("Please add the 'crack-shot' section back or regenerate the addons.yml file");
            return;
        }
        boolean crackShot = crackShotSection.getBoolean("enabled");
        ConfigurationSection weaponMechanicsSection = config.getConfigurationSection("weapon-mechanics");
        if (weaponMechanicsSection == null) {
            getPlugin().getLogger().severe("'weapon-mechanics' section could not be found in addons.yml");
            getPlugin().getLogger().severe("Please add the 'weapon-mechanics' section back or regenerate the addons.yml file");
            return;
        }
        boolean weaponMechanics = weaponMechanicsSection.getBoolean("enabled");
        ConfigurationSection brewerySection = config.getConfigurationSection("brewery");
        if (brewerySection == null) {
            getPlugin().getLogger().severe("'brewery' section could not be found in addons.yml");
            getPlugin().getLogger().severe("Please add the 'brewery' section back or regenerate the addons.yml file");
            return;
        }
        boolean brewery = mythicMobsSection.getBoolean("enabled");
        getConfigManager().getManager().setAddonManager(new AddonManager(getConfigManager().getManager().getRadiationManager(), mythicMobs, crackShot, weaponMechanics, brewery));
        if (getPlugin().hasMythicMobs() && mythicMobs) {
            loadMythicMobs(mythicMobsSection);
        }
        if (getPlugin().hasCrackShot() && crackShot) {
            loadCrackShot(crackShotSection);
        }
        if (getPlugin().hasWeaponMechanics() && weaponMechanics) {
            loadWeaponMechanics(weaponMechanicsSection);
        }
        if (getPlugin().hasBrewery() && brewery) {
            loadBrewery(brewerySection);
        }
    }

    private void loadMythicMobs(ConfigurationSection section) {
        ConfigurationSection mobs = section.getConfigurationSection("mobs");
        if (mobs == null) {
            getPlugin().getLogger().severe("'mobs' section could not be found at 'mythic-mobs' section in addons.yml");
            getPlugin().getLogger().severe("Please add the 'mobs' section back at 'mythic-mobs' section or regenerate the addons.yml file");
            return;
        }
        for (String key : mobs.getKeys(false)) {
            ConfigurationSection mob = mobs.getConfigurationSection(key);
            if (mob == null) {
                getPlugin().getLogger().severe("Detected a problem with '" + key + "' in 'mobs' section at 'mythic-mobs' section in addons.yml");
                continue;
            }
            String mythicMobId = mob.getString("mythic-mob");
            RadiationType type;
            try {
                type = RadiationType.valueOf(mob.getString("type"));
            }catch (IllegalArgumentException exp) {
                getPlugin().getLogger().severe("Detected a problem with 'type' at '" + key + "' section in 'mobs' section at 'mythic-mobs' section in addons.yml");
                getPlugin().getLogger().severe("Please enter a valid type");
                continue;
            }
            RadiationMode mode;
            try {
                mode = RadiationMode.valueOf(mob.getString("mode"));
            }catch (IllegalArgumentException exp) {
                getPlugin().getLogger().severe("Detected a problem with 'mode' at '" + key + "' section in 'mobs' section at 'mythic-mobs' section in addons.yml");
                getPlugin().getLogger().severe("Please enter a valid mode");
                continue;
            }
            double radiation = mob.getDouble("radiation");
            MythicMob mythicMob = new MythicMob(key, mythicMobId, type, mode, radiation);
            getConfigManager().getManager().getAddonManager().getMythicMobs().getMobs().add(mythicMob);
        }
    }

    private void loadCrackShot(ConfigurationSection section) {
        ConfigurationSection weapons = section.getConfigurationSection("weapons");
        if (weapons == null) {
            getPlugin().getLogger().severe("'weapons' section could not be found at 'crack-shot' section in addons.yml");
            getPlugin().getLogger().severe("Please add the 'weapons' section back at 'crack-shot' section or regenerate the addons.yml file");
            return;
        }
        for (String key : weapons.getKeys(false)) {
            ConfigurationSection weapon = weapons.getConfigurationSection(key);
            if (weapon == null) {
                getPlugin().getLogger().severe("Detected a problem with '" + key + "' in 'weapons' section at 'crack-shot' section in addons.yml");
                continue;
            }
            String weaponId = weapon.getString("weapon");
            RadiationType type;
            try {
                type = RadiationType.valueOf(weapon.getString("type"));
            }catch (IllegalArgumentException exp) {
                getPlugin().getLogger().severe("Detected a problem with 'type' at '" + key + "' section in 'weapons' section at 'crack-shot' section in addons.yml");
                getPlugin().getLogger().severe("Please enter a valid type");
                continue;
            }
            RadiationMode mode;
            try {
                mode = RadiationMode.valueOf(weapon.getString("mode"));
            }catch (IllegalArgumentException exp) {
                getPlugin().getLogger().severe("Detected a problem with 'mode' at '" + key + "' section in 'weapons' section at 'crack-shot' section in addons.yml");
                getPlugin().getLogger().severe("Please enter a valid mode");
                continue;
            }
            double radiation = weapon.getDouble("radiation");
            int radius = weapon.getInt("radius");
            Weapon crackShotWeapon = new Weapon(key, weaponId, type, mode, radiation, radius);
            getConfigManager().getManager().getAddonManager().getCrackShot().getWeapons().add(crackShotWeapon);
        }
        ConfigurationSection grenades = section.getConfigurationSection("grenades");
        if (grenades == null) {
            getPlugin().getLogger().severe("'grenades' section could not be found at 'crack-shot' section in addons.yml");
            getPlugin().getLogger().severe("Please add the 'grenades' section back at 'crack-shot' section or regenerate the addons.yml file");
            return;
        }
        for (String key : grenades.getKeys(false)) {
            ConfigurationSection grenade = grenades.getConfigurationSection(key);
            if (grenade == null) {
                getPlugin().getLogger().severe("Detected a problem with '" + key + "' in 'grenades' section at 'crack-shot' section in addons.yml");
                continue;
            }
            String grenadeId = grenade.getString("grenade");
            RadiationType type;
            try {
                type = RadiationType.valueOf(grenade.getString("type"));
            }catch (IllegalArgumentException exp) {
                getPlugin().getLogger().severe("Detected a problem with 'type' at '" + key + "' section in 'grenades' section at 'crack-shot' section in addons.yml");
                getPlugin().getLogger().severe("Please enter a valid type");
                continue;
            }
            double radiation = grenade.getDouble("radiation");
            int radius = grenade.getInt("radius");
            long duration = grenade.getLong("duration");
            Grenade crackShotGrenade = new Grenade(key, grenadeId, type, radiation, radius, duration);
            getConfigManager().getManager().getAddonManager().getCrackShot().getGrenades().add(crackShotGrenade);
        }
    }

    private void loadWeaponMechanics(ConfigurationSection section) {
        ConfigurationSection weapons = section.getConfigurationSection("weapons");
        if (weapons == null) {
            getPlugin().getLogger().severe("'weapons' section could not be found at 'weapon-mechanics' section in addons.yml");
            getPlugin().getLogger().severe("Please add the 'weapons' section back at 'weapon-mechanics' section or regenerate the addons.yml file");
            return;
        }
        for (String key : weapons.getKeys(false)) {
            ConfigurationSection weapon = weapons.getConfigurationSection(key);
            if (weapon == null) {
                getPlugin().getLogger().severe("Detected a problem with '" + key + "' in 'weapons' section at 'weapon-mechanics' section in addons.yml");
                continue;
            }
            String weaponId = weapon.getString("weapon");
            RadiationType type;
            try {
                type = RadiationType.valueOf(weapon.getString("type"));
            }catch (IllegalArgumentException exp) {
                getPlugin().getLogger().severe("Detected a problem with 'type' at '" + key + "' section in 'weapons' section at 'weapon-mechanics' section in addons.yml");
                getPlugin().getLogger().severe("Please enter a valid type");
                continue;
            }
            RadiationMode mode;
            try {
                mode = RadiationMode.valueOf(weapon.getString("mode"));
            }catch (IllegalArgumentException exp) {
                getPlugin().getLogger().severe("Detected a problem with 'mode' at '" + key + "' section in 'weapons' section at 'weapon-mechanics' section in addons.yml");
                getPlugin().getLogger().severe("Please enter a valid mode");
                continue;
            }
            double radiation = weapon.getDouble("radiation");
            int radius = weapon.getInt("radius");
            Weapon weaponMechanicsWeapon = new Weapon(key, weaponId, type, mode, radiation, radius);
            getConfigManager().getManager().getAddonManager().getWeaponMechanics().getWeapons().add(weaponMechanicsWeapon);
        }
        ConfigurationSection grenades = section.getConfigurationSection("grenades");
        if (grenades == null) {
            getPlugin().getLogger().severe("'grenades' section could not be found at 'weapon-mechanics' section in addons.yml");
            getPlugin().getLogger().severe("Please add the 'grenades' section back at 'weapon-mechanics' section or regenerate the addons.yml file");
            return;
        }
        for (String key : grenades.getKeys(false)) {
            ConfigurationSection grenade = grenades.getConfigurationSection(key);
            if (grenade == null) {
                getPlugin().getLogger().severe("Detected a problem with '" + key + "' in 'grenades' section at 'weapon-mechanics' section in addons.yml");
                continue;
            }
            String grenadeId = grenade.getString("grenade");
            RadiationType type;
            try {
                type = RadiationType.valueOf(grenade.getString("type"));
            }catch (IllegalArgumentException exp) {
                getPlugin().getLogger().severe("Detected a problem with 'type' at '" + key + "' section in 'grenades' section at 'weapon-mechanics' section in addons.yml");
                getPlugin().getLogger().severe("Please enter a valid type");
                continue;
            }
            double radiation = grenade.getDouble("radiation");
            int radius = grenade.getInt("radius");
            long duration = grenade.getLong("duration");
            Grenade weaponMechanicsGrenade = new Grenade(key, grenadeId, type, radiation, radius, duration);
            getConfigManager().getManager().getAddonManager().getWeaponMechanics().getGrenades().add(weaponMechanicsGrenade);
        }
    }

    private void loadBrewery(ConfigurationSection section) {
        ConfigurationSection brews = section.getConfigurationSection("brews");
        if (brews == null) {
            getPlugin().getLogger().severe("'brews' section could not be found in addons.yml");
            getPlugin().getLogger().severe("Please add the 'brews' section back or regenerate the addons.yml file");
            return;
        }
        for (String key : brews.getKeys(false)) {
            ConfigurationSection brew = brews.getConfigurationSection(key);
            if (brew == null) {
                getPlugin().getLogger().severe("Detected a problem with '" + key + "' in 'brews' section at 'brewery' section in addons.yml");
                continue;
            }
            Equation equation;
            try {
                equation = Equation.valueOf(brew.getString("equation"));
            }catch (IllegalArgumentException exp) {
                getPlugin().getLogger().severe("Detected a problem with 'equation' at '" + key + "' section in 'brews' section at 'brewery' section in addons.yml");
                getPlugin().getLogger().severe("Please enter a valid equation");
                continue;
            }
            int alcohol = brew.getInt("alcohol");
            RadiationType type;
            try {
                type = RadiationType.valueOf(brew.getString("type"));
            }catch (IllegalArgumentException exp) {
                getPlugin().getLogger().severe("Detected a problem with 'type' at '" + key + "' section in 'brews' section at 'brewery' section in addons.yml");
                getPlugin().getLogger().severe("Please enter a valid type");
                continue;
            }
            double radiation = brew.getDouble("radiation");
            Drink drink = new Drink(key, equation, alcohol, type, radiation);
            getConfigManager().getManager().getAddonManager().getBrewery().getDrinks().add(drink);
        }
    }
}
