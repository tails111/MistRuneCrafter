package MistRuneCrafter.Nodes.CraftingHandlers;

import MistRuneCrafter.Nodes.Globals;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.Tile;


public class ImbueHandler extends Node {
    private final Tile altar = new Tile(2843,4832,0);

    @Override
    public boolean activate(){
        return(Inventory.containsAll(Globals.ITEMS_REQUIRED));
    }

    @Override
    public void execute(){
        if(Calculations.distanceTo(altar)<=5){
            Widgets.get(640,105).click(true);
            Task.sleep(75,125);
            Globals.activeImbueSetting = Settings.get(4);
        }
    }
}