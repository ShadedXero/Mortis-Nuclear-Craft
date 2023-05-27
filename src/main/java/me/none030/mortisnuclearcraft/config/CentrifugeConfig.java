package me.none030.mortisnuclearcraft.config;

import me.none030.mortisnuclearcraft.menu.MenuItems;
import me.none030.mortisnuclearcraft.nuclearcraft.centrifuge.Centrifuge;
import me.none030.mortisnuclearcraft.nuclearcraft.centrifuge.CentrifugeManager;
import me.none030.mortisnuclearcraft.nuclearcraft.centrifuge.CentrifugeRecipe;
import me.none030.mortisnuclearcraft.structures.Structure;
import me.none030.mortisnuclearcraft.utils.Fuel;
import me.none030.mortisnuclearcraft.utils.Randomizer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CentrifugeConfig extends Config {

    public CentrifugeConfig(ConfigManager configManager) {
        super("centrifuge.yml", configManager);
    }

    @Override
    public void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = config.getConfigurationSection("centrifuge");
        if (section == null) {
            getPlugin().getLogger().severe("'centrifuge' section could not be found in centrifuge.yml");
            getPlugin().getLogger().severe("Please add the 'centrifuge' section back or regenerate the centrifuge.yml file");
            return;
        }
        Centrifuge centrifuge = loadStructure(section.getStringList("structures"));
        if (centrifuge == null) {
            return;
        }
        MenuItems menuItems = loadMenuItems(section.getConfigurationSection("menu-items"));
        if (menuItems == null) {
            return;
        }
        getConfigManager().getManager().setCentrifugeManager(new CentrifugeManager(centrifuge, menuItems));
        loadFuels(section.getConfigurationSection("fuel-powers"));
        loadRecipes(section.getConfigurationSection("recipes"));
        getConfigManager().getManager().getCentrifugeManager().addMessages(loadMessages(section.getConfigurationSection("messages")));
    }

    private Centrifuge loadStructure(List<String> structureIds) {
        if (structureIds == null || structureIds.size() == 0) {
            getPlugin().getLogger().severe("Detected a problem with 'structure' at 'centrifuge' section in centrifuge.yml");
            getPlugin().getLogger().severe("Please enter a valid structure id");
            return null;
        }
        List<Structure> structures = new ArrayList<>();
        for (String structureId : structureIds) {
            Structure structure = getConfigManager().getManager().getStructureManager().getStructureById().get(structureId);
            if (structure == null) {
                getPlugin().getLogger().severe("Detected a problem with 'structure' at 'centrifuge' section in centrifuge.yml");
                getPlugin().getLogger().severe("Please enter a valid structure id");
                continue;
            }
            structures.add(structure);
        }
        return new Centrifuge(structures);
    }

    private void loadFuels(ConfigurationSection fuels) {
        if (fuels == null) {
            getPlugin().getLogger().severe("'fuel-powers' section could not be found at 'centrifuge' section in centrifuge.yml");
            getPlugin().getLogger().severe("Please add the 'fuel-powers' section back in 'centrifuge' section or regenerate the centrifuge.yml file");
            return;
        }
        for (String key : fuels.getKeys(false)) {
            ItemStack item = getConfigManager().getManager().getItemManager().getItem(key);
            if (item == null) {
                getPlugin().getLogger().severe("Detected a problem with 'item' at '" + key + "' section in 'fuel-powers' section in 'centrifuge' section in centrifuge.yml");
                getPlugin().getLogger().severe("Please enter a valid item id");
                continue;
            }
            int power = fuels.getInt(key);
            Fuel fuel = new Fuel(item, power);
            getConfigManager().getManager().getCentrifugeManager().getCentrifuge().getFuels().add(fuel);
        }
    }

    private void loadRecipes(ConfigurationSection recipes) {
        if (recipes == null) {
            getPlugin().getLogger().severe("'fuel-powers' section could not be found at 'centrifuge' section in centrifuge.yml");
            getPlugin().getLogger().severe("Please add the 'fuel-powers' section back in 'centrifuge' section or regenerate the centrifuge.yml file");
            return;
        }
        for (String key : recipes.getKeys(false)) {
            ConfigurationSection section = recipes.getConfigurationSection(key);
            if (section == null) {
                getPlugin().getLogger().severe("Detected a problem with '" + key + "' at 'recipes' section in centrifuge section in centrifuge.yml");
                continue;
            }
            String input1Id = section.getString("input-1");
            ItemStack input1 = getConfigManager().getManager().getItemManager().getItem(input1Id);
            String input2Id = section.getString("input-2");
            ItemStack input2 = getConfigManager().getManager().getItemManager().getItem(input2Id);
            Randomizer<ItemStack> output1 = new Randomizer<>();
            List<String> output1List = new ArrayList<>(section.getStringList("output-1"));
            for (String output1Id : output1List) {
                String[] rawOutput1 = output1Id.split(":");
                String itemId = rawOutput1[0];
                double chance = Double.parseDouble(rawOutput1[1]);
                ItemStack item = getConfigManager().getManager().getItemManager().getItem(itemId);
                if (item == null) {
                    continue;
                }
                output1.addEntry(item, chance);
            }
            Randomizer<ItemStack> output2 = new Randomizer<>();
            List<String> output2List = new ArrayList<>(section.getStringList("output-2"));
            for (String output2Id : output2List) {
                String[] rawOutput2 = output2Id.split(":");
                String itemId = rawOutput2[0];
                double chance = Double.parseDouble(rawOutput2[1]);
                ItemStack item = getConfigManager().getManager().getItemManager().getItem(itemId);
                if (item == null) {
                    continue;
                }
                output2.addEntry(item, chance);
            }
            long duration = section.getLong("duration");
            int fuelPower = section.getInt("fuel-power");
            CentrifugeRecipe centrifugeRecipe = new CentrifugeRecipe(key, input1, input2, output1, output2, duration, fuelPower);
            getConfigManager().getManager().getCentrifugeManager().getCentrifuge().getRecipes().add(centrifugeRecipe);
            getConfigManager().getManager().getCentrifugeManager().getCentrifuge().getRecipeById().put(key, centrifugeRecipe);
        }
    }
}
