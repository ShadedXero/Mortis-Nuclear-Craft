package me.none030.mortisnuclearcraft.config;

import me.none030.mortisnuclearcraft.nuclearcraft.waste.Waste;
import me.none030.mortisnuclearcraft.nuclearcraft.waste.WasteManager;
import me.none030.mortisnuclearcraft.utils.waste.WasteType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class WasteConfig extends Config {

    public WasteConfig(ConfigManager configManager) {
        super("nuclearwaste.yml", configManager);
    }

    @Override
    public void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        getConfigManager().getManager().setWasteManager(new WasteManager(getConfigManager().getManager().getRadiationManager()));
        loadNuclearWaste(config.getConfigurationSection("nuclear-waste"));
    }

    private void loadNuclearWaste(ConfigurationSection section) {
        if (section == null) {
            return;
        }
        for (String key : section.getKeys(false)) {
            ConfigurationSection wasteSection = section.getConfigurationSection(key);
            if (wasteSection == null) {
               return;
            }
            String itemId = wasteSection.getString("item");
            if (itemId == null) {
               continue;
            }
            ItemStack item = getConfigManager().getManager().getItemManager().getItem(itemId);
            if (item == null) {
                continue;
            }
            double radiation = wasteSection.getDouble("radiation");
            WasteType type;
            try {
                type = WasteType.valueOf(wasteSection.getString("type"));
            }catch (IllegalArgumentException exp) {
                continue;
            }
            int radius = wasteSection.getInt("radius");
            Waste waste = new Waste(key, item, type, radiation, radius);
            getConfigManager().getManager().getWasteManager().getWasteById().put(key, waste);
        }
    }
}
