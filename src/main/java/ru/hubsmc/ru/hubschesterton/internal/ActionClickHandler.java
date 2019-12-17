package ru.hubsmc.ru.hubschesterton.internal;

import org.bukkit.entity.Player;
import ru.hubsmc.ru.hubschesterton.internal.action.ItemAction;

import java.util.List;

public class ActionClickHandler implements ClickHandler {

    private List<ItemAction> actions;

    public ActionClickHandler(List<ItemAction> actions) {
        this.actions = actions;
    }

    @Override
    public boolean onClick(Player player) {
        if (actions != null) {
            for (ItemAction itemAction : actions) {
                itemAction.execute(player);
            }
        }
        return false;
    }
}
