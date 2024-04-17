package com.mortisdevelopment.mortisnuclearcraft.config;

import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.bombs.itembomb.ItemBomb;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.bombs.itembomb.ItemBombManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class ItemBombConfig extends Config {

    public ItemBombConfig(ConfigManager configManager) {
        super("itembombs.yml", configManager);
    }

    @Override
    public void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        getConfigManager().getManager().setItemBombManager(new ItemBombManager(getConfigManager().getManager().getRadiationManager()));
        loadItemBombs(config.getConfigurationSection("item-bombs"));
    }

    private void loadItemBombs(ConfigurationSection itemBombs) {
        if (itemBombs == null) {
            getPlugin().getLogger().severe("'item-bombs' section could not be found in bombs.yml");
            getPlugin().getLogger().severe("Please add the 'item-bombs' section back or regenerate the bombs.yml file");
            return;
        }
        for (String key : itemBombs.getKeys(false)) {
            ConfigurationSection itemBomb = itemBombs.getConfigurationSection(key);
            if (itemBomb == null) {
                getPlugin().getLogger().severe("Detected a problem with '" + key + "' at 'item-bombs' section in bombs.yml");
                continue;
            }
            String itemId = itemBomb.getString("item");
            ItemStack item = getConfigManager().getManager().getItemManager().getItem(itemId);
            if (item == null) {
                getPlugin().getLogger().severe("Detected a problem with 'item' at '" + key + "' section in 'item-bombs' section in bombs.yml");
                getPlugin().getLogger().severe("Please enter a valid item id");
                continue;
            }
            int speed = itemBomb.getInt("speed");
            int strength = itemBomb.getInt("explosion-strength");
            int radius = itemBomb.getInt("radiation-radius");
            int duration = itemBomb.getInt("radiation-duration");
            double radiation = itemBomb.getInt("radiation-per-second");
            boolean vehicles = itemBomb.getBoolean("destroy-vehicle");
            boolean drain = itemBomb.getBoolean("drain");
            boolean fire = itemBomb.getBoolean("fire");
            boolean blockDamage = itemBomb.getBoolean("block-damage");
            boolean townyBlockDamage = itemBomb.getBoolean("towny-block-damage");
            boolean blockRegen = itemBomb.getBoolean("block-regen");
            boolean townyRegen = itemBomb.getBoolean("towny-regen");
            long regenTime = itemBomb.getLong("regen-time");
            boolean townyPlayerDamage = itemBomb.getBoolean("towny-player-damage");
            boolean explosionsEnabledPlotsWilderness = itemBomb.getBoolean("explosions-enabled-plots-wilderness");
            ItemBomb bomb = new ItemBomb(key, item, speed, strength, radius, duration, radiation, vehicles, drain, fire, blockDamage, townyBlockDamage, blockRegen, townyRegen, regenTime, townyPlayerDamage, explosionsEnabledPlotsWilderness);
            getConfigManager().getManager().getItemBombManager().getItemBombById().put(key, bomb);
        }
    }
}
