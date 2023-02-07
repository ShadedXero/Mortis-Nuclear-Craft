package me.none030.mortisnuclearcraft.radiation;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
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

import static me.none030.mortisnuclearcraft.radiation.RadiationMessages.PLAYER_RADIATION;

public class RadiationDisplay {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final DecimalFormat formatter = new DecimalFormat("#.#");
    private final RadiationManager radiationManager;
    private final DisplayType type;
    private final DisplayMode mode;
    private final DisplayToggleMode toggleMode;
    private final ItemStack toggleItem;

    public RadiationDisplay(RadiationManager radiationManager, DisplayType type, DisplayMode mode, DisplayToggleMode toggleMode, ItemStack toggleItem) {
        this.radiationManager = radiationManager;
        this.type = type;
        this.mode = mode;
        this.toggleMode = toggleMode;
        this.toggleItem = toggleItem;
        check();
    }

    private void check() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            show(player);
        }
    }

    public void show(Player player) {
        radiationManager.getToggleByPlayer().put(player.getUniqueId(), true);
        if (type.equals(DisplayType.BOSSBAR)) {
            if (mode.equals(DisplayMode.SINGLE)) {
                showBossBar(player);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        hideBossBar(player);
                    }
                }.runTaskLater(plugin, 20L);
            } else {
                showBossBar(player);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Boolean value = radiationManager.getToggleByPlayer().get(player.getUniqueId());
                        if (value == null || !value || !player.isOnline()) {
                            radiationManager.getToggleByPlayer().put(player.getUniqueId(), false);
                            hideBossBar(player);
                            cancel();
                        }
                        updateBossBar(player);
                    }
                }.runTaskTimer(plugin, 0L, 20L);
            }
        }else if (type.equals(DisplayType.CHAT)) {
            String radiation = formatter.format(radiationManager.getRadiation(player));
            player.sendMessage(PLAYER_RADIATION.replace("%radiation%", radiation));
        } else {
            if (mode.equals(DisplayMode.SINGLE)) {
                String value = formatter.format(radiationManager.getRadiation(player));
                player.sendActionBar(Component.text(PLAYER_RADIATION.replace("%radiation%", value)));
            } else {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Boolean value = radiationManager.getToggleByPlayer().get(player.getUniqueId());
                        if (value == null || !value || !player.isOnline()) {
                            radiationManager.getToggleByPlayer().put(player.getUniqueId(), false);
                            cancel();
                        }
                        String radiation = formatter.format(radiationManager.getRadiation(player));
                        player.sendActionBar(Component.text(PLAYER_RADIATION.replace("%radiation%", radiation)));
                    }
                }.runTaskTimer(plugin, 0L, 20L);
            }
        }
    }

    public void hide(Player player) {
        radiationManager.getToggleByPlayer().put(player.getUniqueId(), false);
    }

    public void createBossBar(Player player) {
        String value = formatter.format(radiationManager.getRadiation(player));
        BossBar bossBar = Bukkit.createBossBar(PLAYER_RADIATION.replace("%radiation%", value), BarColor.RED, BarStyle.SEGMENTED_6);
        bossBar.addPlayer(player);
        radiationManager.getBossBarByPlayer().put(player.getUniqueId(), bossBar);
        bossBar.setVisible(false);
    }

    private void showBossBar(Player player) {
        BossBar bossBar = radiationManager.getBossBarByPlayer().get(player.getUniqueId());
        if (bossBar == null) {
            createBossBar(player);
            bossBar = radiationManager.getBossBarByPlayer().get(player.getUniqueId());
        }
        bossBar.addPlayer(player);
        bossBar.setVisible(true);
    }

    private void hideBossBar(Player player) {
        BossBar bossBar = radiationManager.getBossBarByPlayer().get(player.getUniqueId());
        if (bossBar == null) {
            createBossBar(player);
            bossBar = radiationManager.getBossBarByPlayer().get(player.getUniqueId());
        }
        bossBar.removePlayer(player);
        bossBar.setVisible(false);
    }

    private void updateBossBar(Player player) {
        String radiation = formatter.format(radiationManager.getRadiation(player));
        BossBar bossBar = radiationManager.getBossBarByPlayer().get(player.getUniqueId());
        if (bossBar == null) {
            createBossBar(player);
            bossBar = radiationManager.getBossBarByPlayer().get(player.getUniqueId());
            bossBar.setVisible(true);
        }
        bossBar.setTitle(PLAYER_RADIATION.replace("%radiation%", radiation));
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
