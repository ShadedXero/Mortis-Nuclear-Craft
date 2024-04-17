package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.bombs.blockbomb;

import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.bombs.Bomb;
import com.mortisdevelopment.mortisnuclearcraft.structures.Structure;
import org.bukkit.Location;

import java.util.List;

public class BlockBomb extends Bomb {

    private final String id;
    private final String name;
    private final List<Structure> structures;

    public BlockBomb(String id, String name, int strength, int radius, long duration, double radiation, boolean vehicles, boolean drain, boolean fire, boolean blockDamage, boolean townyBlockDamage, boolean blockRegen, boolean townyBlockRegen, long regenTime, boolean townyPlayerDamage, boolean explosionsEnabledPlotsWilderness, List<Structure> structures) {
        super(strength, radius, duration, radiation, vehicles, drain, fire, blockDamage, townyBlockDamage, blockRegen, townyBlockRegen, regenTime, townyPlayerDamage, explosionsEnabledPlotsWilderness);
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
