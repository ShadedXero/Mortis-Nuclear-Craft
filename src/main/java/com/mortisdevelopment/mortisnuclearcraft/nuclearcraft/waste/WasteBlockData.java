package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.waste;

import com.mortisdevelopment.mortisnuclearcraft.data.BlockData;
import com.mortisdevelopment.mortisnuclearcraft.utils.NuclearType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class WasteBlockData extends BlockData {

    private final String idKey = "NuclearCraftId";

    public WasteBlockData(@NotNull Block block) {
        super(block, NuclearType.WASTE);
    }

    public WasteBlockData(@NotNull Location location) {
        super(location, NuclearType.WASTE);
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

    public String getIdKey() {
        return idKey;
    }
}
