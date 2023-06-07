package me.none030.mortisnuclearcraft.structures;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Hopper;
import org.bukkit.block.data.Directional;

import java.util.ArrayList;
import java.util.List;

public class Structure {

    private final String id;
    private final StructureBlock core;
    private final List<StructureBlock> blocks;

    public Structure(String id, StructureBlock core, List<StructureBlock> blocks) {
        this.id = id;
        this.core = core;
        this.blocks = blocks;
    }

    public boolean isStructure(Location loc) {
        if (!getCore().isBlock(loc.getBlock())) {
            return false;
        }
        for (StructureBlock block : getBlocks()) {
            Location location = block.getVector().getLocation(loc);
            if (!block.isBlock(location.getBlock())) {
                return false;
            }
        }
        return true;
    }

    public boolean isStructure(Location loc, boolean core) {
        if (core) {
            if (!getCore().isBlock(loc.getBlock())) {
                return false;
            }
        }
        for (StructureBlock block : getBlocks()) {
            Location location = block.getVector().getLocation(loc);
            if (!block.isBlock(location.getBlock())) {
                return false;
            }
        }
        return true;
    }

    public boolean isStructure(Location loc, List<Material> ignored) {
        if (!getCore().isBlock(loc.getBlock())) {
            return false;
        }
        for (StructureBlock block : getBlocks()) {
            Location location = block.getVector().getLocation(loc);
            if (block.isBlock(ignored)) {
                if (!block.isBlock(ignored, location.getBlock().getType())) {
                    return false;
                }
            }else {
                if (!block.isBlock(location.getBlock())) {
                    return false;
                }
            }
        }
        return true;
    }

    public void destroy(Location core) {
        core.getBlock().setType(Material.AIR);
        for (StructureBlock block : getBlocks()) {
            Vector vector = block.getVector();
            Location loc = vector.getLocation(core);
            loc.getBlock().setType(Material.AIR);
        }
    }

    public List<Block> getSpecificBlocks(Location core, Material material) {
        List<Block> blockList = new ArrayList<>();
        for (StructureBlock structureBlock : getBlocks()) {
            Vector vector = structureBlock.getVector();
            Location loc = vector.getLocation(core);
            Block block = loc.getBlock();
            if (block.getType().equals(material)) {
                blockList.add(block);
            }
        }
        return blockList;
    }

    public Block getSpecificBlock(Location core, Vector vector) {
        Location loc = vector.getLocation(core);
        return loc.getBlock();
    }

    public boolean isSpecifiedBlock(Location core, Vector vector, Material material) {
        Location loc = vector.getLocation(core);
        return loc.getBlock().getType().equals(material);
    }

    public boolean hasRedstoneSignal(Location core) {
        if (getCore().hasRedstoneSignal(core)) {
            return true;
        }
        for (StructureBlock structureBlock : blocks) {
            if (structureBlock.hasRedstoneSignal(core)) {
                return true;
            }
        }
        return false;
    }

    public List<Hopper> getInHoppers(Location core) {
        List<StructureBlock> structureBlocks = new ArrayList<>(this.blocks);
        blocks.add(this.core);
        List<Hopper> hoppers = new ArrayList<>();
        for (StructureBlock structureBlock : structureBlocks) {
            Location location = structureBlock.getVector().getLocation(core);
            List<Block> blocks = structureBlock.getBorder(core, Material.HOPPER);
            for (Block block : blocks) {
                Hopper hopper = (Hopper) block.getState();
                Directional data = (Directional) hopper.getBlockData();
                BlockFace face = data.getFacing();
                Location facedLoc = block.getRelative(face).getLocation();
                if (facedLoc.equals(location)) {
                    hoppers.add(hopper);
                }
            }
        }
        return hoppers;
    }

    public List<Hopper> getOutHoppers(Location core) {
        List<StructureBlock> structureBlocks = new ArrayList<>(this.blocks);
        blocks.add(this.core);
        List<Hopper> hoppers = new ArrayList<>();
        for (StructureBlock structureBlock : structureBlocks) {
            Location location = structureBlock.getVector().getLocation(core);
            List<Block> blocks = structureBlock.getBorder(core, Material.HOPPER);
            for (Block block : blocks) {
                Hopper hopper = (Hopper) block.getState();
                Directional data = (Directional) hopper.getBlockData();
                BlockFace face = data.getFacing();
                Location facedLoc = block.getRelative(face).getLocation();
                if (!facedLoc.equals(location)) {
                    hoppers.add(hopper);
                }
            }
        }
        return hoppers;
    }

    public String getId() {
        return id;
    }

    public StructureBlock getCore() {
        return core;
    }

    public List<StructureBlock> getBlocks() {
        return blocks;
    }

}
