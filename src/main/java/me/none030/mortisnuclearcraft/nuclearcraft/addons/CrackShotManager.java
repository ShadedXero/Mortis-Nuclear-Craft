package me.none030.mortisnuclearcraft.nuclearcraft.addons;

import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;
import com.shampaggon.crackshot.events.WeaponExplodeEvent;
import com.shampaggon.crackshot.events.WeaponHitBlockEvent;
import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.utils.addons.Grenade;
import me.none030.mortisnuclearcraft.utils.addons.Weapon;
import me.none030.mortisnuclearcraft.utils.radiation.RadiationMode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class CrackShotManager implements Listener {

    private final AddonManager addonManager;
    private final List<Weapon> weapons;
    private final List<Grenade> grenades;

    public CrackShotManager(AddonManager addonManager) {
        this.addonManager = addonManager;
        this.weapons = new ArrayList<>();
        this.grenades = new ArrayList<>();
        MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public List<Grenade> getGrenades() {
        return grenades;
    }

    @EventHandler
    public void onCrackShotWeaponHit(WeaponDamageEntityEvent e) {
        if (weapons.size() < 1) {
            return;
        }
        if (e.isCancelled()) {
            return;
        }
        String id = e.getWeaponTitle();
        if (!(e.getVictim() instanceof Player)) {
            return;
        }
        Player target = (Player) e.getVictim();
        for (Weapon weapon : weapons) {
            if (!weapon.getMode().equals(RadiationMode.ATTACK)) {
                continue;
            }
            if (!weapon.isWeapon(id)) {
                continue;
            }
            weapon.changeRadiation(addonManager.getRadiationManager(), target);
        }
    }

    @EventHandler
    public void onCrackShotWeaponShoot(WeaponHitBlockEvent e) {
        if (weapons.size() < 1) {
            return;
        }
        String id = e.getWeaponTitle();
        for (Weapon weapon : weapons) {
            if (!weapon.getMode().equals(RadiationMode.PROXIMITY)) {
                continue;
            }
            if (!weapon.isWeapon(id)) {
                continue;
            }
            List<LivingEntity> entities = new ArrayList<>(e.getBlock().getLocation().getNearbyLivingEntities(weapon.getRadius()));
            for (LivingEntity entity : entities) {
                if (!(entity instanceof Player)) {
                    entity.setHealth(0);
                    continue;
                }
                Player player = (Player) entity;
                weapon.changeRadiation(addonManager.getRadiationManager(), player);
            }
        }
    }

    @EventHandler
    public void onCrackShotWeaponExplode(WeaponExplodeEvent e) {
        if (grenades.size() < 1) {
            return;
        }
        String id = e.getWeaponTitle();
        for (Grenade grenade : grenades) {
            if (!grenade.isGrenade(id)) {
                continue;
            }
            grenade.explode(addonManager.getRadiationManager(), e.getLocation());
        }
    }
}
