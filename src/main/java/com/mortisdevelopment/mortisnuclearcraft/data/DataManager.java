package com.mortisdevelopment.mortisnuclearcraft.data;

import com.mortisdevelopment.mortisnuclearcraft.MortisNuclearCraft;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.Manager;
import com.mortisdevelopment.mortisnuclearcraft.utils.NuclearType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DataManager {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final H2Database database;

    public DataManager(H2Database database) {
        this.database = database;
        initializeDatabase();
    }

    private void initializeDatabase() {
        new BukkitRunnable() {
            @Override
            public void run() {
                database.execute("CREATE TABLE IF NOT EXISTS MortisMachines(type tinytext, world tinytext, x double, y double, z double)");
            }
        }.runTask(plugin);
    }

    public void load(Manager manager, NuclearType type) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    ResultSet result = database.query("SELECT * FROM MortisMachines WHERE type = ?", type.name());
                    while (result.next()) {
                        String worldName = result.getString("world");
                        if (worldName == null) {
                            continue;
                        }
                        World world = Bukkit.getWorld(worldName);
                        if (world == null) {
                            continue;
                        }
                        double x = result.getDouble("x");
                        double y = result.getDouble("y");
                        double z = result.getDouble("z");
                        Location location = new Location(world, x, y, z);
                        manager.getCores().add(location);
                    }
                }catch (SQLException exp) {
                    exp.printStackTrace();
                }
            }
        }.runTask(plugin);
    }

    public void add(NuclearType type, Location location) {
        new BukkitRunnable() {
            @Override
            public void run() {
                database.update("INSERT INTO MortisMachines(type, world, x, y, z) VALUES (?, ?, ?, ?, ?)", type.name(), location.getWorld().getName(), location.getX(), location.getY(), location.getZ());
            }
        }.runTask(plugin);
    }

    public void remove(NuclearType type, Location location) {
        new BukkitRunnable() {
            @Override
            public void run() {
                database.update("DELETE FROM MortisMachines WHERE type = ? AND world = ? AND x = ? AND y = ? AND z = ?", type.name(), location.getWorld().getName(), location.getX(), location.getY(), location.getZ());
            }
        }.runTask(plugin);
    }
}
