package me.none030.mortisnuclearcraft.nuclearcraft.centrifuge;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.menu.MenuItems;
import me.none030.mortisnuclearcraft.nuclearcraft.Manager;
import me.none030.mortisnuclearcraft.structures.Structure;
import me.none030.mortisnuclearcraft.utils.NuclearType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class CentrifugeManager extends Manager {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final Centrifuge centrifuge;
    private final MenuItems menuItems;
    private final HashMap<UUID, Integer> playersInCoolDown;

    public CentrifugeManager(Centrifuge centrifuge, MenuItems menuItems) {
        super(NuclearType.CENTRIFUGE);
        this.centrifuge = centrifuge;
        this.menuItems = menuItems;
        this.playersInCoolDown = new HashMap<>();
        check();
        plugin.getServer().getPluginManager().registerEvents(new CentrifugeListener(this), plugin);
    }

    public CentrifugeData getCentrifugeData(Location core) {
        if (!getCores().contains(core)) {
            return null;
        }
        CentrifugeData data = new CentrifugeData(core);
        if (!data.isType()) {
            return null;
        }
        return data;
    }

    private void check() {
        new BukkitRunnable() {
            @Override
            public void run() {
                playersInCoolDown.clear();
                for (int i = 0; i < getCores().size(); i++) {
                    Location core = getCores().get(i);
                    if (core == null) {
                        continue;
                    }
                    CentrifugeData data = getCentrifugeData(core);
                    if (data == null) {
                        delete(core);
                        continue;
                    }
                    Structure structure = centrifuge.getStructure(core);
                    if (structure == null || !structure.isStructure(data.getCore())) {
                        data.empty();
                        delete(data);
                        continue;
                    }
                    centrifuge.checkOutHoppers(data, structure);
                    if (data.getProcess() == null) {
                        continue;
                    }
                    CentrifugeRecipe recipe = centrifuge.getRecipeById().get(data.getProcess());
                    if (recipe == null) {
                        data.setProcess(null);
                        data.setTimer(-1);
                        continue;
                    }
                    if (!data.isManualMode()) {
                        if (!structure.hasRedstoneSignal(core)) {
                            continue;
                        }
                    }
                    if (data.getTimer() <= 0) {
                        recipe.endProcess(data);
                    } else {
                        data.setTimer(data.getTimer() - 1);
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

    public Centrifuge getCentrifuge() {
        return centrifuge;
    }

    public MenuItems getMenuItems() {
        return menuItems;
    }

    public HashMap<UUID, Integer> getPlayersInCoolDown() {
        return playersInCoolDown;
    }
}
