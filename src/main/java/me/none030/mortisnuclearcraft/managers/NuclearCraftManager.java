package me.none030.mortisnuclearcraft.managers;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.addons.AddonManager;
import me.none030.mortisnuclearcraft.armors.ArmorManager;
import me.none030.mortisnuclearcraft.centrifuge.CentrifugeManager;
import me.none030.mortisnuclearcraft.config.ConfigManager;
import me.none030.mortisnuclearcraft.data.DataManager;
import me.none030.mortisnuclearcraft.drops.DropManager;
import me.none030.mortisnuclearcraft.items.ItemManager;
import me.none030.mortisnuclearcraft.bombs.BombManager;
import me.none030.mortisnuclearcraft.radiation.RadiationManager;
import me.none030.mortisnuclearcraft.reactor.ReactorManager;
import me.none030.mortisnuclearcraft.structures.StructureManager;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.event.HandlerList;

public class NuclearCraftManager {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private ItemManager itemManager;
    private DropManager dropManager;
    private ArmorManager armorManager;
    private ConfigManager configManager;
    private DataManager dataManager;
    private RadiationManager radiationManager;
    private StructureManager structureManager;
    private BombManager bombManager;
    private CentrifugeManager centrifugeManager;
    private ReactorManager reactorManager;
    private AddonManager addonManager;

    public NuclearCraftManager() {
        this.itemManager = new ItemManager();
        this.dropManager = new DropManager();
        this.configManager = new ConfigManager(this);
        plugin.getServer().getPluginCommand("nuclearcraft").setExecutor(new NuclearCraftCommand(this));
    }

    public void reload() {
        HandlerList.unregisterAll(plugin);
        Bukkit.getScheduler().cancelTasks(plugin);
        radiationManager.getBossBarByPlayer().values().forEach(BossBar::removeAll);
        setItemManager(new ItemManager());
        setDropManager(new DropManager());
        setConfigManager(new ConfigManager(this));
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public void setItemManager(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    public DropManager getDropManager() {
        return dropManager;
    }

    public void setDropManager(DropManager dropManager) {
        this.dropManager = dropManager;
    }

    public ArmorManager getArmorManager() {
        return armorManager;
    }

    public void setArmorManager(ArmorManager armorManager) {
        this.armorManager = armorManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public void setConfigManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public RadiationManager getRadiationManager() {
        return radiationManager;
    }

    public void setRadiationManager(RadiationManager radiationManager) {
        this.radiationManager = radiationManager;
    }

    public StructureManager getStructureManager() {
        return structureManager;
    }

    public void setStructureManager(StructureManager structureManager) {
        this.structureManager = structureManager;
    }

    public BombManager getBombManager() {
        return bombManager;
    }

    public void setBombManager(BombManager bombManager) {
        this.bombManager = bombManager;
    }

    public CentrifugeManager getCentrifugeManager() {
        return centrifugeManager;
    }

    public void setCentrifugeManager(CentrifugeManager centrifugeManager) {
        this.centrifugeManager = centrifugeManager;
    }

    public ReactorManager getReactorManager() {
        return reactorManager;
    }

    public void setReactorManager(ReactorManager reactorManager) {
        this.reactorManager = reactorManager;
    }

    public AddonManager getAddonManager() {
        return addonManager;
    }

    public void setAddonManager(AddonManager addonManager) {
        this.addonManager = addonManager;
    }
}
