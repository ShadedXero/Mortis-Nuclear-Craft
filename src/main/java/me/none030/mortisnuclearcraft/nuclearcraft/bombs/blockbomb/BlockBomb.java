package me.none030.mortisnuclearcraft.nuclearcraft.bombs.blockbomb;

import me.none030.mortisnuclearcraft.nuclearcraft.bombs.Bomb;
import me.none030.mortisnuclearcraft.structures.Structure;
import org.bukkit.Location;

import java.util.List;

public class BlockBomb extends Bomb {

    private final String id;
    private final String name;
    private final List<Structure> structures;

    public BlockBomb(String id, String name, int strength, int radius, long duration, double radiation, boolean vehicles, boolean drain, boolean fire, boolean blockDamage, boolean townyBlockDamage, boolean blockRegen, boolean townyBlockRegen, long regenTime, List<Structure> structures) {
        super(strength, radius, duration, radiation, vehicles, drain, fire, blockDamage, townyBlockDamage, blockRegen, townyBlockRegen, regenTime);
        this.id = id;
        this.name = name;
        this.structures = structures;
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

    public List<Structure> getStructures() {
        return structures;
    }
}
