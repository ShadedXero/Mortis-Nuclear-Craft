package com.mortisdevelopment.mortisnuclearcraft.config;

import com.mortisdevelopment.mortisnuclearcraft.menu.MenuItems;
import com.mortisdevelopment.mortisnuclearcraft.utils.MessageUtils;
import com.mortisdevelopment.mortisnuclearcraft.MortisNuclearCraft;
import com.mortisdevelopment.mortisnuclearcraft.utils.recipe.Recipe;
import com.mortisdevelopment.mortisnuclearcraft.utils.recipe.RecipeType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Config {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final String fileName;
    private final ConfigManager configManager;

    public Config(String fileName, ConfigManager configManager) {
        this.fileName = fileName;
        this.configManager = configManager;
        loadConfig();
    }

    public abstract void loadConfig();

    public MenuItems loadMenuItems(ConfigurationSection menu) {
        if (menu == null) {
            return null;
        }
        MessageUtils title = new MessageUtils(menu.getString("title"));
        title.color();
        MenuItems menuItems = new MenuItems(title.getMessage());
        for (String key : menu.getKeys(false)) {
            if (key.equalsIgnoreCase("title")) {
                continue;
            }
            String itemId = menu.getString(key);
            if (itemId == null) {
                continue;
            }
            ItemStack item = configManager.getManager().getItemManager().getItem(itemId);
            menuItems.addItem(key.replace("-", "_").toUpperCase(), item);
        }
        return menuItems;
    }

    public HashMap<String, String> loadMessages(ConfigurationSection messages) {
        HashMap<String, String> messageById = new HashMap<>();
        if (messages == null) {
            return messageById;
        }
        for (String key : messages.getKeys(false)) {
            String id = key.replace("-", "_").toUpperCase();
            String message = messages.getString(key);
            MessageUtils editor = new MessageUtils(message);
            editor.color();
            messageById.put(id, editor.getMessage());
        }
        return messageById;
    }

    public void addRecipe(ConfigurationSection recipes, ItemStack result) {
        if (recipes == null) {
            return;
        }
        for (String recipeKey : recipes.getKeys(false)) {
            ConfigurationSection recipeSection = recipes.getConfigurationSection(recipeKey);
            if (recipeSection == null) {
                continue;
            }
            RecipeType recipeType;
            try {
                recipeType = RecipeType.valueOf(recipeSection.getString("type"));
            }catch (IllegalArgumentException exp) {
                continue;
            }
            String ingredientIds = recipeSection.getString("recipe");
            if (ingredientIds == null) {
                continue;
            }
            List<String> raw = new ArrayList<>(List.of(ingredientIds.split(",")));
            List<ItemStack> ingredients = new ArrayList<>();
            for (String line : raw) {
                ItemStack ingredient = getConfigManager().getManager().getItemManager().getItem(line);
                if (ingredient == null) {
                    continue;
                }
                ingredients.add(ingredient);
            }
            Recipe recipe = new Recipe(result, ingredients, recipeType);
            recipe.addRecipe();
        }
    }

    public File saveConfig() {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, true);
        }
        return file;
    }

    public MortisNuclearCraft getPlugin() {
        return plugin;
    }

    public String getFileName() {
        return fileName;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
