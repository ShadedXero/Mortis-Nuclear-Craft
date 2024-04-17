package com.mortisdevelopment.mortisnuclearcraft.config;

import com.mortisdevelopment.mortisnuclearcraft.customblocks.CustomBlock;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.waste.WasteManager;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.waste.WasteType;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.waste.types.*;
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
        getConfigManager().getManager().setWasteManager(new WasteManager(getConfigManager().getManager().getDataManager(), getConfigManager().getManager().getRadiationManager()));
        loadNuclearWaste(config.getConfigurationSection("nuclear-waste"));
    }

    private void loadNuclearWaste(ConfigurationSection section) {
        if (section == null) {
            return;
        }
        for (String id : section.getKeys(false)) {
            ConfigurationSection wasteSection = section.getConfigurationSection(id);
            if (wasteSection == null) {
               return;
            }
            WasteType type;
            try {
                type = WasteType.valueOf(wasteSection.getString("type"));
            }catch (IllegalArgumentException exp) {
                continue;
            }
            String object = wasteSection.getString("object");
            if (object == null) {
               continue;
            }
            double radiation = wasteSection.getDouble("radiation");
            int radius = wasteSection.getInt("radius");
            Waste waste = getWaste(type, object, id, radiation, radius);
            if (waste == null) {
                continue;
            }
            getConfigManager().getManager().getWasteManager().getWasteById().put(id, waste);
        }
    }

    private Waste getWaste(WasteType type, String object, String id, double radiation, int radius) {
        if (type.equals(WasteType.ITEM) || type.equals(WasteType.DROPPED) || type.equals(WasteType.BLOCK)) {
            ItemStack item = getConfigManager().getManager().getItemManager().getItem(object);
            if (item == null) {
                return null;
            }
            if (type.equals(WasteType.ITEM)) {
                return new ItemWaste(id, radiation, item);
            }
            if (type.equals(WasteType.DROPPED)) {
                return new DroppedWaste(id, radiation, radius, item);
            }
            return new BlockWaste(id, radiation, radius, item);
        }
        if (type.equals(WasteType.CUSTOM_BLOCK)) {
            CustomBlock customBlock = getConfigManager().getManager().getCustomBlockManager().getCustomBlockById().get(object);
            if (customBlock == null) {
                return null;
            }
            return new CustomBlockWaste(id, radiation, radius, customBlock);
        }
        return null;
    }
}
