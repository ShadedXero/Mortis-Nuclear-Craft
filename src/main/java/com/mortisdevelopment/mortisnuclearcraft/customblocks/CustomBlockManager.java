package com.mortisdevelopment.mortisnuclearcraft.customblocks;

import com.mortisdevelopment.mortisnuclearcraft.MortisNuclearCraft;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class CustomBlockManager {

    private final HashMap<String, CustomBlock> customBlockById;

    public CustomBlockManager() {
        this.customBlockById = new HashMap<>();
        MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
        plugin.getServer().getPluginManager().registerEvents(new CustomBlockListener(this), plugin);
    }

    public CustomBlock getCustomBlock(ItemStack item) {
        for (CustomBlock customBlock : getCustomBlocks()) {
            if (customBlock.isItem(item)) {
                return customBlock;
            }
        }
        return null;
    }

    public CustomBlock getCustomBlock(List<String> keys) {
        for (CustomBlock customBlock : getCustomBlocks()) {
            if (new HashSet<>(keys).containsAll(customBlock.getKeys())) {
                return customBlock;
            }
        }
        return null;
    }

    public List<CustomBlock> getCustomBlocks() {
        return new ArrayList<>(customBlockById.values());
    }

    public HashMap<String, CustomBlock> getCustomBlockById() {
        return customBlockById;
    }
}
