package me.none030.mortisnuclearcraft.nuclearcraft.bombs.blockbomb;

import me.none030.mortisnuclearcraft.nuclearcraft.bombs.Bomb;
import me.none030.mortisnuclearcraft.structures.Structure;
import org.bukkit.Location;

import java.util.List;

public class BlockBomb extends Bomb {

    private final String id;
    private final String name;
    private final int strength;
    private final int duration;
    private final double radiation;
    private final boolean vehicles;
    private final boolean drain;
    private final List<Structure> structures;

    public BlockBomb(String id, String name, int strength, int radius, int duration, double radiation, boolean vehicles, boolean drain, List<Structure> structures) {
        super(radius);
        this.id = id;
        this.name = name;
        this.strength = strength;
        this.duration = duration;
        this.radiation = radiation;
        this.vehicles = vehicles;
        this.drain = drain;
        this.structures = structures;
    }

    public void explode(BlockBombManager bombManager, Location loc) {
        explode(loc, strength, vehicles, drain);
        radiate(bombManager.getRadiationManager(), loc, duration, radiation);
    }

    public Structure getStructure(String structureId) {
        for (Structure structure : structures) {
            if (structure.getId().equalsIgnoreCase(structureId)) {
                return structure;
            }
        }
        return null;
    }

    public Structure getStructure(Location location) {
        for (Structure structure : structures) {
            if (structure.isStructure(location)) {
                return structure;
            }
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStrength() {
        return strength;
    }

    public int getDuration() {
        return duration;
    }

    public double getRadiation() {
        return radiation;
    }

    public boolean isVehicles() {
        return vehicles;
    }

    public boolean isDrain() {
        return drain;
    }

    public List<Structure> getStructures() {
        return structures;
    }
}
