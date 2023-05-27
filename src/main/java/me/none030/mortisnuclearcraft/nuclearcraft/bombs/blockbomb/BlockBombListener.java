package me.none030.mortisnuclearcraft.nuclearcraft.bombs.blockbomb;

import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.structures.Structure;
import me.none030.mortisnuclearcraft.utils.MessageUtils;
import me.none030.mortisnuclearcraft.utils.bomb.MessageTime;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;

import java.time.LocalDateTime;

public class BlockBombListener implements Listener {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final BlockBombManager blockBombManager;

    public BlockBombListener(BlockBombManager blockBombManager) {
        this.blockBombManager = blockBombManager;
    }

    @EventHandler
    public void onBlockBombBuild(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlockPlaced();
        BlockBomb bomb = blockBombManager.getBlockBomb(block.getLocation());
        if (bomb == null) {
            return;
        }
        Structure structure = bomb.getStructure(block.getLocation());
        BlockBombData data = new BlockBombData(block.getLocation());
        data.create(bomb.getId(), structure.getId(), true, null);
        blockBombManager.add(block.getLocation());
        player.sendMessage(blockBombManager.getMessage("BOMB_BUILT"));
    }

    @EventHandler
    public void onBlockBombInteract(PlayerInteractEvent e) {
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
        BlockBombData data = blockBombManager.getBombData(block.getLocation());
        if (data == null) {
            return;
        }
        if (!player.hasPermission("nuclearcraft.access")) {
            if (e.useInteractedBlock().equals(Event.Result.DENY) ) {
                return;
            }
            if (plugin.hasTowny()) {
                if (!PlayerCacheUtil.getCachePermission(player, block.getLocation(), block.getType(), TownyPermission.ActionType.SWITCH)) {
                    player.sendMessage(blockBombManager.getMessage("SWITCH"));
                    return;
                }
            }
        }else {
            e.setCancelled(false);
        }
        BlockBombMenu menu = new BlockBombMenu(blockBombManager, data);
        menu.open(player);
    }

    @EventHandler
    public void onBlockBombClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Inventory inv = e.getClickedInventory();
        if (inv == null || !(inv.getHolder() instanceof BlockBombMenu)) {
            return;
        }
        e.setCancelled(true);
        Integer tries = blockBombManager.getPlayersInCoolDown().get(player.getUniqueId());
        if (tries != null && tries >= 3) {
            MessageUtils editor = new MessageUtils("&cPlease slow down");
            editor.color();
            player.sendMessage(editor.getMessage());
            return;
        }
        BlockBombMenu menu = (BlockBombMenu) inv.getHolder();
        menu.click(player, e.getRawSlot());
        if (blockBombManager.getPlayersInCoolDown().get(player.getUniqueId()) == null) {
            blockBombManager.getPlayersInCoolDown().put(player.getUniqueId(), 1);
        }else {
            int number = blockBombManager.getPlayersInCoolDown().get(player.getUniqueId());
            blockBombManager.getPlayersInCoolDown().put(player.getUniqueId(), number + 1);
        }
    }

    @EventHandler
    public void onSettingBlockBombTimer(AsyncChatEvent e) {
        Player player = e.getPlayer();
        BlockBombData data = blockBombManager.getPlayersSettingTimer().get(player.getUniqueId());
        if (data == null) {
            return;
        }
        String message = LegacyComponentSerializer.legacyAmpersand().serialize(e.message());
        MessageTime messageTime = new MessageTime(message);
        LocalDateTime time = messageTime.getTime();
        if (time == null || messageTime.isOverLimit()) {
            return;
        }
        data.setTimer(time);
        blockBombManager.getPlayersSettingTimer().remove(player.getUniqueId());
        player.sendMessage(blockBombManager.getMessage("TIMER_SET"));
        e.setCancelled(true);
    }
}
