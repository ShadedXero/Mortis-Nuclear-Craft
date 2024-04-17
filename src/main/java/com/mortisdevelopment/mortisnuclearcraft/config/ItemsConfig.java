package com.mortisdevelopment.mortisnuclearcraft.config;

import com.mortisdevelopment.mortisnuclearcraft.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ItemsConfig extends Config {

    public ItemsConfig(ConfigManager configManager) {
        super("items.yml", configManager);
    }

    @Override
    public void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        loadItems(config.getConfigurationSection("items"));
    }

    private void loadItems(ConfigurationSection items) {
        if (items == null) {
            getPlugin().getLogger().severe("'items' section could not be found in items.yml");
            getPlugin().getLogger().severe("Please add the 'items' section back or regenerate the items.yml file");
            return;
        }
        for (String key : items.getKeys(false)) {
            ConfigurationSection section = items.getConfigurationSection(key);
            if (section == null) {
                getPlugin().getLogger().severe("Detected a problem with '" + key + "' in items.yml");
                continue;
            }
            Material material;
            try {
                material = Material.valueOf(section.getString("material"));
            }catch (IllegalArgumentException exp) {
                getPlugin().getLogger().severe("Detected a problem with 'material' at '" + key + "' in items.yml");
                getPlugin().getLogger().severe("Please enter a valid material id");
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
                builder.addEnchants(section.getStringList("enchants"));
            }
            if (section.contains("flags")) {
                builder.addFlags(section.getStringList("flags"));
            }
            getConfigManager().getManager().getItemManager().addItem(key, builder.getItem());
        }
    }
}
