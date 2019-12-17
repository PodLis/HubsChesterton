package ru.hubsmc.ru.hubschesterton.util;

import org.bukkit.command.CommandSender;
import ru.hubsmc.ru.hubschesterton.HubsChesterton;

public class MessageUtils {

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(StringUtils.replaceColor(message));
    }

    public static void sendPrefixMessage(CommandSender sender, String message) {
        sendMessage(sender, HubsChesterton.CHAT_PREFIX + message);
    }

    public static void sendNoPermMessage(CommandSender sender, String subject) {
        sendPrefixMessage(sender, "У вас нет привилегий, чтобы использовать " + subject + "!");
    }

    public static void sendUnknownCommandMessage(CommandSender sender, String subject) {
        sendPrefixMessage(sender, "Неизвестная команда " + subject);
    }

    public static void sendNotEnoughArgsMessage(CommandSender sender, String subject) {
        sendPrefixMessage(sender, "Недостаточно аргументов, чтобы использовать " + subject + "!");
    }

    public static void sendMustBePlayerMessage(CommandSender sender) {
        sendPrefixMessage(sender, "Исполнитель должен быть игроком");
    }

    public static void sendPlayerMustBeOnlineMessage(CommandSender sender) {
        sendPrefixMessage(sender, "Игрок должен быть онлайн!");
    }

}
