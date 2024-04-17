package com.mortisdevelopment.mortisnuclearcraft.config;

import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.radiatiton.*;
import com.mortisdevelopment.mortisnuclearcraft.utils.radiation.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RadiationConfig extends Config {

    public RadiationConfig(ConfigManager configManager) {
        super("radiation.yml", configManager);
    }

    @Override
    public void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = config.getConfigurationSection("radiation");
        if (section == null) {
            getPlugin().getLogger().severe("'radiation' section could not be found in radiation.yml");
            getPlugin().getLogger().severe("Please add the 'radiation' section back or regenerate the radiation.yml file");
            return;
        }
        double rad = section.getDouble("radiation-removed-per-second");
        double maxRadiation = section.getDouble("max-radiation");
        RadiationDisplay display = loadDisplay(section.getConfigurationSection("display"));
        if (display == null) {
            return;
        }
        RadiationTolerance tolerance = loadTolerance(section.getConfigurationSection("tolerance"));
        if (tolerance == null) {
            return;
        }
        Radiation radiation = new Radiation(rad, maxRadiation, display, tolerance);
        getConfigManager().getManager().setRadiationManager(new RadiationManager(radiation));
        loadWorlds(section.getConfigurationSection("worlds"));
        loadMobs(section.getConfigurationSection("mobs"));
        loadPills(section.getConfigurationSection("pills"));
        loadEffects(section.getConfigurationSection("effects"));
        getConfigManager().getManager().getRadiationManager().addMessages(loadMessages(section.getConfigurationSection("messages")));
    }

    private RadiationDisplay loadDisplay(ConfigurationSection display) {
        if (display == null) {
            getPlugin().getLogger().severe("'display' section could not be found at 'radiation' section in radiation.yml");
            getPlugin().getLogger().severe("Please add the 'display' section back at 'radiation' section or regenerate the radiation.yml file");
            return null;
        }
        DisplayType type;
        try {
            type = DisplayType.valueOf(display.getString("type"));
        }catch (IllegalArgumentException exp) {
            getPlugin().getLogger().severe("Detected a problem with 'type' at 'display' section in 'radiation' section in radiation.yml");
            getPlugin().getLogger().severe("Please enter a valid type");
            return null;
        }
        DisplayMode mode;
        try {
            mode = DisplayMode.valueOf(display.getString("mode"));
        }catch (IllegalArgumentException exp) {
            getPlugin().getLogger().severe("Detected a problem with 'mode' at 'display' section in 'radiation' section in radiation.yml");
            getPlugin().getLogger().severe("Please enter a valid mode");
            return null;
        }
        DisplayToggleMode toggleMode;
        try {
            toggleMode = DisplayToggleMode.valueOf(display.getString("toggle-mode"));
        }catch (IllegalArgumentException exp) {
            getPlugin().getLogger().severe("Detected a problem with 'toggle-mode' at 'display' section in 'radiation' section in radiation.yml");
            getPlugin().getLogger().severe("Please enter a valid toggle mode");
            return null;
        }
        ItemStack toggleItem = null;
        if (toggleMode.equals(DisplayToggleMode.ITEM)) {
            String itemId = display.getString("toggle-item");
            ItemStack item = getConfigManager().getManager().getItemManager().getItem(itemId);
            if (item == null) {
                getPlugin().getLogger().severe("Detected a problem with 'toggle-item' item at 'display' section in 'radiation' section in radiation.yml");
                getPlugin().getLogger().severe("Please enter a valid item id");
                return null;
            }
            toggleItem = item;
        }
        return new RadiationDisplay(type, mode, toggleMode, toggleItem);
    }

    private RadiationTolerance loadTolerance(ConfigurationSection tolerance) {
        if (tolerance == null) {
            return null;
        }
        ToleranceMode mode;
        try {
            mode = ToleranceMode.valueOf(tolerance.getString("mode"));
        }catch (IllegalArgumentException exp) {
            return null;
        }
        List<EntityType> entities = new ArrayList<>();
        for (String rawEntityType : tolerance.getStringList("entities")) {
            EntityType entity;
            try {
                entity = EntityType.valueOf(rawEntityType);
            }catch (IllegalArgumentException exp) {
                continue;
            }
            entities.add(entity);
        }
        return new RadiationTolerance(mode, entities);
    }

    private void loadWorlds(ConfigurationSection worlds) {
        if (worlds == null) {
            getPlugin().getLogger().severe("'worlds' section could not be found at 'radiation' section in radiation.yml");
            getPlugin().getLogger().severe("Please add the 'worlds' section back at 'radiation' section or regenerate the radiation.yml file");
            return;
        }
        for (String worldKey : worlds.getKeys(false)) {
            World world = Bukkit.getWorld(worldKey);
            if (world == null) {
                getPlugin().getLogger().severe("Detected a problem with world name at 'worlds' section in 'radiation' section in radiation.yml");
                getPlugin().getLogger().severe("Please enter a valid world name");
                continue;
            }
            double radiation = worlds.getDouble(worldKey);
            RadiationWorld radiationWorld = new RadiationWorld(world, radiation);
            getConfigManager().getManager().getRadiationManager().getRadiation().getWorlds().add(radiationWorld);
        }
    }

    private void loadMobs(ConfigurationSection mobs) {
        if (mobs == null) {
            getPlugin().getLogger().severe("'mobs' section could not be found at 'radiation' section in radiation.yml");
            getPlugin().getLogger().severe("Please add the 'mobs' section back at 'radiation' section or regenerate the radiation.yml file");
            return;
        }
        for (String mobKey : mobs.getKeys(false)) {
            ConfigurationSection mob = mobs.getConfigurationSection(mobKey);
            if (mob == null) {
                getPlugin().getLogger().severe("Detected a problem with '" + mobKey + "' at 'mobs' section in 'radiation' section in radiation.yml");
                continue;
            }
            EntityType entityType;
            try {
                entityType = EntityType.valueOf(mob.getString("mob"));
            }catch (IllegalArgumentException exp) {
                getPlugin().getLogger().severe("Detected a problem with entity type at 'mobs' section in 'radiation' section in radiation.yml");
                getPlugin().getLogger().severe("Please enter a valid entity type");
                continue;
            }
            RadiationType mobType;
            try {
                mobType = RadiationType.valueOf(mob.getString("type"));
            }catch (IllegalArgumentException exp) {
                getPlugin().getLogger().severe("Detected a problem with 'type' at 'mobs' section in 'radiation' section in radiation.yml");
                getPlugin().getLogger().severe("Please enter a valid type");
                continue;
            }
            RadiationMode mobMode;
            try {
                mobMode = RadiationMode.valueOf(mob.getString("mode"));
            }catch (IllegalArgumentException exp) {
                getPlugin().getLogger().severe("Detected a problem with 'mode' at 'mobs' section in 'radiation' section in radiation.yml");
                getPlugin().getLogger().severe("Please enter a valid mode");
                continue;
            }
            double mobRadiation = mob.getDouble("radiation");
            RadiationMob radiationMob = new RadiationMob(mobKey, entityType, mobType, mobMode, mobRadiation);
            getConfigManager().getManager().getRadiationManager().getRadiation().getMobs().add(radiationMob);
        }
    }

    private void loadPills(ConfigurationSection pills) {
        if (pills == null) {
            getPlugin().getLogger().severe("'pills' section could not be found at 'radiation' section in radiation.yml");
            getPlugin().getLogger().severe("Please add the 'pills' section back at 'radiation' section or regenerate the radiation.yml file");
            return;
        }
        for (String pillKey : pills.getKeys(false)) {
            ConfigurationSection pill = pills.getConfigurationSection(pillKey);
            if (pill == null) {
                getPlugin().getLogger().severe("Detected a problem with '" + pillKey + "' at 'pills' section in 'radiation' section in radiation.yml");
                continue;
            }
            double pillRad = pill.getDouble("radiation");
            String itemId = pill.getString("item");
            ItemStack item = getConfigManager().getManager().getItemManager().getItem(itemId);
            if (item == null) {
                getPlugin().getLogger().severe("Detected a problem with item at '" + pillKey + "' section in 'pills' section in 'radiation' section in radiation.yml");
                getPlugin().getLogger().severe("Please enter a valid item id");
                continue;
            }
            RadiationPill radiationPill = new RadiationPill(item, pillRad);
            getConfigManager().getManager().getRadiationManager().getRadiation().getPills().add(radiationPill);
            addRecipe(pill.getConfigurationSection("recipes"), radiationPill.getItem());
        }
    }

    private void loadEffects(ConfigurationSection effects) {
        if (effects == null) {
            getPlugin().getLogger().severe("'effects' section could not be found at 'radiation' section in radiation.yml");
            getPlugin().getLogger().severe("Please add the 'effects' section back at 'radiation' section or regenerate the radiation.yml file");
            return;
        }
        for (String effectKey : effects.getKeys(false)) {
            ConfigurationSection effect = effects.getConfigurationSection(effectKey);
            if (effect == null) {
                getPlugin().getLogger().severe("Detected a problem with '" + effectKey + "' at 'effects' section in 'radiation' section in radiation.yml");
                continue;
            }
            PotionEffectType effectType = PotionEffectType.getByName(effectKey);
            if (effectType == null) {
                getPlugin().getLogger().severe("Detected a problem with potion effect at '" + effectKey + "' section at 'effects' section in 'radiation' section in radiation.yml");
                continue;
            }
            double effectRad = effect.getDouble("radiation");
            int amplifier = effect.getInt("amplifier");
            boolean ambient = effect.getBoolean("ambient");
            boolean particle = effect.getBoolean("particle");
            boolean icon = effect.getBoolean("icon");
            PotionEffect potionEffect = new PotionEffect(effectType, 999999999, amplifier, ambient, particle, icon);
            RadiationEffect radiationEffect = new RadiationEffect(potionEffect, effectRad);
            getConfigManager().getManager().getRadiationManager().getRadiation().getEffects().add(radiationEffect);
        }
    }
}
