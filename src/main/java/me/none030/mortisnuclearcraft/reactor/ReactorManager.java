package me.none030.mortisnuclearcraft.reactor;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.data.DataManager;
import me.none030.mortisnuclearcraft.data.ReactorData;
import me.none030.mortisnuclearcraft.radiation.RadiationManager;
import me.none030.mortisnuclearcraft.structures.Structure;
import me.none030.mortisnuclearcraft.utils.reactor.ReactorMenuItems;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class ReactorManager {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final DataManager dataManager;
    private final RadiationManager radiationManager;
    private Reactor reactor;
    private ReactorExplosion reactorExplosion;
    private ReactorMenuItems menuItems;
    private final HashMap<UUID, Integer> playersInCoolDown;

    public ReactorManager(DataManager dataManager, RadiationManager radiationManager) {
        this.dataManager = dataManager;
        this.radiationManager = radiationManager;
        this.playersInCoolDown = new HashMap<>();
        check();
        plugin.getServer().getPluginManager().registerEvents(new ReactorListener(this), plugin);
    }

    private void check() {
        ReactorManager reactorManager = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                playersInCoolDown.clear();
                for (ReactorData data : dataManager.getReactorStorage().getReactors()) {
                    Structure structure = reactor.getStructure();
                    if (!structure.isCore(data.getCore().getBlock()) || !structure.isReactor(data.getCore())) {
                        deleteReactor(data.getCore());
                        continue;
                    }
                    if (!data.isManualMode()) {
                        if (!data.getCore().getBlock().isBlockPowered() && !data.getCore().getBlock().isBlockIndirectlyPowered()) {
                            continue;
                        }
                    }
                    if (data.getProcess() == null) {
                        for (ReactorRecipe recipe : reactorManager.getReactor().getRecipes()) {
                            recipe.process(data);
                        }
                        continue;
                    }
                    ReactorRecipe recipe = reactor.getRecipeById().get(data.getProcess());
                    if (recipe == null) {
                        data.setProcess(reactorManager, null);
                        data.setTimer(reactorManager, -1);
                        continue;
                    }
                    if (data.getTimer() <= 0) {
                        recipe.endProcess(data);
                    } else {
                        data.setTimer(reactorManager, (data.getTimer() - 1));
                    }
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!(player.getOpenInventory().getTopInventory().getHolder() instanceof ReactorMenu)) {
                        continue;
                    }
                    ReactorMenu menu = (ReactorMenu) player.getOpenInventory().getTopInventory().getHolder();
                    ReactorData data = menu.getData();
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

    public void deleteReactor(Location core) {
        ReactorData data = dataManager.getReactorStorage().getReactor(core);
        if (data == null) {
            return;
        }
        data.empty(this);
        dataManager.getReactorStorage().deleteReactor(core);
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public RadiationManager getRadiationManager() {
        return radiationManager;
    }

    public Reactor getReactor() {
        return reactor;
    }

    public void setReactor(Reactor reactor) {
        this.reactor = reactor;
    }

    public ReactorExplosion getReactorExplosion() {
        return reactorExplosion;
    }

    public void setReactorExplosion(ReactorExplosion reactorExplosion) {
        this.reactorExplosion = reactorExplosion;
    }

    public ReactorMenuItems getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(ReactorMenuItems menuItems) {
        this.menuItems = menuItems;
    }

    public HashMap<UUID, Integer> getPlayersInCoolDown() {
        return playersInCoolDown;
    }
}
