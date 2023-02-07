package me.none030.mortisnuclearcraft.config;

import me.none030.mortisnuclearcraft.managers.NuclearCraftManager;
import me.none030.mortisnuclearcraft.utils.recipe.Recipe;
import me.none030.mortisnuclearcraft.utils.recipe.RecipeType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private final NuclearCraftManager manager;
    private final MessagesConfig messagesConfig;
    private final DatabaseConfig databaseConfig;
    private final ItemsConfig itemsConfig;
    private final DropsConfig dropsConfig;
    private final RadiationConfig radiationConfig;
    private final ArmorsConfig armorsConfig;
    private final StructuresConfig structuresConfig;
    private final BombsConfig bombsConfig;
    private final CentrifugeConfig centrifugeConfig;
    private final ReactorConfig reactorConfig;
    private final AddonsConfig addonsConfig;

    public ConfigManager(NuclearCraftManager manager) {
        this.manager = manager;
        this.messagesConfig = new MessagesConfig(this);
        this.databaseConfig = new DatabaseConfig(this);
        this.itemsConfig = new ItemsConfig(this);
        this.dropsConfig = new DropsConfig(this);
        this.radiationConfig = new RadiationConfig(this);
        this.armorsConfig = new ArmorsConfig(this);
        this.structuresConfig = new StructuresConfig(this);
        this.bombsConfig = new BombsConfig(this);
        this.centrifugeConfig = new CentrifugeConfig(this);
        this.reactorConfig = new ReactorConfig(this);
        this.addonsConfig = new AddonsConfig(this);
    }

    public void addRecipe(ConfigurationSection recipes, ItemStack result) {
        if (recipes == null) {
            return;
        }
        for (String recipeKey : recipes.getKeys(false)) {
            ConfigurationSection recipe = recipes.getConfigurationSection(recipeKey);
            if (recipe == null) {
                continue;
            }
            RecipeType recipeType;
            try {
                recipeType = RecipeType.valueOf(recipe.getString("type"));
            }catch (IllegalArgumentException exp) {
                continue;
            }
            String ingredientIds = recipe.getString("recipe");
            if (ingredientIds == null) {
                continue;
            }
            List<String> raw = List.of(ingredientIds.split(","));
            List<ItemStack> ingredients = new ArrayList<>();
            for (String line : raw) {
                ItemStack ingredient = manager.getItemManager().getItemById().get(line);
                if (ingredient == null) {
                    continue;
                }
                ingredients.add(ingredient);
            }
            Recipe pillRecipe = new Recipe(result, ingredients, recipeType);
            pillRecipe.addRecipe();
        }
    }

    public NuclearCraftManager getManager() {
        return manager;
    }

    public MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }

    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

    public ItemsConfig getItemsConfig() {
        return itemsConfig;
    }

    public DropsConfig getDropsConfig() {
        return dropsConfig;
    }

    public RadiationConfig getRadiationConfig() {
        return radiationConfig;
    }

    public ArmorsConfig getArmorsConfig() {
        return armorsConfig;
    }

    public StructuresConfig getStructuresConfig() {
        return structuresConfig;
    }

    public BombsConfig getBombsConfig() {
        return bombsConfig;
    }

    public CentrifugeConfig getCentrifugeConfig() {
        return centrifugeConfig;
    }

    public ReactorConfig getReactorConfig() {
        return reactorConfig;
    }

    public AddonsConfig getAddonsConfig() {
        return addonsConfig;
    }
}
