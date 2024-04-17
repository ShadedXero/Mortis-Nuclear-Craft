package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft;

import com.mortisdevelopment.mortisnuclearcraft.data.BlockData;
import com.mortisdevelopment.mortisnuclearcraft.data.DataManager;
import com.mortisdevelopment.mortisnuclearcraft.utils.NuclearType;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Manager {

    private final DataManager dataManager;
    private final NuclearType type;
    private final List<Location> cores;
    private final HashMap<String, String> messageById;

    public Manager(DataManager dataManager, NuclearType type) {
        this.dataManager = dataManager;
        this.type = type;
        this.cores = new ArrayList<>();
        this.messageById = new HashMap<>();
        if (dataManager != null) {
            dataManager.load(this, type);
        }
    }

    public void delete(BlockData data) {
        data.clear();
        cores.remove(data.getCore());
        dataManager.remove(type, data.getCore());
    }

    public void delete(Location core) {
        cores.remove(core);
        dataManager.remove(type, core);
    }

    public void add(Location core) {
        cores.add(core);
        dataManager.add(type, core);
    }

    public void addMessage(String id, String message) {
        messageById.put(id, message);
    }

    public void addMessages(HashMap<String, String> messageById) {
        this.messageById.putAll(messageById);
    }

    public DataManager getDataManager() {
        return dataManager;
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
