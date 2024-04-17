package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.reactor;

import com.mortisdevelopment.mortisnuclearcraft.MortisNuclearCraft;
import com.mortisdevelopment.mortisnuclearcraft.data.DataManager;
import com.mortisdevelopment.mortisnuclearcraft.menu.MenuItems;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.Manager;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.radiatiton.RadiationManager;
import com.mortisdevelopment.mortisnuclearcraft.structures.Structure;
import com.mortisdevelopment.mortisnuclearcraft.utils.NuclearType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ReactorManager extends Manager {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final RadiationManager radiationManager;
    private final Reactor reactor;
    private final MenuItems menuItems;
    private final HashMap<UUID, Integer> playersInCoolDown;

    public ReactorManager(DataManager dataManager, RadiationManager radiationManager, Reactor reactor, MenuItems menuItems) {
        super(dataManager, NuclearType.REACTOR);
        this.radiationManager = radiationManager;
        this.reactor = reactor;
        this.menuItems = menuItems;
        this.playersInCoolDown = new HashMap<>();
        check();
        plugin.getServer().getPluginManager().registerEvents(new ReactorListener(this), plugin);
    }

    public ReactorData getReactorData(Location core) {
        if (!getCores().contains(core)) {
            return null;
        }
        ReactorData data = new ReactorData(core);
        if (!data.isType()) {
            return null;
        }
        return data;
    }

    private void check() {
        ReactorManager reactorManager = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                playersInCoolDown.clear();
                for (int i = 0; i < getCores().size(); i++) {
                    Location core = getCores().get(i);
                    if (core == null) {
                        continue;
                    }
                    ReactorData data = getReactorData(core);
                    if (data == null) {
                        delete(core);
                        continue;
                    }
                    Structure structure = reactor.getStructure(data.getStructureId());
                    if (structure == null || !structure.isStructure(data.getCore(), List.of(Material.CAULDRON, Material.WATER_CAULDRON))) {
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
                        reactor.checkFuel(data, structure);
                        reactor.checkInHoppers(data, structure);
                        reactor.checkOutHoppers(data, structure);
                        data.setHopperTimer(0);
                    }
                    if (data.getProcess() == null) {
                        structure.lightDown(core);
                        for (ReactorRecipe recipe : reactorManager.getReactor().getRecipes()) {
                            if (recipe.process(reactorManager, data, structure)) {
                                break;
                            }
                        }
                        continue;
                    }
                    structure.lightUp(core);
                    ReactorRecipe recipe = reactor.getRecipeById().get(data.getProcess());
                    if (recipe == null) {
                        data.setProcess( null);
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

    public RadiationManager getRadiationManager() {
        return radiationManager;
    }

    public Reactor getReactor() {
        return reactor;
    }

    public MenuItems getMenuItems() {
        return menuItems;
    }

    public HashMap<UUID, Integer> getPlayersInCoolDown() {
        return playersInCoolDown;
    }
}
