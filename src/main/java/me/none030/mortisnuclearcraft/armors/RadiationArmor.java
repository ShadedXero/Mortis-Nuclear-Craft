package me.none030.mortisnuclearcraft.armors;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.radiation.RadiationManager;
import me.none030.mortisnuclearcraft.utils.radiation.RadiationType;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class RadiationArmor {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final RadiationManager radiationManager;
    private final String id;
    private final ItemStack armor;
    private final RadiationType type;
    private final double radiation;
    private final int weight;

    public RadiationArmor(RadiationManager radiationManager, String id, ItemStack armor, RadiationType type, double radiation, int weight) {
        this.radiationManager = radiationManager;
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
        ItemMeta meta = armor.getItemMeta();
        NamespacedKey nuclearCraft = new NamespacedKey(plugin, "NuclearCraft");
        meta.getPersistentDataContainer().set(nuclearCraft, PersistentDataType.STRING, "ARMOR");
        NamespacedKey nuclearCraftId = new NamespacedKey(plugin, "NuclearCraftId");
        meta.getPersistentDataContainer().set(nuclearCraftId, PersistentDataType.STRING, id);
        armor.setItemMeta(meta);
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

    public RadiationManager getRadiationManager() {
        return radiationManager;
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
