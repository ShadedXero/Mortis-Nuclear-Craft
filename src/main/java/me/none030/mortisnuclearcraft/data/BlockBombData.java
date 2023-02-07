package me.none030.mortisnuclearcraft.data;

import me.none030.mortisnuclearcraft.bombs.BombManager;
import org.bukkit.Location;

import java.time.LocalDateTime;

public class BlockBombData {

    private final Location core;
    private final String id;
    private boolean manualMode;
    private LocalDateTime timer;

    public BlockBombData(Location core, String id, boolean manualMode, LocalDateTime timer) {
        this.core = core;
        this.id = id;
        this.manualMode = manualMode;
        this.timer = timer;
    }

    public Location getCore() {
        return core;
    }

    public String getId() {
        return id;
    }

    public boolean isManualMode() {
        return manualMode;
    }

    public void setManualMode(BombManager bombManager, boolean manualMode) {
        this.manualMode = manualMode;
        bombManager.getDataManager().getBombStorage().updateMode(core, manualMode);
    }

    public LocalDateTime getTimer() {
        return timer;
    }

    public void setTimer(BombManager bombManager, LocalDateTime timer) {
        this.timer = timer;
        bombManager.getDataManager().getBombStorage().updateTimer(core, timer);
    }
}
