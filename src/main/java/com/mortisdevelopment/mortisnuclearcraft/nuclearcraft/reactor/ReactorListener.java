package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.reactor;

import com.mortisdevelopment.mortisnuclearcraft.structures.Structure;
import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import com.mortisdevelopment.mortisnuclearcraft.MortisNuclearCraft;
import com.mortisdevelopment.mortisnuclearcraft.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ReactorListener implements Listener {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final ReactorManager reactorManager;

    public ReactorListener(ReactorManager reactorManager) {
        this.reactorManager = reactorManager;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlockPlaced();
        Structure structure = reactorManager.getReactor().getStructure(block.getLocation());
        if (structure == null) {
            return;
        }
        ReactorData data = new ReactorData(block.getLocation());
        data.create(structure.getId(), null, null, null, null, true, null, -1, 0);
        reactorManager.add(block.getLocation());
        player.sendMessage(reactorManager.getMessage("REACTOR_BUILT"));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getHand() == null || !e.getHand().equals(EquipmentSlot.HAND)) {
            return;
        }
        Player player = e.getPlayer();
        if (!e.getAction().isRightClick() || player.isSneaking()) {
            return;
        }
        Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }
        Location core = block.getLocation();
        ReactorData data = reactorManager.getReactorData(core);
        if (data == null) {
            return;
        }
        if (!player.hasPermission("nuclearcraft.access")) {
            if (e.useInteractedBlock().equals(Event.Result.DENY) ) {
                return;
            }
            if (plugin.hasTowny()) {
                if (!PlayerCacheUtil.getCachePermission(player, block.getLocation(), block.getType(), TownyPermission.ActionType.SWITCH)) {
                    player.sendMessage(reactorManager.getMessage("SWITCH"));
                    return;
                }
            }
        }else {
            e.setCancelled(false);
        }
        ReactorMenu menu = new ReactorMenu(reactorManager, data);
        menu.open(player);
        e.setCancelled(true);
    }

    @EventHandler
    public void onMenuClose(InventoryCloseEvent e) {
        if (!(e.getInventory().getHolder() instanceof ReactorMenu)) {
            return;
        }
        ReactorMenu menu = (ReactorMenu) e.getInventory().getHolder();
        menu.cancelTask();
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == null || !(e.getClickedInventory().getHolder() instanceof ReactorMenu)) {
            return;
        }
        e.setCancelled(true);
        Integer tries = reactorManager.getPlayersInCoolDown().get(player.getUniqueId());
        if (tries != null && tries >= 3) {
            MessageUtils editor = new MessageUtils("&cPlease slow down");
            editor.color();
            player.sendMessage(editor.getMessage());
            return;
        }
        ReactorMenu menu = (ReactorMenu) e.getClickedInventory().getHolder();
        int slot = e.getRawSlot();
        ItemStack cursor = e.getCursor();
        ItemStack item = menu.click(player, slot, cursor);
        player.setItemOnCursor(item);
        if (reactorManager.getPlayersInCoolDown().get(player.getUniqueId()) == null) {
            reactorManager.getPlayersInCoolDown().put(player.getUniqueId(), 1);
        }else {
            int number = reactorManager.getPlayersInCoolDown().get(player.getUniqueId());
            reactorManager.getPlayersInCoolDown().put(player.getUniqueId(), number + 1);
        }
    }
}
