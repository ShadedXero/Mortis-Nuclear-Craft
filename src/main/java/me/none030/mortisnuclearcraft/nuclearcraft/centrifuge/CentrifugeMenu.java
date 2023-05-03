package me.none030.mortisnuclearcraft.nuclearcraft.centrifuge;

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

public class CentrifugeMenu implements InventoryHolder {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final CentrifugeManager centrifugeManager;
    private final CentrifugeData data;
    private Inventory menu;
    private final int input1Slot = 10;
    private final int input2Slot = 28;
    private final int output1Slot = 16;
    private final int output2Slot = 34;
    private final int fuelSlot = 49;
    private final int fuelCalculatorSlot = 40;
    private final int modeSlot = 45;
    private final List<Integer> animationSlots = List.of(11, 12, 13, 14, 15, 29, 30, 31, 32, 33);

    public CentrifugeMenu(CentrifugeManager centrifugeManager, CentrifugeData data) {
        this.centrifugeManager = centrifugeManager;
        this.data = data;
        createMenu();
    }

    private void createMenu() {
        this.menu = Bukkit.createInventory(this, 54, Component.text(centrifugeManager.getMenuItems().getTitle()));
        for (int i = 0; i < menu.getSize(); i++) {
            menu.setItem(i, centrifugeManager.getMenuItems().getItem("FILTER"));
        }
        update();
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
        if (data.getOutput1() != null) {
            menu.setItem(output1Slot, data.getOutput1());
        }else {
            menu.setItem(output1Slot, centrifugeManager.getMenuItems().getItem("OUTPUT_1"));
        }
        if (data.getOutput2() != null) {
            menu.setItem(output2Slot, data.getOutput2());
        }else {
            menu.setItem(output2Slot, centrifugeManager.getMenuItems().getItem("OUTPUT_2"));
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
        check();
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
        if (slot == output1Slot) {
            if (data.getOutput1() != null) {
                if (cursor == null || cursor.getType().isAir()) {
                    ItemStack output = data.getOutput1();
                    data.setOutput1(null);
                    update();
                    return output;
                }
            }
        }
        if (slot == output2Slot) {
            if (data.getOutput2() != null) {
                if (cursor == null || cursor.getType().isAir()) {
                    ItemStack output = data.getOutput2();
                    data.setOutput2(null);
                    update();
                    return output;
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
                player.sendMessage(centrifugeManager.getMessage("REDSTONE_MODE"));
            }else {
                data.setManualMode(true);
                player.sendMessage(centrifugeManager.getMessage("MANUAL_MODE"));
            }
            update();
        }
        return cursor;
    }

    public void check() {
        if (!data.isManualMode()) {
            if (!data.getCore().getBlock().isBlockPowered() && !data.getCore().getBlock().isBlockIndirectlyPowered()) {
                return;
            }
        }
        if (data.getProcess() != null) {
            return;
        }
        if (data.getOutput1() != null || data.getOutput2() != null) {
            return;
        }
        int power = centrifugeManager.getCentrifuge().getFuelPower(data.getFuel());
        for (CentrifugeRecipe recipe : centrifugeManager.getCentrifuge().getRecipes()) {
            if (!recipe.isRecipe(data.getInput1(), data.getInput2())) {
                continue;
            }
            if (!recipe.hasEnoughFuel(power)) {
                continue;
            }
            recipe.process(centrifugeManager, data);
        }
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

    public CentrifugeData getData() {
        return data;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return menu;
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

    public int getOutput1Slot() {
        return output1Slot;
    }

    public int getOutput2Slot() {
        return output2Slot;
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
