package me.none030.mortisnuclearcraft.config;

import me.none030.mortisnuclearcraft.nuclearcraft.NuclearCraftManager;

public class ConfigManager {

    private final NuclearCraftManager manager;
    private final ItemsConfig itemsConfig;
    private final DropsConfig dropsConfig;
    private final RadiationConfig radiationConfig;
    private final WasteConfig wasteConfig;
    private final ArmorsConfig armorsConfig;
    private final StructuresConfig structuresConfig;
    private final ItemBombConfig itemBombConfig;
    private final BlockBombConfig blockBombConfig;
    private final CentrifugeConfig centrifugeConfig;
    private final ReactorConfig reactorConfig;
    private final AddonsConfig addonsConfig;

    public ConfigManager(NuclearCraftManager manager) {
        this.manager = manager;
        this.itemsConfig = new ItemsConfig(this);
        this.dropsConfig = new DropsConfig(this);
        this.radiationConfig = new RadiationConfig(this);
        this.wasteConfig = new WasteConfig(this);
        this.armorsConfig = new ArmorsConfig(this);
        this.structuresConfig = new StructuresConfig(this);
        this.itemBombConfig = new ItemBombConfig(this);
        this.blockBombConfig = new BlockBombConfig(this);
        this.centrifugeConfig = new CentrifugeConfig(this);
        this.reactorConfig = new ReactorConfig(this);
        this.addonsConfig = new AddonsConfig(this);
    }

    public NuclearCraftManager getManager() {
        return manager;
    }

    public ItemsConfig getItemsConfig() {
        return itemsConfig;
    }

    public DropsConfig getDropsConfig() {
        return dropsConfig;
    }

    public WasteConfig getWasteConfig() {
        return wasteConfig;
    }

    public RadiationConfig getRadiationConfig() {
        return radiationConfig;
    }

    public ArmorsConfig getArmorsConfig() {
        return armorsConfig;
    }

    public StructuresConfig getStructuresConfig() {
        return structuresConfig;
    }

    public ItemBombConfig getItemBombConfig() {
        return itemBombConfig;
    }

    public BlockBombConfig getBlockBombConfig() {
        return blockBombConfig;
    }

    public CentrifugeConfig getCentrifugeConfig() {
        return centrifugeConfig;
    }

    public ReactorConfig getReactorConfig() {
        return reactorConfig;
    }

    public AddonsConfig getAddonsConfig() {
        return addonsConfig;
    }
}
