package ru.hubsmc.ru.hubschesterton.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.hubsmc.ru.hubschesterton.Permissions;
import ru.hubsmc.ru.hubschesterton.PluginUtils;
import ru.hubsmc.ru.hubschesterton.internal.special.SellBucket;

import java.util.logging.Level;

import static ru.hubsmc.ru.hubschesterton.util.MessageUtils.sendMustBePlayerMessage;
import static ru.hubsmc.ru.hubschesterton.util.MessageUtils.sendNoPermMessage;
import static ru.hubsmc.ru.hubschesterton.util.ServerUtils.logConsole;

public class SellCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            if (command.getName().equalsIgnoreCase("sell")) {

                if (!(sender instanceof Player)) {
                    sendMustBePlayerMessage(sender);
                    return true;
                }

                Player player = (Player) sender;

                if (!player.hasPermission(Permissions.SELL.getPermission())) {
                    sendNoPermMessage(player, label);
                    return true;
                }

                (new SellBucket(PluginUtils.getConfigurationString("inscriptions.sell-bucket-title"))).open(player);

                return true;

            }
        } catch (Throwable e) {
            e.printStackTrace();
            logConsole(Level.WARNING, "Some troubles with sell.");
        }
        return true;
    }

}
