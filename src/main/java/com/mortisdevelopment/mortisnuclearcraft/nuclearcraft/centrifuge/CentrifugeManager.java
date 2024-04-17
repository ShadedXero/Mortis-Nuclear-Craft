package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.centrifuge;

import com.mortisdevelopment.mortisnuclearcraft.MortisNuclearCraft;
import com.mortisdevelopment.mortisnuclearcraft.data.DataManager;
import com.mortisdevelopment.mortisnuclearcraft.menu.MenuItems;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.Manager;
import com.mortisdevelopment.mortisnuclearcraft.structures.Structure;
import com.mortisdevelopment.mortisnuclearcraft.utils.NuclearType;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class CentrifugeManager extends Manager {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final Centrifuge centrifuge;
    private final MenuItems menuItems;
    private final HashMap<UUID, Integer> playersInCoolDown;

    public CentrifugeManager(DataManager dataManager, Centrifuge centrifuge, MenuItems menuItems) {
        super(dataManager, NuclearType.CENTRIFUGE);
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
        CentrifugeManager centrifugeManager = this;
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
                    if (!data.isManualMode()) {
                        if (!structure.hasRedstoneSignal(core)) {
                            continue;
                        }
                    }
                    data.setHopperTimer(data.getHopperTimer() + 1);
                    if (data.getHopperTimer() > 10) {
                        centrifuge.checkFuel(data, structure);
                        centrifuge.checkInHoppers(data, structure);
                        centrifuge.checkOutHoppers(data, structure);
                        data.setHopperTimer(0);
                    }
                    if (data.getProcess() == null) {
                        structure.lightDown(core);
                        for (CentrifugeRecipe recipe : centrifuge.getRecipes()) {
                            if (recipe.process(centrifugeManager, data)) {
                                break;
                            }
                        }
                        continue;
                    }
                    structure.lightUp(core);
                    CentrifugeRecipe recipe = centrifuge.getRecipeById().get(data.getProcess());
                    if (recipe == null) {
                        data.setProcess(null);
                        data.setTimer(-1);
                        continue;
                    }
                    if (data.getTimer() <= 0) {
                        recipe.endProcess(data);
                    } else {
                        data.setTimer(data.getTimer() - 1);
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
