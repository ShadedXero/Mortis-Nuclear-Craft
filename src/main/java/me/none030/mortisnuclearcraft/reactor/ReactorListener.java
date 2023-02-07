package me.none030.mortisnuclearcraft.reactor;

import me.none030.mortisnuclearcraft.data.ReactorData;
import me.none030.mortisnuclearcraft.structures.Structure;
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

import static me.none030.mortisnuclearcraft.utils.MessageUtils.colorMessage;

public class ReactorListener implements Listener {

    private final ReactorManager reactorManager;

    public ReactorListener(ReactorManager reactorManager) {
        this.reactorManager = reactorManager;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlockPlaced();
        Structure structure = reactorManager.getReactor().getStructure();
        if (structure == null) {
            return;
        }
        if (!structure.isCore(block)) {
            return;
        }
        if (!structure.isReactor(block.getLocation())) {
            return;
        }
        ReactorData data = new ReactorData(block.getLocation(), (ItemStack) null, null, null, true, null, -1);
        reactorManager.getDataManager().getReactorStorage().storeReactor(data);
        player.sendMessage(ReactorMessages.REACTOR_BUILT);
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
        ReactorData data = reactorManager.getDataManager().getReactorStorage().getReactor(core);
        if (data == null) {
            return;
        }
        ReactorMenu menu = new ReactorMenu(reactorManager, data);
        menu.open(player);
        e.setCancelled(true);
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
            player.sendMessage(colorMessage("&cPlease slow down"));
            return;
        }
        ReactorMenu menu = (ReactorMenu) e.getClickedInventory().getHolder();
        int slot = e.getRawSlot();
        ItemStack cursor = e.getCursor();
        ItemStack item = menu.click(player, slot, cursor);
        e.setCursor(item);
        if (reactorManager.getPlayersInCoolDown().get(player.getUniqueId()) == null) {
            reactorManager.getPlayersInCoolDown().put(player.getUniqueId(), 1);
        }else {
            int number = reactorManager.getPlayersInCoolDown().get(player.getUniqueId());
            reactorManager.getPlayersInCoolDown().put(player.getUniqueId(), number + 1);
        }
    }
}
