package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.radiatiton;

import com.mortisdevelopment.mortisnuclearcraft.utils.radiation.ToleranceMode;
import org.bukkit.entity.EntityType;

import java.util.List;

public class RadiationTolerance {

    private final ToleranceMode mode;
    private final List<EntityType> entities;

    public RadiationTolerance(ToleranceMode mode, List<EntityType> entities) {
        this.mode = mode;
        this.entities = entities;
    }

    public boolean isKillable(EntityType entity) {
        if (mode.equals(ToleranceMode.WHITELIST)) {
            return entities.contains(entity);
        }else {
            return !entities.contains(entity);
        }
    }

    public ToleranceMode getMode() {
        return mode;
    }

    public List<EntityType> getEntities() {
        return entities;
    }
}
