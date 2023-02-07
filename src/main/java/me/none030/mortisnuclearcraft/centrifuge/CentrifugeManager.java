package me.none030.mortisnuclearcraft.centrifuge;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.data.CentrifugeData;
import me.none030.mortisnuclearcraft.data.DataManager;
import me.none030.mortisnuclearcraft.structures.Structure;
import me.none030.mortisnuclearcraft.utils.centrifuge.CentrifugeMenuItems;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class CentrifugeManager {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final DataManager dataManager;
    private Centrifuge centrifuge;
    private CentrifugeMenuItems menuItems;
    private final HashMap<UUID, Integer> playersInCoolDown;

    public CentrifugeManager(DataManager dataManager) {
        this.dataManager = dataManager;
        this.playersInCoolDown = new HashMap<>();
        check();
        plugin.getServer().getPluginManager().registerEvents(new CentrifugeListener(this), plugin);
    }

    private void check() {
        CentrifugeManager centrifugeManager = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                playersInCoolDown.clear();
                for (CentrifugeData data : dataManager.getCentrifugeStorage().getCentrifuges()) {
                    Structure structure = centrifuge.getStructure();
                    if (!structure.isCore(data.getCore().getBlock()) || !structure.isStructure(data.getCore())) {
                        deleteCentrifuge(data.getCore());
                        continue;
                    }
                    if (data.getProcess() == null) {
                        continue;
                    }
                    CentrifugeRecipe recipe = centrifuge.getRecipeById().get(data.getProcess());
                    if (recipe == null) {
                        data.setProcess(centrifugeManager, null);
                        data.setTimer(centrifugeManager, -1);
                        continue;
                    }
                    if (!data.isManualMode()) {
                        if (!data.getCore().getBlock().isBlockPowered() && !data.getCore().getBlock().isBlockIndirectlyPowered()) {
                            continue;
                        }
                    }
                    if (data.getTimer() <= 0) {
                        recipe.endProcess(data);
                    } else {
                        data.setTimer(centrifugeManager, (data.getTimer() - 1));
                    }
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!(player.getOpenInventory().getTopInventory().getHolder() instanceof CentrifugeMenu)) {
                        continue;
                    }
                    CentrifugeMenu menu = (CentrifugeMenu) player.getOpenInventory().getTopInventory().getHolder();
                    CentrifugeData data = menu.getData();
                    if (data.getTimer() > 0) {
                        if (!data.isManualMode()) {
                            if (!data.getCore().getBlock().isBlockPowered() && !data.getCore().getBlock().isBlockIndirectlyPowered()) {
                                menu.resetAnimation();
                                continue;
                            }
                        }
                        menu.animate();
                    } else {
                        menu.resetAnimation();
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void deleteCentrifuge(Location core) {
        CentrifugeData data = dataManager.getCentrifugeStorage().getCentrifuge(core);
        if (data == null) {
            return;
        }
        data.empty(this);
        dataManager.getCentrifugeStorage().deleteCentrifuge(core);
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public Centrifuge getCentrifuge() {
        return centrifuge;
    }

    public void setCentrifuge(Centrifuge centrifuge) {
        this.centrifuge = centrifuge;
    }

    public CentrifugeMenuItems getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(CentrifugeMenuItems menuItems) {
        this.menuItems = menuItems;
    }

    public HashMap<UUID, Integer> getPlayersInCoolDown() {
        return playersInCoolDown;
    }
}
