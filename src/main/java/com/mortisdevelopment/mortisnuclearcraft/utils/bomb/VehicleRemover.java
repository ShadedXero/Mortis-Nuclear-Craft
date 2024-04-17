package com.mortisdevelopment.mortisnuclearcraft.utils.bomb;

import me.zombie_striker.qg.exp.cars.VehicleEntity;
import me.zombie_striker.qg.exp.cars.api.QualityArmoryVehicles;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class VehicleRemover {

    private final Location location;
    private final int radius;

    public VehicleRemover(Location location, int radius) {
        this.location = location;
        this.radius = radius;
    }

    public void removeVehicles() {
        for (Entity entity : location.getNearbyEntities(radius, radius, radius)) {
            VehicleEntity vehicle = QualityArmoryVehicles.getVehicleEntity(entity);
            if (vehicle != null) {
                vehicle.despawn();
            }
        }
    }

    public Location getLocation() {
        return location;
    }

    public int getRadius() {
        return radius;
    }
}
