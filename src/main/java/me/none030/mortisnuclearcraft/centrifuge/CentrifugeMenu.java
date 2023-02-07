package me.none030.mortisnuclearcraft.centrifuge;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.data.CentrifugeData;
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

public class CentrifugeMenu implements InventoryHolder {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final CentrifugeManager centrifugeManager;
    private CentrifugeData data;
    private final int input1Slot = 10;
    private final int input2Slot = 28;
    private final int output1Slot = 16;
    private final int output2Slot = 34;
    private final int fuelSlot = 49;
    private final int fuelCalculatorSlot = 40;
    private final int modeSlot = 45;
    private final List<Integer> animationSlots = List.of(11, 12, 13, 14, 15, 29, 30, 31, 32, 33);
    private final Inventory inv;

    public CentrifugeMenu(CentrifugeManager centrifugeManager, CentrifugeData data) {
        this.centrifugeManager = centrifugeManager;
        this.data = data;
        this.inv = createInv();
    }

    private Inventory createInv() {
        Inventory inv = Bukkit.createInventory(this, 54, Component.text(centrifugeManager.getMenuItems().getTitle()));
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, centrifugeManager.getMenuItems().getFilter());
        }
        if (data.getInput1() != null) {
            inv.setItem(input1Slot, data.getInput1());
        }else {
            inv.setItem(input1Slot, centrifugeManager.getMenuItems().getInput1());
        }
        if (data.getInput2() != null) {
            inv.setItem(input2Slot, data.getInput2());
        }else {
            inv.setItem(input2Slot, centrifugeManager.getMenuItems().getInput2());
        }
        if (data.getOutput1() != null) {
            inv.setItem(output1Slot, data.getOutput1());
        }else {
            inv.setItem(output1Slot, centrifugeManager.getMenuItems().getOutput1());
        }
        if (data.getOutput2() != null) {
            inv.setItem(output2Slot, data.getOutput2());
        }else {
            inv.setItem(output2Slot, centrifugeManager.getMenuItems().getOutput2());
        }
        if (data.getFuel() != null) {
            inv.setItem(fuelSlot, data.getFuel());
        }else {
            inv.setItem(fuelSlot, centrifugeManager.getMenuItems().getFuel());
        }
        if (data.isManualMode()) {
            inv.setItem(modeSlot, centrifugeManager.getMenuItems().getManualMode());
        }else {
            inv.setItem(modeSlot, centrifugeManager.getMenuItems().getRedstoneMode());
        }
        int power = centrifugeManager.getCentrifuge().getFuelPower(data.getFuel());
        ItemEditor editor = new ItemEditor(centrifugeManager.getMenuItems().getFuelCalculator());
        editor.setPlaceholder("%fuel%", String.valueOf(power));
        inv.setItem(fuelCalculatorSlot, editor.getItem());
        return inv;
    }

    public ItemStack click(Player player, int slot, ItemStack cursor) {
        if (slot == input1Slot) {
            if (data.getInput1() == null) {
                if (!isNull(cursor)) {
                    inv.setItem(input1Slot, cursor);
                    data.setInput1(centrifugeManager, cursor);
                    check();
                    return null;
                }
            }else {
                ItemStack item = data.getInput1();
                if (isNull(cursor)) {
                    inv.setItem(input1Slot, centrifugeManager.getMenuItems().getInput1());
                    data.setInput1(centrifugeManager, null);
                }else {
                    inv.setItem(input1Slot, cursor);
                    data.setInput1(centrifugeManager, cursor);
                }
                check();
                return item;
            }
        }
        if (slot == input2Slot) {
            if (data.getInput2() == null) {
                if (!isNull(cursor)) {
                    inv.setItem(input2Slot, cursor);
                    data.setInput2(centrifugeManager, cursor);
                    check();
                    return null;
                }
            }else {
                ItemStack item = data.getInput2();
                if (isNull(cursor)) {
                    inv.setItem(input2Slot, centrifugeManager.getMenuItems().getInput2());
                    data.setInput2(centrifugeManager, null);
                }else {
                    inv.setItem(input2Slot, cursor);
                    data.setInput2(centrifugeManager, cursor);
                }
                check();
                return item;
            }
        }
        if (slot == output1Slot) {
            if (data.getOutput1() != null) {
                if (isNull(cursor)) {
                    ItemStack output = data.getOutput1();
                    inv.setItem(output1Slot, centrifugeManager.getMenuItems().getOutput1());
                    data.setOutput1(centrifugeManager, null);
                    check();
                    return output;
                }
            }
        }
        if (slot == output2Slot) {
            if (data.getOutput2() != null) {
                if (isNull(cursor)) {
                    ItemStack output = data.getOutput2();
                    inv.setItem(output2Slot, centrifugeManager.getMenuItems().getOutput2());
                    data.setOutput2(centrifugeManager, null);
                    check();
                    return output;
                }
            }
        }
        if (slot == fuelSlot) {
            if (data.getFuel() == null) {
                if (!isNull(cursor)) {
                    inv.setItem(fuelSlot, cursor);
                    data.setFuel(centrifugeManager, cursor);
                    int power = centrifugeManager.getCentrifuge().getFuelPower(data.getFuel());
                    ItemEditor editor = new ItemEditor(centrifugeManager.getMenuItems().getFuelCalculator());
                    editor.setPlaceholder("%fuel%", String.valueOf(power));
                    inv.setItem(fuelCalculatorSlot, editor.getItem());
                    check();
                    return null;
                }
            }else {
                ItemStack item = data.getFuel();
                if (isNull(cursor)) {
                    inv.setItem(fuelSlot, centrifugeManager.getMenuItems().getFuel());
                    data.setFuel(centrifugeManager, null);
                }else {
                    inv.setItem(fuelSlot, cursor);
                    data.setFuel(centrifugeManager, cursor);
                }
                int power = centrifugeManager.getCentrifuge().getFuelPower(data.getFuel());
                ItemEditor editor = new ItemEditor(centrifugeManager.getMenuItems().getFuelCalculator());
                editor.setPlaceholder("%fuel%", String.valueOf(power));
                inv.setItem(fuelCalculatorSlot, editor.getItem());
                check();
                return item;
            }
        }
        if (slot == modeSlot) {
            if (data.isManualMode()) {
                inv.setItem(modeSlot, centrifugeManager.getMenuItems().getRedstoneMode());
                data.setManualMode(centrifugeManager, false);
                player.sendMessage(CentrifugeMessages.REDSTONE_MODE);
            }else {
                inv.setItem(modeSlot, centrifugeManager.getMenuItems().getManualMode());
                data.setManualMode(centrifugeManager, true);
                player.sendMessage(CentrifugeMessages.MANUAL_MODE);
            }
        }
        check();
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
            recipe.process(data);
        }
    }

    public void update(CentrifugeData data) {
        this.data = data;
        if (data.getInput1() != null) {
            inv.setItem(input1Slot, data.getInput1());
        }else {
            inv.setItem(input1Slot, centrifugeManager.getMenuItems().getInput1());
        }
        if (data.getInput2() != null) {
            inv.setItem(input2Slot, data.getInput2());
        }else {
            inv.setItem(input2Slot, centrifugeManager.getMenuItems().getInput2());
        }
        if (data.getOutput1() != null) {
            inv.setItem(output1Slot, data.getOutput1());
        }else {
            inv.setItem(output1Slot, centrifugeManager.getMenuItems().getOutput1());
        }
        if (data.getOutput2() != null) {
            inv.setItem(output2Slot, data.getOutput2());
        }else {
            inv.setItem(output2Slot, centrifugeManager.getMenuItems().getOutput2());
        }
        if (data.getFuel() != null) {
            inv.setItem(fuelSlot, data.getFuel());
        }else {
            inv.setItem(fuelSlot, centrifugeManager.getMenuItems().getFuel());
        }
        if (data.isManualMode()) {
            inv.setItem(modeSlot, centrifugeManager.getMenuItems().getManualMode());
        }else {
            inv.setItem(modeSlot, centrifugeManager.getMenuItems().getRedstoneMode());
        }
        int power = centrifugeManager.getCentrifuge().getFuelPower(data.getFuel());
        ItemEditor editor = new ItemEditor(centrifugeManager.getMenuItems().getFuelCalculator());
        editor.setPlaceholder("%fuel%", String.valueOf(power));
        inv.setItem(fuelCalculatorSlot, editor.getItem());
    }

    public void open(Player player) {
        player.openInventory(inv);
    }

    private void close(Player player) {
        player.closeInventory();
    }

    public void animate() {
        for (int slot : animationSlots) {
            inv.setItem(slot, centrifugeManager.getMenuItems().getAnimation1());
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int slot : animationSlots) {
                    inv.setItem(slot, centrifugeManager.getMenuItems().getAnimation2());
                }
            }
        }.runTaskLater(plugin, 10L);
    }

    public void resetAnimation() {
        for (int slot : animationSlots) {
            inv.setItem(slot, centrifugeManager.getMenuItems().getFilter());
        }
    }

    private boolean isNull(ItemStack item) {
        if (item == null) {
            return true;
        }
        return item.getType().equals(Material.AIR);
    }

    public CentrifugeData getData() {
        return data;
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

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }
}
