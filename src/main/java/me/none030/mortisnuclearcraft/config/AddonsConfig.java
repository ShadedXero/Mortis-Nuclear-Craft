package me.none030.mortisnuclearcraft.config;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.addons.AddonManager;
import me.none030.mortisnuclearcraft.utils.addons.*;
import me.none030.mortisnuclearcraft.utils.radiation.RadiationMode;
import me.none030.mortisnuclearcraft.utils.radiation.RadiationType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class AddonsConfig {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final ConfigManager configManager;

    public AddonsConfig(ConfigManager configManager) {
        this.configManager = configManager;
        loadConfig();
    }

    private void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection mythicMobsSection = config.getConfigurationSection("mythic-mobs");
        if (mythicMobsSection == null) {
            plugin.getLogger().severe("'mythic-mobs' section could not be found in addons.yml");
            plugin.getLogger().severe("Please add the 'mythic-mobs' section back or regenerate the addons.yml file");
            return;
        }
        boolean mythicMobs = mythicMobsSection.getBoolean("enabled");
        ConfigurationSection crackShotSection = config.getConfigurationSection("crack-shot");
        if (crackShotSection == null) {
            plugin.getLogger().severe("'crack-shot' section could not be found in addons.yml");
            plugin.getLogger().severe("Please add the 'crack-shot' section back or regenerate the addons.yml file");
            return;
        }
        boolean crackShot = crackShotSection.getBoolean("enabled");
        ConfigurationSection weaponMechanicsSection = config.getConfigurationSection("weapon-mechanics");
        if (weaponMechanicsSection == null) {
            plugin.getLogger().severe("'weapon-mechanics' section could not be found in addons.yml");
            plugin.getLogger().severe("Please add the 'weapon-mechanics' section back or regenerate the addons.yml file");
            return;
        }
        boolean weaponMechanics = weaponMechanicsSection.getBoolean("enabled");
        ConfigurationSection brewerySection = config.getConfigurationSection("brewery");
        if (brewerySection == null) {
            plugin.getLogger().severe("'brewery' section could not be found in addons.yml");
            plugin.getLogger().severe("Please add the 'brewery' section back or regenerate the addons.yml file");
            return;
        }
        boolean brewery = mythicMobsSection.getBoolean("enabled");
        configManager.getManager().setAddonManager(new AddonManager(mythicMobs, crackShot, weaponMechanics, brewery));
        if (plugin.hasMythicMobs()) {
            loadMythicMobs(mythicMobsSection);
        }
        if (plugin.hasCrackShot()) {
            loadCrackShot(crackShotSection);
        }
        if (plugin.hasWeaponMechanics()) {
            loadWeaponMechanics(weaponMechanicsSection);
        }
        if (plugin.hasBrewery()) {
            loadBrewery(brewerySection);
        }
    }

    private void loadMythicMobs(ConfigurationSection section) {
        ConfigurationSection mobs = section.getConfigurationSection("mobs");
        if (mobs == null) {
            plugin.getLogger().severe("'mobs' section could not be found at 'mythic-mobs' section in addons.yml");
            plugin.getLogger().severe("Please add the 'mobs' section back at 'mythic-mobs' section or regenerate the addons.yml file");
            return;
        }
        for (String key : mobs.getKeys(false)) {
            ConfigurationSection mob = mobs.getConfigurationSection(key);
            if (mob == null) {
                plugin.getLogger().severe("Detected a problem with '" + key + "' in 'mobs' section at 'mythic-mobs' section in addons.yml");
                continue;
            }
            String mythicMobId = mob.getString("mythic-mob");
            RadiationType type;
            try {
                type = RadiationType.valueOf(mob.getString("type"));
            }catch (IllegalArgumentException exp) {
                plugin.getLogger().severe("Detected a problem with 'type' at '" + key + "' section in 'mobs' section at 'mythic-mobs' section in addons.yml");
                plugin.getLogger().severe("Please enter a valid type");
                continue;
            }
            RadiationMode mode;
            try {
                mode = RadiationMode.valueOf(mob.getString("mode"));
            }catch (IllegalArgumentException exp) {
                plugin.getLogger().severe("Detected a problem with 'mode' at '" + key + "' section in 'mobs' section at 'mythic-mobs' section in addons.yml");
                plugin.getLogger().severe("Please enter a valid mode");
                continue;
            }
            double radiation = mob.getDouble("radiation");
            MythicMob mythicMob = new MythicMob(configManager.getManager().getRadiationManager(), key, mythicMobId, type, mode, radiation);
            configManager.getManager().getAddonManager().getMythicMobs().getMobs().add(mythicMob);
        }
    }

    private void loadCrackShot(ConfigurationSection section) {
        ConfigurationSection weapons = section.getConfigurationSection("weapons");
        if (weapons == null) {
            plugin.getLogger().severe("'weapons' section could not be found at 'crack-shot' section in addons.yml");
            plugin.getLogger().severe("Please add the 'weapons' section back at 'crack-shot' section or regenerate the addons.yml file");
            return;
        }
        for (String key : weapons.getKeys(false)) {
            ConfigurationSection weapon = weapons.getConfigurationSection(key);
            if (weapon == null) {
                plugin.getLogger().severe("Detected a problem with '" + key + "' in 'weapons' section at 'crack-shot' section in addons.yml");
                continue;
            }
            String weaponId = weapon.getString("weapon");
            RadiationType type;
            try {
                type = RadiationType.valueOf(weapon.getString("type"));
            }catch (IllegalArgumentException exp) {
                plugin.getLogger().severe("Detected a problem with 'type' at '" + key + "' section in 'weapons' section at 'crack-shot' section in addons.yml");
                plugin.getLogger().severe("Please enter a valid type");
                continue;
            }
            RadiationMode mode;
            try {
                mode = RadiationMode.valueOf(weapon.getString("mode"));
            }catch (IllegalArgumentException exp) {
                plugin.getLogger().severe("Detected a problem with 'mode' at '" + key + "' section in 'weapons' section at 'crack-shot' section in addons.yml");
                plugin.getLogger().severe("Please enter a valid mode");
                continue;
            }
            double radiation = weapon.getDouble("radiation");
            int radius = weapon.getInt("radius");
            Weapon crackShotWeapon = new Weapon(configManager.getManager().getRadiationManager(), key, weaponId, type, mode, radiation, radius);
            configManager.getManager().getAddonManager().getCrackShot().getWeapons().add(crackShotWeapon);
        }
        ConfigurationSection grenades = section.getConfigurationSection("grenades");
        if (grenades == null) {
            plugin.getLogger().severe("'grenades' section could not be found at 'crack-shot' section in addons.yml");
            plugin.getLogger().severe("Please add the 'grenades' section back at 'crack-shot' section or regenerate the addons.yml file");
            return;
        }
        for (String key : grenades.getKeys(false)) {
            ConfigurationSection grenade = grenades.getConfigurationSection(key);
            if (grenade == null) {
                plugin.getLogger().severe("Detected a problem with '" + key + "' in 'grenades' section at 'crack-shot' section in addons.yml");
                continue;
            }
            String grenadeId = grenade.getString("grenade");
            RadiationType type;
            try {
                type = RadiationType.valueOf(grenade.getString("type"));
            }catch (IllegalArgumentException exp) {
                plugin.getLogger().severe("Detected a problem with 'type' at '" + key + "' section in 'grenades' section at 'crack-shot' section in addons.yml");
                plugin.getLogger().severe("Please enter a valid type");
                continue;
            }
            double radiation = grenade.getDouble("radiation");
            int radius = grenade.getInt("radius");
            long duration = grenade.getLong("duration");
            Grenade crackShotGrenade = new Grenade(configManager.getManager().getRadiationManager(), key, grenadeId, type, radiation, radius, duration);
            configManager.getManager().getAddonManager().getCrackShot().getGrenades().add(crackShotGrenade);
        }
    }

    private void loadWeaponMechanics(ConfigurationSection section) {
        ConfigurationSection weapons = section.getConfigurationSection("weapons");
        if (weapons == null) {
            plugin.getLogger().severe("'weapons' section could not be found at 'weapon-mechanics' section in addons.yml");
            plugin.getLogger().severe("Please add the 'weapons' section back at 'weapon-mechanics' section or regenerate the addons.yml file");
            return;
        }
        for (String key : weapons.getKeys(false)) {
            ConfigurationSection weapon = weapons.getConfigurationSection(key);
            if (weapon == null) {
                plugin.getLogger().severe("Detected a problem with '" + key + "' in 'weapons' section at 'weapon-mechanics' section in addons.yml");
                continue;
            }
            String weaponId = weapon.getString("weapon");
            RadiationType type;
            try {
                type = RadiationType.valueOf(weapon.getString("type"));
            }catch (IllegalArgumentException exp) {
                plugin.getLogger().severe("Detected a problem with 'type' at '" + key + "' section in 'weapons' section at 'weapon-mechanics' section in addons.yml");
                plugin.getLogger().severe("Please enter a valid type");
                continue;
            }
            RadiationMode mode;
            try {
                mode = RadiationMode.valueOf(weapon.getString("mode"));
            }catch (IllegalArgumentException exp) {
                plugin.getLogger().severe("Detected a problem with 'mode' at '" + key + "' section in 'weapons' section at 'weapon-mechanics' section in addons.yml");
                plugin.getLogger().severe("Please enter a valid mode");
                continue;
            }
            double radiation = weapon.getDouble("radiation");
            int radius = weapon.getInt("radius");
            Weapon weaponMechanicsWeapon = new Weapon(configManager.getManager().getRadiationManager(), key, weaponId, type, mode, radiation, radius);
            configManager.getManager().getAddonManager().getWeaponMechanics().getWeapons().add(weaponMechanicsWeapon);
        }
        ConfigurationSection grenades = section.getConfigurationSection("grenades");
        if (grenades == null) {
            plugin.getLogger().severe("'grenades' section could not be found at 'weapon-mechanics' section in addons.yml");
            plugin.getLogger().severe("Please add the 'grenades' section back at 'weapon-mechanics' section or regenerate the addons.yml file");
            return;
        }
        for (String key : grenades.getKeys(false)) {
            ConfigurationSection grenade = grenades.getConfigurationSection(key);
            if (grenade == null) {
                plugin.getLogger().severe("Detected a problem with '" + key + "' in 'grenades' section at 'weapon-mechanics' section in addons.yml");
                continue;
            }
            String grenadeId = grenade.getString("grenade");
            RadiationType type;
            try {
                type = RadiationType.valueOf(grenade.getString("type"));
            }catch (IllegalArgumentException exp) {
                plugin.getLogger().severe("Detected a problem with 'type' at '" + key + "' section in 'grenades' section at 'weapon-mechanics' section in addons.yml");
                plugin.getLogger().severe("Please enter a valid type");
                continue;
            }
            double radiation = grenade.getDouble("radiation");
            int radius = grenade.getInt("radius");
            long duration = grenade.getLong("duration");
            Grenade weaponMechanicsGrenade = new Grenade(configManager.getManager().getRadiationManager(), key, grenadeId, type, radiation, radius, duration);
            configManager.getManager().getAddonManager().getWeaponMechanics().getGrenades().add(weaponMechanicsGrenade);
        }
    }

    private void loadBrewery(ConfigurationSection section) {
        ConfigurationSection brews = section.getConfigurationSection("brews");
        if (brews == null) {
            plugin.getLogger().severe("'brews' section could not be found in addons.yml");
            plugin.getLogger().severe("Please add the 'brews' section back or regenerate the addons.yml file");
            return;
        }
        for (String key : brews.getKeys(false)) {
            ConfigurationSection brew = brews.getConfigurationSection(key);
            if (brew == null) {
                plugin.getLogger().severe("Detected a problem with '" + key + "' in 'brews' section at 'brewery' section in addons.yml");
                continue;
            }
            Equation equation;
            try {
                equation = Equation.valueOf(brew.getString("equation"));
            }catch (IllegalArgumentException exp) {
                plugin.getLogger().severe("Detected a problem with 'equation' at '" + key + "' section in 'brews' section at 'brewery' section in addons.yml");
                plugin.getLogger().severe("Please enter a valid equation");
                continue;
            }
            int alcohol = brew.getInt("alcohol");
            RadiationType type;
            try {
                type = RadiationType.valueOf(brew.getString("type"));
            }catch (IllegalArgumentException exp) {
                plugin.getLogger().severe("Detected a problem with 'type' at '" + key + "' section in 'brews' section at 'brewery' section in addons.yml");
                plugin.getLogger().severe("Please enter a valid type");
                continue;
            }
            double radiation = brew.getDouble("radiation");
            Drink drink = new Drink(configManager.getManager().getRadiationManager(), key, equation, alcohol, type, radiation);
            configManager.getManager().getAddonManager().getBrewery().getDrinks().add(drink);
        }
    }

    private File saveConfig() {
        File file = new File(plugin.getDataFolder(), "addons.yml");
        if (!file.exists()) {
            plugin.saveResource("addons.yml", false);
        }
        return file;
    }
}
