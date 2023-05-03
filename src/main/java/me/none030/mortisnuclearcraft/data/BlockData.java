package me.none030.mortisnuclearcraft.data;

import com.jeff_media.customblockdata.CustomBlockData;
import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.utils.NuclearType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class BlockData extends Data{

    private final Location core;
    private final NuclearType type;

    public BlockData(@NotNull Block block, @NotNull NuclearType type) {
        super(new CustomBlockData(block, MortisNuclearCraft.getInstance()));
        this.core = block.getLocation();
        this.type = type;
    }

    public BlockData(@NotNull Location location, @NotNull NuclearType type) {
        super(new CustomBlockData(location.getBlock(), MortisNuclearCraft.getInstance()));
        this.core = location;
        this.type = type;
    }

    public boolean isType() {
        return isType(type);
    }

    public void create() {
        String typeKey = "NuclearCraft";
        set(typeKey, type.name());
    }

    public Location getCore() {
        return core;
    }

    public NuclearType getType() {
        return type;
    }
}
