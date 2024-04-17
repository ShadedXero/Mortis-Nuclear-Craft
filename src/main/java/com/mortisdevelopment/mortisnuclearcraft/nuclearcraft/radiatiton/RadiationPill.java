package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.radiatiton;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RadiationPill {

    private final ItemStack item;
    private final double radiation;

    public RadiationPill(ItemStack item, double radiation) {
        this.item = item;
        this.radiation = radiation;
    }

    public void removeRadiation(RadiationManager radiationManager, Player player) {
        int amount = player.getInventory().getItemInMainHand().getAmount();
        if (amount == 1) {
            player.getInventory().setItemInMainHand(null);
        }else {
            player.getInventory().getItemInMainHand().setAmount(amount - 1);
        }
        radiationManager.removeRadiation(player, radiation);
        player.sendMessage(radiationManager.getMessage("PILL_USED"));
    }

    public boolean isPill(ItemStack item) {
        return this.item.isSimilar(item);
    }

    public ItemStack getItem() {
        return item;
    }

    public double getRadiation() {
        return radiation;
    }
}
