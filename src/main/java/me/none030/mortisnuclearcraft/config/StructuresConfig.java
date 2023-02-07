package me.none030.mortisnuclearcraft.config;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.structures.Structure;
import me.none030.mortisnuclearcraft.structures.StructureBlock;
import me.none030.mortisnuclearcraft.structures.StructureManager;
import me.none030.mortisnuclearcraft.structures.Vector;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StructuresConfig {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final ConfigManager configManager;

    public StructuresConfig(ConfigManager configManager) {
        this.configManager = configManager;
        loadConfig();
    }

    private void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        loadSettings(config.getConfigurationSection("settings"));
        loadStructures(config.getConfigurationSection("structures"));
    }

    private void loadSettings(ConfigurationSection settings) {
        if (settings == null) {
            plugin.getLogger().severe("'settings' section could not be found in structures.yml");
            plugin.getLogger().severe("Please add the 'settings' section back or regenerate the structures.yml file");
            return;
        }
        if (!settings.contains("radiusX")) {
            plugin.getLogger().severe("Detected a problem with 'radiusX' at 'settings' section in structures.yml");
        }
        if (!settings.contains("radiusY")) {
            plugin.getLogger().severe("Detected a problem with 'radiusY' at 'settings' section in structures.yml");
        }
        if (!settings.contains("radiusZ")) {
            plugin.getLogger().severe("Detected a problem with 'radiusZ' at 'settings' section in structures.yml");
        }
        int radiusX = settings.getInt("radiusX");
        int radiusY = settings.getInt("radiusY");
        int radiusZ = settings.getInt("radiusZ");
        Vector vector = new Vector(radiusX, radiusY, radiusZ);
        configManager.getManager().setStructureManager(new StructureManager(vector));
    }

    private void loadStructures(ConfigurationSection structures) {
        if (structures == null) {
            plugin.getLogger().severe("'structures' section could not be found in structures.yml");
            plugin.getLogger().severe("Please add the 'structures' section back or regenerate the structures.yml file");
            return;
        }
        for (String key : structures.getKeys(false)) {
            ConfigurationSection section = structures.getConfigurationSection(key);
            if (section == null) {
                plugin.getLogger().severe("Detected a problem with '" + key + "' at 'structures' section in structures.yml");
                continue;
            }
            Material core = Material.valueOf(section.getString("core-block"));
            List<StructureBlock> structureBlocks = new ArrayList<>();
            for (String id : section.getKeys(false)) {
                if (id.equals("core-block")) {
                    continue;
                }
                ConfigurationSection blocks = section.getConfigurationSection(id);
                if (blocks == null) {
                    plugin.getLogger().severe("Detected a problem with '" + id + "' at '" + key + "' section at 'structures' section in structures.yml");
                    continue;
                }
                Material material = Material.valueOf(blocks.getString("material"));
                String raw = blocks.getString("vector");
                if (raw == null) {
                    plugin.getLogger().severe("Detected a problem with vector at '" + id + "' section at '" + key + "' section at 'structures' section in structures.yml");
                    continue;
                }
                String[] rawVector = raw.split(",");
                Vector vector = new Vector(Double.parseDouble(rawVector[0]), Double.parseDouble(rawVector[1]), Double.parseDouble(rawVector[2]));
                StructureBlock structureBlock = new StructureBlock(material, vector);
                structureBlocks.add(structureBlock);
            }
            Structure structure = new Structure(key, core, structureBlocks);
            configManager.getManager().getStructureManager().getStructures().add(structure);
            configManager.getManager().getStructureManager().getStructureById().put(key, structure);
        }
    }

    public void saveStructures(Structure structure) {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection structures = config.getConfigurationSection("structures");
        if (structures == null) {
            plugin.getLogger().severe("'structures' section could not be found in structures.yml");
            plugin.getLogger().severe("Please add the 'structures' section back or regenerate the structures.yml file");
            return;
        }
        String id = structure.getId();
        ConfigurationSection section = structures.createSection(id);
        Material core = structure.getCore();
        section.set("core-block", core.toString());
        for (StructureBlock structureBlock : structure.getBlocks()) {
            Material type = structureBlock.getType();
            Vector vector = structureBlock.getVector();
            ConfigurationSection blockSection = section.createSection(UUID.randomUUID().toString());
            blockSection.set("material", type.toString());
            blockSection.set("vector", vector.getX() + ", " + vector.getY() + ", " + vector.getZ());
        }
        try {
            config.save(file);
        }catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    public void deleteStructure(String id) {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection structures = config.getConfigurationSection("structures");
        if (structures == null) {
            plugin.getLogger().severe("'structures' section could not be found in structures.yml");
            plugin.getLogger().severe("Please add the 'structures' section back or regenerate the structures.yml file");
            return;
        }
        ConfigurationSection structure = structures.getConfigurationSection(id);
        if (structure == null) {
            return;
        }
        String path = structure.getCurrentPath();
        if (path == null) {
            return;
        }
        config.set(path, null);
        try {
            config.save(file);
        }catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    private File saveConfig() {
        File file = new File(plugin.getDataFolder(), "structures.yml");
        if (!file.exists()) {
            plugin.saveResource("structures.yml", false);
        }
        return file;
    }
}
