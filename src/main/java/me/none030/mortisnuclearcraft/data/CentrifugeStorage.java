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

public class CentrifugeStorage {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final Connection connection;

    public CentrifugeStorage(Connection connection) {
        this.connection = connection;
        initializeDatabase();
    }

    private void initializeDatabase() {
        new BukkitRunnable() {
            @Override
            public void run() {
                String sql = "CREATE TABLE IF NOT EXISTS Centrifuge(core varchar(100) primary key, input1 varchar(8000), input2 varchar(8000), output1 varchar(8000), output2 varchar(8000), fuel varchar(8000), manualMode boolean, process varchar(100), timer long);";
                try {
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.executeUpdate();
                } catch (SQLException exp) {
                    exp.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public List<CentrifugeData> getCentrifuges() {
        List<CentrifugeData> dataList = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Centrifuge;");
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                String[] raw = resultSet.getString("core").split(",");
                Location core = new Location(Bukkit.getWorld(raw[0]), Double.parseDouble(raw[1]), Double.parseDouble(raw[2]), Double.parseDouble(raw[3]));
                String rawInput1 = resultSet.getString("input1");
                String rawInput2 = resultSet.getString("input2");
                String rawOutput1 = resultSet.getString("output1");
                String rawOutput2 = resultSet.getString("output2");
                String rawFuel = resultSet.getString("fuel");
                boolean manualMode = resultSet.getBoolean("manualMode");
                String process = resultSet.getString("process");
                long timer = resultSet.getLong("timer");
                CentrifugeData data = new CentrifugeData(core, rawInput1, rawInput2, rawOutput1, rawOutput2, rawFuel, manualMode, process, timer);
                dataList.add(data);
            }
        } catch (SQLException exp) {
            exp.printStackTrace();
        }
        return dataList;
    }

    public CentrifugeData getCentrifuge(Location core) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Centrifuge WHERE core = ?;");
            stmt.setString(1, core.getWorld().getName() + ", " + core.getX() + ", " + core.getY() + ", " + core.getZ());
            ResultSet resultSet = stmt.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            String rawInput1 = resultSet.getString("input1");
            String rawInput2 = resultSet.getString("input2");
            String rawOutput1 = resultSet.getString("output1");
            String rawOutput2 = resultSet.getString("output2");
            String rawFuel = resultSet.getString("fuel");
            boolean manualMode = resultSet.getBoolean("manualMode");
            String process = resultSet.getString("process");
            long timer = resultSet.getLong("timer");
            return new CentrifugeData(core, rawInput1, rawInput2, rawOutput1, rawOutput2, rawFuel, manualMode, process, timer);
        } catch (SQLException exp) {
            exp.printStackTrace();
        }
        return null;
    }

    public void storeCentrifuge(CentrifugeData data) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String sql = "INSERT INTO Centrifuge(core, input1, input2, output1, output2, fuel, manualMode, process, timer) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    Location loc = data.getCore();
                    stmt.setString(1, loc.getWorld().getName() + ", " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ());
                    stmt.setString(2, data.getRawInput1());
                    stmt.setString(3, data.getRawInput2());
                    stmt.setString(4, data.getRawOutput1());
                    stmt.setString(5, data.getRawOutput2());
                    stmt.setString(6, data.getRawFuel());
                    stmt.setBoolean(7, data.isManualMode());
                    stmt.setString(8, data.getProcess());
                    stmt.setLong(9, data.getTimer());
                    stmt.execute();
                }catch (SQLException exp) {
                    exp.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void updateInput1(Location core, String input1) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String sql = "UPDATE Centrifuge SET input1 = ? WHERE core = ?";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setString(1, input1);
                    stmt.setString(2, core.getWorld().getName() + ", " + core.getX() + ", " + core.getY() + ", " + core.getZ());
                    stmt.executeUpdate();
                } catch (SQLException exp) {
                    exp.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void updateInput2(Location core, String input2) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String sql = "UPDATE Centrifuge SET input2 = ? WHERE core = ?";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setString(1, input2);
                    stmt.setString(2, core.getWorld().getName() + ", " + core.getX() + ", " + core.getY() + ", " + core.getZ());
                    stmt.executeUpdate();
                } catch (SQLException exp) {
                    exp.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void updateOutput1(Location core, String output1) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String sql = "UPDATE Centrifuge SET output1 = ? WHERE core = ?";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setString(1, output1);
                    stmt.setString(2, core.getWorld().getName() + ", " + core.getX() + ", " + core.getY() + ", " + core.getZ());
                    stmt.executeUpdate();
                } catch (SQLException exp) {
                    exp.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void updateOutput2(Location core, String output2) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String sql = "UPDATE Centrifuge SET output2 = ? WHERE core = ?";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setString(1, output2);
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
                    String sql = "UPDATE Centrifuge SET fuel = ? WHERE core = ?";
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
                    String sql = "UPDATE Centrifuge SET manualMode = ? WHERE core = ?";
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
                    String sql = "UPDATE Centrifuge SET process = ? WHERE core = ?";
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
                    String sql = "UPDATE Centrifuge SET timer = ? WHERE core = ?";
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

    public void deleteCentrifuge(Location core) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement stmt = connection.prepareStatement("DELETE FROM Centrifuge WHERE core = ?;");
                    stmt.setString(1, core.getWorld().getName() + ", " + core.getX() + ", " + core.getY() + ", " + core.getZ());
                    stmt.executeUpdate();
                } catch (SQLException exp) {
                    exp.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }
}
