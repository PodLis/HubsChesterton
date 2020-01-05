package ru.hubsmc.ru.hubschesterton.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.hubsmc.ru.hubschesterton.Permissions;
import ru.hubsmc.ru.hubschesterton.internal.parser.MenuParser;

import java.util.logging.Level;

import static ru.hubsmc.ru.hubschesterton.util.MessageUtils.*;
import static ru.hubsmc.ru.hubschesterton.util.ServerUtils.logConsole;

public class ShopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            if (command.getName().equalsIgnoreCase("shop")) {

                if (!(sender instanceof Player)) {
                    sendMustBePlayerMessage(sender);
                    return true;
                }

                Player player = (Player) sender;

                if (!player.hasPermission(Permissions.SHOP.getPermission())) {
                    sendNoPermMessage(player, label);
                    return true;
                }

                MenuParser.parseChestMenu("shop").open(player);

                return true;

            }
        } catch (Throwable e) {
            e.printStackTrace();
            logConsole(Level.WARNING, "Some troubles with shop.");
        }
        return true;
    }

}
