package me.none030.mortisnuclearcraft.reactor;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.data.ReactorData;
import me.none030.mortisnuclearcraft.items.ItemEditor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ReactorMenu implements InventoryHolder {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final ReactorManager reactorManager;
    private ReactorData data;
    private final int inputSlot = 20;
    private final int outputSlot = 24;
    private final int fuelSlot = 49;
    private final int fuelCalculatorSlot = 40;
    private final int waterCalculatorSlot = 8;
    private final int modeSlot = 45;
    private final List<Integer> animationSlots = List.of(21, 22, 23);
    private final Inventory inv;

    public ReactorMenu(ReactorManager reactorManager, ReactorData data) {
        this.reactorManager = reactorManager;
        this.data = data;
        this.inv = createInv();
    }

    private Inventory createInv() {
        Inventory inv = Bukkit.createInventory(this, 54, Component.text(reactorManager.getMenuItems().getTitle()));
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, reactorManager.getMenuItems().getFilter());
        }
        if (data.getInput() != null) {
            inv.setItem(inputSlot, data.getInput());
        }else {
            inv.setItem(inputSlot, reactorManager.getMenuItems().getInput());
        }
        if (data.getOutput() != null) {
            inv.setItem(outputSlot, data.getOutput());
        }else {
            inv.setItem(outputSlot, reactorManager.getMenuItems().getOutput());
        }
        if (data.getFuel() != null) {
            inv.setItem(fuelSlot, data.getFuel());
        }else {
            inv.setItem(fuelSlot, reactorManager.getMenuItems().getFuel());
        }
        if (data.isManualMode()) {
            inv.setItem(modeSlot, reactorManager.getMenuItems().getManualMode());
        }else {
            inv.setItem(modeSlot, reactorManager.getMenuItems().getRedstoneMode());
        }
        int power = reactorManager.getReactor().getFuelPower(data.getFuel());
        ItemEditor editor = new ItemEditor(reactorManager.getMenuItems().getFuelCalculator());
        editor.setPlaceholder("%fuel%", String.valueOf(power));
        inv.setItem(fuelCalculatorSlot, editor.getItem());
        int water = reactorManager.getReactor().getWater(data.getCore());
        ItemEditor waterEditor = new ItemEditor(reactorManager.getMenuItems().getWaterCalculator());
        waterEditor.setPlaceholder("%water%", String.valueOf(water));
        inv.setItem(waterCalculatorSlot, waterEditor.getItem());
        return inv;
    }

    public ItemStack click(Player player, int slot, ItemStack cursor) {
        if (slot == inputSlot) {
            if (data.getInput() == null) {
                if (!isNull(cursor)) {
                    inv.setItem(inputSlot, cursor);
                    data.setInput(reactorManager, cursor);
                    return null;
                }
            }else {
                ItemStack item = data.getInput();
                if (isNull(cursor)) {
                    inv.setItem(inputSlot, reactorManager.getMenuItems().getInput());
                    data.setInput(reactorManager, null);
                }else {
                    inv.setItem(inputSlot, cursor);
                    data.setInput(reactorManager, cursor);
                }
                return item;
            }
        }
        if (slot == outputSlot) {
            if (data.getOutput() != null) {
                if (isNull(cursor)) {
                    ItemStack output = data.getOutput();
                    inv.setItem(outputSlot, reactorManager.getMenuItems().getOutput());
                    data.setOutput(reactorManager, null);
                    return output;
                }
            }
        }
        if (slot == fuelSlot) {
            if (data.getFuel() == null) {
                if (!isNull(cursor)) {
                    inv.setItem(fuelSlot, cursor);
                    data.setFuel(reactorManager, cursor);
                    int power = reactorManager.getReactor().getFuelPower(data.getFuel());
                    ItemEditor editor = new ItemEditor(reactorManager.getMenuItems().getFuelCalculator());
                    editor.setPlaceholder("%fuel%", String.valueOf(power));
                    inv.setItem(fuelCalculatorSlot, editor.getItem());
                    return null;
                }
            }else {
                ItemStack item = data.getFuel();
                if (isNull(cursor)) {
                    inv.setItem(fuelSlot, reactorManager.getMenuItems().getFuel());
                    data.setFuel(reactorManager, null);
                }else {
                    inv.setItem(fuelSlot, cursor);
                    data.setFuel(reactorManager, cursor);
                }
                int power = reactorManager.getReactor().getFuelPower(data.getFuel());
                ItemEditor editor = new ItemEditor(reactorManager.getMenuItems().getFuelCalculator());
                editor.setPlaceholder("%fuel%", String.valueOf(power));
                inv.setItem(fuelCalculatorSlot, editor.getItem());
                return item;
            }
        }
        if (slot == modeSlot) {
            if (data.isManualMode()) {
                inv.setItem(modeSlot, reactorManager.getMenuItems().getRedstoneMode());
                data.setManualMode(reactorManager, false);
                player.sendMessage(ReactorMessages.REDSTONE_MODE);
            }else {
                inv.setItem(modeSlot, reactorManager.getMenuItems().getManualMode());
                data.setManualMode(reactorManager, true);
                player.sendMessage(ReactorMessages.MANUAL_MODE);
            }
        }
        int water = reactorManager.getReactor().getWater(data.getCore());
        ItemEditor waterEditor = new ItemEditor(reactorManager.getMenuItems().getWaterCalculator());
        waterEditor.setPlaceholder("%water%", String.valueOf(water));
        inv.setItem(waterCalculatorSlot, waterEditor.getItem());
        return cursor;
    }

    public void update(ReactorData data) {
        this.data = data;
        if (data.getInput() != null) {
            inv.setItem(inputSlot, data.getInput());
        }else {
            inv.setItem(inputSlot, reactorManager.getMenuItems().getInput());
        }
        if (data.getOutput() != null) {
            inv.setItem(outputSlot, data.getOutput());
        }else {
            inv.setItem(outputSlot, reactorManager.getMenuItems().getOutput());
        }
        if (data.getFuel() != null) {
            inv.setItem(fuelSlot, data.getFuel());
        }else {
            inv.setItem(fuelSlot, reactorManager.getMenuItems().getFuel());
        }
        if (data.isManualMode()) {
            inv.setItem(modeSlot, reactorManager.getMenuItems().getManualMode());
        }else {
            inv.setItem(modeSlot, reactorManager.getMenuItems().getRedstoneMode());
        }
        int power = reactorManager.getReactor().getFuelPower(data.getFuel());
        ItemEditor editor = new ItemEditor(reactorManager.getMenuItems().getFuelCalculator());
        editor.setPlaceholder("%fuel%", String.valueOf(power));
        inv.setItem(fuelCalculatorSlot, editor.getItem());
        int water = reactorManager.getReactor().getWater(data.getCore());
        ItemEditor waterEditor = new ItemEditor(reactorManager.getMenuItems().getWaterCalculator());
        waterEditor.setPlaceholder("%water%", String.valueOf(water));
        inv.setItem(waterCalculatorSlot, waterEditor.getItem());
    }

    public void open(Player player) {
        player.openInventory(inv);
    }

    public void close(Player player) {
        player.closeInventory();
    }

    public void animate() {
        for (int slot : animationSlots) {
            inv.setItem(slot, reactorManager.getMenuItems().getAnimation1());
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int slot : animationSlots) {
                    inv.setItem(slot, reactorManager.getMenuItems().getAnimation2());
                }
            }
        }.runTaskLater(plugin, 10L);
    }

    public void resetAnimation() {
        for (int slot : animationSlots) {
            inv.setItem(slot, reactorManager.getMenuItems().getFilter());
        }
    }

    private boolean isNull(ItemStack item) {
        if (item == null) {
            return true;
        }
        return item.getType().equals(Material.AIR);
    }

    public ReactorData getData() {
        return data;
    }

    public List<Integer> getAnimationSlots() {
        return animationSlots;
    }

    public int getInputSlot() {
        return inputSlot;
    }

    public int getOutputSlot() {
        return outputSlot;
    }

    public int getFuelSlot() {
        return fuelSlot;
    }

    public int getFuelCalculatorSlot() {
        return fuelCalculatorSlot;
    }

    public int getModeSlot() {
        return modeSlot;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }
}
