package me.none030.mortisnuclearcraft.data;

import me.none030.mortisnuclearcraft.centrifuge.CentrifugeMenu;
import me.none030.mortisnuclearcraft.reactor.ReactorManager;
import me.none030.mortisnuclearcraft.reactor.ReactorMenu;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ReactorData {

    private final Location core;
    private ItemStack input;
    private ItemStack output;
    private ItemStack fuel;
    private String rawInput;
    private String rawOutput;
    private String rawFuel;
    private boolean manualMode;
    private String process;
    private long timer;

    public ReactorData(Location core, ItemStack input, ItemStack output, ItemStack fuel, boolean manualMode, String process, long timer) {
        this.core = core;
        this.input = input;
        this.output = output;
        this.fuel = fuel;
        this.rawInput = serialize(input);
        this.rawOutput = serialize(output);
        this.rawFuel = serialize(fuel);
        this.manualMode = manualMode;
        this.process = process;
        this.timer = timer;
    }

    public ReactorData(Location core, String rawInput, String rawOutput, String rawFuel, boolean manualMode, String process, long timer) {
        this.core = core;
        this.input = deserialize(rawInput);
        this.output = deserialize(rawOutput);
        this.fuel = deserialize(rawFuel);
        this.rawInput = rawInput;
        this.rawOutput = rawOutput;
        this.rawFuel = rawFuel;
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

    public void empty(ReactorManager reactorManager) {
        if (input != null) {
            core.getWorld().dropItemNaturally(core, input);
            setInput(reactorManager, null);
        }
        if (output != null) {
            core.getWorld().dropItemNaturally(core, output);
            setOutput(reactorManager, null);
        }
        if (fuel != null) {
            core.getWorld().dropItemNaturally(core, fuel);
            setFuel(reactorManager, null);
        }
    }

    public void update(ReactorData reactorData) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!(player.getOpenInventory().getTopInventory().getHolder() instanceof ReactorMenu)) {
                continue;
            }
            ReactorMenu menu = (ReactorMenu) player.getOpenInventory().getTopInventory().getHolder();
            if (menu == null) {
                continue;
            }
            ReactorData data = menu.getData();
            if (data == null || !data.getCore().equals(core)) {
                continue;
            }
            menu.update(reactorData);
        }
    }

    public void close() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!(player.getOpenInventory().getTopInventory().getHolder() instanceof ReactorMenu)) {
                continue;
            }
            ReactorMenu menu = (ReactorMenu) player.getOpenInventory().getTopInventory().getHolder();
            if (menu == null) {
                continue;
            }
            ReactorData data = menu.getData();
            if (data == null || !data.getCore().equals(core)) {
                continue;
            }
            menu.close(player);
        }
    }

    public void explode(ReactorManager reactorManager) {
        reactorManager.getReactorExplosion().explode(core);
    }

    public Location getCore() {
        return core;
    }

    public ItemStack getInput() {
        return input;
    }

    public void setInput(ReactorManager reactorManager, ItemStack input) {
        this.input = input;
        this.rawInput = serialize(input);
        reactorManager.getDataManager().getReactorStorage().updateInput(core, rawInput);
    }

    public ItemStack getOutput() {
        return output;
    }

    public void setOutput(ReactorManager reactorManager, ItemStack output) {
        this.output = output;
        this.rawOutput = serialize(output);
        reactorManager.getDataManager().getReactorStorage().updateOutput(core, rawOutput);
    }

    public ItemStack getFuel() {
        return fuel;
    }

    public void setFuel(ReactorManager reactorManager, ItemStack fuel) {
        this.fuel = fuel;
        this.rawFuel = serialize(fuel);
        reactorManager.getDataManager().getReactorStorage().updateFuel(core, rawFuel);
    }

    public String getRawInput() {
        return rawInput;
    }

    public String getRawOutput() {
        return rawOutput;
    }

    public String getRawFuel() {
        return rawFuel;
    }

    public boolean isManualMode() {
        return manualMode;
    }

    public void setManualMode(ReactorManager reactorManager, boolean manualMode) {
        this.manualMode = manualMode;
        reactorManager.getDataManager().getReactorStorage().updateMode(core, manualMode);
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(ReactorManager reactorManager, String process) {
        this.process = process;
        reactorManager.getDataManager().getReactorStorage().updateProcess(core, process);
    }

    public long getTimer() {
        return timer;
    }

    public void setTimer(ReactorManager reactorManager, long timer) {
        this.timer = timer;
        reactorManager.getDataManager().getReactorStorage().updateTimer(core, timer);
    }
}
