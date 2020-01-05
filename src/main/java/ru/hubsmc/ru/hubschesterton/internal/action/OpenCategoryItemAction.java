package ru.hubsmc.ru.hubschesterton.internal.action;

import org.bukkit.entity.Player;
import ru.hubsmc.ru.hubschesterton.PluginUtils;
import ru.hubsmc.ru.hubschesterton.internal.menu.ChestertonMenu;
import ru.hubsmc.ru.hubschesterton.internal.menu.ShopCategoryMenu;
import ru.hubsmc.ru.hubschesterton.internal.parser.MenuParser;
import ru.hubsmc.ru.hubschesterton.util.MessageUtils;

public class OpenCategoryItemAction extends ItemAction {

    private ChestertonMenu thisMenu;
    private String childMenuName;
    private String title;

    public OpenCategoryItemAction(ChestertonMenu thisMenu, String childMenuName) {
        this.thisMenu = thisMenu;
        this.childMenuName = childMenuName;
        this.title = PluginUtils.getConfigurationString("inscriptions.title-not-found-title");
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void execute(Player player) {
        ShopCategoryMenu childMenu = MenuParser.parseCategoryMenu(childMenuName, title);
        if (childMenu == null) {
            MessageUtils.sendPrefixMessage(player, "Данного меню не существует!");
            return;
        }
        childMenu.setParentMenu(thisMenu);
        childMenu.setNextMenu(MenuParser.parseCategoryMenu(childMenuName, title, 2, childMenu.getMaxPage()));
        childMenu.open(player);
    }

}
