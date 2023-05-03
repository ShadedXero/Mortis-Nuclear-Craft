package me.none030.mortisnuclearcraft.nuclearcraft.addons;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.utils.addons.MythicMob;
import me.none030.mortisnuclearcraft.utils.radiation.RadiationMode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class MythicMobsManager implements Listener {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final AddonManager addonManager;
    private final List<MythicMob> mobs;

    public MythicMobsManager(AddonManager addonManager) {
        this.addonManager = addonManager;
        this.mobs = new ArrayList<>();
        check();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public List<MythicMob> getMobs() {
        return mobs;

    }

    private void check() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    List<LivingEntity> entities = new ArrayList<>(player.getLocation().getNearbyLivingEntities(3));
                    for (MythicMob mythicMob : mobs) {
                        if (!mythicMob.getMode().equals(RadiationMode.PROXIMITY)) {
                            continue;
                        }
                        for (LivingEntity entity : entities) {
                            ActiveMob mob = MythicBukkit.inst().getAPIHelper().getMythicMobInstance(entity);
                            if (mob == null) {
                                continue;
                            }
                            if (mythicMob.isMythicMob(mob.getMobType())) {
                                mythicMob.changeRadiation(addonManager.getRadiationManager(), player);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    @EventHandler
    public void onMythicMobDamage(EntityDamageByEntityEvent e) {
        Entity entity = e.getDamager();
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) e.getEntity();
        ActiveMob mob = MythicBukkit.inst().getAPIHelper().getMythicMobInstance(entity);
        if (mob == null) {
            return;
        }
        String id = mob.getMobType();
        for (MythicMob mythicMob : mobs) {
            if (!mythicMob.getMode().equals(RadiationMode.ATTACK)) {
                continue;
            }
            if (!mythicMob.isMythicMob(id)) {
                continue;
            }
            mythicMob.changeRadiation(addonManager.getRadiationManager(), player);
        }
    }
}
