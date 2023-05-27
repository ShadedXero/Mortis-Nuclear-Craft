package me.none030.mortisnuclearcraft.nuclearcraft.reactor;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.utils.ItemEditor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
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
    private final ReactorData data;
    private final Inventory menu;
    private final int wasteSlot = 4;
    private final int inputSlot = 20;
    private final int outputSlot = 24;
    private final int fuelSlot = 49;
    private final int fuelCalculatorSlot = 40;
    private final int waterCalculatorSlot = 53;
    private final int modeSlot = 45;
    private final List<Integer> animationSlots = List.of(21, 22, 23);

    public ReactorMenu(ReactorManager reactorManager, ReactorData data) {
        this.reactorManager = reactorManager;
        this.data = data;
        this.menu = Bukkit.createInventory(this, 54, Component.text(reactorManager.getMenuItems().getTitle()));
        createInv();
    }

    private void createInv() {
        for (int i = 0; i < menu.getSize(); i++) {
            menu.setItem(i, reactorManager.getMenuItems().getItem("FILTER"));
        }
        update();
    }

    public void update() {
        if (data.getInput() != null) {
            menu.setItem(inputSlot, data.getInput());
        }else {
            menu.setItem(inputSlot, reactorManager.getMenuItems().getItem("INPUT"));
        }
        if (data.getOutput() != null) {
            menu.setItem(outputSlot, data.getOutput());
        }else {
            menu.setItem(outputSlot, reactorManager.getMenuItems().getItem("OUTPUT"));
        }
        if (data.getWaste() != null) {
            menu.setItem(wasteSlot, data.getWaste());
        }else {
            menu.setItem(wasteSlot, reactorManager.getMenuItems().getItem("WASTE"));
        }
        if (data.getFuel() != null) {
            menu.setItem(fuelSlot, data.getFuel());
        }else {
            menu.setItem(fuelSlot, reactorManager.getMenuItems().getItem("FUEL"));
        }
        if (data.isManualMode()) {
            menu.setItem(modeSlot, reactorManager.getMenuItems().getItem("MANUAL_MODE"));
        }else {
            menu.setItem(modeSlot, reactorManager.getMenuItems().getItem("REDSTONE_MODE"));
        }
        int power = reactorManager.getReactor().getFuelPower(data.getFuel());
        ItemEditor editor = new ItemEditor(reactorManager.getMenuItems().getItem("FUEL_CALCULATOR"));
        editor.setPlaceholder("%fuel%", String.valueOf(power));
        menu.setItem(fuelCalculatorSlot, editor.getItem());
        int water = reactorManager.getReactor().getWater(data.getStructureId(), data.getCore());
        ItemEditor waterEditor = new ItemEditor(reactorManager.getMenuItems().getItem("WATER_CALCULATOR"));
        waterEditor.setPlaceholder("%water%", String.valueOf(water));
        menu.setItem(waterCalculatorSlot, waterEditor.getItem());
    }

    public ItemStack click(Player player, int slot, ItemStack cursor) {
        if (slot == inputSlot) {
            if (data.getInput() == null) {
                if (cursor != null && !cursor.getType().isAir()) {
                    data.setInput(cursor);
                    update();
                    return null;
                }
            }else {
                ItemStack item = data.getInput();
                if (cursor == null || cursor.getType().isAir()) {
                    data.setInput(null);
                }else {
                    data.setInput(cursor);
                }
                update();
                return item;
            }
        }
        if (slot == outputSlot) {
            if (data.getOutput() != null) {
                if (cursor == null || cursor.getType().isAir()) {
                    ItemStack output = data.getOutput();
                    data.setOutput(null);
                    update();
                    return output;
                }
            }
        }
        if (slot == wasteSlot) {
            if (data.getWaste() != null) {
                if (cursor == null || cursor.getType().isAir()) {
                    ItemStack waste = data.getWaste();
                    data.setWaste(null);
                    update();
                    return waste;
                }
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
                player.sendMessage(reactorManager.getMessage("REDSTONE_MODE"));
            }else {
                data.setManualMode(true);
                player.sendMessage(reactorManager.getMessage("MANUAL_MODE"));
            }
            update();
        }
        return cursor;
    }

    public void open(Player player) {
        player.openInventory(menu);
    }

    public void close(Player player) {
        player.closeInventory();
    }

    public void animate() {
        for (int slot : animationSlots) {
            menu.setItem(slot, reactorManager.getMenuItems().getItem("ANIMATION_1"));
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int slot : animationSlots) {
                    menu.setItem(slot, reactorManager.getMenuItems().getItem("ANIMATION_2"));
                }
            }
        }.runTaskLater(plugin, 10L);
    }

    public void resetAnimation() {
        for (int slot : animationSlots) {
            menu.setItem(slot, reactorManager.getMenuItems().getItem("FILTER"));
        }
        update();
    }

    public ReactorData getData() {
        return data;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return menu;
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

    public int getWaterCalculatorSlot() {
        return waterCalculatorSlot;
    }

    public int getModeSlot() {
        return modeSlot;
    }
}
