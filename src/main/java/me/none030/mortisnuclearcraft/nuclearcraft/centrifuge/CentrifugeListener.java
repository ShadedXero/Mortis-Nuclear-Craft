package me.none030.mortisnuclearcraft.nuclearcraft.centrifuge;

import me.none030.mortisnuclearcraft.structures.Structure;
import me.none030.mortisnuclearcraft.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CentrifugeListener implements Listener {

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
        data.create(structure.getId(), null, null, null, null, null, true, null, -1);
        centrifugeManager.add(block.getLocation());
        player.sendMessage(centrifugeManager.getMessage("CENTRIFUGE_BUILT"));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (!e.getAction().isRightClick()) {
            return;
        }
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }
        Location core = block.getLocation();
        CentrifugeData data = centrifugeManager.getCentrifugeData(core);
        if (data == null) {
            return;
        }
        CentrifugeMenu menu = new CentrifugeMenu(centrifugeManager, data);
        menu.open(player);
        e.setCancelled(true);
    }

    @EventHandler
    public void onMenuOpen(InventoryOpenEvent e) {
        if (!(e.getInventory().getHolder() instanceof CentrifugeMenu)) {
            return;
        }
        CentrifugeMenu menu = (CentrifugeMenu) e.getInventory().getHolder();
        menu.check();
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
        e.setCursor(item);
        if (centrifugeManager.getPlayersInCoolDown().get(player.getUniqueId()) == null) {
            centrifugeManager.getPlayersInCoolDown().put(player.getUniqueId(), 1);
        }else {
            int number = centrifugeManager.getPlayersInCoolDown().get(player.getUniqueId());
            centrifugeManager.getPlayersInCoolDown().put(player.getUniqueId(), number + 1);
        }
    }
}
