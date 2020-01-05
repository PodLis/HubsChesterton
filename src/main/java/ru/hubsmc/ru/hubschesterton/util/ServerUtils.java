package ru.hubsmc.ru.hubschesterton.util;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.hubsmc.ru.hubschesterton.Permissions;

import java.util.logging.Level;

public class ServerUtils {

    public static void logConsole(Level level, String message) {
        Bukkit.getLogger().log(level, "[HC] " + message);
    }

    public static void logConsole(String message) {
        logConsole(Level.INFO, message);
    }

    public static boolean hasSenderPermission (CommandSender sender, Permissions perm) {
        if (sender instanceof Player) {
            return sender.hasPermission(perm.getPermission());
        }
        return true;
    }

    public static boolean playerIsOnline (String name) {
        return Bukkit.getPlayer(name) != null;
    }

}
