package me.none030.mortisnuclearcraft.addons;

import me.deecaad.weaponmechanics.weapon.weaponevents.ProjectileExplodeEvent;
import me.deecaad.weaponmechanics.weapon.weaponevents.ProjectileHitBlockEvent;
import me.deecaad.weaponmechanics.weapon.weaponevents.WeaponDamageEntityEvent;
import me.none030.mortisnuclearcraft.utils.addons.Grenade;
import me.none030.mortisnuclearcraft.utils.addons.Weapon;
import me.none030.mortisnuclearcraft.utils.radiation.RadiationMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class WeaponMechanicsAddon implements Listener {

    private final boolean enabled;
    private final List<Weapon> weapons;
    private final List<Grenade> grenades;

    public WeaponMechanicsAddon(boolean enabled) {
        this.enabled = enabled;
        this.weapons = new ArrayList<>();
        this.grenades = new ArrayList<>();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public List<Grenade> getGrenades() {
        return grenades;
    }

    @EventHandler
    public void onWeaponMechanicsWeaponHit(WeaponDamageEntityEvent e) {
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
            weapon.changeRadiation(target);
        }
    }

    @EventHandler
    public void onWeaponMechanicsWeaponShoot(ProjectileHitBlockEvent e) {
        if (weapons.size() < 1) {
            return;
        }
        if (e.isCancelled()) {
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
            List<LivingEntity> entities = new ArrayList<>(e.getLocation().getNearbyLivingEntities(weapon.getRadius()));
            for (LivingEntity entity : entities) {
                if (!(entity instanceof Player)) {
                    entity.setHealth(0);
                    continue;
                }
                Player player = (Player) entity;
                weapon.changeRadiation(player);
            }
        }
    }

    @EventHandler
    public void onWeaponMechanicsWeaponExplode(ProjectileExplodeEvent e) {
        if (grenades.size() < 1) {
            return;
        }
        if (e.isCancelled()) {
            return;
        }
        String id = e.getWeaponTitle();
        for (Grenade grenade : grenades) {
            if (!grenade.isGrenade(id)) {
                continue;
            }
            grenade.explode(e.getLocation());
        }
    }
}
