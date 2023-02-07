package me.none030.mortisnuclearcraft.config;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.radiation.*;
import me.none030.mortisnuclearcraft.utils.radiation.*;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;

public class RadiationConfig {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final ConfigManager configManager;

    public RadiationConfig(ConfigManager configManager) {
        this.configManager = configManager;
        loadConfig();
    }

    private void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = config.getConfigurationSection("radiation");
        if (section == null) {
            plugin.getLogger().severe("'radiation' section could not be found in radiation.yml");
            plugin.getLogger().severe("Please add the 'radiation' section back or regenerate the radiation.yml file");
            return;
        }
        configManager.getManager().setRadiationManager(new RadiationManager(configManager.getManager().getDataManager()));
        double rad = section.getDouble("radiation-removed-per-second");
        double maxRadiation = section.getDouble("max-radiation");
        RadiationDisplay display = getDisplay(section.getConfigurationSection("display"));
        if (display == null) {
            return;
        }
        Radiation radiation = new Radiation(configManager.getManager().getRadiationManager(), rad, maxRadiation, display);
        configManager.getManager().getRadiationManager().setRadiation(radiation);
        loadMobs(section.getConfigurationSection("mobs"));
        loadPills(section.getConfigurationSection("pills"));
        loadEffects(section.getConfigurationSection("effects"));
    }

    private RadiationDisplay getDisplay(ConfigurationSection display) {
        if (display == null) {
            plugin.getLogger().severe("'display' section could not be found at 'radiation' section in radiation.yml");
            plugin.getLogger().severe("Please add the 'display' section back at 'radiation' section or regenerate the radiation.yml file");
            return null;
        }
        DisplayType type;
        try {
            type = DisplayType.valueOf(display.getString("type"));
        }catch (IllegalArgumentException exp) {
            plugin.getLogger().severe("Detected a problem with 'type' at 'display' section in 'radiation' section in radiation.yml");
            plugin.getLogger().severe("Please enter a valid type");
            return null;
        }
        DisplayMode mode;
        try {
            mode = DisplayMode.valueOf(display.getString("mode"));
        }catch (IllegalArgumentException exp) {
            plugin.getLogger().severe("Detected a problem with 'mode' at 'display' section in 'radiation' section in radiation.yml");
            plugin.getLogger().severe("Please enter a valid mode");
            return null;
        }
        DisplayToggleMode toggleMode;
        try {
            toggleMode = DisplayToggleMode.valueOf(display.getString("toggle-mode"));
        }catch (IllegalArgumentException exp) {
            plugin.getLogger().severe("Detected a problem with 'toggle-mode' at 'display' section in 'radiation' section in radiation.yml");
            plugin.getLogger().severe("Please enter a valid toggle mode");
            return null;
        }
        ItemStack toggleItem = null;
        if (toggleMode.equals(DisplayToggleMode.ITEM)) {
            String itemId = display.getString("toggle-item");
            ItemStack item = configManager.getManager().getItemManager().getItemById().get(itemId);
            if (item == null) {
                plugin.getLogger().severe("Detected a problem with 'toggle-item' item at 'display' section in 'radiation' section in radiation.yml");
                plugin.getLogger().severe("Please enter a valid item id");
                return null;
            }
            toggleItem = item;
        }
        return new RadiationDisplay(configManager.getManager().getRadiationManager(), type, mode, toggleMode, toggleItem);
    }

    private void loadMobs(ConfigurationSection mobs) {
        if (mobs == null) {
            plugin.getLogger().severe("'mobs' section could not be found at 'radiation' section in radiation.yml");
            plugin.getLogger().severe("Please add the 'mobs' section back at 'radiation' section or regenerate the radiation.yml file");
            return;
        }
        for (String mobKey : mobs.getKeys(false)) {
            ConfigurationSection mob = mobs.getConfigurationSection(mobKey);
            if (mob == null) {
                plugin.getLogger().severe("Detected a problem with '" + mobKey + "' at 'mobs' section in 'radiation' section in radiation.yml");
                continue;
            }
            EntityType entityType;
            try {
                entityType = EntityType.valueOf(mob.getString("mob"));
            }catch (IllegalArgumentException exp) {
                plugin.getLogger().severe("Detected a problem with entity type at 'mobs' section in 'radiation' section in radiation.yml");
                plugin.getLogger().severe("Please enter a valid entity type");
                continue;
            }
            RadiationType mobType;
            try {
                mobType = RadiationType.valueOf(mob.getString("type"));
            }catch (IllegalArgumentException exp) {
                plugin.getLogger().severe("Detected a problem with 'type' at 'mobs' section in 'radiation' section in radiation.yml");
                plugin.getLogger().severe("Please enter a valid type");
                continue;
            }
            RadiationMode mobMode;
            try {
                mobMode = RadiationMode.valueOf(mob.getString("mode"));
            }catch (IllegalArgumentException exp) {
                plugin.getLogger().severe("Detected a problem with 'mode' at 'mobs' section in 'radiation' section in radiation.yml");
                plugin.getLogger().severe("Please enter a valid mode");
                continue;
            }
            double mobRadiation = mob.getDouble("radiation");
            RadiationMob radiationMob = new RadiationMob(configManager.getManager().getRadiationManager(), mobKey, entityType, mobType, mobMode, mobRadiation);
            configManager.getManager().getRadiationManager().getRadiation().getMobs().add(radiationMob);
        }
    }

    private void loadPills(ConfigurationSection pills) {
        if (pills == null) {
            plugin.getLogger().severe("'pills' section could not be found at 'radiation' section in radiation.yml");
            plugin.getLogger().severe("Please add the 'pills' section back at 'radiation' section or regenerate the radiation.yml file");
            return;
        }
        for (String pillKey : pills.getKeys(false)) {
            ConfigurationSection pill = pills.getConfigurationSection(pillKey);
            if (pill == null) {
                plugin.getLogger().severe("Detected a problem with '" + pillKey + "' at 'pills' section in 'radiation' section in radiation.yml");
                continue;
            }
            double pillRad = pill.getDouble("radiation");
            String itemId = pill.getString("item");
            ItemStack item = configManager.getManager().getItemManager().getItemById().get(itemId);
            if (item == null) {
                plugin.getLogger().severe("Detected a problem with item at '" + pillKey + "' section in 'pills' section in 'radiation' section in radiation.yml");
                plugin.getLogger().severe("Please enter a valid item id");
                continue;
            }
            RadiationPill radiationPill = new RadiationPill(configManager.getManager().getRadiationManager(), item, pillRad);
            configManager.getManager().getRadiationManager().getRadiation().getPills().add(radiationPill);
            configManager.addRecipe(pill.getConfigurationSection("recipes"), radiationPill.getItem());
        }
    }

    private void loadEffects(ConfigurationSection effects) {
        if (effects == null) {
            plugin.getLogger().severe("'effects' section could not be found at 'radiation' section in radiation.yml");
            plugin.getLogger().severe("Please add the 'effects' section back at 'radiation' section or regenerate the radiation.yml file");
            return;
        }
        for (String effectKey : effects.getKeys(false)) {
            ConfigurationSection effect = effects.getConfigurationSection(effectKey);
            if (effect == null) {
                plugin.getLogger().severe("Detected a problem with '" + effectKey + "' at 'effects' section in 'radiation' section in radiation.yml");
                continue;
            }
            PotionEffectType effectType = PotionEffectType.getByName(effectKey);
            if (effectType == null) {
                plugin.getLogger().severe("Detected a problem with potion effect at '" + effectKey + "' section at 'effects' section in 'radiation' section in radiation.yml");
                continue;
            }
            double effectRad = effect.getDouble("radiation");
            int amplifier = effect.getInt("amplifier");
            boolean ambient = effect.getBoolean("ambient");
            boolean particle = effect.getBoolean("particle");
            boolean icon = effect.getBoolean("icon");
            PotionEffect potionEffect = new PotionEffect(effectType, 999999999, amplifier, ambient, particle, icon);
            RadiationEffect radiationEffect = new RadiationEffect(configManager.getManager().getRadiationManager(), potionEffect, effectRad);
            configManager.getManager().getRadiationManager().getRadiation().getEffects().add(radiationEffect);
        }
    }

    private File saveConfig() {
        File file = new File(plugin.getDataFolder(), "radiation.yml");
        if (!file.exists()) {
            plugin.saveResource("radiation.yml", false);
        }
        return file;
    }
}
