package com.mortisdevelopment.mortisnuclearcraft.structures;

import com.jeff_media.customblockdata.CustomBlockData;
import com.mortisdevelopment.mortisnuclearcraft.MortisNuclearCraft;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class StructureBlock {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final Material material;
    private final BlockData data;
    private final Vector vector;
    private final boolean strict;
    private final List<String> keys;

    public StructureBlock(Material material, BlockData data, Vector vector, boolean strict, List<String> keys) {
        this.material = material;
        this.data = data;
        this.vector = vector;
        this.strict = strict;
        this.keys = keys;
    }

    public boolean isBlock(Block block) {
        if (!block.getType().equals(material)) {
            return false;
        }
        for (String key : keys) {
            CustomBlockData blockData = new CustomBlockData(block, plugin);
            String value = blockData.get(new NamespacedKey(plugin, key), PersistentDataType.STRING);
            if (value == null || !value.equalsIgnoreCase(key)) {
                return false;
            }
        }
        if (strict) {
            return block.getBlockData().matches(data);
        }else {
            return true;
        }
    }

    public boolean isBlock(List<Material> materials) {
        for (Material material : materials) {
            if (this.material.equals(material)) {
                return true;
            }
        }
        return false;
    }

    public boolean isBlock(List<Material> blocks, Material block) {
        for (Material material : blocks) {
            if (block.equals(material)) {
                return true;
            }
        }
        return false;
    }

    public List<Block> getBorder(Location core, Material material) {
        Location location = vector.getLocation(core);
        Block block = location.getBlock();
        List<Block> blocks = new ArrayList<>();
        blocks.add(block.getRelative(BlockFace.NORTH));
        blocks.add(block.getRelative(BlockFace.SOUTH));
        blocks.add(block.getRelative(BlockFace.EAST));
        blocks.add(block.getRelative(BlockFace.WEST));
        blocks.add(block.getRelative(BlockFace.UP));
        blocks.add(block.getRelative(BlockFace.DOWN));
        List<Block> borders = new ArrayList<>();
        for (Block border : blocks) {
            if (border.getType().equals(material)) {
                borders.add(border);
            }
        }
        return borders;
    }

    public boolean hasRedstoneSignal(Location core) {
        Location location = vector.getLocation(core);
        Block block = location.getBlock();
        return block.isBlockPowered() || block.isBlockIndirectlyPowered();
    }

    public Material getMaterial() {
        return material;
    }

    public BlockData getData() {
        return data;
    }

    public Vector getVector() {
        return vector;
    }

    public boolean isStrict() {
        return strict;
    }

    public List<String> getKeys() {
        return keys;
    }
}
