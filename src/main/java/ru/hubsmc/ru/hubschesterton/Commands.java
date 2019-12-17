package ru.hubsmc.ru.hubschesterton;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import ru.hubsmc.ru.hubschesterton.internal.MenuUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import static ru.hubsmc.ru.hubschesterton.PluginUtils.reloadConfig;
import static ru.hubsmc.ru.hubschesterton.util.MessageUtils.*;
import static ru.hubsmc.ru.hubschesterton.PluginUtils.getVersion;
import static ru.hubsmc.ru.hubschesterton.util.ServerUtils.*;

public class Commands implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            if (command.getName().equalsIgnoreCase("chesterton")) {
                if (args.length == 0) {
                    sendPrefixMessage(sender, "&b&l Информация о плагине HubsChesterton:");
                    sendMessage(sender, "&5Версия:&a " + getVersion());
                    sendMessage(sender, "&5Автор и создатель плагина:&a Rosenboum");
                    return true;
                }
                switch (args[0].toLowerCase()) {
                    case "help":
                        if (!hasSenderPermission(sender, Permissions.HELP)) {
                            sendNoPermMessage(sender, args[0]);
                            return true;
                        }
                        sendPrefixMessage(sender, "Доступные команды:");
                        sendMessage(sender, "/" + label + " reload&7 - Перезагружает плагин.");
                        sendMessage(sender, "/" + label + " open <menu> [player]&7 - Открывает указанное меню тебе или игроку.");
                        return true;

                    case "reload":
                        if (!hasSenderPermission(sender, Permissions.RELOAD)) {
                            sendNoPermMessage(sender, args[0]);
                            return true;
                        }
                        reloadConfig();
                        sendPrefixMessage(sender, "Плагин успешно перезагружен");
                        return true;

                    case "open":
                        if (!hasSenderPermission(sender, Permissions.OPEN)) {
                            sendNoPermMessage(sender, args[0]);
                            return true;
                        }

                        if (args.length < 2) {
                            sendNotEnoughArgsMessage(sender, args[0]);
                            return true;
                        }

                        if (!MenuUtils.menuExists(args[1])) {
                            sendPrefixMessage(sender, "Данного меню не существует!");
                            return true;
                        }

                        if (args.length == 2) {
                            if (!(sender instanceof Player)) {
                                sendMustBePlayerMessage(sender);
                                return true;
                            }
                            MenuUtils.openMenu((Player) sender, args[1]);
                            return true;
                        }

                        if (!playerIsOnline(args[2])) {
                            sendPlayerMustBeOnlineMessage(sender);
                            return true;
                        }
                        MenuUtils.openMenu(Bukkit.getPlayer(args[2]), args[1]);
                        return true;

                    default:
                        sendUnknownCommandMessage(sender, args[0]);
                        return true;

                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            logConsole(Level.WARNING, "Some troubles with commands.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completionList = new ArrayList<>();
        String partOfCommand;
        List<String> cmds = new ArrayList<>();

        switch (args.length) {
            case 1:
                cmds = new ArrayList<>(getCmds(sender));
                partOfCommand = args[0];

                StringUtil.copyPartialMatches(partOfCommand, cmds, completionList);
                Collections.sort(completionList);
                return completionList;

            case 2:
                if (args[0].equalsIgnoreCase("open")) {
                    cmds.addAll(MenuUtils.getAllMenuSet());
                    partOfCommand = args[1];
                    StringUtil.copyPartialMatches(partOfCommand, cmds, completionList);
                    Collections.sort(completionList);
                    return completionList;
                } else {
                    return null;
                }

            case 3:
                if (args[0].equalsIgnoreCase("open")) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        cmds.add(player.getName());
                    }
                    partOfCommand = args[2];
                    StringUtil.copyPartialMatches(partOfCommand, cmds, completionList);
                    Collections.sort(completionList);
                    return completionList;
                } else {
                    return null;
                }

            default:
                return null;
        }

    }

    private List<String> getCmds(CommandSender sender) {
        List<String> c = new ArrayList<>();
        for (String cmd : Arrays.asList("help", "reload", "open")) {
            if (!sender.hasPermission("chesterton." + cmd)) {
                continue;
            }
            c.add(cmd);
        }
        return c;
    }

}
