package me.none030.mortisnuclearcraft.config;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.drops.Drop;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;

public class DropsConfig {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final ConfigManager configManager;

    public DropsConfig(ConfigManager configManager) {
        this.configManager = configManager;
        loadConfig();
    }

    private void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        loadDrops(config.getConfigurationSection("drops"));
    }

    private void loadDrops(ConfigurationSection drops) {
        if (drops == null) {
            plugin.getLogger().severe("'drops' section could not be found in drops.yml");
            plugin.getLogger().severe("Please add the 'drops' section back or regenerate the drops.yml file");
            return;
        }
        for (String key : drops.getKeys(false)) {
            ConfigurationSection section = drops.getConfigurationSection(key);
            if (section == null) {
                plugin.getLogger().severe("Detected a problem with '" + key + "' in drops.yml");
                continue;
            }
            String itemId = section.getString("item");
            ItemStack item = configManager.getManager().getItemManager().getItemById().get(itemId);
            if (item == null) {
                plugin.getLogger().severe("No item could be found with the id '" + itemId + "' at '" + key + "' in drops.yml");
                plugin.getLogger().severe("Please use a valid item id from items.yml");
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
                        plugin.getLogger().severe("Detected a problem with material '" + type + "' at '" + key + "' in drops.yml");
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
                        plugin.getLogger().severe("Detected a problem with entity '" + type + "' at '" + key + "' in drops.yml");
                        continue;
                    }
                    double chance = mobs.getDouble(type);
                    mobsList.put(entity, chance);
                }
            }
            Drop drop = new Drop(key, item, blocksList, mobsList);
            configManager.getManager().getDropManager().getDrops().add(drop);
            configManager.getManager().getDropManager().getDropById().put(key, drop);
        }
    }

    private File saveConfig() {
        File file = new File(plugin.getDataFolder(), "drops.yml");
        if (!file.exists()) {
            plugin.saveResource("drops.yml", false);
        }
        return file;
    }
}
