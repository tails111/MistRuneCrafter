package MistRuneCrafter.Nodes.CraftingHandlers;

import MistRuneCrafter.Nodes.BankHandlers.BankHandler;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.Tile;


public class ImbueHandler extends Node {
    Tile altar = new Tile(2843,4832,0);

    @Override
    public boolean activate(){
        return(Inventory.containsAll(BankHandler.ITEMS_REQUIRED) && Settings.get(4)==1282);
    }

    @Override
    public void execute(){
        if(Calculations.distanceTo(altar)<=4){
            Widgets.get(640,105).click(true);
            Task.sleep(75,125);
        }
    }
}