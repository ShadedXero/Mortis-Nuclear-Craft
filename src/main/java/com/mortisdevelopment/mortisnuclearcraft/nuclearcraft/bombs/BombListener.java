package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.bombs;

import com.palmergames.bukkit.towny.event.actions.TownyExplodingBlocksEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.List;
import java.util.UUID;

public class BombListener implements Listener {

    private final Bomb bomb;

    public BombListener(Bomb bomb) {
        this.bomb = bomb;
    }

    @EventHandler
    public void onTownExplode(TownyExplodingBlocksEvent e) {
        bomb.debug("Bomb onTownExplode");
        System.out.println("Bomb onTownExplode");
        Entity entity = e.getEntity();
        if (entity == null) {
            return;
        }
        UUID uuid = entity.getUniqueId();
        Boolean regen = bomb.getRegenByTownExplosion().get(uuid);
        if (regen == null) {
            return;
        }
        bomb.getRegenByTownExplosion().remove(uuid);
        List<Block> blockList = e.getVanillaBlockList();
        e.setBlockList(blockList);
        if (regen) {
            Event event = e.getBukkitExplodeEvent();
            if (event instanceof BlockExplodeEvent) {
                ((BlockExplodeEvent) event).setYield(0);
            }else if (event instanceof EntityExplodeEvent) {
                ((EntityExplodeEvent) event).setYield(0);
            }
            bomb.regen(uuid, blockList);
        }
    }
}
