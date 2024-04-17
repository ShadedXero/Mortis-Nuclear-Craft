package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.waste.types;

import com.mortisdevelopment.mortisnuclearcraft.customblocks.CustomBlock;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.waste.WasteBlockData;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CustomBlockWaste extends Waste {

    private final CustomBlock customBlock;
    private final int radius;

    public CustomBlockWaste(String id, double radiation, int radius, CustomBlock customBlock) {
        super(id, radiation);
        this.customBlock = customBlock;
        this.radius = radius;
    }

    public void createWaste(Block block) {
        WasteBlockData data = new WasteBlockData(block);
        data.create(getId());
    }

    @Override
    public void giveWaste(Player player) {
        customBlock.giveCustomBlock(player);
    }

    @Override
    public boolean isWaste(ItemStack item) {
        return customBlock.isItem(item);
    }

    public CustomBlock getCustomBlock() {
        return customBlock;
    }

    public int getRadius() {
        return radius;
    }
}
