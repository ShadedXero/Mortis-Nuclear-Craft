package com.mortisdevelopment.mortisnuclearcraft.config;

import com.mortisdevelopment.mortisnuclearcraft.data.DataManager;
import com.mortisdevelopment.mortisnuclearcraft.data.H2Database;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MainConfig extends Config {

    public MainConfig(ConfigManager configManager) {
        super("config.yml", configManager);
    }

    @Override
    public void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        loadDatabase(config.getConfigurationSection("database"));
    }

    private void loadDatabase(ConfigurationSection section) {
        if (section == null) {
            return;
        }
        String fileName = section.getString("file");
        if (fileName == null) {
            return;
        }
        File file = new File(getPlugin().getDataFolder(), fileName);
        String username = section.getString("username");
        String password = section.getString("password");
        getConfigManager().getManager().setDataManager(new DataManager(new H2Database(file, username, password)));
    }
}
