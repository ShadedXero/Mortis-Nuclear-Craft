package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.bombs.blockbomb;

import com.mortisdevelopment.mortisnuclearcraft.MortisNuclearCraft;
import com.mortisdevelopment.mortisnuclearcraft.utils.ItemEditor;
import com.mortisdevelopment.mortisnuclearcraft.utils.bomb.BombTime;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class BlockBombMenu implements InventoryHolder {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final BlockBombManager blockBombManager;
    private final BlockBombData data;
    private final Inventory inv;
    private final int modeSlot = 10;
    private final int timerSlot = 13;
    private final int timerSetterSlot = 16;

    public BlockBombMenu(BlockBombManager blockBombManager, BlockBombData data) {
        this.blockBombManager = blockBombManager;
        this.data = data;
        BlockBomb bomb = blockBombManager.getBlockBomb(data.getCore());
        this.inv = Bukkit.createInventory(this, 27, Component.text(bomb.getName()));
        createInv();
    }

    private void createInv() {
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, blockBombManager.getMenuItems().getItem("FILTER"));
        }
        update();
    }

    public void update() {
        if (data.isManualMode()) {
            inv.setItem(modeSlot, blockBombManager.getMenuItems().getItem("MANUAL_MODE"));
        } else {
            inv.setItem(modeSlot, blockBombManager.getMenuItems().getItem("REDSTONE_MODE"));
        }
        if (data.getTimer() == null) {
            inv.setItem(timerSlot, blockBombManager.getMenuItems().getItem("NO_TIMER"));
            inv.setItem(timerSetterSlot, blockBombManager.getMenuItems().getItem("TIMER_SETTER"));
        } else {
            ItemEditor timer = new ItemEditor(blockBombManager.getMenuItems().getItem("TIMER"));
            BombTime bombTime = new BombTime(data.getTimer());
            timer.setPlaceholder("%time%", bombTime.getTextTime());
            inv.setItem(timerSlot, timer.getItem());
            inv.setItem(timerSetterSlot, blockBombManager.getMenuItems().getItem("TIMER_SET"));
        }
    }

    public void click(Player player, int slot) {
        if (slot == modeSlot) {
            if (data.getTimer() != null) {
                return;
            }
            if (data.isManualMode()) {
                data.setManualMode(false);
                player.sendMessage(blockBombManager.getMessage("REDSTONE_MODE"));
            } else {
                data.setManualMode(true);
                player.sendMessage(blockBombManager.getMessage("MANUAL_MODE"));
            }
            update();
        }
        if (slot == timerSlot) {
            if (data.getTimer() == null) {
                player.sendMessage(blockBombManager.getMessage("TIMER_NOT_SET"));
            }else {
                BombTime time = new BombTime(data.getTimer());
                player.sendMessage(blockBombManager.getMessage("TIME_LEFT").replace("%time%", time.getTextTime()));
            }
        }
        if (slot == timerSetterSlot) {
            if (data.getTimer() != null) {
                return;
            }
            if (!data.isManualMode()) {
                return;
            }
            blockBombManager.getPlayersSettingTimer().put(player.getUniqueId(), data);
            close(player);
            new BukkitRunnable() {
                long seconds;
                @Override
                public void run() {
                    seconds++;
                    if (seconds > 10) {
                        blockBombManager.getPlayersSettingTimer().remove(player.getUniqueId());
                        player.sendMessage(blockBombManager.getMessage("TIMER_SET_EXPIRED"));
                        cancel();
                    }
                    if (blockBombManager.getPlayersSettingTimer().get(player.getUniqueId()) == null) {
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0L, 20L);
            player.sendMessage(blockBombManager.getMessage("SET_TIMER"));
        }
    }

    public void open(Player player) {
        player.openInventory(inv);
    }

    public void close(Player player) {
        player.closeInventory();
    }

    public BlockBombManager getBlockBombManager() {
        return blockBombManager;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

    public BlockBombData getData() {
        return data;
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
}
