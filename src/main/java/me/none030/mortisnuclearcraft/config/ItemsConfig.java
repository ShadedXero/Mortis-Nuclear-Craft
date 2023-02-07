package me.none030.mortisnuclearcraft.config;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ItemsConfig {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final ConfigManager configManager;

    public ItemsConfig(ConfigManager configManager) {
        this.configManager = configManager;
        loadConfig();
    }

    private void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        loadItems(config.getConfigurationSection("items"));
    }

    private void loadItems(ConfigurationSection items) {
        if (items == null) {
            plugin.getLogger().severe("'items' section could not be found in items.yml");
            plugin.getLogger().severe("Please add the 'items' section back or regenerate the items.yml file");
            return;
        }
        for (String key : items.getKeys(false)) {
            ConfigurationSection section = items.getConfigurationSection(key);
            if (section == null) {
                plugin.getLogger().severe("Detected a problem with '" + key + "' in items.yml");
                continue;
            }
            Material material;
            try {
                material = Material.valueOf(section.getString("material"));
            }catch (IllegalArgumentException exp) {
                plugin.getLogger().severe("Detected a problem with 'material' at '" + key + "' in items.yml");
                plugin.getLogger().severe("Please enter a valid material id");
                continue;
            }
            int amount = section.getInt("amount");
            ItemBuilder builder = new ItemBuilder(material, amount);
            if (section.contains("custom-model-data")) {
                builder.setCustomModelData(section.getInt("custom-model-data"));
            }
            if (section.contains("name")) {
                builder.setName(section.getString("name"));
            }
            if (section.contains("lore")) {
                builder.setLore(section.getStringList("lore"));
            }
            if (section.contains("enchants")) {
                builder.setEnchants(section.getStringList("enchants"));
            }
            if (section.contains("flags")) {
                builder.setFlags(section.getStringList("flags"));
            }
            configManager.getManager().getItemManager().getItems().add(builder.getItem());
            configManager.getManager().getItemManager().getItemById().put(key, builder.getItem());
        }
    }

    private File saveConfig() {
        File file = new File(plugin.getDataFolder(), "items.yml");
        if (!file.exists()) {
            plugin.saveResource("items.yml", false);
        }
        return file;
    }
}
