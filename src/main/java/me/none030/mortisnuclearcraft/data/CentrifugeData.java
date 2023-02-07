package me.none030.mortisnuclearcraft.data;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.centrifuge.CentrifugeManager;
import me.none030.mortisnuclearcraft.centrifuge.CentrifugeMenu;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class CentrifugeData {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final Location core;
    private ItemStack input1;
    private ItemStack input2;
    private ItemStack output1;
    private ItemStack output2;
    private ItemStack fuel;
    private String rawInput1;
    private String rawInput2;
    private String rawOutput1;
    private String rawOutput2;
    private String rawFuel;
    private boolean manualMode;
    private String process;
    private long timer;

    public CentrifugeData(Location core, ItemStack input1, ItemStack input2, ItemStack output1, ItemStack output2, ItemStack fuel, boolean manualMode, String process, long timer) {
        this.core = core;
        this.input1 = input1;
        this.input2 = input2;
        this.output1 = output1;
        this.output2 = output2;
        this.fuel = fuel;
        this.rawInput1 = serialize(input1);
        this.rawInput2 = serialize(input2);
        this.rawOutput1 = serialize(output1);
        this.rawOutput2 = serialize(output2);
        this.rawFuel = serialize(fuel);
        this.manualMode = manualMode;
        this.process = process;
        this.timer = timer;
    }

    public CentrifugeData(Location core, String rawInput1, String rawInput2, String rawOutput1, String rawOutput2, String rawFuel, boolean manualMode, String process, long timer) {
        this.core = core;
        this.rawInput1 = rawInput1;
        this.rawInput2 = rawInput2;
        this.rawOutput1 = rawOutput1;
        this.rawOutput2 = rawOutput2;
        this.rawFuel = rawFuel;
        this.input1 = deserialize(rawInput1);
        this.input2 = deserialize(rawInput2);
        this.output1 = deserialize(rawOutput1);
        this.output2 = deserialize(rawOutput2);
        this.fuel = deserialize(rawFuel);
        this.manualMode = manualMode;
        this.process = process;
        this.timer = timer;
    }

    private String serialize(ItemStack item) {
        if (item == null) {
            return null;
        }
        try {
            ByteArrayOutputStream io = new ByteArrayOutputStream();
            BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);
            os.writeObject(item);
            os.flush();
            return new String(Base64.getEncoder().encode(io.toByteArray()));
        }catch (IOException exp) {
            return null;
        }
    }

    private ItemStack deserialize(String rawItem) {
        if (rawItem == null) {
            return null;
        }
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(rawItem));
            BukkitObjectInputStream is = new BukkitObjectInputStream(in);
            return (ItemStack) is.readObject();
        }catch (IOException | ClassNotFoundException exp) {
            return null;
        }
    }

    public void empty(CentrifugeManager centrifugeManager) {
        if (input1 != null) {
            core.getWorld().dropItemNaturally(core, input1);
            setInput1(centrifugeManager, null);
        }
        if (input2 != null) {
            core.getWorld().dropItemNaturally(core, input2);
            setInput2(centrifugeManager, null);
        }
        if (output1 != null) {
            core.getWorld().dropItemNaturally(core, output1);
            setOutput1(centrifugeManager, null);
        }
        if (output2 != null) {
            core.getWorld().dropItemNaturally(core, output2);
            setOutput2(centrifugeManager, null);
        }
        if (fuel != null) {
            core.getWorld().dropItemNaturally(core, fuel);
            setFuel(centrifugeManager, null);
        }
    }

    public Location getCore() {
        return core;
    }

    public ItemStack getInput1() {
        return input1;
    }

    public void setInput1(CentrifugeManager centrifugeManager, ItemStack input1) {
        this.input1 = input1;
        this.rawInput1 = serialize(input1);
        centrifugeManager.getDataManager().getCentrifugeStorage().updateInput1(core, rawInput1);
    }

    public ItemStack getInput2() {
        return input2;
    }

    public void setInput2(CentrifugeManager centrifugeManager, ItemStack input2) {
        this.input2 = input2;
        this.rawInput2 = serialize(input2);
        centrifugeManager.getDataManager().getCentrifugeStorage().updateInput2(core, rawInput2);
    }

    public ItemStack getOutput1() {
        return output1;
    }

    public void setOutput1(CentrifugeManager centrifugeManager, ItemStack output1) {
        this.output1 = output1;
        this.rawOutput1 = serialize(output1);
        centrifugeManager.getDataManager().getCentrifugeStorage().updateOutput1(core, rawOutput1);
    }

    public ItemStack getOutput2() {
        return output2;
    }

    public void setOutput2(CentrifugeManager centrifugeManager, ItemStack output2) {
        this.output2 = output2;
        this.rawOutput2 = serialize(output2);
        centrifugeManager.getDataManager().getCentrifugeStorage().updateOutput2(core, rawOutput2);
    }

    public ItemStack getFuel() {
        return fuel;
    }

    public void setFuel(CentrifugeManager centrifugeManager, ItemStack fuel) {
        this.fuel = fuel;
        this.rawFuel = serialize(fuel);
        centrifugeManager.getDataManager().getCentrifugeStorage().updateFuel(core, rawFuel);
    }

    public void update(CentrifugeData centrifugeData) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!(player.getOpenInventory().getTopInventory().getHolder() instanceof CentrifugeMenu)) {
                continue;
            }
            CentrifugeMenu menu = (CentrifugeMenu) player.getOpenInventory().getTopInventory().getHolder();
            if (menu == null) {
                continue;
            }
            CentrifugeData data = menu.getData();
            if (data == null || !data.getCore().equals(core)) {
                continue;
            }
            menu.update(centrifugeData);
        }
    }

    public String getRawInput1() {
        return rawInput1;
    }

    public String getRawInput2() {
        return rawInput2;
    }

    public String getRawOutput1() {
        return rawOutput1;
    }

    public String getRawOutput2() {
        return rawOutput2;
    }

    public String getRawFuel() {
        return rawFuel;
    }

    public boolean isManualMode() {
        return manualMode;
    }

    public void setManualMode(CentrifugeManager centrifugeManager, boolean manualMode) {
        this.manualMode = manualMode;
        centrifugeManager.getDataManager().getCentrifugeStorage().updateMode(core, manualMode);
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(CentrifugeManager centrifugeManager, String process) {
        this.process = process;
        centrifugeManager.getDataManager().getCentrifugeStorage().updateProcess(core, process);
    }

    public long getTimer() {
        return timer;
    }

    public void setTimer(CentrifugeManager centrifugeManager, long timer) {
        this.timer = timer;
        centrifugeManager.getDataManager().getCentrifugeStorage().updateTimer(core, timer);
    }
}
