package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft;

import com.mortisdevelopment.mortisnuclearcraft.customblocks.CustomBlock;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.armors.RadiationArmor;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.bombs.itembomb.ItemBomb;
import com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.waste.types.Waste;
import com.mortisdevelopment.mortisnuclearcraft.structures.Structure;
import com.mortisdevelopment.mortisnuclearcraft.utils.MessageUtils;
import com.mortisdevelopment.mortisnuclearcraft.utils.radiation.DisplayMode;
import com.mortisdevelopment.mortisnuclearcraft.utils.radiation.DisplayToggleMode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NuclearCraftCommand implements TabExecutor {

    private final NuclearCraftManager manager;
    private final String NO_PERMISSION = new MessageUtils("&cYou don't have permission to use this").color();
    private final String INVALID_TARGET = new MessageUtils("&cPlease enter a valid target").color();
    private final String INVALID_NUMBER = new MessageUtils("&cPlease enter a valid number").color();

    public NuclearCraftCommand(NuclearCraftManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return false;
        }
        if (args[0].equalsIgnoreCase("help")) {
            if (!sender.hasPermission("nuclearcraft.help")) {
                sender.sendMessage(NO_PERMISSION);
                return false;
            }
            sender.sendMessage(new MessageUtils("").color());
        }
        if (args[0].equalsIgnoreCase("structure")) {
            if (args[1].equalsIgnoreCase("save")) {
                if (!sender.hasPermission("nuclearcraft.structure.save")) {
                    MessageUtils utils = new MessageUtils("&cYou do not have the permission to use this");
                    utils.color();
                    sender.sendMessage(utils.getMessage());
                    return false;
                }
                String structureId = args[2];
                World world = Bukkit.getWorld(args[3]);
                if (world == null) {
                    MessageUtils utils = new MessageUtils("&cCould not save the structure");
                    utils.color();
                    sender.sendMessage(utils.getMessage());
                    return false;
                }
                double x;
                double y;
                double z;
                try {
                    x = Double.parseDouble(args[4]);
                    y = Double.parseDouble(args[5]);
                    z = Double.parseDouble(args[6]);
                } catch (NumberFormatException exp) {
                    MessageUtils utils = new MessageUtils("&cCould not save the structure");
                    utils.color();
                    sender.sendMessage(utils.getMessage());
                    return false;
                }
                boolean strict = Boolean.parseBoolean(args[7]);
                Location center = new Location(world, x, y, z);
                Structure structure = manager.getStructureManager().getStructure(center, structureId, strict);
                manager.getStructureManager().saveStructure(structure);
                manager.getConfigManager().getStructuresConfig().saveStructure(structure);
                MessageUtils utils = new MessageUtils("&cThe structure has been saved");
                utils.color();
                sender.sendMessage(utils.getMessage());
            }
            if (args[1].equalsIgnoreCase("delete")) {
                if (!sender.hasPermission("nuclearcraft.structure.delete")) {
                    MessageUtils utils = new MessageUtils("&cYou do not have the permission to use this");
                    utils.color();
                    sender.sendMessage(utils.getMessage());
                    return false;
                }
                String structureId = args[2];
                manager.getConfigManager().getStructuresConfig().deleteStructure(structureId);
                manager.getStructureManager().deleteStructure(structureId);
                MessageUtils utils = new MessageUtils("&cThe structure has been deleted");
                utils.color();
                sender.sendMessage(utils.getMessage());
            }
        }
        if (args[0].equalsIgnoreCase("radiation")) {
            if (!sender.hasPermission("nuclearcraft.radiation")) {
                sender.sendMessage(NO_PERMISSION);
                return false;
            }
            if (args.length == 1) {
                sender.sendMessage(new MessageUtils("&eNuclearCraft Radiation Commands:\n&e/nuclearcraft radiation set <player_name> <number>\n&e/nuclearcraft radiation add <player_name> <number>\n&e/nuclearcraft radiation remove <player_name> <number>").color());
                return false;
            }
            if (args[1].equalsIgnoreCase("set")) {
                if (!sender.hasPermission("nuclearcraft.radiation.set")) {
                    sender.sendMessage(NO_PERMISSION);
                    return false;
                }
                if (args.length != 4) {
                    sender.sendMessage(new MessageUtils("&cUsage: /nuclearcraft radiation set <player_name> <number>").color());
                    return false;
                }
                Player target = Bukkit.getPlayer(args[2]);
                if (target == null) {
                    sender.sendMessage(INVALID_TARGET);
                    return false;
                }
                double radiation;
                try {
                    radiation = Double.parseDouble(args[3]);
                }catch (NumberFormatException exp) {
                    sender.sendMessage(INVALID_NUMBER);
                    return false;
                }
                manager.getRadiationManager().setRadiation(target, radiation);
                sender.sendMessage(new MessageUtils("&cPlayer radiation changed").color());
            }
            if (args[1].equalsIgnoreCase("add")) {
                if (!sender.hasPermission("nuclearcraft.radiation.add")) {
                    sender.sendMessage(NO_PERMISSION);
                    return false;
                }
                if (args.length != 4) {
                    sender.sendMessage(new MessageUtils("&cUsage: /nuclearcraft radiation add <player_name> <number>").color());
                    return false;
                }
                Player target = Bukkit.getPlayer(args[2]);
                if (target == null) {
                    sender.sendMessage(INVALID_TARGET);
                    return false;
                }
                double radiation;
                try {
                    radiation = Double.parseDouble(args[3]);
                }catch (NumberFormatException exp) {
                    sender.sendMessage(INVALID_NUMBER);
                    return false;
                }
                manager.getRadiationManager().addRadiation(target, radiation);
                sender.sendMessage(new MessageUtils("&cPlayer radiation changed").color());
            }
            if (args[1].equalsIgnoreCase("remove")) {
                if (!sender.hasPermission("nuclearcraft.radiation.remove")) {
                    sender.sendMessage(NO_PERMISSION);
                    return false;
                }
                if (args.length != 4) {
                    sender.sendMessage(new MessageUtils("&cUsage: /nuclearcraft radiation remove <player_name> <number>").color());
                    return false;
                }
                Player target = Bukkit.getPlayer(args[2]);
                if (target == null) {
                    sender.sendMessage(INVALID_TARGET);
                    return false;
                }
                double radiation;
                try {
                    radiation = Double.parseDouble(args[3]);
                }catch (NumberFormatException exp) {
                    sender.sendMessage(INVALID_NUMBER);
                    return false;
                }
                manager.getRadiationManager().removeRadiation(target, radiation);
                sender.sendMessage(new MessageUtils("&cPlayer radiation changed").color());
            }
        }
        if (args[0].equalsIgnoreCase("give")) {
            if (!sender.hasPermission("nuclearcraft.give")) {
                sender.sendMessage(NO_PERMISSION);
                return false;
            }
            if (args.length == 1) {
                sender.sendMessage(new MessageUtils("&eNuclearCraft Give Commands:\n&e/nuclearcraft give armor <player_name> <armor-id>\n&e/nuclearcraft give bomb <player_name> <armor-id>\n&e/nuclearcraft give item <player_name> <item-id>").color());
                return false;
            }
            if (args[1].equalsIgnoreCase("waste")) {
                if (!sender.hasPermission("nuclearcraft.give.waste")) {
                    sender.sendMessage(NO_PERMISSION);
                    return false;
                }
                if (args.length != 4) {
                    sender.sendMessage(new MessageUtils("&cUsage: /nuclearcraft give waste <player_name> <waste-id>").color());
                    return false;
                }
                Player target = Bukkit.getPlayer(args[2]);
                if (target == null) {
                    sender.sendMessage(INVALID_TARGET);
                    return false;
                }
                Waste waste = manager.getWasteManager().getWasteById().get(args[3]);
                if (waste == null) {
                    sender.sendMessage(new MessageUtils("&cPlease enter a valid id").color());
                    return false;
                }
                waste.giveWaste(target);
                sender.sendMessage(new MessageUtils("&eItem given to the specified player").color());
            }
            if (args[1].equalsIgnoreCase("armor")) {
                if (!sender.hasPermission("nuclearcraft.give.armor")) {
                    sender.sendMessage(NO_PERMISSION);
                    return false;
                }
                if (args.length != 4) {
                    sender.sendMessage(new MessageUtils("&cUsage: /nuclearcraft give armor <player_name> <armor-id>").color());
                    return false;
                }
                Player target = Bukkit.getPlayer(args[2]);
                if (target == null) {
                    sender.sendMessage(INVALID_TARGET);
                    return false;
                }
                RadiationArmor armor = manager.getArmorManager().getArmorById().get(args[3]);
                if (armor == null) {
                    sender.sendMessage(new MessageUtils("&cPlease enter a valid id").color());
                    return false;
                }
                armor.giveArmor(target);
                sender.sendMessage(new MessageUtils("&eItem given to the specified player").color());
            }
            if (args[1].equalsIgnoreCase("bomb")) {
                if (!sender.hasPermission("nuclearcraft.give.bomb")) {
                    sender.sendMessage(NO_PERMISSION);
                    return false;
                }
                if (args.length != 4) {
                    sender.sendMessage(new MessageUtils("&cUsage: /nuclearcraft give bomb <player_name> <bomb-id>").color());
                    return false;
                }
                Player target = Bukkit.getPlayer(args[2]);
                if (target == null) {
                    sender.sendMessage(INVALID_TARGET);
                    return false;
                }
                ItemBomb bomb = manager.getItemBombManager().getItemBombById().get(args[3]);
                if (bomb == null) {
                    sender.sendMessage(new MessageUtils("&cPlease enter a valid id").color());
                    return false;
                }
                bomb.giveBomb(target);
                sender.sendMessage(new MessageUtils("&eItem given to the specified player").color());
            }
            if (args[1].equalsIgnoreCase("item")) {
                if (!sender.hasPermission("nuclearcraft.give.item")) {
                    sender.sendMessage(NO_PERMISSION);
                    return false;
                }
                if (args.length != 4) {
                    sender.sendMessage(new MessageUtils("&cUsage: /nuclearcraft give item <player_name> <item-id>").color());
                    return false;
                }
                Player target = Bukkit.getPlayer(args[2]);
                if (target == null) {
                    sender.sendMessage(INVALID_TARGET);
                    return false;
                }
                ItemStack item = manager.getItemManager().getItemById().get(args[3]);
                if (item == null) {
                    sender.sendMessage(new MessageUtils("&cPlease enter a valid id").color());
                    return false;
                }
                manager.getItemManager().giveItem(target, item);
                sender.sendMessage(new MessageUtils("&eItem given to the specified player").color());
            }
            if (args[1].equalsIgnoreCase("block")) {
                if (!sender.hasPermission("nuclearcraft.give.block")) {
                    sender.sendMessage(NO_PERMISSION);
                    return false;
                }
                if (args.length != 4) {
                    sender.sendMessage(new MessageUtils("&cUsage: /nuclearcraft give block <player_name> <item-id>").color());
                    return false;
                }
                Player target = Bukkit.getPlayer(args[2]);
                if (target == null) {
                    sender.sendMessage(INVALID_TARGET);
                    return false;
                }
                CustomBlock customBlock = manager.getCustomBlockManager().getCustomBlockById().get(args[3]);
                if (customBlock == null) {
                    sender.sendMessage(new MessageUtils("&cPlease enter a valid id").color());
                    return false;
                }
                customBlock.giveCustomBlock(target);
                sender.sendMessage(new MessageUtils("&eBlock given to the specified player").color());
            }
        }
        if (args[0].equalsIgnoreCase("toggle")) {
            if (!sender.hasPermission("nuclearcraft.toggle")) {
                sender.sendMessage(NO_PERMISSION);
                return false;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(new MessageUtils("&cThis command can't be used in console").color());
                return false;
            }
            Player player = (Player) sender;
            if (args.length != 1) {
                sender.sendMessage(new MessageUtils("&cUsage: /nuclearcraft toggle").color());
                return false;
            }
            if (!manager.getRadiationManager().getRadiation().getDisplay().getToggleMode().equals(DisplayToggleMode.COMMAND)) {
                sender.sendMessage(new MessageUtils("&cPlease use the toggle item to toggle radiation display").color());
                return false;
            }
            if (manager.getRadiationManager().getRadiation().getDisplay().getMode().equals(DisplayMode.SINGLE)) {
                manager.getRadiationManager().getRadiation().getDisplay().show(manager.getRadiationManager(), player);
            }else {
                Boolean value = manager.getRadiationManager().getToggleByPlayer().get(player.getUniqueId());
                if (value == null) {
                    value = false;
                }
                if (value) {
                    manager.getRadiationManager().getRadiation().getDisplay().hide(manager.getRadiationManager(), player);
                    sender.sendMessage(new MessageUtils("&eToggled off your radiation display").color());
                } else {
                    manager.getRadiationManager().getRadiation().getDisplay().show(manager.getRadiationManager(), player);
                    sender.sendMessage(new MessageUtils("&eToggled on your radiation display").color());
                }
            }
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("nuclearcraft.reload")) {
                sender.sendMessage(NO_PERMISSION);
                return false;
            }
            manager.reload();
            sender.sendMessage(new MessageUtils("&cNuclearCraft reloaded").color());
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> arguments = new ArrayList<>();
            arguments.add("help");
            arguments.add("radiation");
            arguments.add("give");
            arguments.add("toggle");
            arguments.add("structure");
            arguments.add("reload");
            return arguments;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("radiation")) {
                List<String> arguments = new ArrayList<>();
                arguments.add("set");
                arguments.add("add");
                arguments.add("remove");
                return arguments;
            }
            if (args[0].equalsIgnoreCase("give")) {
                List<String> arguments = new ArrayList<>();
                arguments.add("armor");
                arguments.add("bomb");
                arguments.add("item");
                arguments.add("waste");
                arguments.add("block");
                return arguments;
            }
            if (args[0].equalsIgnoreCase("structure")) {
                List<String> arguments = new ArrayList<>();
                arguments.add("save");
                arguments.add("delete");
                return arguments;
            }
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("structure")) {
                if (args[1].equalsIgnoreCase("delete")) {
                    return new ArrayList<>(manager.getStructureManager().getStructureById().keySet());
                }
            }
        }
        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("give")) {
                if (args[1].equalsIgnoreCase("armor")) {
                    return new ArrayList<>(manager.getArmorManager().getArmorById().keySet());
                }
                if (args[1].equalsIgnoreCase("bomb")) {
                    return new ArrayList<>(manager.getItemBombManager().getItemBombById().keySet());
                }
                if (args[1].equalsIgnoreCase("item")) {
                    return new ArrayList<>(manager.getItemManager().getItemById().keySet());
                }
                if (args[1].equalsIgnoreCase("waste")) {
                    return new ArrayList<>(manager.getWasteManager().getWasteById().keySet());
                }
                if (args[1].equalsIgnoreCase("block")) {
                    return new ArrayList<>(manager.getCustomBlockManager().getCustomBlockById().keySet());
                }
            }
        }
        return null;
    }
}
