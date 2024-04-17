package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.bombs.itembomb;

import com.mortisdevelopment.mortisnuclearcraft.MortisNuclearCraft;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.Manager;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.radiatiton.RadiationManager;
import com.mortisdevelopment.mortisnuclearcraft.utils.NuclearType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class ItemBombManager extends Manager {

    private final RadiationManager radiationManager;
    private final HashMap<String, ItemBomb> itemBombById;

    public ItemBombManager(RadiationManager radiationManager) {
        super(null, NuclearType.ITEM_BOMB);
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
