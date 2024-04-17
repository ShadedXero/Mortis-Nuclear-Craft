package com.mortisdevelopment.mortisnuclearcraft.config;

import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.drops.Drop;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;

public class DropsConfig extends Config {

    public DropsConfig(ConfigManager configManager) {
        super("drops.yml", configManager);
    }

    @Override
    public void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        loadDrops(config.getConfigurationSection("drops"));
    }

    private void loadDrops(ConfigurationSection drops) {
        if (drops == null) {
            getPlugin().getLogger().severe("'drops' section could not be found in drops.yml");
            getPlugin().getLogger().severe("Please add the 'drops' section back or regenerate the drops.yml file");
            return;
        }
        for (String key : drops.getKeys(false)) {
            ConfigurationSection section = drops.getConfigurationSection(key);
            if (section == null) {
                getPlugin().getLogger().severe("Detected a problem with '" + key + "' in drops.yml");
                continue;
            }
            String itemId = section.getString("item");
            ItemStack item = getConfigManager().getManager().getItemManager().getItem(itemId);
            if (item == null) {
                getPlugin().getLogger().severe("No item could be found with the id '" + itemId + "' at '" + key + "' in drops.yml");
                getPlugin().getLogger().severe("Please use a valid item id from items.yml");
                continue;
            }
            HashMap<Material, Double> blocksList = null;
            ConfigurationSection blocks = section.getConfigurationSection("blocks");
            if (blocks != null) {
                blocksList = new HashMap<>();
                for (String type : blocks.getKeys(false)) {
                    Material material;
                    try {
                        material = Material.valueOf(type);
                    }catch (IllegalArgumentException exp) {
                        getPlugin().getLogger().severe("Detected a problem with material '" + type + "' at '" + key + "' in drops.yml");
                        continue;
                    }
                    double chance = blocks.getDouble(type);
                    blocksList.put(material, chance);
                }
            }
            HashMap<EntityType, Double> mobsList = null;
            ConfigurationSection mobs = section.getConfigurationSection("mobs");
            if (mobs != null) {
                mobsList = new HashMap<>();
                for (String type : mobs.getKeys(false)) {
                    EntityType entity;
                    try {
                        entity = EntityType.valueOf(type);
                    }catch (IllegalArgumentException exp) {
                        getPlugin().getLogger().severe("Detected a problem with entity '" + type + "' at '" + key + "' in drops.yml");
                        continue;
                    }
                    double chance = mobs.getDouble(type);
                    mobsList.put(entity, chance);
                }
            }
            Drop drop = new Drop(key, item, blocksList, mobsList);
            getConfigManager().getManager().getDropManager().getDrops().add(drop);
            getConfigManager().getManager().getDropManager().getDropById().put(key, drop);
        }
    }
}
