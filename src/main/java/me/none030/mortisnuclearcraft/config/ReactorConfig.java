package me.none030.mortisnuclearcraft.config;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.centrifuge.Centrifuge;
import me.none030.mortisnuclearcraft.centrifuge.CentrifugeManager;
import me.none030.mortisnuclearcraft.centrifuge.CentrifugeRecipe;
import me.none030.mortisnuclearcraft.reactor.Reactor;
import me.none030.mortisnuclearcraft.reactor.ReactorExplosion;
import me.none030.mortisnuclearcraft.reactor.ReactorManager;
import me.none030.mortisnuclearcraft.reactor.ReactorRecipe;
import me.none030.mortisnuclearcraft.structures.Structure;
import me.none030.mortisnuclearcraft.utils.Fuel;
import me.none030.mortisnuclearcraft.utils.centrifuge.CentrifugeMenuItems;
import me.none030.mortisnuclearcraft.utils.chance.ChanceContainer;
import me.none030.mortisnuclearcraft.utils.reactor.ReactorMenuItems;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static me.none030.mortisnuclearcraft.utils.MessageUtils.colorMessage;

public class ReactorConfig {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final ConfigManager configManager;

    public ReactorConfig(ConfigManager configManager) {
        this.configManager = configManager;
        loadConfig();
    }

    private void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = config.getConfigurationSection("reactor");
        if (section == null) {
            plugin.getLogger().severe("'centrifuge' section could not be found in reactor.yml");
            plugin.getLogger().severe("Please add the 'centrifuge' section back or regenerate the reactor.yml file");
            return;
        }
        configManager.getManager().setReactorManager(new ReactorManager(configManager.getManager().getDataManager(), configManager.getManager().getRadiationManager()));
        loadStructure(section.getString("structure"));
        loadExplosion(section.getConfigurationSection("explosion"));
        loadMenuItems(section.getConfigurationSection("menu-items"));
        loadFuels(section.getConfigurationSection("fuel-powers"));
        loadRecipes(section.getConfigurationSection("recipes"));
    }

    private void loadStructure(String structureId) {
        if (structureId == null) {
            plugin.getLogger().severe("Detected a problem with 'structure' at 'reactor' section in reactor.yml");
            plugin.getLogger().severe("Please enter a valid structure id");
            return;
        }
        Structure structure = configManager.getManager().getStructureManager().getStructureById().get(structureId);
        if (structure == null) {
            plugin.getLogger().severe("Detected a problem with 'structure' at 'reactor' section in reactor.yml");
            plugin.getLogger().severe("Please enter a valid structure id");
            return;
        }
        configManager.getManager().getReactorManager().setReactor(new Reactor(structure));
    }

    private void loadExplosion(ConfigurationSection explosion) {
        if (explosion == null) {
            return;
        }
        int strength = explosion.getInt("explosion-strength");
        int radius = explosion.getInt("radiation-radius");
        long duration = explosion.getLong("radiation-duration");
        double radiation = explosion.getDouble("radiation-per-second");
        boolean vehicles = explosion.getBoolean("destroy-vehicles");
        ReactorExplosion reactorExplosion = new ReactorExplosion(configManager.getManager().getReactorManager(), strength, radius, duration, radiation, vehicles);
        configManager.getManager().getReactorManager().setReactorExplosion(reactorExplosion);
    }

    private void loadMenuItems(ConfigurationSection menuItems) {
        if (menuItems == null) {
            return;
        }
        String title = menuItems.getString("title");
        if (title == null) {
            plugin.getLogger().severe("Detected a problem with 'title' at 'menu-items' section in 'reactor' section in reactor.yml");
            return;
        }
        title = colorMessage(title);
        String filterId = menuItems.getString("filter");
        ItemStack filter = configManager.getManager().getItemManager().getItemById().get(filterId);
        if (filter == null) {
            plugin.getLogger().severe("Detected a problem with 'filter' item at 'menu-items' section in 'reactor' section in reactor.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        String inputId = menuItems.getString("input");
        ItemStack input = configManager.getManager().getItemManager().getItemById().get(inputId);
        if (input == null) {
            plugin.getLogger().severe("Detected a problem with 'input' item at 'menu-items' section in 'reactor' section in reactor.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        String outputId = menuItems.getString("output");
        ItemStack output = configManager.getManager().getItemManager().getItemById().get(outputId);
        if (output == null) {
            plugin.getLogger().severe("Detected a problem with 'output' item at 'menu-items' section in 'reactor' section in reactor.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        String fuelId = menuItems.getString("fuel");
        ItemStack fuel = configManager.getManager().getItemManager().getItemById().get(fuelId);
        if (fuel == null) {
            plugin.getLogger().severe("Detected a problem with 'fuel' item at 'menu-items' section in 'reactor' section in reactor.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        String fuelCalculatorId = menuItems.getString("fuel-calculator");
        ItemStack fuelCalculator = configManager.getManager().getItemManager().getItemById().get(fuelCalculatorId);
        if (fuelCalculator == null) {
            plugin.getLogger().severe("Detected a problem with 'fuel-calculator' item at 'menu-items' section in 'reactor' section in reactor.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        String waterCalculatorId = menuItems.getString("water-calculator");
        ItemStack waterCalculator = configManager.getManager().getItemManager().getItemById().get(waterCalculatorId);
        if (waterCalculator == null) {
            plugin.getLogger().severe("Detected a problem with 'water-calculator' item at 'menu-items' section in 'reactor' section in reactor.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        String manualModeId = menuItems.getString("manual-mode");
        ItemStack manualMode = configManager.getManager().getItemManager().getItemById().get(manualModeId);
        if (manualMode == null) {
            plugin.getLogger().severe("Detected a problem with 'manual-mode' item at 'menu-items' section in 'reactor' section in reactor.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        String redstoneModeId = menuItems.getString("redstone-mode");
        ItemStack redstoneMode = configManager.getManager().getItemManager().getItemById().get(redstoneModeId);
        if (redstoneMode == null) {
            plugin.getLogger().severe("Detected a problem with 'redstone-mode' item at 'menu-items' section in 'reactor' section in reactor.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        String animation1Id = menuItems.getString("animation-1");
        ItemStack animation1 = configManager.getManager().getItemManager().getItemById().get(animation1Id);
        if (animation1 == null) {
            plugin.getLogger().severe("Detected a problem with 'animation-1' item at 'menu-items' section in 'reactor' section in reactor.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        String animation2Id = menuItems.getString("animation-2");
        ItemStack animation2 = configManager.getManager().getItemManager().getItemById().get(animation2Id);
        if (animation2 == null) {
            plugin.getLogger().severe("Detected a problem with 'animation-2' item at 'menu-items' section in 'reactor' section in reactor.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        ReactorMenuItems reactorMenuItems = new ReactorMenuItems(title, filter, input, output, fuel, fuelCalculator, waterCalculator, manualMode, redstoneMode, animation1, animation2);
        configManager.getManager().getReactorManager().setMenuItems(reactorMenuItems);
    }

    private void loadFuels(ConfigurationSection fuels) {
        if (fuels == null) {
            plugin.getLogger().severe("'fuel-powers' section could not be found at 'reactor' section in reactor.yml");
            plugin.getLogger().severe("Please add the 'fuel-powers' section back in 'reactor' section or regenerate the reactor.yml file");
            return;
        }
        for (String key : fuels.getKeys(false)) {
            ItemStack item = configManager.getManager().getItemManager().getItemById().get(key);
            if (item == null) {
                plugin.getLogger().severe("Detected a problem with 'item' at '" + key + "' section in 'fuel-powers' section in 'reactor' section in reactor.yml");
                plugin.getLogger().severe("Please enter a valid item id");
                continue;
            }
            int power = fuels.getInt(key);
            Fuel fuel = new Fuel(item, power);
            configManager.getManager().getReactorManager().getReactor().getFuels().add(fuel);
        }
    }

    private void loadRecipes(ConfigurationSection recipes) {
        if (recipes == null) {
            plugin.getLogger().severe("'fuel-powers' section could not be found at 'reactor' section in reactor.yml");
            plugin.getLogger().severe("Please add the 'fuel-powers' section back in 'reactor' section or regenerate the reactor.yml file");
            return;
        }
        for (String key : recipes.getKeys(false)) {
            ConfigurationSection section = recipes.getConfigurationSection(key);
            if (section == null) {
                plugin.getLogger().severe("Detected a problem with '" + key + "' at 'recipes' section in 'reactor' section in reactor.yml");
                continue;
            }
            String inputId = section.getString("input");
            ItemStack input = configManager.getManager().getItemManager().getItemById().get(inputId);
            if (input == null) {
                plugin.getLogger().severe("Detected a problem with 'input' item at '" + key + "' section in 'recipes' section in 'reactor' section in reactor.yml");
                plugin.getLogger().severe("Please enter a valid item id");
                return;
            }
            String outputId = section.getString("output");
            ItemStack output = configManager.getManager().getItemManager().getItemById().get(outputId);
            if (output == null) {
                plugin.getLogger().severe("Detected a problem with 'output' item at '" + key + "' section in 'recipes' section in 'reactor' section in reactor.yml");
                plugin.getLogger().severe("Please enter a valid item id");
                return;
            }
            long duration = section.getLong("duration");
            int fuelPower = section.getInt("fuel-power");
            int water = section.getInt("water");
            ReactorRecipe reactorRecipe = new ReactorRecipe(configManager.getManager().getReactorManager(), key, input, output, duration, fuelPower, water);
            configManager.getManager().getReactorManager().getReactor().getRecipes().add(reactorRecipe);
            configManager.getManager().getReactorManager().getReactor().getRecipeById().put(key, reactorRecipe);
        }
    }

    private File saveConfig() {
        File file = new File(plugin.getDataFolder(), "reactor.yml");
        if (!file.exists()) {
            plugin.saveResource("reactor.yml", false);
        }
        return file;
    }
}
