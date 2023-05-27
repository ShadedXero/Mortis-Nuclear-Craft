package me.none030.mortisnuclearcraft.nuclearcraft.bombs.itembomb;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public class ItemBombListener implements Listener {

    private final ItemBombManager itemBombManager;

    public ItemBombListener(ItemBombManager itemBombManager) {
        this.itemBombManager = itemBombManager;
    }

    @EventHandler
    public void onItemBombActivate(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItem();
        if (item == null) {
            return;
        }
        String bombId = itemBombManager.getItemBomb(item);
        if (bombId == null) {
            return;
        }
        ItemBomb bomb = itemBombManager.getItemBombById().get(bombId);
        if (bomb == null) {
            return;
        }
        if (!e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            e.setCancelled(true);
        }
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        }else {
            player.getInventory().removeItem(item);
        }
        bomb.launch(itemBombManager, player);
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
        String bombId = itemBombManager.getItemBomb(item);
        if (bombId == null) {
            return;
        }
        ItemBomb bomb = itemBombManager.getItemBombById().get(bombId);
        if (bomb == null) {
            return;
        }
        e.setConsumeItem(true);
        e.setCancelled(true);
        bomb.launch(itemBombManager, player);
    }
}
