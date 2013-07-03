package MistRuneCrafter.Nodes.WalkingHandlers;

import MistRuneCrafter.MistRuneCrafter;
import MistRuneCrafter.Nodes.BankHandlers.BankHandler;
import MistRuneCrafter.Nodes.Globals;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

public class WalkToAltar  extends Node {

    private final Tile[] toAltar = new Tile[] {
            new Tile(3185,3433,0), new Tile(3178, 3429, 0), new Tile(3167, 3424, 0), new Tile(3160, 3419, 0),
            new Tile(3151,3416,0), new Tile(3142,3413,0), new Tile(3136,3408,0), new Tile(3129,3405,0)};

    private final Tile altarEntrance = new Tile(3129,3405,0);
    private final Tile altar = new Tile(2843,4832,0);

    private final int AIR_RUINS = 2452;   //Enter
    private final int AIR_ALTAR = 2478;  // Craft-rune

    @Override
    public boolean activate(){
        SceneObject runeCraftAltar = SceneEntities.getNearest(AIR_ALTAR);
        return (Calculations.distanceTo(altar)>=3 && Inventory.containsAll(Globals.ITEMS_REQUIRED) && !Bank.isOpen()
            && runeCraftAltar == null);
    }

    @Override
    public void execute(){
       MistRuneCrafter.status="Walking to Altar.";
       Walking.newTilePath(toAltar).traverse();
        SceneObject toAltarObj = SceneEntities.getNearest(AIR_RUINS);
        if(Inventory.contains(Globals.ID_BINDING_NECKLACE)){
            Inventory.getItem(Globals.ID_BINDING_NECKLACE).getWidgetChild().interact("Wear");
            BankHandler.invChangeSleep();
        }
       if(Calculations.distanceTo(altarEntrance)<=4){

           Timer timeCheck = new Timer(Random.nextInt(10000, 15000));
           if(toAltarObj != null){
               SceneObject runeCraftAltar = SceneEntities.getNearest(AIR_ALTAR);
               MistRuneCrafter.interacting=toAltarObj;
               Camera.turnTo(toAltarObj);
               MistRuneCrafter.status="Clicking altar";
               toAltarObj.interact("Enter");
                do{
                    Task.sleep(60,85);
                    MistRuneCrafter.status="Waiting to enter altar for: " + (timeCheck.getRemaining()/1000);
                }while(runeCraftAltar == null && timeCheck.isRunning() && Players.getLocal().isMoving());
               if(runeCraftAltar!=null){
                   Camera.turnTo(runeCraftAltar);
               }
           }
           MistRuneCrafter.status="Done walking to Altar.";
       }
    }
}
