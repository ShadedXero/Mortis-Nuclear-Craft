package me.none030.mortisnuclearcraft.nuclearcraft.waste;

import me.none030.mortisnuclearcraft.data.BlockData;
import me.none030.mortisnuclearcraft.utils.NuclearType;
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
}
