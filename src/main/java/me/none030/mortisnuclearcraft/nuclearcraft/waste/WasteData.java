package me.none030.mortisnuclearcraft.nuclearcraft.waste;

import me.none030.mortisnuclearcraft.data.ItemData;
import me.none030.mortisnuclearcraft.utils.NuclearType;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class WasteData extends ItemData {

    private final String idKey = "NuclearCraftId";

    public WasteData(@NotNull ItemMeta meta) {
        super(meta, NuclearType.WASTE);
    }

    public void create(String id) {
        create();
        setId(id);
    }

    public void setId(String id) {
        set(idKey, id);
    }

    public String getId() {
        return get(idKey);
    }
}
