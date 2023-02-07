package me.none030.mortisnuclearcraft.data;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class RadiationStorage {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final Connection connection;

    public RadiationStorage(Connection connection) {
        this.connection = connection;
        initializeDatabase();
    }
    private void initializeDatabase() {
        new BukkitRunnable() {
            @Override
            public void run() {
                String sql = "CREATE TABLE IF NOT EXISTS PlayerRadiation(uuid varchar(100) primary key, radiation double);";
                try {
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.executeUpdate();
                } catch (SQLException exp) {
                    exp.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public HashMap<UUID, Double> getRadiations() {
        HashMap<UUID, Double> radiationByPlayer = new HashMap<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM PlayerRadiation;");
            ResultSet resultSet = stmt.executeQuery();
            if (!resultSet.next()) {
                return radiationByPlayer;
            }
            while (resultSet.next()) {
                UUID player;
                try {
                    player = UUID.fromString(resultSet.getString("uuid"));
                }catch (IllegalArgumentException exp) {
                    continue;
                }
                double radiation = resultSet.getDouble("radiation");
                radiationByPlayer.put(player, radiation);
            }
        } catch (SQLException exp) {
            exp.printStackTrace();
        }
        return radiationByPlayer;
    }

    public double getRadiation(UUID player) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM PlayerRadiation WHERE uuid = ?;");
            stmt.setString(1, player.toString());
            ResultSet resultSet = stmt.executeQuery();
            if (!resultSet.next()) {
                return -1;
            }
            return resultSet.getDouble("radiation");
        } catch (SQLException exp) {
            exp.printStackTrace();
        }
        return -1;
    }

    public void storeRadiation(UUID player, double radiation) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getRadiation(player) != -1) {
                    updateRadiation(player, radiation);
                    return;
                }
                try {
                    String sql = "INSERT INTO PlayerRadiation(uuid, radiation) VALUES(?, ?);";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setString(1, player.toString());
                    stmt.setDouble(2, radiation);
                    stmt.execute();
                }catch (SQLException exp) {
                    exp.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    private void updateRadiation(UUID player, double radiation) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String sql = "UPDATE PlayerRadiation SET radiation = ? WHERE uuid = ?";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setDouble(1, radiation);
                    stmt.setString(2, player.toString());
                    stmt.executeUpdate();
                } catch (SQLException exp) {
                    exp.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }
}
