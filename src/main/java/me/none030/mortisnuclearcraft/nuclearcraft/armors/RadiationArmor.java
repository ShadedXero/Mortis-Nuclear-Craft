package me.none030.mortisnuclearcraft.nuclearcraft.armors;

import me.none030.mortisnuclearcraft.data.ItemData;
import me.none030.mortisnuclearcraft.utils.NuclearType;
import me.none030.mortisnuclearcraft.utils.radiation.RadiationType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RadiationArmor {

    private final String id;
    private final ItemStack armor;
    private final RadiationType type;
    private final double radiation;
    private final int weight;

    public RadiationArmor(String id, ItemStack armor, RadiationType type, double radiation, int weight) {
        this.id = id;
        this.armor = armor;
        this.type = type;
        this.radiation = radiation;
        this.weight = weight;
        createArmor();
    }

    public void giveArmor(Player player) {
        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItemNaturally(player.getLocation(), armor);
        } else {
            player.getInventory().addItem(armor);
        }
    }

    private void createArmor() {
        ArmorData data = new ArmorData(armor.getItemMeta());
        data.create(id);
        armor.setItemMeta(data.getMeta());
    }

    public void applyWeight(Player player) {
        player.setWalkSpeed(getWeight());
    }

    private float getWeight() {
        int number = 10 - this.weight;
        double number2 = (double) number / 100;
        double weight = number2 + 0.1;
        return (float) weight;
    }

    public String getId() {
        return id;
    }

    public ItemStack getArmor() {
        return armor;
    }

    public RadiationType getType() {
        return type;
    }

    public double getRadiation() {
        return radiation;
    }
}
