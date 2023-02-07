package me.none030.mortisnuclearcraft.config;

import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.bombs.BombMessages;
import me.none030.mortisnuclearcraft.centrifuge.CentrifugeMessages;
import me.none030.mortisnuclearcraft.managers.NuclearCraftMessages;
import me.none030.mortisnuclearcraft.radiation.RadiationMessages;
import me.none030.mortisnuclearcraft.reactor.ReactorMessages;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import static me.none030.mortisnuclearcraft.utils.MessageUtils.colorMessage;

public class MessagesConfig {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final ConfigManager configManager;

    public MessagesConfig(ConfigManager configManager) {
        this.configManager = configManager;
        loadConfig();
    }

    private void loadConfig() {
        File file = saveConfig();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        loadBombMessages(config.getConfigurationSection("bomb-messages"));
        loadCentrifugeMessages(config.getConfigurationSection("centrifuge-messages"));
        loadRadiationMessages(config.getConfigurationSection("radiation-messages"));
        loadReactorMessages(config.getConfigurationSection("reactor-messages"));
        loadCommandMessages(config.getConfigurationSection("command-messages"));
    }

    private void loadBombMessages(ConfigurationSection section) {
        if (section == null) {
            return;
        }
        BombMessages.TIME_LEFT = colorMessage(section.getString("time-left"));
        BombMessages.TIMER_NOT_SET = colorMessage(section.getString("timer-not-set"));
        BombMessages.TIMER_SET_EXPIRED = colorMessage(section.getString("timer-set-expired"));
        BombMessages.TIMER_SET = colorMessage(section.getString("timer-set"));
        BombMessages.SET_TIMER = colorMessage(section.getString("set-timer"));
        BombMessages.REDSTONE_MODE = colorMessage(section.getString("redstone-mode"));
        BombMessages.MANUAL_MODE = colorMessage(section.getString("manual-mode"));
        BombMessages.BOMB_BUILT = colorMessage(section.getString("bomb-built"));
        BombMessages.BOMB_EXPLODE = colorMessage(section.getString("bomb-explode"));
    }

    private void loadCentrifugeMessages(ConfigurationSection section) {
        if (section == null) {
            return;
        }
        CentrifugeMessages.CENTRIFUGE_BUILT = colorMessage(section.getString("centrifuge-built"));
        CentrifugeMessages.REDSTONE_MODE = colorMessage(section.getString("redstone-mode"));
        CentrifugeMessages.MANUAL_MODE = colorMessage(section.getString("manual-mode"));
    }

    private void loadReactorMessages(ConfigurationSection section) {
        if (section == null) {
            return;
        }
        ReactorMessages.REACTOR_BUILT = colorMessage(section.getString("reactor-built"));
        ReactorMessages.REDSTONE_MODE = colorMessage(section.getString("redstone-mode"));
        ReactorMessages.MANUAL_MODE = colorMessage(section.getString("manual-mode"));
    }

    private void loadRadiationMessages(ConfigurationSection section) {
        if (section == null) {
            return;
        }
        RadiationMessages.PLAYER_RADIATION = colorMessage(section.getString("player-radiation"));
        RadiationMessages.PILL_USED = colorMessage(section.getString("pill-used"));
    }

    private void loadCommandMessages(ConfigurationSection section) {
        if (section == null) {
            return;
        }
        NuclearCraftMessages.NO_PERMISSION = colorMessage(section.getString("no-permission"));
        NuclearCraftMessages.NO_CONSOLE_USAGE = colorMessage(section.getString("no-console-usage"));
        NuclearCraftMessages.HELP = colorMessage(section.getString("help"));
        NuclearCraftMessages.RADIATION_USAGE = colorMessage(section.getString("radiation-usage"));
        NuclearCraftMessages.INVALID_NUMBER = colorMessage(section.getString("invalid-number"));
        NuclearCraftMessages.RADIATION_CHANGED = colorMessage(section.getString("radiation-changed"));
        NuclearCraftMessages.RADIATION_SET_USAGE = colorMessage(section.getString("radiation-set-usage"));
        NuclearCraftMessages.RADIATION_ADD_USAGE = colorMessage(section.getString("radiation-add-usage"));
        NuclearCraftMessages.RADIATION_REMOVE_USAGE = colorMessage(section.getString("radiation-remove-usage"));
        NuclearCraftMessages.GIVE_USAGE = colorMessage(section.getString("give-usage"));
        NuclearCraftMessages.ITEM_GIVEN = colorMessage(section.getString("item-given"));
        NuclearCraftMessages.GIVE_ARMOR_INVALID = colorMessage(section.getString("give-armor-invalid"));
        NuclearCraftMessages.GIVE_ARMOR_USAGE = colorMessage(section.getString("give-armor-usage"));
        NuclearCraftMessages.GIVE_BOMB_INVALID = colorMessage(section.getString("give-bomb-invalid"));
        NuclearCraftMessages.GIVE_BOMB_USAGE = colorMessage(section.getString("give-bomb-usage"));
        NuclearCraftMessages.GIVE_ITEM_INVALID = colorMessage(section.getString("give-item-invalid"));
        NuclearCraftMessages.GIVE_ITEM_USAGE = colorMessage(section.getString("give-item-usage"));
        NuclearCraftMessages.TOGGLE_USAGE = colorMessage(section.getString("toggle-usage"));
        NuclearCraftMessages.TOGGLE_ON = colorMessage(section.getString("toggle-on"));
        NuclearCraftMessages.TOGGLE_OFF = colorMessage(section.getString("toggle-off"));
        NuclearCraftMessages.RELOAD = colorMessage(section.getString("reload"));
        NuclearCraftMessages.STRUCTURE_USAGE = colorMessage(section.getString("structure-usage"));
        NuclearCraftMessages.STRUCTURE_SAVE_USAGE = colorMessage(section.getString("structure-save-usage"));
        NuclearCraftMessages.INVALID_WORLD = colorMessage(section.getString("invalid-world"));
        NuclearCraftMessages.INVALID_MATERIAL = colorMessage(section.getString("invalid-material"));
        NuclearCraftMessages.STRUCTURE_SAVE = colorMessage(section.getString("structure-save"));
        NuclearCraftMessages.STRUCTURE_DELETE_USAGE = colorMessage(section.getString("structure-delete-usage"));
        NuclearCraftMessages.STRUCTURE_DELETE = colorMessage(section.getString("structure-delete"));
    }

    private File saveConfig() {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        return file;
    }
}
