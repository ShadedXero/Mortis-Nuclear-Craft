package me.none030.mortisnuclearcraft.managers;

import me.none030.mortisnuclearcraft.armors.RadiationArmor;
import me.none030.mortisnuclearcraft.bombs.ItemBomb;
import me.none030.mortisnuclearcraft.structures.Structure;
import me.none030.mortisnuclearcraft.utils.radiation.DisplayMode;
import me.none030.mortisnuclearcraft.utils.radiation.DisplayToggleMode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NuclearCraftCommand implements TabExecutor {

    private final NuclearCraftManager manager;

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
                sender.sendMessage(NuclearCraftMessages.NO_PERMISSION);
                return false;
            }
            sender.sendMessage(NuclearCraftMessages.HELP);
        }
        if (args[0].equalsIgnoreCase("structure")) {
            if (!sender.hasPermission("nuclearcraft.structure")) {
                sender.sendMessage(NuclearCraftMessages.NO_PERMISSION);
                return false;
            }
            if (args.length == 1) {
                sender.sendMessage(NuclearCraftMessages.STRUCTURE_USAGE);
                return false;
            }
            if (args[1].equalsIgnoreCase("save")) {
                if (!sender.hasPermission("nuclearcraft.structure.save")) {
                    sender.sendMessage(NuclearCraftMessages.NO_PERMISSION);
                    return false;
                }
                if (args.length != 8) {
                    sender.sendMessage(NuclearCraftMessages.STRUCTURE_SAVE_USAGE);
                    return false;
                }
                String structureId = args[2];
                World world = Bukkit.getWorld(args[3]);
                if (world == null) {
                    sender.sendMessage(NuclearCraftMessages.INVALID_WORLD);
                    return false;
                }
                double x;
                double y;
                double z;
                try {
                    x = Double.parseDouble(args[4]);
                    y = Double.parseDouble(args[5]);
                    z = Double.parseDouble(args[6]);
                }catch (NumberFormatException exp) {
                    sender.sendMessage(NuclearCraftMessages.INVALID_NUMBER);
                    return false;
                }
                Location center = new Location(world, x, y, z);
                Material core;
                try {
                    core = Material.valueOf(args[7]);
                }catch (IllegalArgumentException exp) {
                    sender.sendMessage(NuclearCraftMessages.INVALID_MATERIAL);
                    return false;
                }
                Structure structure = manager.getStructureManager().getStructure(center, core, structureId);
                manager.getConfigManager().getStructuresConfig().saveStructures(structure);
                manager.getStructureManager().saveStructure(structure);
                sender.sendMessage(NuclearCraftMessages.STRUCTURE_SAVE);
            }
            if (args[1].equalsIgnoreCase("delete")) {
                if (!sender.hasPermission("nuclearcraft.structure.delete")) {
                    sender.sendMessage(NuclearCraftMessages.NO_PERMISSION);
                    return false;
                }
                if (args.length != 3) {
                    sender.sendMessage(NuclearCraftMessages.STRUCTURE_DELETE_USAGE);
                    return false;
                }
                String structureId = args[2];
                manager.getConfigManager().getStructuresConfig().deleteStructure(structureId);
                manager.getStructureManager().deleteStructure(structureId);
                sender.sendMessage(NuclearCraftMessages.STRUCTURE_DELETE);
            }
        }
        if (args[0].equalsIgnoreCase("radiation")) {
            if (!sender.hasPermission("nuclearcraft.radiation")) {
                sender.sendMessage(NuclearCraftMessages.NO_PERMISSION);
                return false;
            }
            if (args.length == 1) {
                sender.sendMessage(NuclearCraftMessages.RADIATION_USAGE);
                return false;
            }
            if (args[1].equalsIgnoreCase("set")) {
                if (!sender.hasPermission("nuclearcraft.radiation.set")) {
                    sender.sendMessage(NuclearCraftMessages.NO_PERMISSION);
                    return false;
                }
                if (args.length != 4) {
                    sender.sendMessage(NuclearCraftMessages.RADIATION_SET_USAGE);
                    return false;
                }
                Player target = Bukkit.getPlayer(args[2]);
                if (target == null) {
                    sender.sendMessage(NuclearCraftMessages.INVALID_TARGET);
                    return false;
                }
                double radiation;
                try {
                    radiation = Double.parseDouble(args[3]);
                }catch (NumberFormatException exp) {
                    sender.sendMessage(NuclearCraftMessages.INVALID_NUMBER);
                    return false;
                }
                manager.getRadiationManager().setRadiation(target, radiation);
                sender.sendMessage(NuclearCraftMessages.RADIATION_CHANGED);
            }
            if (args[1].equalsIgnoreCase("add")) {
                if (!sender.hasPermission("nuclearcraft.radiation.add")) {
                    sender.sendMessage(NuclearCraftMessages.NO_PERMISSION);
                    return false;
                }
                if (args.length != 4) {
                    sender.sendMessage(NuclearCraftMessages.RADIATION_ADD_USAGE);
                    return false;
                }
                Player target = Bukkit.getPlayer(args[2]);
                if (target == null) {
                    sender.sendMessage(NuclearCraftMessages.INVALID_TARGET);
                    return false;
                }
                double radiation;
                try {
                    radiation = Double.parseDouble(args[3]);
                }catch (NumberFormatException exp) {
                    sender.sendMessage(NuclearCraftMessages.INVALID_NUMBER);
                    return false;
                }
                manager.getRadiationManager().addRadiation(target, radiation);
                sender.sendMessage(NuclearCraftMessages.RADIATION_CHANGED);
            }
            if (args[1].equalsIgnoreCase("remove")) {
                if (!sender.hasPermission("nuclearcraft.radiation.remove")) {
                    sender.sendMessage(NuclearCraftMessages.NO_PERMISSION);
                    return false;
                }
                if (args.length != 4) {
                    sender.sendMessage(NuclearCraftMessages.RADIATION_REMOVE_USAGE);
                    return false;
                }
                Player target = Bukkit.getPlayer(args[2]);
                if (target == null) {
                    sender.sendMessage(NuclearCraftMessages.INVALID_TARGET);
                    return false;
                }
                double radiation;
                try {
                    radiation = Double.parseDouble(args[3]);
                }catch (NumberFormatException exp) {
                    sender.sendMessage(NuclearCraftMessages.INVALID_NUMBER);
                    return false;
                }
                manager.getRadiationManager().removeRadiation(target, radiation);
                sender.sendMessage(NuclearCraftMessages.RADIATION_CHANGED);
            }
        }
        if (args[0].equalsIgnoreCase("give")) {
            if (!sender.hasPermission("nuclearcraft.give")) {
                sender.sendMessage(NuclearCraftMessages.NO_PERMISSION);
                return false;
            }
            if (args.length == 1) {
                sender.sendMessage(NuclearCraftMessages.GIVE_USAGE);
                return false;
            }
            if (args[1].equalsIgnoreCase("armor")) {
                if (!sender.hasPermission("nuclearcraft.give.armor")) {
                    sender.sendMessage(NuclearCraftMessages.NO_PERMISSION);
                    return false;
                }
                if (args.length != 4) {
                    sender.sendMessage(NuclearCraftMessages.GIVE_ARMOR_USAGE);
                    return false;
                }
                Player target = Bukkit.getPlayer(args[2]);
                if (target == null) {
                    sender.sendMessage(NuclearCraftMessages.INVALID_TARGET);
                    return false;
                }
                RadiationArmor armor = manager.getArmorManager().getArmorById().get(args[3]);
                if (armor == null) {
                    sender.sendMessage(NuclearCraftMessages.GIVE_ARMOR_INVALID);
                    return false;
                }
                armor.giveArmor(target);
                sender.sendMessage(NuclearCraftMessages.ITEM_GIVEN);
            }
            if (args[1].equalsIgnoreCase("bomb")) {
                if (!sender.hasPermission("nuclearcraft.give.bomb")) {
                    sender.sendMessage(NuclearCraftMessages.NO_PERMISSION);
                    return false;
                }
                if (args.length != 4) {
                    sender.sendMessage(NuclearCraftMessages.GIVE_BOMB_USAGE);
                    return false;
                }
                Player target = Bukkit.getPlayer(args[2]);
                if (target == null) {
                    sender.sendMessage(NuclearCraftMessages.INVALID_TARGET);
                    return false;
                }
                ItemBomb bomb = manager.getBombManager().getItemBombById().get(args[3]);
                if (bomb == null) {
                    sender.sendMessage(NuclearCraftMessages.GIVE_BOMB_INVALID);
                    return false;
                }
                bomb.giveBomb(target);
                sender.sendMessage(NuclearCraftMessages.ITEM_GIVEN);
            }
            if (args[1].equalsIgnoreCase("item")) {
                if (!sender.hasPermission("nuclearcraft.give.item")) {
                    sender.sendMessage(NuclearCraftMessages.NO_PERMISSION);
                    return false;
                }
                if (args.length != 4) {
                    sender.sendMessage(NuclearCraftMessages.GIVE_ITEM_USAGE);
                    return false;
                }
                Player target = Bukkit.getPlayer(args[2]);
                if (target == null) {
                    sender.sendMessage(NuclearCraftMessages.INVALID_TARGET);
                    return false;
                }
                ItemStack item = manager.getItemManager().getItemById().get(args[3]);
                if (item == null) {
                    sender.sendMessage(NuclearCraftMessages.GIVE_ITEM_INVALID);
                    return false;
                }
                manager.getItemManager().giveItem(target, item);
                sender.sendMessage(NuclearCraftMessages.ITEM_GIVEN);
            }
        }
        if (args[0].equalsIgnoreCase("toggle")) {
            if (!sender.hasPermission("nuclearcraft.toggle")) {
                sender.sendMessage(NuclearCraftMessages.NO_PERMISSION);
                return false;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(NuclearCraftMessages.NO_CONSOLE_USAGE);
                return false;
            }
            Player player = (Player) sender;
            if (args.length != 1) {
                sender.sendMessage(NuclearCraftMessages.TOGGLE_USAGE);
                return false;
            }
            if (!manager.getRadiationManager().getRadiation().getDisplay().getToggleMode().equals(DisplayToggleMode.COMMAND)) {
                sender.sendMessage(NuclearCraftMessages.TOGGLE_MODE_ITEM);
                return false;
            }
            if (manager.getRadiationManager().getRadiation().getDisplay().getMode().equals(DisplayMode.SINGLE)) {
                manager.getRadiationManager().getRadiation().getDisplay().show(player);
            }else {
                Boolean value = manager.getRadiationManager().getToggleByPlayer().get(player.getUniqueId());
                if (value == null) {
                    value = false;
                }
                if (value) {
                    manager.getRadiationManager().getRadiation().getDisplay().hide(player);
                    sender.sendMessage(NuclearCraftMessages.TOGGLE_OFF);
                } else {
                    manager.getRadiationManager().getRadiation().getDisplay().show(player);
                    sender.sendMessage(NuclearCraftMessages.TOGGLE_ON);
                }
            }
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("nuclearcraft.reload")) {
                sender.sendMessage(NuclearCraftMessages.NO_PERMISSION);
                return false;
            }
            manager.reload();
            sender.sendMessage(NuclearCraftMessages.RELOAD);
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
                    return new ArrayList<>(manager.getBombManager().getItemBombById().keySet());
                }
                if (args[1].equalsIgnoreCase("item")) {
                    return new ArrayList<>(manager.getItemManager().getItemById().keySet());
                }
            }
        }
        return null;
    }
}
