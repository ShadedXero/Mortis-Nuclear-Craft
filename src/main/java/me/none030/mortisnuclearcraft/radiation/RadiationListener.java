package me.none030.mortisnuclearcraft.radiation;

import me.none030.mortisnuclearcraft.managers.NuclearCraftMessages;
import me.none030.mortisnuclearcraft.utils.radiation.DisplayMode;
import me.none030.mortisnuclearcraft.utils.radiation.DisplayToggleMode;
import me.none030.mortisnuclearcraft.utils.radiation.RadiationMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class RadiationListener implements Listener {

    private final RadiationManager radiationManager;

    public RadiationListener(RadiationManager radiationManager) {
        this.radiationManager = radiationManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (radiationManager.getRadiation(player) == -1) {
            radiationManager.getRadiationByPlayer().put(player.getUniqueId(), 0.0);
        }
        if (radiationManager.getBossBarByPlayer().get(player.getUniqueId()) == null) {
            radiationManager.getRadiation().getDisplay().createBossBar(player);
        }
        radiationManager.getRadiation().getDisplay().show(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        radiationManager.updateRadiation(player);
    }

    @EventHandler
    public void onUsePill(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItem();
        if (item == null) {
            return;
        }
        RadiationPill pill = radiationManager.getRadiation().getPill(item);
        if (pill == null) {
            return;
        }
        pill.removeRadiation(player);
    }

    @EventHandler
    public void onToggle(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItem();
        if (item == null) {
            return;
        }
        if (!radiationManager.getRadiation().getDisplay().getToggleMode().equals(DisplayToggleMode.ITEM)) {
            return;
        }
        ItemStack itemStack = radiationManager.getRadiation().getDisplay().getToggleItem();
        if (!item.equals(itemStack)) {
            return;
        }
        if (radiationManager.getRadiation().getDisplay().getMode().equals(DisplayMode.SINGLE)) {
            radiationManager.getRadiation().getDisplay().show(player);
        }else {
            Boolean value = radiationManager.getToggleByPlayer().get(player.getUniqueId());
            if (value == null) {
                value = false;
            }
            if (value) {
                radiationManager.getRadiation().getDisplay().hide(player);
                player.sendMessage(NuclearCraftMessages.TOGGLE_OFF);
            } else {
                radiationManager.getRadiation().getDisplay().show(player);
                player.sendMessage(NuclearCraftMessages.TOGGLE_ON);
            }
        }
    }

    @EventHandler
    public void onRadiationMobAttack(EntityDamageByEntityEvent e) {
        Entity entity = e.getDamager();
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) e.getEntity();
        for (RadiationMob mob : radiationManager.getRadiation().getMobs()) {
            if (!mob.isMob(entity)) {
                continue;
            }
            if (!mob.getMode().equals(RadiationMode.ATTACK)) {
                continue;
            }
            mob.changeRadiation(player);
        }
    }
}
