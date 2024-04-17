package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.armors;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class ArmorListener implements Listener {

    private final ArmorManager armorManager;

    public ArmorListener(ArmorManager armorManager) {
        this.armorManager = armorManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (armorManager.getPlayersWithArmor().contains(player.getUniqueId())) {
            return;
        }
        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (item == null || item.getType().isAir()) {
                return;
            }
            String armorId = armorManager.getArmor(item);
            if (armorId == null) {
                continue;
            }
            RadiationArmor radiationArmor = armorManager.getArmorById().get(armorId);
            if (radiationArmor == null) {
                continue;
            }
            armorManager.getPlayersWithArmor().add(player.getUniqueId());
            return;
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        armorManager.getPlayersWithArmor().remove(player.getUniqueId());
    }

    @EventHandler
    public void onEquip(PlayerArmorChangeEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getNewItem();
        if (item == null || item.getType().isAir()) {
            return;
        }
        if (armorManager.getPlayersWithArmor().contains(player.getUniqueId())) {
            return;
        }
        String armorId = armorManager.getArmor(item);
        if (armorId == null) {
            return;
        }
        RadiationArmor radiationArmor = armorManager.getArmorById().get(armorId);
        if (radiationArmor == null) {
            return;
        }
        armorManager.getPlayersWithArmor().add(player.getUniqueId());
    }

    @EventHandler
    public void onUnEquip(PlayerArmorChangeEvent e) {
        Player player = e.getPlayer();
        ItemStack newItem = e.getNewItem();
        if (newItem != null && !newItem.getType().isAir()) {
            return;
        }
        ItemStack oldItem = e.getOldItem();
        if (oldItem == null || oldItem.getType().isAir()) {
            return;
        }
        if (!armorManager.getPlayersWithArmor().contains(player.getUniqueId())) {
            return;
        }
        boolean value = true;
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor == null || armor.getType().isAir()) {
                continue;
            }
            if (oldItem.equals(armor)) {
                continue;
            }
            String armorId = armorManager.getArmor(armor);
            if (armorId == null) {
                continue;
            }
            RadiationArmor radiationArmor = armorManager.getArmorById().get(armorId);
            if (radiationArmor == null) {
                continue;
            }
            value = false;
        }
        if (!value) {
            return;
        }
        armorManager.resetSpeed(player);
        armorManager.getPlayersWithArmor().remove(player.getUniqueId());
    }
}
