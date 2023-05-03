package me.none030.mortisnuclearcraft.nuclearcraft.waste;

import me.none030.mortisnuclearcraft.utils.waste.WasteType;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class WasteListener implements Listener {

    private final WasteManager wasteManager;

    public WasteListener(WasteManager wasteManager) {
        this.wasteManager = wasteManager;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Block block = e.getBlockPlaced();
        ItemStack item = e.getItemInHand();
        String wasteId = wasteManager.getWasteItem(item);
        if (wasteId == null) {
            return;
        }
        Waste waste = wasteManager.getWasteById().get(wasteId);
        if (waste == null || waste.getType().equals(WasteType.ITEM)) {
            return;
        }
        waste.createWasteBlock(block);
        wasteManager.add(block.getLocation());
    }
}
