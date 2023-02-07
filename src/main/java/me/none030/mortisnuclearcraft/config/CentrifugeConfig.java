package me.none030.mortisnuclearcraft.config;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.centrifuge.Centrifuge;
import me.none030.mortisnuclearcraft.centrifuge.CentrifugeManager;
import me.none030.mortisnuclearcraft.utils.Fuel;
import me.none030.mortisnuclearcraft.centrifuge.CentrifugeRecipe;
import me.none030.mortisnuclearcraft.structures.Structure;
import me.none030.mortisnuclearcraft.utils.centrifuge.CentrifugeMenuItems;
import me.none030.mortisnuclearcraft.utils.chance.ChanceContainer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static me.none030.mortisnuclearcraft.utils.MessageUtils.colorMessage;

public class CentrifugeConfig {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final ConfigManager configManager;

    public CentrifugeConfig(ConfigManager configManager) {
        this.configManager = configManager;
        loadConfig();
    }

    private void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = config.getConfigurationSection("centrifuge");
        if (section == null) {
            plugin.getLogger().severe("'centrifuge' section could not be found in centrifuge.yml");
            plugin.getLogger().severe("Please add the 'centrifuge' section back or regenerate the centrifuge.yml file");
            return;
        }
        configManager.getManager().setCentrifugeManager(new CentrifugeManager(configManager.getManager().getDataManager()));
        loadStructure(section.getString("structure"));
        loadMenuItems(section.getConfigurationSection("menu-items"));
        loadFuels(section.getConfigurationSection("fuel-powers"));
        loadRecipes(section.getConfigurationSection("recipes"));

    }

    private void loadStructure(String structureId) {
        if (structureId == null) {
            plugin.getLogger().severe("Detected a problem with 'structure' at 'centrifuge' section in centrifuge.yml");
            plugin.getLogger().severe("Please enter a valid structure id");
            return;
        }
        Structure structure = configManager.getManager().getStructureManager().getStructureById().get(structureId);
        if (structure == null) {
            plugin.getLogger().severe("Detected a problem with 'structure' at 'centrifuge' section in centrifuge.yml");
            plugin.getLogger().severe("Please enter a valid structure id");
            return;
        }
        configManager.getManager().getCentrifugeManager().setCentrifuge(new Centrifuge(structure));
    }

    private void loadMenuItems(ConfigurationSection menuItems) {
        if (menuItems == null) {
            return;
        }
        String title = menuItems.getString("title");
        if (title == null) {
            plugin.getLogger().severe("Detected a problem with 'title' at 'menu-items' section in 'centrifuge' section in centrifuge.yml");
            return;
        }
        title = colorMessage(title);
        String filterId = menuItems.getString("filter");
        ItemStack filter = configManager.getManager().getItemManager().getItemById().get(filterId);
        if (filter == null) {
            plugin.getLogger().severe("Detected a problem with 'filter' item at 'menu-items' section in 'centrifuge' section in centrifuge.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        String input1Id = menuItems.getString("input-1");
        ItemStack input1 = configManager.getManager().getItemManager().getItemById().get(input1Id);
        if (input1 == null) {
            plugin.getLogger().severe("Detected a problem with 'input-1' item at 'menu-items' section in 'centrifuge' section in centrifuge.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        String input2Id = menuItems.getString("input-2");
        ItemStack input2 = configManager.getManager().getItemManager().getItemById().get(input2Id);
        if (input2 == null) {
            plugin.getLogger().severe("Detected a problem with 'input-2' item at 'menu-items' section in 'centrifuge' section in centrifuge.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        String output1Id = menuItems.getString("output-1");
        ItemStack output1 = configManager.getManager().getItemManager().getItemById().get(output1Id);
        if (output1 == null) {
            plugin.getLogger().severe("Detected a problem with 'output-1' item at 'menu-items' section in 'centrifuge' section in centrifuge.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        String output2Id = menuItems.getString("output-2");
        ItemStack output2 = configManager.getManager().getItemManager().getItemById().get(output2Id);
        if (output2 == null) {
            plugin.getLogger().severe("Detected a problem with 'output-2' item at 'menu-items' section in 'centrifuge' section in centrifuge.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        String fuelId = menuItems.getString("fuel");
        ItemStack fuel = configManager.getManager().getItemManager().getItemById().get(fuelId);
        if (fuel == null) {
            plugin.getLogger().severe("Detected a problem with 'fuel' item at 'menu-items' section in 'centrifuge' section in centrifuge.yml");
            plugin.getLogger().severe("Please enter a valid item id");
           return;
        }
        String fuelCalculatorId = menuItems.getString("fuel-calculator");
        ItemStack fuelCalculator = configManager.getManager().getItemManager().getItemById().get(fuelCalculatorId);
        if (fuelCalculator == null) {
            plugin.getLogger().severe("Detected a problem with 'fuel-calculator' item at 'menu-items' section in 'centrifuge' section in centrifuge.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        String manualModeId = menuItems.getString("manual-mode");
        ItemStack manualMode = configManager.getManager().getItemManager().getItemById().get(manualModeId);
        if (manualMode == null) {
            plugin.getLogger().severe("Detected a problem with 'manual-mode' item at 'menu-items' section in 'centrifuge' section in centrifuge.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        String redstoneModeId = menuItems.getString("redstone-mode");
        ItemStack redstoneMode = configManager.getManager().getItemManager().getItemById().get(redstoneModeId);
        if (redstoneMode == null) {
            plugin.getLogger().severe("Detected a problem with 'redstone-mode' item at 'menu-items' section in 'centrifuge' section in centrifuge.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        String animation1Id = menuItems.getString("animation-1");
        ItemStack animation1 = configManager.getManager().getItemManager().getItemById().get(animation1Id);
        if (animation1 == null) {
            plugin.getLogger().severe("Detected a problem with 'animation-1' item at 'menu-items' section in 'centrifuge' section in centrifuge.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        String animation2Id = menuItems.getString("animation-2");
        ItemStack animation2 = configManager.getManager().getItemManager().getItemById().get(animation2Id);
        if (animation2 == null) {
            plugin.getLogger().severe("Detected a problem with 'animation-2' item at 'menu-items' section in 'centrifuge' section in centrifuge.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        CentrifugeMenuItems centrifugeMenuItems = new CentrifugeMenuItems(title, filter, input1, input2, output1, output2, fuelCalculator, fuel, manualMode, redstoneMode, animation1, animation2);
        configManager.getManager().getCentrifugeManager().setMenuItems(centrifugeMenuItems);
    }

    private void loadFuels(ConfigurationSection fuels) {
        if (fuels == null) {
            plugin.getLogger().severe("'fuel-powers' section could not be found at 'centrifuge' section in centrifuge.yml");
            plugin.getLogger().severe("Please add the 'fuel-powers' section back in 'centrifuge' section or regenerate the centrifuge.yml file");
            return;
        }
        for (String key : fuels.getKeys(false)) {
            ItemStack item = configManager.getManager().getItemManager().getItemById().get(key);
            if (item == null) {
                plugin.getLogger().severe("Detected a problem with 'item' at '" + key + "' section in 'fuel-powers' section in 'centrifuge' section in centrifuge.yml");
                plugin.getLogger().severe("Please enter a valid item id");
                continue;
            }
            int power = fuels.getInt(key);
            Fuel fuel = new Fuel(item, power);
            configManager.getManager().getCentrifugeManager().getCentrifuge().getFuels().add(fuel);
        }
    }

    private void loadRecipes(ConfigurationSection recipes) {
        if (recipes == null) {
            plugin.getLogger().severe("'fuel-powers' section could not be found at 'centrifuge' section in centrifuge.yml");
            plugin.getLogger().severe("Please add the 'fuel-powers' section back in 'centrifuge' section or regenerate the centrifuge.yml file");
            return;
        }
        for (String key : recipes.getKeys(false)) {
            ConfigurationSection section = recipes.getConfigurationSection(key);
            if (section == null) {
                plugin.getLogger().severe("Detected a problem with '" + key + "' at 'recipes' section in centrifuge section in centrifuge.yml");
                continue;
            }
            String input1Id = section.getString("input-1");
            ItemStack input1 = configManager.getManager().getItemManager().getItemById().get(input1Id);
            if (input1 == null) {
                plugin.getLogger().severe("Detected a problem with 'input-1' item at '" + key + "' section in 'recipes' section in 'centrifuge' section in centrifuge.yml");
                plugin.getLogger().severe("Please enter a valid item id");
                return;
            }
            String input2Id = section.getString("input-2");
            ItemStack input2 = configManager.getManager().getItemManager().getItemById().get(input2Id);
            if (input2 == null) {
                plugin.getLogger().severe("Detected a problem with 'input-2' item at '" + key + "' section in 'recipes' section in 'centrifuge' section in centrifuge.yml");
                plugin.getLogger().severe("Please enter a valid item id");
                return;
            }
            ChanceContainer<ItemStack> output1 = new ChanceContainer<>();
            List<String> output1List = new ArrayList<>(section.getStringList("output-1"));
            for (String output1Id : output1List) {
                String[] rawOutput1 = output1Id.split(":");
                String itemId = rawOutput1[0];
                double chance = Double.parseDouble(rawOutput1[1]);
                ItemStack item = configManager.getManager().getItemManager().getItemById().get(itemId);
                if (item == null) {
                    plugin.getLogger().severe("Detected a problem with 'output-1' item '" + itemId + "' at '" + key + "' section in 'recipes' section in 'centrifuge' section in centrifuge.yml");
                    plugin.getLogger().severe("Please enter a valid item id");
                    continue;
                }
                output1.addEntry(item, chance);
            }
            ChanceContainer<ItemStack> output2 = new ChanceContainer<>();
            List<String> output2List = new ArrayList<>(section.getStringList("output-2"));
            for (String output2Id : output2List) {
                String[] rawOutput2 = output2Id.split(":");
                String itemId = rawOutput2[0];
                double chance = Double.parseDouble(rawOutput2[1]);
                ItemStack item = configManager.getManager().getItemManager().getItemById().get(itemId);
                if (item == null) {
                    plugin.getLogger().severe("Detected a problem with 'output-2' item '" + itemId + "' at '" + key + "' section in 'recipes' section in 'centrifuge' section in centrifuge.yml");
                    plugin.getLogger().severe("Please enter a valid item id");
                    continue;
                }
                output2.addEntry(item, chance);
            }
            long duration = section.getLong("duration");
            int fuelPower = section.getInt("fuel-power");
            CentrifugeRecipe centrifugeRecipe = new CentrifugeRecipe(configManager.getManager().getCentrifugeManager(), key, input1, input2, output1, output2, duration, fuelPower);
            configManager.getManager().getCentrifugeManager().getCentrifuge().getRecipes().add(centrifugeRecipe);
            configManager.getManager().getCentrifugeManager().getCentrifuge().getRecipeById().put(key, centrifugeRecipe);
        }
    }

    private File saveConfig() {
        File file = new File(plugin.getDataFolder(), "centrifuge.yml");
        if (!file.exists()) {
            plugin.saveResource("centrifuge.yml", false);
        }
        return file;
    }
}
