package me.none030.mortisnuclearcraft.bombs;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.data.BlockBombData;
import me.none030.mortisnuclearcraft.utils.bomb.MessageTime;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.time.LocalDateTime;

import static me.none030.mortisnuclearcraft.utils.MessageUtils.colorMessage;

public class BombListener implements Listener {

    private final BombManager bombManager;

    public BombListener(BombManager bombManager) {
        this.bombManager = bombManager;
    }

    @EventHandler
    public void onItemBombActivate(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItem();
        if (item == null) {
            return;
        }
        String bombId = bombManager.getItemBomb(item);
        if (bombId == null) {
            return;
        }
        ItemBomb bomb = bombManager.getItemBombById().get(bombId);
        if (bomb == null) {
            return;
        }
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        }else {
            player.getInventory().removeItem(item);
        }
        bomb.launch(player);
    }

    @EventHandler
    public void onFireworkDamage(EntityDamageByEntityEvent e) {
        if (!e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) || !(e.getDamager() instanceof Firework)) {
            return;
        }
        Firework firework = (Firework) e.getDamager();
        FireworkMeta meta = firework.getFireworkMeta();
        Component component = meta.displayName();
        if (component == null) {
            return;
        }
        String name = LegacyComponentSerializer.legacyAmpersand().serialize(component);
        if (!name.equalsIgnoreCase("BOMB")) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onItemBombShoot(EntityShootBowEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) e.getEntity();
        ItemStack crossbow = e.getBow();
        if (crossbow == null || !crossbow.getType().equals(Material.CROSSBOW)) {
            return;
        }
        ItemStack item = e.getConsumable();
        if (item == null) {
            return;
        }
        String bombId = bombManager.getItemBomb(item);
        if (bombId == null) {
            return;
        }
        ItemBomb bomb = bombManager.getItemBombById().get(bombId);
        if (bomb == null) {
            return;
        }
        e.setConsumeItem(true);
        e.setCancelled(true);
        bomb.launch(player);

    }

    @EventHandler
    public void onBlockBombBuild(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlockPlaced();
        BlockBomb bomb = bombManager.getBlockBomb(block.getLocation());
        if (bomb == null) {
            return;
        }
        BlockBombData data = new BlockBombData(block.getLocation(), bomb.getId(), true, null);
        bombManager.getDataManager().getBombStorage().storeBomb(data);
        player.sendMessage(BombMessages.BOMB_BUILT);
    }

    @EventHandler
    public void onBlockBombInteract(PlayerInteractEvent e) {
        if (!e.getAction().isRightClick()) {
            return;
        }
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }
        BlockBombData data = bombManager.getDataManager().getBombStorage().getBomb(block.getLocation());
        if (data == null) {
            return;
        }
        BlockBombMenu menu = new BlockBombMenu(bombManager, data);
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
        Integer tries = bombManager.getPlayersInCoolDown().get(player.getUniqueId());
        if (tries != null && tries >= 3) {
            player.sendMessage(colorMessage("&cPlease slow down"));
            return;
        }
        BlockBombMenu menu = (BlockBombMenu) inv.getHolder();
        menu.click(player, e.getRawSlot());
        if (bombManager.getPlayersInCoolDown().get(player.getUniqueId()) == null) {
            bombManager.getPlayersInCoolDown().put(player.getUniqueId(), 1);
        }else {
            int number = bombManager.getPlayersInCoolDown().get(player.getUniqueId());
            bombManager.getPlayersInCoolDown().put(player.getUniqueId(), number + 1);
        }
    }

    @EventHandler
    public void onSettingBlockBombTimer(AsyncChatEvent e) {
        Player player = e.getPlayer();
        BlockBombData data = bombManager.getPlayersSettingTimer().get(player.getUniqueId());
        if (data == null) {
            return;
        }
        String message = LegacyComponentSerializer.legacyAmpersand().serialize(e.message());
        MessageTime messageTime = new MessageTime(message);
        LocalDateTime time = messageTime.getTime();
        if (time == null || messageTime.isOverLimit()) {
            return;
        }
        data.setTimer(bombManager, time);
        bombManager.getPlayersSettingTimer().remove(player.getUniqueId());
        player.sendMessage(BombMessages.TIMER_SET);
        e.setCancelled(true);
    }
}
