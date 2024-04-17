package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.waste;

import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.waste.types.BlockWaste;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.waste.types.CustomBlockWaste;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.waste.types.DroppedWaste;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class WasteListener implements Listener {

    private final WasteManager wasteManager;

    public WasteListener(WasteManager wasteManager) {
        this.wasteManager = wasteManager;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        BlockWaste waste = wasteManager.getBlockWaste(e.getItemInHand());
        if (waste == null) {
            return;
        }
        waste.createWaste(e.getBlockPlaced());
        wasteManager.add(e.getBlockPlaced().getLocation());
    }

    @EventHandler
    public void onCustomBlockPlace(BlockPlaceEvent e) {
        CustomBlockWaste waste = wasteManager.getCustomBlockWaste(e.getItemInHand());
        if (waste == null) {
            return;
        }
        waste.createWaste(e.getBlockPlaced());
        wasteManager.add(e.getBlockPlaced().getLocation());
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        WasteBlockData data = new WasteBlockData(e.getBlock());
        if (!data.isType() || data.getId() == null) {
            return;
        }
        data.remove(data.getIdKey());
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        DroppedWaste waste = wasteManager.getDroppedWaste(e.getItemDrop().getItemStack());
        if (waste == null) {
            return;
        }
        wasteManager.getDroppedItems().add(e.getItemDrop().getUniqueId());
    }
}
