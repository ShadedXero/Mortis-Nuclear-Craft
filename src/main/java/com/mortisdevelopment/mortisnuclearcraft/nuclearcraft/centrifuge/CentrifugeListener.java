package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.centrifuge;

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

public class CentrifugeListener implements Listener {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final CentrifugeManager centrifugeManager;

    public CentrifugeListener(CentrifugeManager centrifugeManager) {
        this.centrifugeManager = centrifugeManager;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlockPlaced();
        Structure structure = centrifugeManager.getCentrifuge().getStructure(block.getLocation());
        if (structure == null) {
            return;
        }
        CentrifugeData data = new CentrifugeData(block.getLocation());
        data.create(structure.getId(), null, null, null, null, true, null, -1, 0);
        centrifugeManager.add(block.getLocation());
        player.sendMessage(centrifugeManager.getMessage("CENTRIFUGE_BUILT"));
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
        CentrifugeData data = centrifugeManager.getCentrifugeData(core);
        if (data == null) {
            return;
        }
        if (!player.hasPermission("nuclearcraft.access")) {
            if (e.useInteractedBlock().equals(Event.Result.DENY) ) {
                return;
            }
            if (plugin.hasTowny()) {
                if (!PlayerCacheUtil.getCachePermission(player, block.getLocation(), block.getType(), TownyPermission.ActionType.SWITCH)) {
                    player.sendMessage(centrifugeManager.getMessage("SWITCH"));
                    return;
                }
            }
        }else {
            e.setCancelled(false);
        }
        CentrifugeMenu menu = new CentrifugeMenu(centrifugeManager, data);
        menu.open(player);
        e.setCancelled(true);
    }

    @EventHandler
    public void onMenuClose(InventoryCloseEvent e) {
        if (!(e.getInventory().getHolder() instanceof CentrifugeMenu)) {
            return;
        }
        CentrifugeMenu menu = (CentrifugeMenu) e.getInventory().getHolder();
        menu.cancelTask();
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == null || !(e.getClickedInventory().getHolder() instanceof CentrifugeMenu)) {
            return;
        }
        e.setCancelled(true);
        Integer tries = centrifugeManager.getPlayersInCoolDown().get(player.getUniqueId());
        if (tries != null && tries >= 3) {
            MessageUtils editor = new MessageUtils("&cPlease slow down");
            editor.color();
            player.sendMessage(editor.getMessage());
            return;
        }
        CentrifugeMenu menu = (CentrifugeMenu) e.getClickedInventory().getHolder();
        int slot = e.getRawSlot();
        ItemStack cursor = e.getCursor();
        ItemStack item = menu.click(player, slot, cursor);
        player.setItemOnCursor(item);
        if (centrifugeManager.getPlayersInCoolDown().get(player.getUniqueId()) == null) {
            centrifugeManager.getPlayersInCoolDown().put(player.getUniqueId(), 1);
        }else {
            int number = centrifugeManager.getPlayersInCoolDown().get(player.getUniqueId());
            centrifugeManager.getPlayersInCoolDown().put(player.getUniqueId(), number + 1);
        }
    }
}
