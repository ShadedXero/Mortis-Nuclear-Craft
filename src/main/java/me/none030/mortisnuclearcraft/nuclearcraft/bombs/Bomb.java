package me.none030.mortisnuclearcraft.nuclearcraft.bombs;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.TownyWorld;
import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.nuclearcraft.radiatiton.RadiationManager;
import me.none030.mortisnuclearcraft.utils.MessageUtils;
import me.none030.mortisnuclearcraft.utils.bomb.VehicleRemover;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public abstract class Bomb implements Listener {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final DecimalFormat formatter = new DecimalFormat("#.#");
    private final Set<UUID> explosions;
    private final HashMap<UUID, Set<BlockState>> water;
    private final int strength;
    private final int radius;
    private final long duration;
    private final double radiation;
    private final boolean vehicles;
    private final boolean drain;
    private final boolean fire;
    private final boolean blockDamage;
    private final boolean townyBlockDamage;
    private final boolean blockRegen;
    private final boolean townyBlockRegen;
    private final long regenTime;

    protected Bomb(int strength, int radius, long duration, double radiation, boolean vehicles, boolean drain, boolean fire, boolean blockDamage, boolean townyBlockDamage, boolean blockRegen, boolean townyBlockRegen, long regenTime) {
        this.explosions = new HashSet<>();
        this.water = new HashMap<>();
        this.strength = strength;
        this.radius = radius;
        this.duration = duration;
        this.radiation = radiation;
        this.vehicles = vehicles;
        this.drain = drain;
        this.fire = fire;
        this.blockDamage = blockDamage;
        this.townyBlockDamage = townyBlockDamage;
        this.blockRegen = blockRegen;
        this.townyBlockRegen = townyBlockRegen;
        this.regenTime = regenTime;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void explode(RadiationManager radiationManager, Location location) {
        boolean explosion = false;
        boolean forceExplosion = false;
        if (plugin.hasTowny()) {
            TownyWorld townyWorld = TownyAPI.getInstance().getTownyWorld(location.getWorld());
            if (townyWorld != null) {
                if (!townyWorld.isExpl()) {
                    explosion = true;
                    townyWorld.setExpl(true);
                }
                if (!townyWorld.isForceExpl()) {
                    forceExplosion = true;
                    townyWorld.setForceExpl(true);
                }
            }
        }
        Firework firework = location.getWorld().spawn(location, Firework.class);
        if (drain) {
            drain(firework.getUniqueId(), location);
        }
        if (vehicles) {
            destroyVehicles(location);
        }
        if (plugin.hasTowny()) {
            if (!TownyAPI.getInstance().isWilderness(location)) {
                if (townyBlockRegen) {
                    explosions.add(firework.getUniqueId());
                }
                firework.remove();
                location.getWorld().createExplosion(location, strength, fire, townyBlockDamage, firework);
                return;
            }
        }
        if (blockRegen) {
            explosions.add(firework.getUniqueId());
        }
        firework.remove();
        location.getWorld().createExplosion(location, strength, fire, blockDamage, firework);
        radiate(radiationManager, location);
        if (plugin.hasTowny()) {
            TownyWorld townyWorld = TownyAPI.getInstance().getTownyWorld(location.getWorld());
            if (townyWorld != null) {
                if (explosion) {
                    townyWorld.setExpl(true);
                }
                if (forceExplosion) {
                    townyWorld.setForceExpl(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        Entity entity = e.getEntity();
        if (!explosions.contains(entity.getUniqueId())) {
            return;
        }
        explosions.remove(entity.getUniqueId());
        e.setYield(0);
        List<BlockState> states = e.blockList().stream().map(Block::getState).collect(Collectors.toList());
        new BukkitRunnable() {
            @Override
            public void run() {
                for (BlockState state : states) {
                    state.update(true);
                }
                Set<BlockState> waterBlocks = water.get(entity.getUniqueId());
                if (waterBlocks != null) {
                    for (BlockState state : waterBlocks) {
                        state.update(true);
                    }
                }
            }
        }.runTaskLater(plugin, regenTime * 20);
    }

    public void radiate(RadiationManager radiationManager, Location location) {
        new BukkitRunnable() {
            long seconds;
            @Override
            public void run() {
                seconds++;
                if (seconds > duration) {
                    cancel();
                }
                List<LivingEntity> entities = new ArrayList<>(location.getNearbyLivingEntities(getRadius()));
                for (int i = 0; i < entities.size(); i++) {
                    LivingEntity entity = entities.get(i);
                    if (!(entity instanceof Player)) {
                        entity.setHealth(0);
                        entities.remove(entity);
                        continue;
                    }
                    Player player = (Player) entity;
                    radiationManager.addRadiation(player, radiation);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
        MessageUtils editor = new MessageUtils(radiationManager.getMessage("BOMB_EXPLODE"));
        editor.replace("%world%", location.getWorld().getName());
        editor.replace("%x%", formatter.format(location.getX()));
        editor.replace("%y%", formatter.format(location.getY()));
        editor.replace("%z%", formatter.format(location.getZ()));
        Bukkit.broadcast(Component.text(editor.getMessage()));
    }

    private void drain(UUID uuid, Location loc) {
        Set<BlockState> states = new HashSet<>();
        for (double x = loc.getX() - getRadius(); x <= loc.getX() + getRadius(); x++) {
            for (double y = loc.getY() - getRadius(); y <= loc.getY() + getRadius(); y++) {
                for (double z = loc.getZ() - getRadius(); z <= loc.getZ() + getRadius(); z++) {
                    Location location = new Location(loc.getWorld(), x, y, z);
                    Block block = location.getBlock();
                    if (block.getType().equals(Material.SEAGRASS) || block.getType().equals(Material.TALL_SEAGRASS) || block.getType().equals(Material.KELP) || block.getType().equals(Material.KELP_PLANT) || block.getType().equals(Material.WATER)) {
                        states.add(block.getState());
                        block.setBlockData(Material.AIR.createBlockData());
                    }
                }
            }
        }
        if (plugin.hasTowny()) {
            if (!TownyAPI.getInstance().isWilderness(loc)) {
                if (townyBlockRegen) {
                    water.put(uuid, states);
                    return;
                }
            }
        }
        if (blockRegen) {
            water.put(uuid, states);
        }
    }

    private void destroyVehicles(Location loc) {
        if (!plugin.hasQAV()) {
            return;
        }
        VehicleRemover remover = new VehicleRemover(loc, getRadius());
        remover.removeVehicles();
    }

    public DecimalFormat getFormatter() {
        return formatter;
    }

    public int getStrength() {
        return strength;
    }

    public int getRadius() {
        return radius;
    }

    public long getDuration() {
        return duration;
    }

    public double getRadiation() {
        return radiation;
    }

    public boolean isVehicles() {
        return vehicles;
    }

    public boolean isDrain() {
        return drain;
    }

    public boolean isBlockDamage() {
        return blockDamage;
    }

    public boolean isTownyBlockDamage() {
        return townyBlockDamage;
    }

    public boolean isBlockRegen() {
        return blockRegen;
    }

    public boolean isTownyBlockRegen() {
        return townyBlockRegen;
    }

    public long getRegenTime() {
        return regenTime;
    }
}
