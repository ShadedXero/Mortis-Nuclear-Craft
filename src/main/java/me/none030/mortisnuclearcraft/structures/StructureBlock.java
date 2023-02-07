package me.none030.mortisnuclearcraft.structures;

import org.bukkit.Material;

public class StructureBlock {

    private final Material type;
    private final Vector vector;

    public StructureBlock(Material type, Vector vector) {
        this.type = type;
        this.vector = vector;
    }

    public Material getType() {
        return type;
    }

    public Vector getVector() {
        return vector;
    }
}
