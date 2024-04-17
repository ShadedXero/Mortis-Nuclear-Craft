package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.reactor;

import com.mortisdevelopment.mortisnuclearcraft.data.BlockData;
import com.mortisdevelopment.mortisnuclearcraft.utils.NuclearType;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class ReactorData extends BlockData {

    private final String structureIdKey = "NuclearCraftStructureId";
    private final String inputKey = "NuclearCraftInput";
    private final String outputKey = "NuclearCraftOutput";
    private final String wasteKey = "NuclearCraftWaste";
    private final String fuelKey = "NuclearCraftFuel";
    private final String modeKey = "NuclearCraftManualMode";
    private final String processKey = "NuclearCraftProcess";
    private final String timerKey = "NuclearCraftTimer";
    private final String hopperTimerKey = "NuclearCraftHopperTimer";

    public ReactorData(Location core) {
        super(core, NuclearType.REACTOR);
    }

    public void create(String structureId, ItemStack input, ItemStack output, ItemStack waste, ItemStack fuel, boolean manualMode, String process, long timer, long hopperTimer) {
        create();
        setStructureId(structureId);
        setInput(input);
        setOutput(output);
        setWaste(waste);
        setFuel(fuel);
        setManualMode(manualMode);
        setProcess(process);
        setTimer(timer);
        setHopperTimer(hopperTimer);
    }

    public void setStructureId(String structureId) {
        set(structureIdKey, structureId);
    }

    public String getStructureId() {
        return get(structureIdKey);
    }

    public void setInput(ItemStack input) {
        set(inputKey, serialize(input));
    }

    public ItemStack getInput() {
        String value = get(inputKey);
        if (value == null) {
            return null;
        }
        return deserialize(value);
    }

    public void setOutput(ItemStack output) {
        set(outputKey, serialize(output));
    }

    public ItemStack getOutput() {
        String value = get(outputKey);
        if (value == null) {
            return null;
        }
        return deserialize(value);
    }

    public void setWaste(ItemStack output) {
        set(wasteKey, serialize(output));
    }

    public ItemStack getWaste() {
        String value = get(wasteKey);
        if (value == null) {
            return null;
        }
        return deserialize(value);
    }

    public void setFuel(ItemStack fuel) {
        set(fuelKey, serialize(fuel));
    }

    public ItemStack getFuel() {
        String value = get(fuelKey);
        if (value == null) {
            return null;
        }
        return deserialize(value);
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

    public void setProcess(String process) {
        set(processKey, process);
    }

    public String getProcess() {
        return get(processKey);
    }

    public void setTimer(long timer) {
        set(timerKey, Long.toString(timer));
    }

    public long getTimer() {
        String value = get(timerKey);
        if (value == null) {
            return 0;
        }
        return Long.parseLong(value);
    }

    public void setHopperTimer(long hopperTimer) {
        set(hopperTimerKey, Long.toString(hopperTimer));
    }

    public long getHopperTimer() {
        String value = get(hopperTimerKey);
        if (value == null) {
            return 0;
        }
        return Long.parseLong(value);
    }

    public void empty() {
        ItemStack input = getInput();
        if (input != null) {
            getCore().getWorld().dropItemNaturally(getCore(), input);
            setInput(null);
        }
        ItemStack output = getOutput();
        if (output != null) {
            getCore().getWorld().dropItemNaturally(getCore(), output);
            setOutput(null);
        }
        ItemStack waste = getWaste();
        if (waste != null) {
            getCore().getWorld().dropItemNaturally(getCore(), waste);
            setWaste(null);
        }
        ItemStack fuel = getFuel();
        if (fuel != null) {
            getCore().getWorld().dropItemNaturally(getCore(), fuel);
            setFuel(null);
        }
    }
}
