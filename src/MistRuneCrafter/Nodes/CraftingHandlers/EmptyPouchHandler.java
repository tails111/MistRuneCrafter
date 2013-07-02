package MistRuneCrafter.Nodes.CraftingHandlers;


import MistRuneCrafter.MistRuneCrafter;
import MistRuneCrafter.Nodes.BankHandlers.BankHandler;
import MistRuneCrafter.Nodes.BankHandlers.PouchHandlers;
import MistRuneCrafter.Nodes.Globals;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.tab.Inventory;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

//548, 436

public class EmptyPouchHandler extends Node {

    private void sleepGameTick(){
        Task.sleep(650,850);
    }

    @Override
    public boolean activate(){
       // return(Inventory.contains(Globals.ITEMS_OPTIONAL) && !Inventory.isFull());
        return(true);
    }

    @Override
    public void execute(){
       int pouchNum;
       PouchHandlers.Pouch pouch = PouchHandlers.Pouch.GIANT;

            for(int x =0;  x<= Globals.ITEMS_OPTIONAL.length-1; x++){
                List<Integer> used = new ArrayList<Integer>();
                pouchNum = Globals.ITEMS_OPTIONAL[x];
                MistRuneCrafter.status= "Emptying pouches.";
                if(pouchNum==Globals.ID_GIANT_POUCH){pouch = PouchHandlers.Pouch.GIANT;}
                if(pouchNum==Globals.ID_LARGE_POUCH){pouch = PouchHandlers.Pouch.LARGE;}
                if(pouchNum==Globals.ID_MEDIUM_POUCH){pouch = PouchHandlers.Pouch.MEDIUM;}
                if(pouchNum==Globals.ID_SMALL_POUCH){pouch = PouchHandlers.Pouch.SMALL;}

                if(pouch.isFull() && Inventory.getCount()<=20){
                    MistRuneCrafter.status="Emptying Pouch: " + Globals.ITEMS_OPTIONAL[x];
                    System.out.println(pouch.getId() + " is being emptied.");
                    if(!used.contains(pouch.getId())){
                        PouchHandlers.emptyPouch(Globals.ITEMS_OPTIONAL[x]);
                        used.add(Globals.ITEMS_OPTIONAL[x]);
                    }
                    sleepGameTick();
                }
            }

    }
}
