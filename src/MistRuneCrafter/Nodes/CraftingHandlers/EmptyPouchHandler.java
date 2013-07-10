package MistRuneCrafter.Nodes.CraftingHandlers;


import MistRuneCrafter.MistRuneCrafter;
import MistRuneCrafter.Nodes.BankHandlers.BankHandler;
import MistRuneCrafter.Nodes.BankHandlers.PouchHandlers;
import MistRuneCrafter.Nodes.Globals;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

//548, 436

public class EmptyPouchHandler extends Node {

    private void sleepGameTick(){
        Task.sleep(350,450);
    }

    @Override
    public boolean activate(){
        System.out.println("Attempting to activate Empty Pouches Handler");
        return(!Inventory.isFull() && !PouchHandlers.Pouch.allEmpty());
    }

    @Override
    public void execute(){
        System.out.println("Executing Empty Pouches.");
        for(int x =0;  x<=Globals.ITEMS_OPTIONAL.length-1; x++){
            MistRuneCrafter.status="Filling Pouches through BankHandler";
            if(PouchHandlers.Pouch.emptyPouch(Globals.ITEMS_OPTIONAL[x])){sleepGameTick();}
        }
        if(PouchHandlers.Pouch.allFull()){ System.out.println("PouchHandlers.allFull{} = true");}
    }
}
