package me.none030.mortisnuclearcraft.nuclearcraft.bombs.itembomb;

import me.none030.mortisnuclearcraft.data.ItemData;
import me.none030.mortisnuclearcraft.utils.NuclearType;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ItemBombData extends ItemData {

    private final String idKey = "NuclearCraftId";

    public ItemBombData(@NotNull ItemMeta meta) {
        super(meta, NuclearType.ITEM_BOMB);
    }

    public void create(String id) {
        create();
        setId(id);
    }

    public String getId() {
        return get(idKey);
    }

    public void setId(String id) {
        set(idKey, id);
    }
}
