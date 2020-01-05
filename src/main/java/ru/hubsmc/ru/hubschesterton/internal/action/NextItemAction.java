package ru.hubsmc.ru.hubschesterton.internal.action;

import org.bukkit.entity.Player;
import ru.hubsmc.ru.hubschesterton.internal.menu.ShopCategoryMenu;
import ru.hubsmc.ru.hubschesterton.internal.parser.MenuParser;

public class NextItemAction extends ItemAction {

    ShopCategoryMenu menu;

    public NextItemAction(ShopCategoryMenu menu) {
        this.menu = menu;
    }

    @Override
    public void execute(Player player) {
        ShopCategoryMenu nextMenu = menu.getNextMenu();
        if (nextMenu != null) {
            nextMenu.setParentMenu(menu.getParentMenu());
            nextMenu.setPrevMenu(menu);
            nextMenu.setNextMenu(MenuParser.parseCategoryMenu(menu.getCategoryName(), menu.getOriginalTitle(), menu.getPage() + 2, menu.getMaxPage()));
            nextMenu.open(player);
        }
    }

}
