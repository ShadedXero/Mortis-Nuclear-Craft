package me.none030.mortisnuclearcraft.config;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.data.DataManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final ConfigManager configManager;
    private Connection connection;

    public DatabaseConfig(ConfigManager configManager) {
        this.configManager = configManager;
        loadConfig();
    }

    private void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        loadDatabase(config.getConfigurationSection("database"));
    }

    private void loadDatabase(ConfigurationSection section) {
        if (section == null) {
            plugin.getLogger().severe("Database could not be found");
            plugin.getLogger().severe("Please enter the correct credentials for your database!");
            return;
        }
        String host = section.getString("host");
        int port = section.getInt("port");
        String database = section.getString("database");
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        String user = section.getString("user");
        String password = section.getString("password");
        connect(url, user, password);
        configManager.getManager().setDataManager(new DataManager(connection));
    }

    private void connect(String url, String user, String password) {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            plugin.getLogger().info("Connected to database");
            this.connection = connection;
        } catch (SQLException exp) {
            plugin.getLogger().severe("Database could not be found");
            plugin.getLogger().severe("Please enter the correct credentials for your database!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    private File saveConfig() {
        File file = new File(plugin.getDataFolder(), "database.yml");
        if (!file.exists()) {
            plugin.saveResource("database.yml", false);
        }
        return file;
    }
}
