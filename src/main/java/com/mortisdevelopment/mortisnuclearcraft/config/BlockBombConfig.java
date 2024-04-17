package com.mortisdevelopment.mortisnuclearcraft.config;

import com.mortisdevelopment.mortisnuclearcraft.menu.MenuItems;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.bombs.blockbomb.BlockBomb;
import com.mortisdevelopment.mortisnuclearcraft.structures.Structure;
import com.mortisdevelopment.mortisnuclearcraft.utils.MessageUtils;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.bombs.blockbomb.BlockBombManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BlockBombConfig extends Config {

    public BlockBombConfig(ConfigManager configManager) {
        super("blockbombs.yml", configManager);
    }

    @Override
    public void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = config.getConfigurationSection("block-bombs");
        if (section == null) {
           return;
        }
        MenuItems menuItems = loadMenuItems(section.getConfigurationSection("menu-items"));
        if (menuItems == null) {
            return;
        }
        getConfigManager().getManager().setBlockBombManager(new BlockBombManager(getConfigManager().getManager().getDataManager(), getConfigManager().getManager().getRadiationManager(), menuItems));
        loadBlockBombs(section.getConfigurationSection("bombs"));
        getConfigManager().getManager().getBlockBombManager().addMessages(loadMessages(section.getConfigurationSection("messages")));
    }

    private void loadBlockBombs(ConfigurationSection bombs) {
        if (bombs == null) {
            getPlugin().getLogger().severe("'block-bombs' section could not be found in bombs.yml");
            getPlugin().getLogger().severe("Please add the 'block-bombs' section back or regenerate the bombs.yml file");
            return;
        }
        for (String key : bombs.getKeys(false)) {
            ConfigurationSection blockBomb = bombs.getConfigurationSection(key);
            if (blockBomb == null) {
                getPlugin().getLogger().severe("Detected a problem with '" + key + "' at 'block-bombs' section in bombs.yml");
                continue;
            }
            MessageUtils editor = new MessageUtils(blockBomb.getString("name"));
            editor.color();
            String name = editor.getMessage();
            int strength = blockBomb.getInt("explosion-strength");
            int radius = blockBomb.getInt("radiation-radius");
            int duration = blockBomb.getInt("radiation-duration");
            double radiation = blockBomb.getDouble("radiation-per-second");
            boolean vehicles = blockBomb.getBoolean("destroy-vehicle");
            boolean drain = blockBomb.getBoolean("drain");
            boolean fire = blockBomb.getBoolean("fire");
            List<String> structureIds = blockBomb.getStringList("structures");
            if (structureIds.size() == 0) {
                getPlugin().getLogger().severe("Detected a problem with 'structures' in '" + key + "' section at 'block-bombs' section in bombs.yml");
                continue;
            }
            List<Structure> structures = new ArrayList<>();
            for (String structureId : structureIds) {
                Structure structure = getConfigManager().getManager().getStructureManager().getStructureById().get(structureId);
                if (structure == null) {
                    getPlugin().getLogger().severe("Detected a problem with 'structures' in '" + key + "' section at 'block-bombs' section in bombs.yml");
                    continue;
                }
                structures.add(structure);
            }
            boolean blockDamage = blockBomb.getBoolean("block-damage");
            boolean townyBlockDamage = blockBomb.getBoolean("towny-block-damage");
            boolean blockRegen = blockBomb.getBoolean("block-regen");
            boolean townyRegen = blockBomb.getBoolean("towny-regen");
            long regenTime = blockBomb.getLong("regen-time");
            boolean townyPlayerDamage = blockBomb.getBoolean("towny-player-damage");
            boolean explosionsEnabledPlotsWilderness = blockBomb.getBoolean("explosions-enabled-plots-wilderness");
            BlockBomb bomb = new BlockBomb(key, name, strength, radius, duration, radiation, vehicles, drain, fire, blockDamage, townyBlockDamage, blockRegen, townyRegen, regenTime, townyPlayerDamage, explosionsEnabledPlotsWilderness, structures);
            getConfigManager().getManager().getBlockBombManager().getBlockBombById().put(key, bomb);
        }
    }
}
