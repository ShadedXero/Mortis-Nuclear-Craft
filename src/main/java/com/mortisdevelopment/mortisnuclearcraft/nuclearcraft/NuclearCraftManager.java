package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft;

import com.mortisdevelopment.mortisnuclearcraft.config.ConfigManager;
import com.mortisdevelopment.mortisnuclearcraft.customblocks.CustomBlockManager;
import com.mortisdevelopment.mortisnuclearcraft.data.DataManager;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.centrifuge.CentrifugeManager;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.reactor.ReactorManager;
import com.mortisdevelopment.mortisnuclearcraft.MortisNuclearCraft;
import com.mortisdevelopment.mortisnuclearcraft.items.ItemManager;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.addons.AddonManager;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.armors.ArmorManager;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.bombs.blockbomb.BlockBombManager;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.bombs.itembomb.ItemBombManager;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.drops.DropManager;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.radiatiton.RadiationManager;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.waste.WasteManager;
import com.mortisdevelopment.mortisnuclearcraft.structures.StructureManager;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

public class NuclearCraftManager {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private ItemManager itemManager;
    private DropManager dropManager;
    private ArmorManager armorManager;
    private ConfigManager configManager;
    private RadiationManager radiationManager;
    private StructureManager structureManager;
    private CustomBlockManager customBlockManager;
    private DataManager dataManager;
    private WasteManager wasteManager;
    private ItemBombManager itemBombManager;
    private BlockBombManager blockBombManager;
    private CentrifugeManager centrifugeManager;
    private ReactorManager reactorManager;
    private AddonManager addonManager;

    public NuclearCraftManager() {
        this.itemManager = new ItemManager();
        this.dropManager = new DropManager();
        this.customBlockManager = new CustomBlockManager();
        this.configManager = new ConfigManager(this);
        plugin.getServer().getPluginCommand("nuclearcraft").setExecutor(new NuclearCraftCommand(this));
    }

    public void reload() {
        radiationManager.preReload();
        HandlerList.unregisterAll(plugin);
        Bukkit.getScheduler().cancelTasks(plugin);
        setItemManager(new ItemManager());
        setDropManager(new DropManager());
        setCustomBlockManager(new CustomBlockManager());
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

    public CustomBlockManager getCustomBlockManager() {
        return customBlockManager;
    }

    public void setCustomBlockManager(CustomBlockManager customBlockManager) {
        this.customBlockManager = customBlockManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
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
