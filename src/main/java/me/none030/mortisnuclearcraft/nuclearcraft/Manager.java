package me.none030.mortisnuclearcraft.nuclearcraft;

import me.none030.mortisnuclearcraft.config.DataConfig;
import me.none030.mortisnuclearcraft.data.BlockData;
import me.none030.mortisnuclearcraft.utils.NuclearType;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Manager {

    private final DataConfig config;
    private final List<Location> cores;
    private final HashMap<String, String> messageById;

    public Manager(NuclearType type) {
        this.cores = new ArrayList<>();
        this.config = new DataConfig(this, type);
        this.messageById = new HashMap<>();
    }

    public void delete(BlockData data) {
        data.clear();
        cores.remove(data.getCore());
        config.remove(data.getCore());
    }

    public void delete(Location core) {
        cores.remove(core);
        config.remove(core);
    }

    public void add(Location core) {
        cores.add(core);
        config.add(core);
    }

    public void addMessage(String id, String message) {
        messageById.put(id, message);
    }

    public void addMessages(HashMap<String, String> messageById) {
        this.messageById.putAll(messageById);
    }

    public DataConfig getConfig() {
        return config;
    }

    public List<Location> getCores() {
        return cores;
    }

    public HashMap<String, String> getMessageById() {
        return messageById;
    }

    public String getMessage(String id) {
        return messageById.get(id);
    }
}
