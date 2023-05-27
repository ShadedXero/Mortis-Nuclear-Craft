package me.none030.mortisnuclearcraft.nuclearcraft.waste;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.nuclearcraft.Manager;
import me.none030.mortisnuclearcraft.nuclearcraft.radiatiton.RadiationManager;
import me.none030.mortisnuclearcraft.utils.NuclearType;
import me.none030.mortisnuclearcraft.utils.waste.WasteType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WasteManager extends Manager {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final RadiationManager radiationManager;
    private final HashMap<String, Waste> wasteById;

    public WasteManager(RadiationManager radiationManager) {
        super(NuclearType.WASTE);
        this.radiationManager = radiationManager;
        this.wasteById = new HashMap<>();
        check();
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
                    String wasteId = getWasteBlock(core.getBlock());
                    if (wasteId == null) {
                        delete(core);
                        continue;
                    }
                    Waste waste = wasteById.get(wasteId);
                    if (waste == null) {
                        delete(core);
                        continue;
                    }
                    List<LivingEntity> entities = new ArrayList<>(core.getNearbyLivingEntities(waste.getRadius()));
                    for (LivingEntity entity : entities) {
                        if (!(entity instanceof Player)) {
                            entity.setHealth(0);
                            entities.remove(entity);
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
                        String wasteId = getWasteItem(item);
                        if (wasteId == null) {
                            continue;
                        }
                        Waste waste = wasteById.get(wasteId);
                        if (waste == null || waste.getType().equals(WasteType.BLOCK)) {
                            continue;
                        }
                        waste.giveRadiation(radiationManager, player);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public String getWasteItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        WasteData data = new WasteData(meta);
        if (!data.isType()) {
            return null;
        }
        return data.getId();
    }

    public String getWasteBlock(Block block) {
        WasteBlockData data = new WasteBlockData(block);
        if (!data.isType()) {
            return null;
        }
        return data.getId();
    }

    public RadiationManager getRadiationManager() {
        return radiationManager;
    }

    public HashMap<String, Waste> getWasteById() {
        return wasteById;
    }

}
