package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.waste;

import com.mortisdevelopment.mortisnuclearcraft.MortisNuclearCraft;
import com.mortisdevelopment.mortisnuclearcraft.data.DataManager;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.Manager;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.radiatiton.RadiationManager;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.waste.types.*;
import com.mortisdevelopment.mortisnuclearcraft.utils.NuclearType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class WasteManager extends Manager {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final RadiationManager radiationManager;
    private final HashMap<String, Waste> wasteById;
    private final List<UUID> droppedItems;

    public WasteManager(DataManager dataManager, RadiationManager radiationManager) {
        super(dataManager, NuclearType.WASTE);
        this.radiationManager = radiationManager;
        this.wasteById = new HashMap<>();
        this.droppedItems = new ArrayList<>();
        check();
        plugin.getServer().getPluginManager().registerEvents(new WasteListener(this), plugin);
    }

    private void check() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < getCores().size(); i++) {
                    Location core = getCores().get(i);
                    if (core == null) {
                        continue;
                    }
                    String wasteId = getWaste(core.getBlock());
                    if (wasteId == null) {
                        delete(core);
                        continue;
                    }
                    Waste waste = wasteById.get(wasteId);
                    if (waste == null) {
                        delete(core);
                        continue;
                    }
                    List<LivingEntity> entities = new ArrayList<>();
                    if (waste instanceof BlockWaste) {
                        entities.addAll(core.getNearbyLivingEntities(((BlockWaste) waste).getRadius()));
                    }
                    if (waste instanceof CustomBlockWaste) {
                        entities.addAll(core.getNearbyLivingEntities(((CustomBlockWaste) waste).getRadius()));
                    }
                    for (LivingEntity entity : entities) {
                        if (!(entity instanceof Player)) {
                            if (radiationManager.getRadiation().getTolerance().isKillable(entity.getType())) {
                                entity.setHealth(0);
                            }
                            continue;
                        }
                        Player player = (Player) entity;
                        waste.giveRadiation(radiationManager, player);
                    }
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (ItemStack item : player.getInventory().getContents()) {
                        if (item == null || item.getType().isAir()) {
                            continue;
                        }
                        Waste waste = getItemWaste(item);
                        if (waste == null) {
                            continue;
                        }
                        waste.giveRadiation(radiationManager, player);
                    }
                }
                Iterator<UUID> droppedItemIterator = droppedItems.iterator();
                while (droppedItemIterator.hasNext()) {
                    UUID uuid = droppedItemIterator.next();
                    if (uuid == null) {
                        droppedItemIterator.remove();
                        continue;
                    }
                    Entity entity = Bukkit.getEntity(uuid);
                    if (!(entity instanceof Item)) {
                        droppedItemIterator.remove();
                        continue;
                    }
                    Item item = (Item) entity;
                    DroppedWaste waste = getDroppedWaste(item.getItemStack());
                    if (waste == null) {
                        droppedItemIterator.remove();
                        continue;
                    }
                    List<LivingEntity> entities = new ArrayList<>(item.getLocation().getNearbyLivingEntities(waste.getRadius()));
                    for (LivingEntity livingEntity : entities) {
                        if (!(livingEntity instanceof Player)) {
                            if (radiationManager.getRadiation().getTolerance().isKillable(livingEntity.getType())) {
                                livingEntity.setHealth(0);
                            }
                            continue;
                        }
                        Player player = (Player) livingEntity;
                        waste.giveRadiation(radiationManager, player);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public ItemWaste getItemWaste(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return null;
        }
        for (Waste waste : getWastes()) {
            if (!(waste instanceof ItemWaste) || !waste.isWaste(item)) {
                continue;
            }
            return (ItemWaste) waste;
        }
        return null;
    }

    public DroppedWaste getDroppedWaste(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return null;
        }
        for (Waste waste : getWastes()) {
            if (!(waste instanceof DroppedWaste) || !waste.isWaste(item)) {
                continue;
            }
            return (DroppedWaste) waste;
        }
        return null;
    }

    public CustomBlockWaste getCustomBlockWaste(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return null;
        }
        for (Waste waste : getWastes()) {
            if (!(waste instanceof CustomBlockWaste) || !waste.isWaste(item)) {
                continue;
            }
            return (CustomBlockWaste) waste;
        }
        return null;
    }

    public BlockWaste getBlockWaste(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return null;
        }
        for (Waste waste : getWastes()) {
            if (!(waste instanceof BlockWaste) || !waste.isWaste(item)) {
                continue;
            }
            return (BlockWaste) waste;
        }
        return null;
    }

    public String getWaste(Block block) {
        WasteBlockData data = new WasteBlockData(block);
        if (!data.isType()) {
            return null;
        }
        return data.getId();
    }

    public RadiationManager getRadiationManager() {
        return radiationManager;
    }

    public List<Waste> getWastes() {
        return new ArrayList<>(wasteById.values());
    }

    public HashMap<String, Waste> getWasteById() {
        return wasteById;
    }

    public List<UUID> getDroppedItems() {
        return droppedItems;
    }
}
