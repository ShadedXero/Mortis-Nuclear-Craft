package com.mortisdevelopment.mortisnuclearcraft.config;

import com.mortisdevelopment.mortisnuclearcraft.structures.Structure;
import com.mortisdevelopment.mortisnuclearcraft.structures.StructureBlock;
import com.mortisdevelopment.mortisnuclearcraft.structures.Vector;
import com.mortisdevelopment.mortisnuclearcraft.structures.StructureManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StructuresConfig extends Config {

    public StructuresConfig(ConfigManager configManager) {
        super("structures.yml", configManager);
    }

    @Override
    public void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        loadSettings(config.getConfigurationSection("settings"));
        loadStructures(config.getConfigurationSection("structures"));
    }

    private void loadSettings(ConfigurationSection settings) {
        if (settings == null) {
            return;
        }
        if (!settings.contains("radiusX") || !settings.contains("radiusY") || !settings.contains("radiusZ")) {
            return;
        }
        int radiusX = settings.getInt("radiusX");
        int radiusY = settings.getInt("radiusY");
        int radiusZ = settings.getInt("radiusZ");
        Vector vector = new Vector(radiusX, radiusY, radiusZ);
        getConfigManager().getManager().setStructureManager(new StructureManager(vector));
    }

    private void loadStructures(ConfigurationSection structures) {
        if (structures == null) {
            return;
        }
        for (String key : structures.getKeys(false)) {
            ConfigurationSection section = structures.getConfigurationSection(key);
            if (section == null) {
                continue;
            }
            ConfigurationSection coreSection = section.getConfigurationSection("core-block");
            if (coreSection == null) {
                continue;
            }
            boolean coreStrict = coreSection.getBoolean("strict");
            Material coreMaterial;
            try {
                coreMaterial = Material.valueOf(coreSection.getString("material"));
            }catch (IllegalArgumentException exp) {
                continue;
            }
            String rawCoreData = coreSection.getString("data");
            if (rawCoreData == null) {
                continue;
            }
            BlockData coreData = Bukkit.createBlockData(rawCoreData);
            String coreRaw = coreSection.getString("vector");
            if (coreRaw == null) {
                continue;
            }
            String[] coreRawVector = coreRaw.split(",");
            Vector coreVector = new Vector(Double.parseDouble(coreRawVector[0]), Double.parseDouble(coreRawVector[1]), Double.parseDouble(coreRawVector[2]));
            List<String> coreKeys = coreSection.getStringList("keys");
            StructureBlock core = new StructureBlock(coreMaterial, coreData, coreVector, coreStrict, coreKeys);
            List<StructureBlock> structureBlocks = new ArrayList<>();
            List<String> keys = new ArrayList<>(section.getKeys(false));
            keys.remove("core-block");
            for (String id : keys) {
                ConfigurationSection blocks = section.getConfigurationSection(id);
                if (blocks == null) {
                    continue;
                }
                boolean strict = blocks.getBoolean("strict");
                Material material;
                try {
                    material = Material.valueOf(blocks.getString("material"));
                }catch (IllegalArgumentException exp) {
                    continue;
                }
                String rawData = blocks.getString("data");
                if (rawData == null) {
                    continue;
                }
                BlockData data = Bukkit.createBlockData(rawData);
                String raw = blocks.getString("vector");
                if (raw == null) {
                    continue;
                }
                String[] rawVector = raw.split(",");
                Vector vector = new Vector(Double.parseDouble(rawVector[0]), Double.parseDouble(rawVector[1]), Double.parseDouble(rawVector[2]));
                List<String> blockKeys = blocks.getStringList("keys");
                StructureBlock structureBlock = new StructureBlock(material, data, vector, strict, blockKeys);
                structureBlocks.add(structureBlock);
            }
            Structure structure = new Structure(key, core, structureBlocks);
            getConfigManager().getManager().getStructureManager().getStructures().add(structure);
            getConfigManager().getManager().getStructureManager().getStructureById().put(key, structure);
        }
    }

    public void saveStructure(Structure structure) {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection structures = config.getConfigurationSection("structures");
        if (structures == null) {
            return;
        }
        String id = structure.getId();
        ConfigurationSection section = structures.createSection(id);
        ConfigurationSection coreSection = section.createSection("core-block");
        StructureBlock core = structure.getCore();
        coreSection.set("strict", core.isStrict());
        coreSection.set("material", core.getMaterial().toString());
        coreSection.set("data", core.getData().getAsString());
        coreSection.set("vector", core.getVector().getX() + ", " + core.getVector().getY() + ", " + core.getVector().getZ());
        for (StructureBlock structureBlock : structure.getBlocks()) {
            Material type = structureBlock.getMaterial();
            BlockData data = structureBlock.getData();
            Vector vector = structureBlock.getVector();
            ConfigurationSection blockSection = section.createSection(UUID.randomUUID().toString());
            blockSection.set("strict", structureBlock.isStrict());
            blockSection.set("material", type.toString());
            blockSection.set("data", data.getAsString());
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
}
