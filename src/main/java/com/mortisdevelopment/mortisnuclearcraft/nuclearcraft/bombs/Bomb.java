package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.bombs;

import com.mortisdevelopment.mortisnuclearcraft.MortisNuclearCraft;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.radiatiton.RadiationManager;
import com.mortisdevelopment.mortisnuclearcraft.utils.MessageUtils;
import com.mortisdevelopment.mortisnuclearcraft.utils.bomb.VehicleRemover;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.TownBlock;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.*;

public abstract class Bomb implements Listener {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final DecimalFormat formatter = new DecimalFormat("#.#");
    private final Set<UUID> sources;
    private final Map<UUID, Boolean> regenByExplosion;
    private final Map<UUID, Boolean> regenByTownExplosion;
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
    private final boolean townyPlayerDamage;
    private final boolean explosionsEnabledPlotsWilderness;

    protected Bomb(int strength, int radius, long duration, double radiation, boolean vehicles, boolean drain, boolean fire, boolean blockDamage, boolean townyBlockDamage, boolean blockRegen, boolean townyBlockRegen, long regenTime, boolean townyPlayerDamage, boolean explosionsEnabledPlotsWilderness) {
        this.sources = new HashSet<>();
        this.regenByExplosion = new HashMap<>();
        this.regenByTownExplosion = new HashMap<>();
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
        this.townyPlayerDamage = townyPlayerDamage;
        this.explosionsEnabledPlotsWilderness = explosionsEnabledPlotsWilderness;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        if (plugin.hasTowny()) {
            plugin.getServer().getPluginManager().registerEvents(new BombListener(this), plugin);
        }
    }

    private void explode(UUID uuid, Location location, boolean regen, boolean town) {
        if (drain) {
            drain(uuid, location);
        }
        if (vehicles) {
            destroyVehicles(location);
        }
        if (town) {
            regenByTownExplosion.put(uuid, regen);
            return;
        }
        regenByExplosion.put(uuid, regen);
    }

    private ArmorStand getSource(Location location) {
        ArmorStand source = location.getWorld().spawn(location, ArmorStand.class, stand -> {
            stand.setInvulnerable(true);
            stand.setVisible(true);
        });
        sources.add(source.getUniqueId());
        new BukkitRunnable() {
            @Override
            public void run() {
                source.remove();
                sources.remove(source.getUniqueId());
            }
        }.runTaskLater(plugin, 30 * 20);
        return source;
    }

    public void explode(RadiationManager radiationManager, Location location) {
        location = location.getBlock().getLocation();
        if (plugin.hasTowny() && TownyAPI.getInstance().isWilderness(location)) {
            operateWilderness(radiationManager, location);
        }else {
            operateTown(radiationManager, location);
        }
    }

    private void operateWilderness(RadiationManager radiationManager, Location location) {
        World world = location.getWorld();
        ArmorStand source = getSource(location);
        radiate(radiationManager, location);
        if (blockDamage) {
            explode(source.getUniqueId(), location, blockRegen, false);
        }
        world.createExplosion(source, location, strength, fire, blockDamage);
    }

    private void operateTown(RadiationManager radiationManager, Location location) {
        World world = location.getWorld();
        if (explosionsEnabledPlotsWilderness) {
            TownBlock block = TownyAPI.getInstance().getTownBlock(location);
            if (block != null) {
                if (block.getPermissions().explosion) {
                    operateWilderness(radiationManager, location);
                    return;
                }
            }
        }
        ArmorStand source = getSource(location);
        radiate(radiationManager, location);
        if (townyBlockDamage) {
            explode(source.getUniqueId(), location, townyBlockRegen, true);
        }
        world.createExplosion(source, location, strength, fire, townyBlockDamage);
    }

//    public void explode(RadiationManager radiationManager, Location location) {
//        World world = location.getWorld();
//        Location calculatedLocation = location.getBlock().getLocation();
//        if (plugin.hasTowny() && !TownyAPI.getInstance().isWilderness(calculatedLocation)) {
//            if (explosionsEnabledPlotsWilderness) {
//                TownBlock block = TownyAPI.getInstance().getTownBlock(location);
//                if (block != null) {
//                    if (block.getPermissions().explosion) {
//                        if (townyPlayerDamage) {
//                            world.createExplosion(getSource(calculatedLocation), calculatedLocation, strength, fire, blockDamage);
//                        }else {
//                            world.createExplosion(calculatedLocation, strength, fire, blockDamage);
//                        }
//                        radiate(radiationManager, calculatedLocation);
//                        if (blockDamage) {
//                            explode(calculatedLocation, blockRegen);
//                        }
//                        return;
//                    }
//                }
//            }
//            if (townyPlayerDamage) {
//                world.createExplosion(getSource(calculatedLocation), calculatedLocation, strength, fire, townyBlockDamage);
//            }else {
//                world.createExplosion(calculatedLocation, strength, fire, townyBlockDamage);
//            }
//            radiate(radiationManager, calculatedLocation);
//            if (townyBlockDamage) {
//                explode(calculatedLocation, townyBlockRegen);
//            }
//        } else {
//            if (townyPlayerDamage) {
//                world.createExplosion(getSource(calculatedLocation), calculatedLocation, strength, fire, blockDamage);
//            }else {
//                world.createExplosion(calculatedLocation, strength, fire, blockDamage);
//            }
//            radiate(radiationManager, calculatedLocation);
//            if (blockDamage) {
//                explode(calculatedLocation, blockRegen);
//            }
//        }
//    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        Entity entity = e.getEntity();
        if (!sources.contains(entity.getUniqueId())) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onExplosionDamage(EntityDamageByEntityEvent e) {
        UUID uuid = e.getDamager().getUniqueId();
        if (!sources.contains(uuid)) {
            return;
        }
        debug("Bomb onExplosionDamage");
        if (townyPlayerDamage) {
            return;
        }
        Entity damaged = e.getEntity();
        if (!(damaged instanceof Player)) {
            return;
        }
        if (plugin.hasTowny()) {
            TownBlock block = TownyAPI.getInstance().getTownBlock(damaged.getLocation());
            if (block == null) {
                return;
            }
            if (!block.getPermissions().pvp) {
                e.setCancelled(true);
            }
        }
    }

    public void debug(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        UUID uuid = e.getEntity().getUniqueId();
        debug("Bomb onEntityExplode " + uuid);
        Boolean regen = regenByExplosion.get(uuid);
        if (regen == null) {
            return;
        }
        regenByExplosion.remove(uuid);
        debug("Bomb onEntityExplode 2");
        System.out.println("Bomb onEntityExplode " + regen);
        if (regen) {
            e.setYield(0);
            regen(uuid, e.blockList());
        }
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent e) {
        debug("Bomb onBlockExplode");
        System.out.println("Bomb onBlockExplode");
    }

    public void regen(UUID uuid, List<Block> blockList) {
        Map<Location, BlockData> dataByLocation = new HashMap<>();
        for (Block block : blockList) {
            dataByLocation.put(block.getLocation(), block.getBlockData());
            if (block instanceof Container) {
                Container container = (Container) block;
                container.getInventory().clear();
            }
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<Location, BlockData> entry : dataByLocation.entrySet()) {
                    entry.getKey().getBlock().setBlockData(entry.getValue());
                }
            }
        }.runTaskLater(plugin, regenTime * 20);
    }

//    public void regen(UUID uuid, List<Block> blockList) {
//        List<BlockState> states = blockList.stream().map(Block::getState).collect(Collectors.toList());
//        for (BlockState state : new ArrayList<>(states)) {
//            Block block = state.getBlock();
//            if (block instanceof Container) {
//                Container container = (Container) block;
//                container.getInventory().clear();
//            }
//        }
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//                for (BlockState state : states) {
//                    state.getBlock().setBlockData(state.getBlockData());
////                    state.update(true);
//                }
//                Set<BlockState> waterBlocks = water.get(uuid);
//                if (waterBlocks != null) {
//                    water.remove(uuid);
//                    for (BlockState state : waterBlocks) {
//                        state.getBlock().setBlockData(state.getBlockData());
////                        state.update(true);
//                    }
//                }
//            }
//        }.runTaskLater(plugin, regenTime * 20);
//    }

//    @EventHandler
//    public void onBlockExplode(BlockExplodeEvent e) {
//        Location location = e.getBlock().getLocation();
//        if (!explosions.contains(location)) {
//            return;
//        }
//        explosions.remove(location);
//        e.setYield(0);
//        List<BlockState> states = e.blockList().stream().map(Block::getState).collect(Collectors.toList());
//        for (BlockState state : new ArrayList<>(states)) {
//            Block block = state.getBlock();
//            if (block instanceof Container) {
//                Container container = (Container) block;
//                container.getInventory().clear();
//            }
//            block.setType(Material.AIR);
//        }
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//                for (BlockState state : states) {
//                    state.update(true);
//                }
//                Set<BlockState> waterBlocks = water.get(location);
//                if (waterBlocks != null) {
//                    for (BlockState state : waterBlocks) {
//                        state.update(true);
//                    }
//                }
//            }
//        }.runTaskLater(plugin, regenTime * 20);
//    }

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
                for (LivingEntity entity : entities) {
                    if (!(entity instanceof Player)) {
                        if (radiationManager.getRadiation().getTolerance().isKillable(entity.getType())) {
                            entity.setHealth(0);
                        }
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
                        BlockState state = block.getState();
                        states.add(state);
                        state.getBlock().setType(Material.AIR);
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

    public Map<UUID, Set<BlockState>> getWater() {
        return water;
    }

    public Set<UUID> getSources() {
        return sources;
    }

    public Map<UUID, Boolean> getRegenByExplosion() {
        return regenByExplosion;
    }

    public Map<UUID, Boolean> getRegenByTownExplosion() {
        return regenByTownExplosion;
    }

    public boolean isFire() {
        return fire;
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

    public boolean isTownyPlayerDamage() {
        return townyPlayerDamage;
    }

    public boolean isExplosionsEnabledPlotsWilderness() {
        return explosionsEnabledPlotsWilderness;
    }
}
