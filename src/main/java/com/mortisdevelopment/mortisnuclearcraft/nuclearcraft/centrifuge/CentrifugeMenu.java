package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.centrifuge;

import com.mortisdevelopment.mortisnuclearcraft.structures.Structure;
import com.mortisdevelopment.mortisnuclearcraft.MortisNuclearCraft;
import com.mortisdevelopment.mortisnuclearcraft.utils.ItemEditor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CentrifugeMenu implements InventoryHolder {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final CentrifugeManager centrifugeManager;
    private final CentrifugeData data;
    private final Inventory menu;
    private final int taskId;
    private final int input1Slot = 10;
    private final int input2Slot = 28;
    private final List<Integer> outputSlots = List.of(16,17,25,26,34,35);
    private final int fuelSlot = 49;
    private final int fuelCalculatorSlot = 40;
    private final int modeSlot = 45;
    private final List<Integer> animationSlots = List.of(11, 12, 13, 14, 15, 29, 30, 31, 32, 33);

    public CentrifugeMenu(CentrifugeManager centrifugeManager, CentrifugeData data) {
        this.centrifugeManager = centrifugeManager;
        this.data = data;
        this.menu = getMenu();
        update();
        taskId = check();
    }

    private int check() {
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                Structure structure = centrifugeManager.getCentrifuge().getStructure(data.getCore());
                if (data.getProcess() != null) {
                    if (!data.isManualMode()) {
                        if (structure != null && !structure.hasRedstoneSignal(data.getCore())) {
                            resetAnimation();
                            return;
                        }
                    }
                    animate();
                }else {
                    resetAnimation();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
        return task.getTaskId();
    }

    private Inventory getMenu() {
        Inventory menu = Bukkit.createInventory(this, 54, Component.text(centrifugeManager.getMenuItems().getTitle()));
        for (int i = 0; i < menu.getSize(); i++) {
            menu.setItem(i, centrifugeManager.getMenuItems().getItem("FILTER"));
        }
        return menu;
    }

    public void cancelTask() {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    public void update() {
        if (data.getInput1() != null) {
            menu.setItem(input1Slot, data.getInput1());
        }else {
            menu.setItem(input1Slot, centrifugeManager.getMenuItems().getItem("INPUT_1"));
        }
        if (data.getInput2() != null) {
            menu.setItem(input2Slot, data.getInput2());
        }else {
            menu.setItem(input2Slot, centrifugeManager.getMenuItems().getItem("INPUT_2"));
        }
        ItemStack[] output = new OutputStorage(data).getOutputs();
        for (int i = 0; i < 6; i++) {
            ItemStack item = output[i];
            if (item == null || item.getType().isAir()) {
                menu.setItem(outputSlots.get(i), centrifugeManager.getMenuItems().getItem("OUTPUT"));
                continue;
            }
            menu.setItem(outputSlots.get(i), item);
        }
        if (data.getFuel() != null) {
            menu.setItem(fuelSlot, data.getFuel());
        }else {
            menu.setItem(fuelSlot, centrifugeManager.getMenuItems().getItem("FUEL"));
        }
        if (data.isManualMode()) {
            menu.setItem(modeSlot, centrifugeManager.getMenuItems().getItem("MANUAL_MODE"));
        }else {
            menu.setItem(modeSlot, centrifugeManager.getMenuItems().getItem("REDSTONE_MODE"));
        }
        int power = centrifugeManager.getCentrifuge().getFuelPower(data.getFuel());
        ItemEditor editor = new ItemEditor(centrifugeManager.getMenuItems().getItem("FUEL_CALCULATOR"));
        editor.setPlaceholder("%fuel%", String.valueOf(power));
        menu.setItem(fuelCalculatorSlot, editor.getItem());
    }

    public ItemStack click(Player player, int slot, ItemStack cursor) {
        if (slot == input1Slot) {
            if (data.getInput1() == null) {
                if (cursor != null && !cursor.getType().isAir()) {
                    data.setInput1(cursor);
                    update();
                    return null;
                }
            }else {
                ItemStack item = data.getInput1();
                if (cursor == null || cursor.getType().isAir()) {
                    data.setInput1(null);
                }else {
                    data.setInput1(cursor);
                }
                update();
                return item;
            }
        }
        if (slot == input2Slot) {
            if (data.getInput2() == null) {
                if (cursor != null && !cursor.getType().isAir()) {
                    data.setInput2(cursor);
                    update();
                    return null;
                }
            }else {
                ItemStack item = data.getInput2();
                if (cursor == null || cursor.getType().isAir()) {
                    data.setInput2(null);
                }else {
                    data.setInput2(cursor);
                }
                update();
                return item;
            }
        }
        if (slot == fuelSlot) {
            if (data.getFuel() == null) {
                if (cursor != null && !cursor.getType().isAir()) {
                    data.setFuel(cursor);
                    update();
                    return null;
                }
            }else {
                ItemStack item = data.getFuel();
                if (cursor == null || cursor.getType().isAir()) {
                    data.setFuel(null);
                }else {
                    data.setFuel(cursor);
                }
                update();
                return item;
            }
        }
        if (slot == modeSlot) {
            if (data.isManualMode()) {
                data.setManualMode(false);
                player.sendMessage(centrifugeManager.getMessage("REDSTONE_MODE"));
            }else {
                data.setManualMode(true);
                player.sendMessage(centrifugeManager.getMessage("MANUAL_MODE"));
            }
            update();
        }
        if (outputSlots.contains(slot)) {
            if (cursor != null && !cursor.getType().isAir()) {
                return cursor;
            }
            int index = outputSlots.indexOf(slot);
            if (index == -1) {
                return cursor;
            }
            OutputStorage storage = new OutputStorage(data);
            ItemStack item = storage.getOutput(index);
            if (item == null || item.getType().isAir()) {
                return cursor;
            }
            storage.getInventory().removeItem(item);
            data.setOutput(storage.getInventory());
            update();
            return item;
        }
        return cursor;
    }

    public void animate() {
        for (int slot : animationSlots) {
            menu.setItem(slot, centrifugeManager.getMenuItems().getItem("ANIMATION_1"));
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int slot : animationSlots) {
                    menu.setItem(slot, centrifugeManager.getMenuItems().getItem("ANIMATION_2"));
                }
            }
        }.runTaskLater(plugin, 10L);
    }

    public void resetAnimation() {
        for (int slot : animationSlots) {
            menu.setItem(slot, centrifugeManager.getMenuItems().getItem("FILTER"));
        }
    }

    public void open(Player player) {
        player.openInventory(menu);
    }

    private void close(Player player) {
        player.closeInventory();
    }

    public CentrifugeManager getCentrifugeManager() {
        return centrifugeManager;
    }

    public CentrifugeData getData() {
        return data;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return menu;
    }

    public int getTaskId() {
        return taskId;
    }

    public List<Integer> getAnimationSlots() {
        return animationSlots;
    }

    public int getInput1Slot() {
        return input1Slot;
    }

    public int getInput2Slot() {
        return input2Slot;
    }

    public List<Integer> getOutputSlots() {
        return outputSlots;
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
}
