package me.none030.mortisnuclearcraft.config;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.nuclearcraft.Manager;
import me.none030.mortisnuclearcraft.utils.NuclearType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataConfig {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final NuclearType type;

    public DataConfig(Manager manager, NuclearType type) {
        this.type = type;
        load(manager);
    }

    private void load(Manager manager) {
        File file = getFile();
        if (file == null) {
            return;
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        String key = getSection();
        if (key == null) {
            return;
        }
        List<String> locations = new ArrayList<>(config.getStringList(key));
        for (String line : locations) {
            String[] raw = line.split(",");
            World world = Bukkit.getWorld(raw[0]);
            if (world == null) {
                remove(line);
                continue;
            }
            Location loc = new Location(world, Double.parseDouble(raw[1]), Double.parseDouble(raw[2]), Double.parseDouble(raw[3]));
            manager.getCores().add(loc);
        }
    }

    public void add(Location location) {
        File file = getFile();
        if (file == null) {
            return;
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        String key = getSection();
        if (key == null) {
            return;
        }
        List<String> locations = new ArrayList<>(config.getStringList(key));
        String loc = location.getWorld().getName() + ", " + location.getX() + ", " + location.getY() + ", " + location.getZ();
        locations.add(loc);
        config.set(key, locations);
        try {
            config.save(file);
        }catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    public void add(String loc) {
        File file = getFile();
        if (file == null) {
            return;
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        String key = getSection();
        if (key == null) {
            return;
        }
        List<String> locations = new ArrayList<>(config.getStringList(key));
        locations.add(loc);
        config.set(key, locations);
        try {
            config.save(file);
        }catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    public void remove(Location location) {
        File file = getFile();
        if (file == null) {
            return;
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        String key = getSection();
        if (key == null) {
            return;
        }
        List<String> locations = new ArrayList<>(config.getStringList(key));
        String loc = location.getWorld().getName() + ", " + location.getX() + ", " + location.getY() + ", " + location.getZ();
        locations.remove(loc);
        config.set(key, locations);
        try {
            config.save(file);
        }catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    public void remove(String loc) {
        File file = getFile();
        if (file == null) {
            return;
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        String key = getSection();
        if (key == null) {
            return;
        }
        List<String> locations = new ArrayList<>(config.getStringList(key));
        locations.remove(loc);
        config.set(key, locations);
        try {
            config.save(file);
        }catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    private String getSection() {
        switch (type) {
            case BLOCK_BOMB: return "block-bombs";
            case CENTRIFUGE: return "centrifuges";
            case REACTOR: return "reactors";
            case WASTE: return "nuclear-wastes";
        }
        return null;
    }

    private File getFile() {
        switch (type) {
            case BLOCK_BOMB: return saveFile("blockbombs.yml");
            case CENTRIFUGE: return saveFile("centrifuges.yml");
            case REACTOR: return saveFile("reactors.yml");
            case WASTE: return saveFile("wastes.yml");
        }
        return null;
    }

    private File saveFile(String fileName) {
        File file = new File(plugin.getDataFolder() + "/data/", fileName);
        if (!file.exists()) {
            plugin.saveResource("data/" + fileName, false);
        }
        return file;
    }
}
