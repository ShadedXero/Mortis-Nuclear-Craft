package me.none030.mortisnuclearcraft.drops;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DropManager {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final List<Drop> drops;
    private final HashMap<String, Drop> dropById;

    public DropManager() {
        this.drops = new ArrayList<>();
        this.dropById = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(new DropListener(this), plugin);
    }

    public List<Drop> getDrops() {
        return drops;
    }

    public HashMap<String, Drop> getDropById() {
        return dropById;
    }
}
