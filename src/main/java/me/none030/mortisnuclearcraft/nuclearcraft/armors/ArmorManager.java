package me.none030.mortisnuclearcraft.nuclearcraft.armors;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.nuclearcraft.radiatiton.RadiationManager;
import me.none030.mortisnuclearcraft.utils.radiation.RadiationType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ArmorManager {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final RadiationManager radiationManager;
    private final List<RadiationArmor> armors;
    private final List<UUID> playersWithArmor;
    private final HashMap<String, RadiationArmor> armorById;

    public ArmorManager(RadiationManager radiationManager) {
        this.radiationManager = radiationManager;
        this.armors = new ArrayList<>();
        this.playersWithArmor = new ArrayList<>();
        this.armorById = new HashMap<>();
        check();
        plugin.getServer().getPluginManager().registerEvents(new ArmorListener(this), plugin);
    }

    public String getArmor(ItemStack item) {
        if (item == null) {
            return null;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }
        ArmorData data = new ArmorData(meta);
        if (!data.isType()) {
            return null;
        }
        return data.getId();
    }

    public void resetSpeed(Player player) {
        player.setWalkSpeed((float) 0.2);
    }

    public void check() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID uuid : playersWithArmor) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player == null) {
                        playersWithArmor.remove(uuid);
                        continue;
                    }
                    double radIncrease = 0;
                    double radDecrease = 0;
                    for (ItemStack item : player.getInventory().getArmorContents()) {
                        if (item == null) {
                            continue;
                        }
                        String armorId = getArmor(item);
                        if (armorId == null) {
                            continue;
                        }
                        RadiationArmor armor = getArmorById().get(armorId);
                        if (armor == null) {
                            continue;
                        }
                        armor.applyWeight(player);
                        if (armor.getType().equals(RadiationType.INCREASE)) {
                            radIncrease = radIncrease + armor.getRadiation();
                        }else {
                            radDecrease = radDecrease + armor.getRadiation();
                        }
                    }
                    double max = Math.max(radIncrease, radDecrease);
                    double min = Math.min(radIncrease, radDecrease);
                    double radiation = max - min;
                    if (max == radIncrease) {
                        radiationManager.addRadiation(player, radiation);
                    }else {
                        radiationManager.removeRadiation(player, radiation);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public List<RadiationArmor> getArmors() {
        return armors;
    }

    public List<UUID> getPlayersWithArmor() {
        return playersWithArmor;
    }

    public HashMap<String, RadiationArmor> getArmorById() {
        return armorById;
    }
}