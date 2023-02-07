package me.none030.mortisnuclearcraft.bombs;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.data.BlockBombData;
import me.none030.mortisnuclearcraft.items.ItemEditor;
import me.none030.mortisnuclearcraft.utils.bomb.BombTime;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class BlockBombMenu implements InventoryHolder {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final BombManager bombManager;
    private final int modeSlot = 10;
    private final int timerSlot = 13;
    private final int timerSetterSlot = 16;
    private final BlockBombData data;
    private Inventory inv;
    private final BlockBomb bomb;

    public BlockBombMenu(BombManager bombManager, BlockBombData data) {
        this.bombManager = bombManager;
        this.data = data;
        this.bomb = bombManager.getBlockBombById().get(data.getId());
        createInv();
    }

    private void createInv() {
        Inventory inv = Bukkit.createInventory(this, 27, Component.text(bomb.getName()));
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, bombManager.getMenuItems().getFilter());
        }
        if (data.isManualMode()) {
            inv.setItem(modeSlot, bombManager.getMenuItems().getManualMode());
        } else {
            inv.setItem(modeSlot, bombManager.getMenuItems().getRedstoneMode());
        }
        if (data.getTimer() == null) {
            inv.setItem(timerSlot, bombManager.getMenuItems().getNoTimer());
            inv.setItem(timerSetterSlot, bombManager.getMenuItems().getTimerSetter());
        } else {
            ItemEditor timer = new ItemEditor(bombManager.getMenuItems().getTimer());
            BombTime bombTime = new BombTime(data.getTimer());
            timer.setPlaceholder("%time%", bombTime.getTextTime());
            inv.setItem(timerSlot, timer.getItem());
            inv.setItem(timerSetterSlot, bombManager.getMenuItems().getTimerSet());
        }
        setInv(inv);
    }

    public void click(Player player, int slot) {
        if (slot == modeSlot) {
            if (data.getTimer() != null) {
                return;
            }
            if (data.isManualMode()) {
                data.setManualMode(bombManager, false);
                inv.setItem(modeSlot, bombManager.getMenuItems().getRedstoneMode());
                player.sendMessage(BombMessages.REDSTONE_MODE);
            } else {
                data.setManualMode(bombManager, true);
                inv.setItem(modeSlot, bombManager.getMenuItems().getManualMode());
                player.sendMessage(BombMessages.MANUAL_MODE);
            }
        }
        if (slot == timerSlot) {
            if (data.getTimer() == null) {
                player.sendMessage(BombMessages.TIMER_NOT_SET);
            }else {
                BombTime time = new BombTime(data.getTimer());
                player.sendMessage(BombMessages.TIME_LEFT.replace("%time%", time.getTextTime()));
            }
        }
        if (slot == timerSetterSlot) {
            if (data.getTimer() != null) {
                return;
            }
            if (!data.isManualMode()) {
                return;
            }
            bombManager.getPlayersSettingTimer().put(player.getUniqueId(), data);
            close(player);
            long[] count = {0};
            new BukkitRunnable() {
                @Override
                public void run() {
                    count[0] = count[0]++;
                    if (count[0] > 10) {
                        bombManager.getPlayersSettingTimer().remove(player.getUniqueId());
                        player.sendMessage(BombMessages.TIMER_SET_EXPIRED);
                        cancel();
                    }
                    if (bombManager.getPlayersSettingTimer().get(player.getUniqueId()) == null) {
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0L, 20L);
            player.sendMessage(BombMessages.SET_TIMER);
        }
    }

    public void open(Player player) {
        player.openInventory(inv);
    }

    public void close(Player player) {
        player.closeInventory();
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

    private void setInv(Inventory inv) {
        this.inv = inv;
    }

    public int getModeSlot() {
        return modeSlot;
    }

    public int getTimerSlot() {
        return timerSlot;
    }

    public int getTimerSetterSlot() {
        return timerSetterSlot;
    }

    public BlockBombData getData() {
        return data;
    }

    public BlockBomb getBomb() {
        return bomb;
    }
}
