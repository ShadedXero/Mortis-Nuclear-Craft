package me.none030.mortisnuclearcraft.structures;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class Structure {

    private final String id;
    private final Material core;
    private final List<StructureBlock> blocks;

    public Structure(String id, Material coreBlock, List<StructureBlock> blocks) {
        this.id = id;
        this.core = coreBlock;
        this.blocks = blocks;
    }

    public boolean isCore(Block block) {
        return core.equals(block.getType());
    }

    public boolean isStructure(Location core) {
        for (StructureBlock block : blocks) {
            Vector vector = block.getVector();
            Material type = block.getType();
            Location loc = vector.getLocation(core);
            if (!loc.getBlock().getType().equals(type)) {
                return false;
            }
        }
        return true;
    }

    public boolean isReactor(Location core) {
        for (StructureBlock block : blocks) {
            Vector vector = block.getVector();
            Material type = block.getType();
            Location loc = vector.getLocation(core);
            if (type.equals(Material.CAULDRON) || type.equals(Material.WATER_CAULDRON)) {
                if (!loc.getBlock().getType().equals(Material.CAULDRON) && !loc.getBlock().getType().equals(Material.WATER_CAULDRON)) {
                    return false;
                }
            }else {
                if (!loc.getBlock().getType().equals(type)) {
                    return false;
                }
            }
        }
        return true;
    }


    public List<Block> getBlocksWithType(Location core, Material material) {
        List<Block> blockList = new ArrayList<>();
        for (StructureBlock structureBlock : blocks) {
            Vector vector = structureBlock.getVector();
            Location loc = vector.getLocation(core);
            Block block = loc.getBlock();
            if (block.getType().equals(material)) {
                blockList.add(block);
            }
        }
        return blockList;
    }

    public String getId() {
        return id;
    }

    public Material getCore() {
        return core;
    }

    public List<StructureBlock> getBlocks() {
        return blocks;
    }
}
