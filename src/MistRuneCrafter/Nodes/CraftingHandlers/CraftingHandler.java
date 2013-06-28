package MistRuneCrafter.Nodes.CraftingHandlers;

import MistRuneCrafter.MistRuneCrafter;
import MistRuneCrafter.Nodes.BankHandlers.BankHandler;
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

    EmptyPouchHandler emptyPouches = new EmptyPouchHandler();
    ImbueHandler castImbue = new ImbueHandler();

    private final int AIR_ALTAR = 2478;

    Tile altar = new Tile(2843,4832,0);

    public void finishCrafting(SceneObject altar){
        Timer timeCheck = new Timer(3000);
        do{
            if(Inventory.contains(Globals.ID_PURE_ESS) && !Inventory.contains(Globals.ID_RUNE_ASTRAL)){
                altar.interact("Craft-rune");
                Task.sleep(250,350);
                Timer timeCheck2 = new Timer(2000);
                do{
                    Task.sleep(20,35);
                }while(Players.getLocal().isIdle() && timeCheck2.isRunning());
            }
        }while(Inventory.contains(Globals.ID_PURE_ESS) && timeCheck.isRunning());
    }

    @Override
    public boolean activate(){
        return (Calculations.distanceTo(altar)<=5 && Inventory.containsAll(Globals.ID_PURE_ESS, Globals.ID_RUNE_WATER) && Settings.get(3215) <= 1);
    }

    @Override
    public void execute(){
        SceneObject airAltar = SceneEntities.getNearest(AIR_ALTAR);
        Timer timeCheck = new Timer(12000);
        do{
            if(Settings.get(4)==1282){
                MistRuneCrafter.status="Navigating to Altar";
                Camera.turnTo(airAltar);
                Walking.walk(airAltar);

            }
            if(castImbue.activate()){
                castImbue.execute();
            }
            if(Settings.get(4)>=1282){
                MistRuneCrafter.status="Crafting Mist Runes.";
                MistRuneCrafter.interacting = Inventory.getItem(Globals.ID_RUNE_WATER).getWidgetChild();
                Inventory.getItem(Globals.ID_RUNE_WATER).getWidgetChild().interact("Use");
                MistRuneCrafter.interacting = airAltar;
                airAltar.click(true);
                Task.sleep(500,750);
                Timer timeCheck2 = new Timer(1250);
                do{
                    Task.sleep(50,75);
                }while(timeCheck2.isRunning() && !Players.getLocal().isIdle());
                Task.sleep(1000,1250);
                if(emptyPouches.activate()){
                    emptyPouches.execute();
                }
            }
        }while(Settings.get(4)>1282 && timeCheck.isRunning() && !Players.getLocal().getMessage().contains("Your Magic Imbue charge has ended."));
        MistRuneCrafter.postedProfit= MistRuneCrafter.postedProfit + (Inventory.getItem(Globals.ID_RUNE_MIST).getStackSize()*Globals.RUNE_MIST_PRICE);
        MistRuneCrafter.postedCrafts= MistRuneCrafter.postedCrafts + Inventory.getItem(Globals.ID_RUNE_MIST).getStackSize();
        finishCrafting(airAltar);
    }
}
