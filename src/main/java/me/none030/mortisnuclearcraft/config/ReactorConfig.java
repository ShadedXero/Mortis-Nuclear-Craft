package me.none030.mortisnuclearcraft.config;

import me.none030.mortisnuclearcraft.menu.MenuItems;
import me.none030.mortisnuclearcraft.nuclearcraft.reactor.Reactor;
import me.none030.mortisnuclearcraft.nuclearcraft.reactor.ReactorManager;
import me.none030.mortisnuclearcraft.nuclearcraft.reactor.ReactorRecipe;
import me.none030.mortisnuclearcraft.structures.Structure;
import me.none030.mortisnuclearcraft.utils.Fuel;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReactorConfig extends Config {

    public ReactorConfig(ConfigManager configManager) {
        super("reactor.yml", configManager);
    }

    @Override
    public void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = config.getConfigurationSection("reactor");
        if (section == null) {
            getPlugin().getLogger().severe("'centrifuge' section could not be found in reactor.yml");
            getPlugin().getLogger().severe("Please add the 'centrifuge' section back or regenerate the reactor.yml file");
            return;
        }
        List<Structure> structures = loadStructure(section.getStringList("structures"));
        if (structures == null) {
            return;
        }
        Reactor reactor = loadExplosion(section.getConfigurationSection("explosion"), structures);
        if (reactor == null) {
            return;
        }
        MenuItems menuItems = loadMenuItems(section.getConfigurationSection("menu-items"));
        if (menuItems == null) {
            return;
        }
        getConfigManager().getManager().setReactorManager(new ReactorManager(getConfigManager().getManager().getRadiationManager(), reactor, menuItems));
        loadFuels(section.getConfigurationSection("fuel-powers"));
        loadRecipes(section.getConfigurationSection("recipes"));
        getConfigManager().getManager().getReactorManager().addMessages(loadMessages(section.getConfigurationSection("messages")));
    }

    private List<Structure> loadStructure(List<String> structureIds) {
        if (structureIds == null || structureIds.size() == 0) {
            getPlugin().getLogger().severe("Detected a problem with 'structure' at 'reactor' section in reactor.yml");
            getPlugin().getLogger().severe("Please enter a valid structure id");
            return null;
        }
        List<Structure> structures = new ArrayList<>();
        for (String structureId : structureIds) {
            Structure structure = getConfigManager().getManager().getStructureManager().getStructureById().get(structureId);
            if (structure == null) {
                getPlugin().getLogger().severe("Detected a problem with 'structure' at 'reactor' section in reactor.yml");
                getPlugin().getLogger().severe("Please enter a valid structure id");
                continue;
            }
            structures.add(structure);
        }
        return structures;
    }

    private Reactor loadExplosion(ConfigurationSection explosion, List<Structure> structures) {
        if (explosion == null) {
            return null;
        }
        int strength = explosion.getInt("explosion-strength");
        int radius = explosion.getInt("radiation-radius");
        long duration = explosion.getLong("radiation-duration");
        double radiation = explosion.getDouble("radiation-per-second");
        boolean vehicles = explosion.getBoolean("destroy-vehicles");
        boolean drain = explosion.getBoolean("drain");
        boolean fire = explosion.getBoolean("fire");
        boolean blockDamage = explosion.getBoolean("block-damage");
        boolean townyBlockDamage = explosion.getBoolean("towny-block-damage");
        boolean blockRegen = explosion.getBoolean("block-regen");
        boolean townyRegen = explosion.getBoolean("towny-regen");
        long regenTime = explosion.getLong("regen-time");
        return new Reactor(radius, structures, strength, duration, radiation, vehicles, drain, fire, blockDamage, townyBlockDamage, blockRegen, townyRegen, regenTime);
    }

    private void loadFuels(ConfigurationSection fuels) {
        if (fuels == null) {
            getPlugin().getLogger().severe("'fuel-powers' section could not be found at 'reactor' section in reactor.yml");
            getPlugin().getLogger().severe("Please add the 'fuel-powers' section back in 'reactor' section or regenerate the reactor.yml file");
            return;
        }
        for (String key : fuels.getKeys(false)) {
            ItemStack item = getConfigManager().getManager().getItemManager().getItem(key);
            if (item == null) {
                getPlugin().getLogger().severe("Detected a problem with 'item' at '" + key + "' section in 'fuel-powers' section in 'reactor' section in reactor.yml");
                getPlugin().getLogger().severe("Please enter a valid item id");
                continue;
            }
            int power = fuels.getInt(key);
            Fuel fuel = new Fuel(item, power);
            getConfigManager().getManager().getReactorManager().getReactor().getFuels().add(fuel);
        }
    }

    private void loadRecipes(ConfigurationSection recipes) {
        if (recipes == null) {
            getPlugin().getLogger().severe("'fuel-powers' section could not be found at 'reactor' section in reactor.yml");
            getPlugin().getLogger().severe("Please add the 'fuel-powers' section back in 'reactor' section or regenerate the reactor.yml file");
            return;
        }
        for (String key : recipes.getKeys(false)) {
            ConfigurationSection section = recipes.getConfigurationSection(key);
            if (section == null) {
                getPlugin().getLogger().severe("Detected a problem with '" + key + "' at 'recipes' section in 'reactor' section in reactor.yml");
                continue;
            }
            String inputId = section.getString("input");
            ItemStack input = getConfigManager().getManager().getItemManager().getItem(inputId);
            if (input == null) {
                getPlugin().getLogger().severe("Detected a problem with 'input' item at '" + key + "' section in 'recipes' section in 'reactor' section in reactor.yml");
                getPlugin().getLogger().severe("Please enter a valid item id");
                continue;
            }
            String outputId = section.getString("output");
            ItemStack output = getConfigManager().getManager().getItemManager().getItem(outputId);
            if (output == null) {
                getPlugin().getLogger().severe("Detected a problem with 'output' item at '" + key + "' section in 'recipes' section in 'reactor' section in reactor.yml");
                getPlugin().getLogger().severe("Please enter a valid item id");
                continue;
            }
            String wasteId = section.getString("waste");
            ItemStack waste = getConfigManager().getManager().getItemManager().getItem(wasteId);
            if (waste == null) {
                getPlugin().getLogger().severe("Detected a problem with 'waste' item at '" + key + "' section in 'recipes' section in 'reactor' section in reactor.yml");
                getPlugin().getLogger().severe("Please enter a valid item id");
                continue;
            }
            boolean specificFuel = section.getBoolean("specific-fuel");
            List<ItemStack> fuels = new ArrayList<>();
            List<String> rawFuels = section.getStringList("specific-fuel-used");
            for (String fuelId : rawFuels) {
                ItemStack fuel = getConfigManager().getManager().getItemManager().getItem(fuelId);
                if (fuel == null) {
                    continue;
                }
                fuels.add(fuel);
            }
            long duration = section.getLong("duration");
            int fuelPower = section.getInt("fuel-power");
            int water = section.getInt("water");
            ReactorRecipe reactorRecipe = new ReactorRecipe(key, input, output, waste, specificFuel, fuels, duration, fuelPower, water);
            getConfigManager().getManager().getReactorManager().getReactor().getRecipes().add(reactorRecipe);
            getConfigManager().getManager().getReactorManager().getReactor().getRecipeById().put(key, reactorRecipe);
        }
    }
}
