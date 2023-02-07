package me.none030.mortisnuclearcraft.data;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BombStorage {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final Connection connection;

    public BombStorage(Connection connection) {
        this.connection = connection;
        initializeDatabase();
    }

    private void initializeDatabase() {
        new BukkitRunnable() {
            @Override
            public void run() {
                String sql = "CREATE TABLE IF NOT EXISTS BlockBombs(core varchar(100) primary key, id varchar(100), manualMode boolean, timer varchar(100));";
                try {
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.executeUpdate();
                } catch (SQLException exp) {
                    exp.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public BlockBombData getBomb(Location core) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM BlockBombs WHERE core = ?;");
            stmt.setString(1, core.getWorld().getName() + ", " + core.getX() + ", " + core.getY() + ", " + core.getZ());
            ResultSet resultSet = stmt.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            String id = resultSet.getString("id");
            boolean manualMode = resultSet.getBoolean("manualMode");
            String time = resultSet.getString("timer");
            LocalDateTime timer;
            if (time != null) {
                timer = LocalDateTime.parse(time);
            }else {
                timer = null;
            }
            return new BlockBombData(core, id, manualMode, timer);
        } catch (SQLException exp) {
            exp.printStackTrace();
        }
        return null;
    }

    public List<BlockBombData> getBombs() {
        List<BlockBombData> dataList = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM BlockBombs;");
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                String[] location = resultSet.getString("core").split(",");
                Location core = new Location(Bukkit.getWorld(location[0]), Double.parseDouble(location[1]), Double.parseDouble(location[2]), Double.parseDouble(location[3]));
                String id = resultSet.getString("id");
                boolean manualMode = resultSet.getBoolean("manualMode");
                String time = resultSet.getString("timer");
                LocalDateTime timer;
                if (time != null) {
                    timer = LocalDateTime.parse(time);
                }else {
                    timer = null;
                }
                BlockBombData data = new BlockBombData(core, id, manualMode, timer);
                dataList.add(data);
            }
        } catch (SQLException exp) {
            exp.printStackTrace();
        }
        return dataList;
    }
    public void storeBomb(BlockBombData data) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String sql = "INSERT INTO BlockBombs(core, id, manualMode, timer) VALUES(?, ?, ?, ?);";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setString(1, data.getCore().getWorld().getName() + ", " + data.getCore().getX() + ", " + data.getCore().getY() + ", " + data.getCore().getZ());
                    stmt.setString(2, data.getId());
                    stmt.setBoolean(3, data.isManualMode());
                    if (data.getTimer() != null) {
                        stmt.setString(4, data.getTimer().toString());
                    }else {
                        stmt.setString(4, null);
                    }
                    stmt.execute();
                }catch (SQLException exp) {
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
                    String sql = "UPDATE BlockBombs SET manualMode = ? WHERE core = ?";
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

    public void updateTimer(Location core, LocalDateTime timer) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String sql = "UPDATE BlockBombs SET timer = ? WHERE core = ?";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    if (timer != null) {
                        stmt.setString(1, timer.toString());
                    }else {
                        stmt.setString(1, null);
                    }
                    stmt.setString(2, core.getWorld().getName() + ", " + core.getX() + ", " + core.getY() + ", " + core.getZ());
                    stmt.executeUpdate();
                } catch (SQLException exp) {
                    exp.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void deleteBomb(Location core) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement stmt = connection.prepareStatement("DELETE FROM BlockBombs WHERE core = ?;");
                    stmt.setString(1, core.getWorld().getName() + ", " + core.getX() + ", " + core.getY() + ", " + core.getZ());
                    stmt.executeUpdate();
                } catch (SQLException exp) {
                    exp.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }
}
