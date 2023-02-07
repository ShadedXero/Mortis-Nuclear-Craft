package me.none030.mortisnuclearcraft.data;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReactorStorage {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final Connection connection;

    public ReactorStorage(Connection connection) {
        this.connection = connection;
        initializeDatabase();
    }

    private void initializeDatabase() {
        new BukkitRunnable() {
            @Override
            public void run() {
                String sql = "CREATE TABLE IF NOT EXISTS Reactor(core varchar(100) primary key, input varchar(8000), output varchar(8000), fuel varchar(8000), manualMode boolean, process varchar(100), timer long);";
                try {
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.executeUpdate();
                } catch (SQLException exp) {
                    exp.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public List<ReactorData> getReactors() {
        List<ReactorData> dataList = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Reactor;");
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                String[] raw = resultSet.getString("core").split(",");
                Location core = new Location(Bukkit.getWorld(raw[0]), Double.parseDouble(raw[1]), Double.parseDouble(raw[2]), Double.parseDouble(raw[3]));
                String rawInput = resultSet.getString("input");
                String rawOutput = resultSet.getString("output");
                String rawFuel = resultSet.getString("fuel");
                boolean manualMode = resultSet.getBoolean("manualMode");
                String process = resultSet.getString("process");
                long timer = resultSet.getLong("timer");
                ReactorData data = new ReactorData(core, rawInput, rawOutput, rawFuel, manualMode, process, timer);
                dataList.add(data);
            }
        } catch (SQLException exp) {
            exp.printStackTrace();
        }
        return dataList;
    }

    public ReactorData getReactor(Location core) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Reactor WHERE core = ?;");
            stmt.setString(1, core.getWorld().getName() + ", " + core.getX() + ", " + core.getY() + ", " + core.getZ());
            ResultSet resultSet = stmt.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            String rawInput = resultSet.getString("input");
            String rawOutput = resultSet.getString("output");
            String rawFuel = resultSet.getString("fuel");
            boolean manualMode = resultSet.getBoolean("manualMode");
            String process = resultSet.getString("process");
            long timer = resultSet.getLong("timer");
            return new ReactorData(core, rawInput, rawOutput, rawFuel, manualMode, process, timer);
        } catch (SQLException exp) {
            exp.printStackTrace();
        }
        return null;
    }

    public void storeReactor(ReactorData data) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String sql = "INSERT INTO Reactor(core, input, output, fuel, manualMode, process, timer) VALUES(?, ?, ?, ?, ?, ?, ?);";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    Location loc = data.getCore();
                    stmt.setString(1, loc.getWorld().getName() + ", " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ());
                    stmt.setString(2, data.getRawInput());
                    stmt.setString(3, data.getRawOutput());
                    stmt.setString(4, data.getRawFuel());
                    stmt.setBoolean(5, data.isManualMode());
                    stmt.setString(6, data.getProcess());
                    stmt.setLong(7, data.getTimer());
                    stmt.execute();
                }catch (SQLException exp) {
                    exp.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void updateInput(Location core, String input) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String sql = "UPDATE Reactor SET input = ? WHERE core = ?";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setString(1, input);
                    stmt.setString(2, core.getWorld().getName() + ", " + core.getX() + ", " + core.getY() + ", " + core.getZ());
                    stmt.executeUpdate();
                } catch (SQLException exp) {
                    exp.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void updateOutput(Location core, String output) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String sql = "UPDATE Reactor SET output = ? WHERE core = ?";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setString(1, output);
                    stmt.setString(2, core.getWorld().getName() + ", " + core.getX() + ", " + core.getY() + ", " + core.getZ());
                    stmt.executeUpdate();
                } catch (SQLException exp) {
                    exp.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void updateFuel(Location core, String fuel) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String sql = "UPDATE Reactor SET fuel = ? WHERE core = ?";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setString(1, fuel);
                    stmt.setString(2, core.getWorld().getName() + ", " + core.getX() + ", " + core.getY() + ", " + core.getZ());
                    stmt.executeUpdate();
                } catch (SQLException exp) {
                    exp.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void updateMode(Location core, boolean manualMode) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String sql = "UPDATE Reactor SET manualMode = ? WHERE core = ?";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setBoolean(1, manualMode);
                    stmt.setString(2, core.getWorld().getName() + ", " + core.getX() + ", " + core.getY() + ", " + core.getZ());
                    stmt.executeUpdate();
                } catch (SQLException exp) {
                    exp.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void updateProcess(Location core, String process) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String sql = "UPDATE Reactor SET process = ? WHERE core = ?";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setString(1, process);
                    stmt.setString(2, core.getWorld().getName() + ", " + core.getX() + ", " + core.getY() + ", " + core.getZ());
                    stmt.executeUpdate();
                } catch (SQLException exp) {
                    exp.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void updateTimer(Location core, long timer) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String sql = "UPDATE Reactor SET timer = ? WHERE core = ?";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setLong(1, timer);
                    stmt.setString(2, core.getWorld().getName() + ", " + core.getX() + ", " + core.getY() + ", " + core.getZ());
                    stmt.executeUpdate();
                } catch (SQLException exp) {
                    exp.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void deleteReactor(Location core) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement stmt = connection.prepareStatement("DELETE FROM Reactor WHERE core = ?;");
                    stmt.setString(1, core.getWorld().getName() + ", " + core.getX() + ", " + core.getY() + ", " + core.getZ());
                    stmt.executeUpdate();
                } catch (SQLException exp) {
                    exp.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }
}
