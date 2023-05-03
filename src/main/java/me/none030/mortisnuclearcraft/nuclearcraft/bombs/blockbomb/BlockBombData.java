package me.none030.mortisnuclearcraft.nuclearcraft.bombs.blockbomb;

import me.none030.mortisnuclearcraft.data.BlockData;
import me.none030.mortisnuclearcraft.utils.NuclearType;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class BlockBombData extends BlockData {

    private final String idKey = "NuclearCraftId";
    private final String structureIdKey = "NuclearCraftStructureId";
    private final String modeKey = "NuclearCraftManualMode";
    private final String timerKey = "NuclearCraftTimer";

    public BlockBombData(@NotNull Location core) {
        super(core, NuclearType.BLOCK_BOMB);
    }

    public void create(String id, String structureId, boolean manualMode, LocalDateTime timer) {
        create();
        setId(id);
        setStructureId(structureId);
        setManualMode(manualMode);
        setTimer(timer);
    }

    public void setId(String id) {
        set(idKey, id);
    }

    public String getId() {
        return get(idKey);
    }

    public void setStructureId(String structureId) {
        set(structureIdKey, structureId);
    }

    public String getStructureId() {
        return get(structureIdKey);
    }

    public void setManualMode(boolean manualMode) {
        set(modeKey, Boolean.toString(manualMode));
    }

    public boolean isManualMode() {
        String value = get(modeKey);
        if (value == null) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }

    public void setTimer(LocalDateTime timer) {
        if (timer != null) {
            set(timerKey, timer.toString());
        }else {
            set(timerKey, null);
        }
    }

    public LocalDateTime getTimer() {
        String value = get(timerKey);
        if (value == null) {
            return null;
        }
        return LocalDateTime.parse(value);
    }
}
