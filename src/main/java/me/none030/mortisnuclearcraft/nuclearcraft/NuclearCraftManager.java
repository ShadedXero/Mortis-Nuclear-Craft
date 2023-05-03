package me.none030.mortisnuclearcraft.nuclearcraft;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.config.ConfigManager;
import me.none030.mortisnuclearcraft.items.ItemManager;
import me.none030.mortisnuclearcraft.nuclearcraft.addons.AddonManager;
import me.none030.mortisnuclearcraft.nuclearcraft.armors.ArmorManager;
import me.none030.mortisnuclearcraft.nuclearcraft.bombs.blockbomb.BlockBombManager;
import me.none030.mortisnuclearcraft.nuclearcraft.bombs.itembomb.ItemBombManager;
import me.none030.mortisnuclearcraft.nuclearcraft.centrifuge.CentrifugeManager;
import me.none030.mortisnuclearcraft.nuclearcraft.drops.DropManager;
import me.none030.mortisnuclearcraft.nuclearcraft.radiatiton.RadiationManager;
import me.none030.mortisnuclearcraft.nuclearcraft.reactor.ReactorManager;
import me.none030.mortisnuclearcraft.nuclearcraft.waste.WasteManager;
import me.none030.mortisnuclearcraft.structures.StructureManager;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

public class NuclearCraftManager {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private ItemManager itemManager;
    private DropManager dropManager;
    private WasteManager wasteManager;
    private ArmorManager armorManager;
    private ConfigManager configManager;
    private RadiationManager radiationManager;
    private StructureManager structureManager;
    private ItemBombManager itemBombManager;
    private BlockBombManager blockBombManager;
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

    public WasteManager getWasteManager() {
        return wasteManager;
    }

    public void setWasteManager(WasteManager wasteManager) {
        this.wasteManager = wasteManager;
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

    public ItemBombManager getItemBombManager() {
        return itemBombManager;
    }

    public void setItemBombManager(ItemBombManager itemBombManager) {
        this.itemBombManager = itemBombManager;
    }

    public BlockBombManager getBlockBombManager() {
        return blockBombManager;
    }

    public void setBlockBombManager(BlockBombManager blockBombManager) {
        this.blockBombManager = blockBombManager;
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
