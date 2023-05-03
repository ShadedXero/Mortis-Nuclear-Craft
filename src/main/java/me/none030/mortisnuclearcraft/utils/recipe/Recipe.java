package me.none030.mortisnuclearcraft.utils.recipe;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.List;
import java.util.UUID;

public class Recipe {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final ItemStack result;
    private final List<ItemStack> ingredients;
    private final RecipeType type;
    private org.bukkit.inventory.Recipe recipe;

    public Recipe(ItemStack result, List<ItemStack> ingredients, RecipeType type) {
        this.result = result;
        this.ingredients = ingredients;
        this.type = type;
        createRecipe();
    }

    private void createRecipe() {
        NamespacedKey key = new NamespacedKey(plugin, UUID.randomUUID().toString());
        if (type.equals(RecipeType.SHAPED)) {
            if (ingredients.size() < 9) {
                plugin.getLogger().info("Recipe not registered");
                return;
            }
            ShapedRecipe recipe = new ShapedRecipe(key, result);
            recipe.shape("123", "456", "789");
            for (int i = 1; i < 10; i++) {
                ItemStack item = ingredients.get(i - 1);
                char character = '0';
                if (i == 1) character = '1';
                if (i == 2) character = '2';
                if (i == 3) character = '3';
                if (i == 4) character = '4';
                if (i == 5) character = '5';
                if (i == 6) character = '6';
                if (i == 7) character = '7';
                if (i == 8) character = '8';
                if (i == 9) character = '9';
                recipe.setIngredient(character, item);
            }
            setRecipe(recipe);
        }else {
            ShapelessRecipe recipe = new ShapelessRecipe(key, result);
            for (ItemStack item : ingredients) {
                recipe.addIngredient(item);
            }
            setRecipe(recipe);
        }
    }

    public void addRecipe() {
        Bukkit.addRecipe(recipe);
    }

    public ItemStack getResult() {
        return result;
    }

    public List<ItemStack> getIngredients() {
        return ingredients;
    }

    public RecipeType getType() {
        return type;
    }

    public org.bukkit.inventory.Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(org.bukkit.inventory.Recipe recipe) {
        this.recipe = recipe;
    }
}
