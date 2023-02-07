package me.none030.mortisnuclearcraft.radiation;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RadiationPill {

    private final RadiationManager radiationManager;
    private final ItemStack item;
    private final double radiation;

    public RadiationPill(RadiationManager radiationManager, ItemStack item, double radiation) {
        this.radiationManager = radiationManager;
        this.item = item;
        this.radiation = radiation;
    }

    public void removeRadiation(Player player) {
        player.getInventory().removeItem(item);
        radiationManager.removeRadiation(player, radiation);
        player.sendMessage(RadiationMessages.PILL_USED);
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
