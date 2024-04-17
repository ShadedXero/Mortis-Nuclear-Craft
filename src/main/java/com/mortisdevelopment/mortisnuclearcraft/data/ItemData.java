package com.mortisdevelopment.mortisnuclearcraft.data;

import com.mortisdevelopment.mortisnuclearcraft.utils.NuclearType;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ItemData extends Data {

    private final ItemMeta meta;
    private final NuclearType type;

    public ItemData(@NotNull ItemMeta meta, @NotNull NuclearType type) {
        super(meta.getPersistentDataContainer());
        this.meta = meta;
        this.type = type;
    }

    public boolean isType() {
        return isType(type);
    }

    public void create() {
        String typeKey = "NuclearCraft";
        set(typeKey, type.name());
    }

    public ItemMeta getMeta() {
        return meta;
    }

    public NuclearType getType() {
        return type;
    }
}
