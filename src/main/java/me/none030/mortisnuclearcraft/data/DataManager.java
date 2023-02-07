package me.none030.mortisnuclearcraft.data;

import java.sql.Connection;

public class DataManager {

    private final Connection connection;
    private final RadiationStorage radiationStorage;
    private final BombStorage bombStorage;
    private final CentrifugeStorage centrifugeStorage;
    private final ReactorStorage reactorStorage;

    public DataManager(Connection connection) {
        this.connection = connection;
        this.radiationStorage = new RadiationStorage(connection);
        this.bombStorage = new BombStorage(connection);
        this.centrifugeStorage = new CentrifugeStorage(connection);
        this.reactorStorage = new ReactorStorage(connection);
    }

    public Connection getConnection() {
        return connection;
    }

    public RadiationStorage getRadiationStorage() {
        return radiationStorage;
    }

    public BombStorage getBombStorage() {
        return bombStorage;
    }

    public CentrifugeStorage getCentrifugeStorage() {
        return centrifugeStorage;
    }

    public ReactorStorage getReactorStorage() {
        return reactorStorage;
    }
}
