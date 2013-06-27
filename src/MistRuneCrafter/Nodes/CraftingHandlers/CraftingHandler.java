package MistRuneCrafter.Nodes.CraftingHandlers;

import MistRuneCrafter.Nodes.BankHandlers.BankHandler;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

public class CraftingHandler extends Node {

    EmptyPouchHandler emptyPouches = new EmptyPouchHandler();
    ImbueHandler castImbue = new ImbueHandler();

    private final int AIR_ALTAR = 2478;

    Tile altar = new Tile(2843,4832,0);

    @Override
    public boolean activate(){
        return (Calculations.distanceTo(altar)<=5 && Inventory.containsAll(BankHandler.ID_PURE_ESS, BankHandler.ID_RUNE_WATER) && Settings.get(3215) >= 1);
    }

    @Override
    public void execute(){
        SceneObject airAltar = SceneEntities.getNearest(AIR_ALTAR);
        if(Settings.get(4)==1282){
            Camera.turnTo(airAltar);
            Walking.walk(airAltar);

        }
        if(castImbue.activate()){
            castImbue.execute();
        }
        if(Settings.get(4)>=1282){
            Inventory.getItem(BankHandler.ID_RUNE_WATER).getWidgetChild().interact("Use");
            airAltar.click(true);
            Task.sleep(1000,1250);
            if(emptyPouches.activate()){
                emptyPouches.execute();
            }
        }
    }
}
