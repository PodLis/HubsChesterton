package ru.hubsmc.ru.hubschesterton.internal.action;

import org.bukkit.entity.Player;
import ru.hubsmc.ru.hubschesterton.internal.menu.ShopCategoryMenu;
import ru.hubsmc.ru.hubschesterton.internal.parser.MenuParser;

public class PrevItemAction extends ItemAction {

    ShopCategoryMenu menu;

    public PrevItemAction(ShopCategoryMenu menu) {
        this.menu = menu;
    }

    @Override
    public void execute(Player player) {
        ShopCategoryMenu prevMenu = menu.getPrevMenu();
        if (prevMenu != null) {
            prevMenu.setParentMenu(menu.getParentMenu());
            prevMenu.setNextMenu(menu);
            prevMenu.setPrevMenu(MenuParser.parseCategoryMenu(menu.getCategoryName(), menu.getOriginalTitle(), menu.getPage() - 2, menu.getMaxPage()));
            prevMenu.open(player);
        }
    }

}
