package me.none030.mortisnuclearcraft.nuclearcraft.reactor;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.menu.MenuItems;
import me.none030.mortisnuclearcraft.nuclearcraft.Manager;
import me.none030.mortisnuclearcraft.nuclearcraft.radiatiton.RadiationManager;
import me.none030.mortisnuclearcraft.structures.Structure;
import me.none030.mortisnuclearcraft.utils.NuclearType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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

    public ReactorManager(RadiationManager radiationManager, Reactor reactor, MenuItems menuItems) {
        super(NuclearType.REACTOR);
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
                    if (data.getProcess() == null) {
                        for (ReactorRecipe recipe : reactorManager.getReactor().getRecipes()) {
                            recipe.process(reactorManager, data, structure);
                        }
                        continue;
                    }
                    ReactorRecipe recipe = reactor.getRecipeById().get(data.getProcess());
                    if (recipe == null) {
                        data.setProcess( null);
                        data.setTimer(-1);
                        continue;
                    }
                    reactor.checkInHoppers(data, structure);
                    reactor.checkFuel(data, structure);
                    if (data.getTimer() <= 0) {
                        recipe.endProcess(data);
                        reactor.checkOutHoppers(data, structure);
                    } else {
                        data.setTimer(data.getTimer() - 1);
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
