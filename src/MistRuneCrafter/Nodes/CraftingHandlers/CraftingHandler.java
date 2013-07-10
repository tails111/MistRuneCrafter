package MistRuneCrafter.Nodes.CraftingHandlers;

import MistRuneCrafter.MistRuneCrafter;
import MistRuneCrafter.Nodes.BankHandlers.BankHandler;
import MistRuneCrafter.Nodes.BankHandlers.PouchHandlers;
import MistRuneCrafter.Nodes.Globals;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.node.SceneObject;

public class CraftingHandler extends Node {

    private EmptyPouchHandler emptyPouches = new EmptyPouchHandler();
    private ImbueHandler castImbue = new ImbueHandler();

    private final int AIR_ALTAR = 2478;

    SceneObject runeCraftAltar;



    private void sleepGameTick(){
        Task.sleep(650,850);
    }

    public boolean finishCrafting(SceneObject altar){
        Timer timeCheck = new Timer(3000);
        System.out.println("Finishing Rune Crafting.");
        do{
            if(Inventory.contains(Globals.ID_PURE_ESS) && !Inventory.contains(Globals.ID_RUNE_ASTRAL)){
                altar.interact("Craft-rune");
                Timer timeCheck2 = new Timer(2000);
                do{
                    if(BankHandler.invChangeSleep()){break;}
                }while(timeCheck2.isRunning());
            }
            if(emptyPouches.activate()){
                emptyPouches.execute();
                return true;
            }
        }while(Inventory.contains(Globals.ID_PURE_ESS) && timeCheck.isRunning());
        return false;
    }

    @Override
    public boolean activate(){
        runeCraftAltar = SceneEntities.getNearest(AIR_ALTAR);
        return (runeCraftAltar != null && Inventory.containsAll(Globals.ID_PURE_ESS, Globals.ID_RUNE_WATER) && PouchHandlers.Pouch.allFull()
                && !Inventory.contains(Globals.ID_RUNE_AIR));
    }

    @Override
    public void execute(){

        System.out.println("Executing Craft Runes.");
        Timer timeCheck = new Timer(15000);
        MistRuneCrafter.status="Attempting to Craft Runes.";
        do{
            System.out.println("Settings 4 = " + Globals.activeImbueSetting + " Idle = " + Globals.idleImbueSetting);
            if(Globals.activeImbueSetting!=Globals.idleImbueSetting){
                MistRuneCrafter.status="Navigating to Altar";
                Walking.walk(runeCraftAltar);
                Camera.turnTo(runeCraftAltar);
            }
            if(castImbue.activate()){
                castImbue.execute();
            }
            if(Globals.activeImbueSetting>Globals.idleImbueSetting && Inventory.contains(Globals.ID_PURE_ESS)){
                Camera.turnTo(runeCraftAltar);
                MistRuneCrafter.status="Crafting Mist Runes.";
                MistRuneCrafter.interacting = Inventory.getItem(Globals.ID_RUNE_WATER).getWidgetChild();
                Inventory.getItem(Globals.ID_RUNE_WATER).getWidgetChild().interact("Use");
                MistRuneCrafter.interacting = runeCraftAltar;
                runeCraftAltar = SceneEntities.getNearest(AIR_ALTAR);
                runeCraftAltar.click(true);
                BankHandler.invChangeSleep();
            }
            if(emptyPouches.activate()){emptyPouches.execute();}
            sleepGameTick();
        }while(Globals.activeImbueSetting>Globals.idleImbueSetting && timeCheck.isRunning() && Inventory.contains(Globals.ID_PURE_ESS));

        if(emptyPouches.activate()){emptyPouches.execute();}

        Timer timeCheck2 = new Timer(6000);
        while(Inventory.contains(Globals.ID_PURE_ESS) && !Inventory.contains(Globals.ID_RUNE_ASTRAL) && timeCheck2.isRunning()){
            if(Inventory.contains(Globals.ID_RUNE_AIR) && emptyPouches.activate()){
                emptyPouches.execute();
            }
            sleepGameTick();
            if(!Inventory.contains(Globals.ID_RUNE_ASTRAL) && !Inventory.contains(Globals.ID_RUNE_AIR)){
                if(finishCrafting(runeCraftAltar)){
                    sleepGameTick();
                }
            }
        }
        System.out.println("At the end of CraftingHandler.");
        Task.sleep(1000);
        MistRuneCrafter.postedProfit= MistRuneCrafter.postedProfit + (Inventory.getItem(Globals.ID_RUNE_MIST).getStackSize()*Globals.RUNE_MIST_PRICE);
        MistRuneCrafter.postedCrafts= MistRuneCrafter.postedCrafts + Inventory.getItem(Globals.ID_RUNE_MIST).getStackSize();
    }
}
