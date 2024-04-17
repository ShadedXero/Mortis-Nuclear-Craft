package com.mortisdevelopment.mortisnuclearcraft.customblocks;

import com.jeff_media.customblockdata.CustomBlockData;
import com.mortisdevelopment.mortisnuclearcraft.MortisNuclearCraft;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import java.util.List;

public class CustomBlockListener implements Listener {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final CustomBlockManager customBlockManager;

    public CustomBlockListener(CustomBlockManager customBlockManager) {
        this.customBlockManager = customBlockManager;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (e.isCancelled()) {
            return;
        }
        CustomBlock customBlock = customBlockManager.getCustomBlock(e.getItemInHand());
        if (customBlock == null) {
            return;
        }
        customBlock.createBlock(e.getBlockPlaced());
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (e.isCancelled()) {
            return;
        }
        List<String> keys = new ArrayList<>();
        for (NamespacedKey namespacedKey : new CustomBlockData(e.getBlock(), plugin).getKeys()) {
            keys.add(namespacedKey.value());
        }
        CustomBlock customBlock = customBlockManager.getCustomBlock(keys);
        if (customBlock == null) {
            return;
        }
        e.setDropItems(false);
        customBlock.dropCustomBlock(e.getBlock().getLocation());
    }
}
