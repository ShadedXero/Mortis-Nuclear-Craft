package me.none030.mortisnuclearcraft.nuclearcraft.bombs.blockbomb;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.menu.MenuItems;
import me.none030.mortisnuclearcraft.nuclearcraft.Manager;
import me.none030.mortisnuclearcraft.nuclearcraft.radiatiton.RadiationManager;
import me.none030.mortisnuclearcraft.structures.Structure;
import me.none030.mortisnuclearcraft.utils.NuclearType;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

public class BlockBombManager extends Manager {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final RadiationManager radiationManager;
    private final MenuItems menuItems;
    private final HashMap<String, BlockBomb> blockBombById;
    private final HashMap<UUID, BlockBombData> playersSettingTimer;
    private final HashMap<UUID, Integer> playersInCoolDown;

    public BlockBombManager(RadiationManager radiationManager, MenuItems menuItems) {
        super(NuclearType.BLOCK_BOMB);
        this.radiationManager = radiationManager;
        this.menuItems = menuItems;
        this.blockBombById = new HashMap<>();
        this.playersSettingTimer = new HashMap<>();
        this.playersInCoolDown = new HashMap<>();
        check();
        plugin.getServer().getPluginManager().registerEvents(new BlockBombListener(this), plugin);
    }

    private void check() {
        BlockBombManager blockBombManager = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                playersInCoolDown.clear();
                for (int i = 0; i < getCores().size(); i++) {
                    Location core = getCores().get(i);
                    if (core == null) {
                        continue;
                    }
                    BlockBombData data = getBombData(core);
                    if (data == null) {
                        delete(core);
                        continue;
                    }
                    BlockBomb bomb = getBlockBombById().get(data.getId());
                    if (bomb == null) {
                        delete(data);
                        continue;
                    }
                    Structure structure = bomb.getStructure(data.getStructureId());
                    if (structure == null || !structure.isStructure(core)) {
                        delete(data);
                        continue;
                    }
                    if (!data.isManualMode()) {
                        if (structure.hasRedstoneSignal(core)) {
                            structure.destroy(core);
                            bomb.explode(blockBombManager.getRadiationManager(), core);
                            delete(data);
                            continue;
                        }
                    }
                    if (data.getTimer() == null) {
                        continue;
                    }
                    if (data.getTimer().isBefore(LocalDateTime.now())) {
                        structure.destroy(core);
                        bomb.explode(blockBombManager.getRadiationManager(), core);
                        delete(data);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public BlockBomb getBlockBomb(Location location) {
        for (BlockBomb blockBomb : blockBombById.values()) {
            for (Structure structure : blockBomb.getStructures()) {
                if (!structure.isStructure(location)) {
                    continue;
                }
                return blockBomb;
            }
        }
        return null;
    }

    public BlockBombData getBombData(Location core) {
        if (!getCores().contains(core)) {
            return null;
        }
        BlockBombData data = new BlockBombData(core);
        if (!data.isType()) {
            return null;
        }
        return data;
    }

    public RadiationManager getRadiationManager() {
        return radiationManager;
    }

    public MenuItems getMenuItems() {
        return menuItems;
    }

    public HashMap<String, BlockBomb> getBlockBombById() {
        return blockBombById;
    }

    public HashMap<UUID, BlockBombData> getPlayersSettingTimer() {
        return playersSettingTimer;
    }

    public HashMap<UUID, Integer> getPlayersInCoolDown() {
        return playersInCoolDown;
    }
}
