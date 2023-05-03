package me.none030.mortisnuclearcraft.nuclearcraft.radiatiton;

import me.none030.mortisnuclearcraft.utils.radiation.DisplayMode;
import me.none030.mortisnuclearcraft.utils.radiation.DisplayToggleMode;
import me.none030.mortisnuclearcraft.utils.radiation.RadiationMode;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class RadiationListener implements Listener {

    private final RadiationManager radiationManager;

    public RadiationListener(RadiationManager radiationManager) {
        this.radiationManager = radiationManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        radiationManager.getRadiation().getDisplay().show(radiationManager, player);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getPlayer();
        if (radiationManager.getRadiation(player) >= radiationManager.getRadiation().getMinRadiationEffect()) {
            e.deathMessage(Component.text(radiationManager.getMessage("RADIATION_DEATH")));
        }
        radiationManager.setRadiation(player, 0);
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
        pill.removeRadiation(radiationManager, player);
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
            radiationManager.getRadiation().getDisplay().show(radiationManager, player);
        }else {
            Boolean value = radiationManager.getToggleByPlayer().get(player.getUniqueId());
            if (value == null) {
                value = false;
            }
            if (value) {
                radiationManager.getRadiation().getDisplay().hide(radiationManager, player);
                player.sendMessage(radiationManager.getMessage("TOGGLE_OFF"));
            } else {
                radiationManager.getRadiation().getDisplay().show(radiationManager, player);
                player.sendMessage(radiationManager.getMessage("TOGGLE_ON"));
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
            mob.changeRadiation(radiationManager, player);
        }
    }
}
