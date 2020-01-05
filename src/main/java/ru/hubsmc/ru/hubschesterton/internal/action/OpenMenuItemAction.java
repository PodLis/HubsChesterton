package ru.hubsmc.ru.hubschesterton.internal.action;

import org.bukkit.entity.Player;
import ru.hubsmc.ru.hubschesterton.internal.menu.ChestertonMenu;
import ru.hubsmc.ru.hubschesterton.internal.parser.MenuParser;

public class OpenMenuItemAction extends ItemAction {

    private ChestertonMenu thisMenu, nextMenu;
    private String nextMenuName;

    public OpenMenuItemAction(ChestertonMenu thisMenu, ChestertonMenu nextMenu) {
        this.thisMenu = thisMenu;
        this.nextMenu = nextMenu;
    }

    public OpenMenuItemAction(ChestertonMenu thisMenu, String nextMenuName) {
        this.thisMenu = thisMenu;
        this.nextMenuName = nextMenuName;
        this.nextMenu = null;
    }

    @Override
    public void execute(Player player) {
        if (nextMenu == null) {
            nextMenu = MenuParser.parseChestMenu(nextMenuName);
        }
        nextMenu.setParentMenu(thisMenu);
        nextMenu.open(player);
    }

}
