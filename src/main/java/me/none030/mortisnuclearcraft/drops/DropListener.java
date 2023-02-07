package me.none030.mortisnuclearcraft.drops;

import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class DropListener implements Listener {

    private final DropManager dropManager;

    public DropListener(DropManager dropManager) {
        this.dropManager = dropManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        for (Drop drop : dropManager.getDrops()) {
            drop.drop(block.getLocation(), block.getType());
        }
    }

    @EventHandler
    public void onKillMob(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();
        Player player = entity.getKiller();
        if (player == null) {
            return;
        }
        for (Drop drop : dropManager.getDrops()) {
            drop.drop(entity.getLocation(), entity.getType());
        }
    }
}
