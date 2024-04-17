package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.centrifuge;

import com.mortisdevelopment.mortisnuclearcraft.data.BlockData;
import com.mortisdevelopment.mortisnuclearcraft.utils.NuclearType;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class CentrifugeData extends BlockData {

    private final String structureIdKey = "NuclearCraftId";
    private final String input1Key = "NuclearCraftInput1";
    private final String input2Key = "NuclearCraftInput2";
    private final String outputKey = "NuclearCraftOutput";
    private final String fuelKey = "NuclearCraftFuel";
    private final String modeKey = "NuclearCraftManualMode";
    private final String processKey = "NuclearCraftProcess";
    private final String timerKey = "NuclearCraftTimer";
    private final String hopperTimerKey = "NuclearCraftHopperTimer";

    public CentrifugeData(Location core) {
        super(core, NuclearType.CENTRIFUGE);
    }

    public void create(String structureId, ItemStack input1, ItemStack input2, Inventory outputs, ItemStack fuel, boolean manualMode, String process, long timer, long hopperTimer) {
        create();
        setStructureId(structureId);
        setInput1(input1);
        setInput2(input2);
        setOutput(outputs);
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

    public void setInput1(ItemStack input1) {
        set(input1Key, serialize(input1));
    }

    public ItemStack getInput1() {
        String value = get(input1Key);
        if (value == null) {
            return null;
        }
        return deserialize(value);
    }

    public void setInput2(ItemStack input2) {
        set(input2Key, serialize(input2));
    }

    public ItemStack getInput2() {
        String value = get(input2Key);
        if (value == null) {
            return null;
        }
        return deserialize(value);
    }

    public void setOutput(Inventory output) {
        if (output == null) {
            set(outputKey, null);
            return;
        }
        set(outputKey, serialize(output));
    }

    public Inventory getOutput(InventoryHolder holder) {
        String rawOutput = get(outputKey);
        if (rawOutput == null) {
            return null;
        }
        return deserialize(rawOutput, holder, null);
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
        ItemStack input1 = getInput1();
        if (input1 != null) {
            getCore().getWorld().dropItemNaturally(getCore(), input1);
            setInput1(null);
        }
        ItemStack input2 = getInput2();
        if (input2 != null) {
            getCore().getWorld().dropItemNaturally(getCore(), input2);
            setInput2(null);
        }
        for (ItemStack output : new OutputStorage(this).getOutputs()) {
            if (output == null) {
                continue;
            }
            getCore().getWorld().dropItemNaturally(getCore(), output);
        }
        setOutput(null);
        ItemStack fuel = getFuel();
        if (fuel != null) {
            getCore().getWorld().dropItemNaturally(getCore(), fuel);
            setFuel(null);
        }
    }
}
