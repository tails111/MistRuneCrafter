package MistRuneCrafter.Nodes.CraftingHandlers;

import MistRuneCrafter.MistRuneCrafter;
import MistRuneCrafter.Nodes.BankHandlers.BankHandler;
import MistRuneCrafter.Nodes.BankHandlers.PouchHandlers;
import MistRuneCrafter.Nodes.Globals;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

public class CraftingHandler extends Node {

    private EmptyPouchHandler emptyPouches = new EmptyPouchHandler();
    private ImbueHandler castImbue = new ImbueHandler();

    private final int AIR_ALTAR = 2478;

    private final Tile altar = new Tile(2843,4832,0);

    private void sleepGameTick(){
        Task.sleep(650,850);
    }

    public void finishCrafting(SceneObject altar){
        Timer timeCheck = new Timer(3000);
        do{
            if(Inventory.contains(Globals.ID_PURE_ESS) && !Inventory.contains(Globals.ID_RUNE_ASTRAL)){
                altar.interact("Craft-rune");
                Timer timeCheck2 = new Timer(2000);
                do{
                    if(BankHandler.invChangeSleep()){break;}
                }while(timeCheck2.isRunning());
            }
        }while(Inventory.contains(Globals.ID_PURE_ESS) && timeCheck.isRunning());
    }

    @Override
    public boolean activate(){
        SceneObject runeCraftAltar = SceneEntities.getNearest(AIR_ALTAR);
        return (runeCraftAltar != null && Inventory.containsAll(Globals.ID_PURE_ESS, Globals.ID_RUNE_WATER));
    }

    @Override
    public void execute(){;
        Timer timeCheck = new Timer(12000);
        SceneObject runeCraftAltar = SceneEntities.getNearest(AIR_ALTAR);
        MistRuneCrafter.status="Attempting to Craft Runes.";
        do{
            if(Settings.get(4)==1282){
                MistRuneCrafter.status="Navigating to Altar";
                Camera.turnTo(runeCraftAltar);
                Walking.walk(runeCraftAltar);
            }
            if(castImbue.activate()){
                castImbue.execute();
            }
            if(Settings.get(4)>1282 && Inventory.contains(Globals.ID_PURE_ESS)){
                Camera.turnTo(runeCraftAltar);
                MistRuneCrafter.status="Crafting Mist Runes.";
                MistRuneCrafter.interacting = Inventory.getItem(Globals.ID_RUNE_WATER).getWidgetChild();
                Inventory.getItem(Globals.ID_RUNE_WATER).getWidgetChild().interact("Use");
                MistRuneCrafter.interacting = runeCraftAltar;
                runeCraftAltar.click(true);
                BankHandler.invChangeSleep();
            }
            emptyPouches.execute();
        }while(Settings.get(4)>=1282 && timeCheck.isRunning() && Inventory.contains(Globals.ID_PURE_ESS) && PouchHandlers.allEmpty());

        sleepGameTick();
        MistRuneCrafter.postedProfit= MistRuneCrafter.postedProfit + (Inventory.getItem(Globals.ID_RUNE_MIST).getStackSize()*Globals.RUNE_MIST_PRICE);
        MistRuneCrafter.postedCrafts= MistRuneCrafter.postedCrafts + Inventory.getItem(Globals.ID_RUNE_MIST).getStackSize();
        finishCrafting(runeCraftAltar);
    }
}
