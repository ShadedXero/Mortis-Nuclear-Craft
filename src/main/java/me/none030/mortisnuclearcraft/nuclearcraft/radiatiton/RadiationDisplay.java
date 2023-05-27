package me.none030.mortisnuclearcraft.nuclearcraft.radiatiton;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.utils.MessageUtils;
import me.none030.mortisnuclearcraft.utils.radiation.DisplayMode;
import me.none030.mortisnuclearcraft.utils.radiation.DisplayToggleMode;
import me.none030.mortisnuclearcraft.utils.radiation.DisplayType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;

public class RadiationDisplay {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final DecimalFormat formatter = new DecimalFormat("#.#");
    private final DisplayType type;
    private final DisplayMode mode;
    private final DisplayToggleMode toggleMode;
    private final ItemStack toggleItem;

    public RadiationDisplay(DisplayType type, DisplayMode mode, DisplayToggleMode toggleMode, ItemStack toggleItem) {
        this.type = type;
        this.mode = mode;
        this.toggleMode = toggleMode;
        this.toggleItem = toggleItem;
    }

    public void check(RadiationManager radiationManager) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            show(radiationManager, player);
        }
    }

    public void show(RadiationManager radiationManager, Player player) {
        radiationManager.getToggleByPlayer().put(player.getUniqueId(), true);
        if (type.equals(DisplayType.BOSSBAR)) {
           showBossBar(radiationManager, player, mode);
        }else if (type.equals(DisplayType.CHAT)) {
            showChat(radiationManager, player, mode);
        } else {
           showActionBar(radiationManager, player, mode);
        }
    }

    private void showChat(RadiationManager radiationManager, Player player, DisplayMode mode) {
        if (mode.equals(DisplayMode.SINGLE)) {
            player.sendMessage(getRadiation(radiationManager, player));
        }else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Boolean value = radiationManager.getToggleByPlayer().get(player.getUniqueId());
                    if (value == null || !value || !player.isOnline()) {
                        radiationManager.getToggleByPlayer().put(player.getUniqueId(), false);
                        cancel();
                    }
                    player.sendMessage(getRadiation(radiationManager, player));
                }
            }.runTaskTimer(plugin, 0L, 20L);
        }
    }

    private void showActionBar(RadiationManager radiationManager, Player player, DisplayMode mode) {
        if (mode.equals(DisplayMode.SINGLE)) {
            player.sendActionBar(Component.text(getRadiation(radiationManager, player)));
        }else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Boolean value = radiationManager.getToggleByPlayer().get(player.getUniqueId());
                    if (value == null || !value || !player.isOnline()) {
                        radiationManager.getToggleByPlayer().put(player.getUniqueId(), false);
                        cancel();
                    }
                    player.sendActionBar(Component.text(getRadiation(radiationManager, player)));
                }
            }.runTaskTimer(plugin, 0L, 20L);
        }
    }

    private void showBossBar(RadiationManager radiationManager, Player player, DisplayMode mode) {
        if (mode.equals(DisplayMode.SINGLE)) {
            showBossBar(radiationManager, player);
            new BukkitRunnable() {
                @Override
                public void run() {
                    hideBossBar(radiationManager, player);
                }
            }.runTaskLater(plugin, 20L);
        }else {
            showBossBar(radiationManager, player);
            new BukkitRunnable() {
                @Override
                public void run() {
                    Boolean value = radiationManager.getToggleByPlayer().get(player.getUniqueId());
                    if (value == null || !value || !player.isOnline()) {
                        radiationManager.getToggleByPlayer().put(player.getUniqueId(), false);
                        hideBossBar(radiationManager, player);
                        cancel();
                    }
                    updateBossBar(radiationManager, player);
                }
            }.runTaskTimer(plugin, 0L, 20L);
        }
    }

    private void showBossBar(RadiationManager radiationManager, Player player) {
        BossBar bossBar = radiationManager.getBossBarByPlayer().get(player.getUniqueId());
        if (bossBar == null) {
            createBossBar(radiationManager, player);
            bossBar = radiationManager.getBossBarByPlayer().get(player.getUniqueId());
        }
        bossBar.addPlayer(player);
        bossBar.setVisible(true);
    }

    public void hide(RadiationManager radiationManager, Player player) {
        radiationManager.getToggleByPlayer().put(player.getUniqueId(), false);
    }

    private void createBossBar(RadiationManager radiationManager, Player player) {
        BossBar bossBar = Bukkit.createBossBar(getRadiation(radiationManager, player), BarColor.RED, BarStyle.SEGMENTED_6);
        bossBar.addPlayer(player);
        radiationManager.getBossBarByPlayer().put(player.getUniqueId(), bossBar);
        bossBar.setVisible(false);
    }

    private void hideBossBar(RadiationManager radiationManager, Player player) {
        BossBar bossBar = radiationManager.getBossBarByPlayer().get(player.getUniqueId());
        if (bossBar == null) {
            createBossBar(radiationManager, player);
            bossBar = radiationManager.getBossBarByPlayer().get(player.getUniqueId());
        }
        bossBar.removePlayer(player);
        bossBar.setVisible(false);
    }

    private void updateBossBar(RadiationManager radiationManager, Player player) {
        BossBar bossBar = radiationManager.getBossBarByPlayer().get(player.getUniqueId());
        if (bossBar == null) {
            createBossBar(radiationManager, player);
            bossBar = radiationManager.getBossBarByPlayer().get(player.getUniqueId());
            bossBar.setVisible(true);
        }
        bossBar.setTitle(getRadiation(radiationManager, player));
    }

    private String getRadiation(RadiationManager radiationManager, Player player) {
        MessageUtils editor = new MessageUtils(radiationManager.getMessage("PLAYER_RADIATION"));
        editor.replace("%radiation%", formatter.format(radiationManager.getRadiation(player)));
        return editor.getMessage();
    }

    public DisplayType getType() {
        return type;
    }

    public DisplayMode getMode() {
        return mode;
    }

    public DisplayToggleMode getToggleMode() {
        return toggleMode;
    }

    public ItemStack getToggleItem() {
        return toggleItem;
    }
}
