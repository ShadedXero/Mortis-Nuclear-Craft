package me.none030.mortisnuclearcraft.config;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.nuclearcraft.armors.ArmorManager;
import me.none030.mortisnuclearcraft.nuclearcraft.armors.RadiationArmor;
import me.none030.mortisnuclearcraft.utils.radiation.RadiationType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class ArmorsConfig extends Config {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();

    public ArmorsConfig(ConfigManager configManager) {
        super("armors.yml", configManager);
    }

    @Override
    public void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = config.getConfigurationSection("armors");
        if (section == null) {
            plugin.getLogger().severe("'armors' section could not be found in armors.yml");
            plugin.getLogger().severe("Please add the 'armors' section back or regenerate the armors.yml file");
            return;
        }
        getConfigManager().getManager().setArmorManager(new ArmorManager(getConfigManager().getManager().getRadiationManager()));
        loadArmors(section);
    }

    private void loadArmors(ConfigurationSection section) {
        for (String key : section.getKeys(false)) {
            ConfigurationSection armor = section.getConfigurationSection(key);
            if (armor == null) {
                plugin.getLogger().severe("Detected a problem with '" + key + "' at 'armors' section in armors.yml");
                continue;
            }
            String itemId = armor.getString("item");
            ItemStack item = getConfigManager().getManager().getItemManager().getItem(itemId);
            if (item == null) {
                plugin.getLogger().severe("Detected a problem with 'item' at '" + key + "' section in 'armors' section in addons.yml");
                plugin.getLogger().severe("Please enter a valid item id");
                continue;
            }
            RadiationType type;
            try {
                type = RadiationType.valueOf(armor.getString("type"));
            }catch (IllegalArgumentException exp) {
                plugin.getLogger().severe("Detected a problem with 'type' at '" + key + "' section in 'armors' section in addons.yml");
                plugin.getLogger().severe("Please enter a valid type");
                continue;
            }
            double radiation = armor.getDouble("radiation");
            int weight = armor.getInt("weight");
            RadiationArmor radiationArmor = new RadiationArmor(key, item, type, radiation, weight);
            getConfigManager().getManager().getArmorManager().getArmors().add(radiationArmor);
            getConfigManager().getManager().getArmorManager().getArmorById().put(key, radiationArmor);
            addRecipe(armor.getConfigurationSection("recipes"), radiationArmor.getArmor());
        }
    }
}
