package me.none030.mortisnuclearcraft.config;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.bombs.BlockBomb;
import me.none030.mortisnuclearcraft.bombs.BombManager;
import me.none030.mortisnuclearcraft.bombs.ItemBomb;
import me.none030.mortisnuclearcraft.utils.bomb.BombMenuItems;
import me.none030.mortisnuclearcraft.structures.Structure;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static me.none030.mortisnuclearcraft.utils.MessageUtils.colorMessage;

public class BombsConfig {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final ConfigManager configManager;

    public BombsConfig(ConfigManager configManager) {
        this.configManager = configManager;
        loadConfig();
    }

    private void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        configManager.getManager().setBombManager(new BombManager(configManager.getManager().getRadiationManager(), configManager.getManager().getDataManager()));
        loadItemBombs(config.getConfigurationSection("item-bombs"));
        loadBlockBombs(config.getConfigurationSection("block-bombs"));
    }

    private void loadItemBombs(ConfigurationSection itemBombs) {
        if (itemBombs == null) {
            plugin.getLogger().severe("'item-bombs' section could not be found in bombs.yml");
            plugin.getLogger().severe("Please add the 'item-bombs' section back or regenerate the bombs.yml file");
            return;
        }
        for (String key : itemBombs.getKeys(false)) {
            ConfigurationSection itemBomb = itemBombs.getConfigurationSection(key);
            if (itemBomb == null) {
                plugin.getLogger().severe("Detected a problem with '" + key + "' at 'item-bombs' section in bombs.yml");
                continue;
            }
            String itemId = itemBomb.getString("item");
            ItemStack item = configManager.getManager().getItemManager().getItemById().get(itemId);
            if (item == null) {
                plugin.getLogger().severe("Detected a problem with 'item' at '" + key + "' section in 'item-bombs' section in bombs.yml");
                plugin.getLogger().severe("Please enter a valid item id");
                continue;
            }
            int speed = itemBomb.getInt("speed");
            int strength = itemBomb.getInt("explosion-strength");
            int radius = itemBomb.getInt("radiation-radius");
            int duration = itemBomb.getInt("radiation-duration");
            double radiation = itemBomb.getInt("radiation-per-second");
            boolean vehicles = itemBomb.getBoolean("destroy-vehicle");
            ItemBomb bomb = new ItemBomb(configManager.getManager().getBombManager(), key, item, speed, strength, radius, duration, radiation, vehicles);
            configManager.getManager().getBombManager().getItemBombs().add(bomb);
            configManager.getManager().getBombManager().getItemBombById().put(key, bomb);
        }
    }

    private void loadBlockBombs(ConfigurationSection blockBombs) {
        if (blockBombs == null) {
            plugin.getLogger().severe("'block-bombs' section could not be found in bombs.yml");
            plugin.getLogger().severe("Please add the 'block-bombs' section back or regenerate the bombs.yml file");
            return;
        }
        loadBombMenuItems(blockBombs.getConfigurationSection("bomb-menu-items"));
        List<String> blockBombKeys = new ArrayList<>(blockBombs.getKeys(false));
        blockBombKeys.remove("bomb-menu-items");
        for (String key : blockBombKeys) {
            ConfigurationSection blockBomb = blockBombs.getConfigurationSection(key);
            if (blockBomb == null) {
                plugin.getLogger().severe("Detected a problem with '" + key + "' at 'block-bombs' section in bombs.yml");
                continue;
            }
            String name = colorMessage(blockBomb.getString("name"));
            int strength = blockBomb.getInt("explosion-strength");
            int radius = blockBomb.getInt("radiation-radius");
            int duration = blockBomb.getInt("radiation-duration");
            double radiation = blockBomb.getDouble("radiation-per-second");
            boolean vehicles = blockBomb.getBoolean("destroy-vehicle");
            String structureId = blockBomb.getString("structure");
            Structure structure = configManager.getManager().getStructureManager().getStructureById().get(structureId);
            if (structure == null) {
                plugin.getLogger().severe("Detected a problem with 'structure' in '" + key + "' section at 'block-bombs' section in bombs.yml");
                continue;
            }
            BlockBomb bomb = new BlockBomb(configManager.getManager().getBombManager(), key, name, strength, radius, duration, radiation, vehicles, structure);
            configManager.getManager().getBombManager().getBlockBombs().add(bomb);
            configManager.getManager().getBombManager().getBlockBombById().put(key, bomb);
        }
    }

    private void loadBombMenuItems(ConfigurationSection bombMenu) {
        if (bombMenu == null) {
            return;
        }
        String filterId = bombMenu.getString("filter");
        ItemStack filter = configManager.getManager().getItemManager().getItemById().get(filterId);
        if (filter == null) {
            plugin.getLogger().severe("Detected a problem with 'filter' item at 'bomb-menu-items' section in 'block-bombs' section in bombs.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        String redstoneModeId = bombMenu.getString("redstone-mode");
        ItemStack redstoneMode = configManager.getManager().getItemManager().getItemById().get(redstoneModeId);
        if (redstoneMode == null) {
            plugin.getLogger().severe("Detected a problem with 'redstone-mode' item at 'bomb-menu-items' section in 'block-bombs' section in bombs.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        String manualModeId = bombMenu.getString("manual-mode");
        ItemStack manualMode = configManager.getManager().getItemManager().getItemById().get(manualModeId);
        if (manualMode == null) {
            plugin.getLogger().severe("Detected a problem with 'manual-mode' item at 'bomb-menu-items' section in 'block-bombs' section in bombs.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        String timerId = bombMenu.getString("timer");
        ItemStack timer = configManager.getManager().getItemManager().getItemById().get(timerId);
        if (timer == null) {
            plugin.getLogger().severe("Detected a problem with 'timer' item at 'bomb-menu-items' section in 'block-bombs' section in bombs.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        String noTimerId = bombMenu.getString("no-timer");
        ItemStack noTimer = configManager.getManager().getItemManager().getItemById().get(noTimerId);
        if (noTimer == null) {
            plugin.getLogger().severe("Detected a problem with 'no-timer' item at 'bomb-menu-items' section in 'block-bombs' section in bombs.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        String timerSetterId = bombMenu.getString("timer-setter");
        ItemStack timerSetter = configManager.getManager().getItemManager().getItemById().get(timerSetterId);
        if (timerSetter == null) {
            plugin.getLogger().severe("Detected a problem with 'timer-setter' item at 'bomb-menu-items' section in 'block-bombs' section in bombs.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        String timerSetId = bombMenu.getString("timer-set");
        ItemStack timerSet = configManager.getManager().getItemManager().getItemById().get(timerSetId);
        if (timerSet == null) {
            plugin.getLogger().severe("Detected a problem with 'timer-set' item at 'bomb-menu-items' section in 'block-bombs' section in bombs.yml");
            plugin.getLogger().severe("Please enter a valid item id");
            return;
        }
        BombMenuItems bombMenuItems = new BombMenuItems(filter, redstoneMode, manualMode, timer, noTimer, timerSetter, timerSet);
        configManager.getManager().getBombManager().setMenuItems(bombMenuItems);
    }

    private File saveConfig() {
        File file = new File(plugin.getDataFolder(), "bombs.yml");
        if (!file.exists()) {
            plugin.saveResource("bombs.yml", false);
        }
        return file;
    }
}
