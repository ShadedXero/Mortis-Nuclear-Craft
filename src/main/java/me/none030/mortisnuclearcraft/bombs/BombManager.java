package me.none030.mortisnuclearcraft.bombs;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.data.BlockBombData;
import me.none030.mortisnuclearcraft.data.DataManager;
import me.none030.mortisnuclearcraft.radiation.RadiationManager;
import me.none030.mortisnuclearcraft.utils.NuclearItem;
import me.none030.mortisnuclearcraft.utils.bomb.BombMenuItems;
import me.none030.mortisnuclearcraft.structures.Structure;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BombManager {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final RadiationManager radiationManager;
    private final DataManager dataManager;
    private BombMenuItems menuItems;
    private final List<ItemBomb> itemBombs;
    private final List<BlockBomb> blockBombs;
    private final HashMap<String, ItemBomb> itemBombById;
    private final HashMap<String, BlockBomb> blockBombById;
    private final HashMap<UUID, BlockBombData> playersSettingTimer;
    private final HashMap<UUID, Integer> playersInCoolDown;

    public BombManager(RadiationManager radiationManager, DataManager dataManager) {
        this.radiationManager = radiationManager;
        this.dataManager = dataManager;
        this.itemBombs = new ArrayList<>();
        this.blockBombs = new ArrayList<>();
        this.itemBombById = new HashMap<>();
        this.blockBombById = new HashMap<>();
        this.playersSettingTimer = new HashMap<>();
        this.playersInCoolDown = new HashMap<>();
        check();
        plugin.getServer().getPluginManager().registerEvents(new BombListener(this), plugin);
    }

    public String getItemBomb(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        NamespacedKey nuclearCraft = new NamespacedKey(plugin, "NuclearCraft");
        String nuclearCraftString = meta.getPersistentDataContainer().get(nuclearCraft, PersistentDataType.STRING);
        if (nuclearCraftString == null) {
            return null;
        }
        NuclearItem nuclearCraftType = NuclearItem.valueOf(nuclearCraftString);
        if (!nuclearCraftType.equals(NuclearItem.BOMB)) {
            return null;
        }
        NamespacedKey nuclearCraftId = new NamespacedKey(plugin, "NuclearCraftId");
        return meta.getPersistentDataContainer().get(nuclearCraftId, PersistentDataType.STRING);
    }

    public BlockBomb getBlockBomb(Location location) {
        for (BlockBomb blockBomb : blockBombs) {
            if (!blockBomb.getStructure().isCore(location.getBlock())) {
                continue;
            }
            if (!blockBomb.getStructure().isStructure(location)) {
                continue;
            }
            return blockBomb;
        }
        return null;
    }

    private void check() {
        new BukkitRunnable() {
            @Override
            public void run() {
                playersInCoolDown.clear();
                for (BlockBombData data : dataManager.getBombStorage().getBombs()) {
                    BlockBomb bomb = getBlockBombById().get(data.getId());
                    if (bomb == null) {
                        dataManager.getBombStorage().deleteBomb(data.getCore());
                        continue;
                    }
                    Structure structure = bomb.getStructure();
                    if (!structure.isCore(data.getCore().getBlock()) || !structure.isStructure(data.getCore())) {
                        dataManager.getBombStorage().deleteBomb(data.getCore());
                        continue;
                    }
                    if (!data.isManualMode()) {
                        Block block = data.getCore().getBlock();
                        if (block.isBlockPowered() || block.isBlockIndirectlyPowered()) {
                            bomb.explode(data.getCore());
                            dataManager.getBombStorage().deleteBomb(data.getCore());
                        }
                    }
                    if (data.getTimer() == null) {
                        continue;
                    }
                    if (data.getTimer().isBefore(LocalDateTime.now())) {
                        bomb.explode(data.getCore());
                        dataManager.getBombStorage().deleteBomb(data.getCore());
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public RadiationManager getRadiationManager() {
        return radiationManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public BombMenuItems getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(BombMenuItems menuItems) {
        this.menuItems = menuItems;
    }

    public List<ItemBomb> getItemBombs() {
        return itemBombs;
    }

    public List<BlockBomb> getBlockBombs() {
        return blockBombs;
    }

    public HashMap<String, ItemBomb> getItemBombById() {
        return itemBombById;
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
