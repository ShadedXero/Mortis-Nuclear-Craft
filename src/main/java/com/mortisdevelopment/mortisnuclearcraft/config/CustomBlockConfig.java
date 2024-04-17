package com.mortisdevelopment.mortisnuclearcraft.config;

import com.mortisdevelopment.mortisnuclearcraft.customblocks.CustomBlock;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;

public class CustomBlockConfig extends Config {

    public CustomBlockConfig(ConfigManager configManager) {
        super("customblocks.yml", configManager);
    }

    @Override
    public void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        loadCustomBlocks(config.getConfigurationSection("custom-blocks"));
    }

    private void loadCustomBlocks(ConfigurationSection customBlocks) {
        if (customBlocks == null) {
            return;
        }
        for (String id : customBlocks.getKeys(false)) {
            ConfigurationSection section = customBlocks.getConfigurationSection(id);
            if (section == null) {
                continue;
            }
            String itemId = section.getString("item");
            if (itemId == null) {
                continue;
            }
            ItemStack item = getConfigManager().getManager().getItemManager().getItem(itemId);
            if (item == null) {
                continue;
            }
            List<String> keys = section.getStringList("keys");
            CustomBlock customBlock = new CustomBlock(id, item, keys);
            getConfigManager().getManager().getCustomBlockManager().getCustomBlockById().put(id, customBlock);
        }
    }
}
