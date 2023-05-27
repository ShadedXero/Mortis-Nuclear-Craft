package me.none030.mortisnuclearcraft.nuclearcraft.bombs.itembomb;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.nuclearcraft.Manager;
import me.none030.mortisnuclearcraft.nuclearcraft.radiatiton.RadiationManager;
import me.none030.mortisnuclearcraft.utils.NuclearType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class ItemBombManager extends Manager {

    private final RadiationManager radiationManager;
    private final HashMap<String, ItemBomb> itemBombById;

    public ItemBombManager(RadiationManager radiationManager) {
        super(NuclearType.ITEM_BOMB);
        this.radiationManager = radiationManager;
        this.itemBombById = new HashMap<>();
        MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
        plugin.getServer().getPluginManager().registerEvents(new ItemBombListener(this), plugin);
    }


    public String getItemBomb(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        ItemBombData data = new ItemBombData(meta);
        if (!data.isType()) {
            return null;
        }
        return data.getId();
    }

    public RadiationManager getRadiationManager() {
        return radiationManager;
    }

    public HashMap<String, ItemBomb> getItemBombById() {
        return itemBombById;
    }
}
