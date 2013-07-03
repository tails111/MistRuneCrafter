package MistRuneCrafter.Nodes.WalkingHandlers;

import MistRuneCrafter.MistRuneCrafter;
import MistRuneCrafter.Nodes.BankHandlers.BankHandler;
import MistRuneCrafter.Nodes.BankHandlers.PouchHandlers;
import MistRuneCrafter.Nodes.CraftingHandlers.EmptyPouchHandler;
import MistRuneCrafter.Nodes.Globals;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

public class WalkToBank extends Node {
    private final Tile[] toBank = new Tile[] {
            new Tile(3129,3405,0), new Tile(3136, 3408, 0), new Tile(3142, 3413, 0), new Tile(3151, 3416, 0),
            new Tile(3160,3419,0), new Tile(3167,3424,0), new Tile(3178,3429,0), new Tile(3185,3433,0)};

    private final int AIR_EXIT = 2465;
    private final Tile altarExit = new Tile(2841,4829,0);
    private final Tile bankTile = new Tile(3186,3438,0);

    @Override
    public boolean activate(){
        return (Inventory.contains(4695) && !Inventory.contains(Globals.ID_PURE_ESS)
                && Calculations.distanceTo(bankTile)>=7 || PouchHandlers.Pouch.allEmpty());
    }

    @Override
    public void execute(){
        SceneObject ruinExit = SceneEntities.getNearest(AIR_EXIT);
        if(Calculations.distanceTo(altarExit)<=8){
            if(ruinExit != null){
                MistRuneCrafter.interacting=ruinExit;
                Camera.turnTo(ruinExit);
                MistRuneCrafter.status="Clicking portal";
                ruinExit.interact("Enter");
                Timer timeCheck = new Timer(3000);
                do{
                    Task.sleep(20, 45);
                }while(Calculations.distanceTo(altarExit)<=8 && timeCheck.isRunning());
            }
        }
        MistRuneCrafter.status="Walking to bank.";
        Walking.newTilePath(toBank).traverse();
    }

}
