package MistRuneCrafter.Nodes.CraftingHandlers;


import MistRuneCrafter.MistRuneCrafter;
import MistRuneCrafter.Nodes.BankHandlers.BankHandler;
import MistRuneCrafter.Nodes.Globals;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;

import java.util.Arrays;

//548, 436

public class EmptyPouchHandler extends Node {
    @Override
    public boolean activate(){
        return(Inventory.contains(Globals.ITEMS_OPTIONAL) && !Inventory.isFull());
    }

    @Override
    public void execute(){
            for(int x =0;  x<= Globals.ITEMS_OPTIONAL.length-1; x++){
                if(Inventory.getCount()<=27 && !Players.getLocal().getMessage().contains("Your Magic Imbue charge has ended.")){
                    MistRuneCrafter.interacting = Inventory.getItem(Globals.ITEMS_OPTIONAL[x]).getWidgetChild();
                    Inventory.getItem(Globals.ITEMS_OPTIONAL[x]).getWidgetChild().hover();
                    Task.sleep(50,100);
                    if(Widgets.get(548,436).getChild(0).getText().substring(12).startsWith("Empty")){
                        MistRuneCrafter.status= "Emptying pouches." + (Widgets.get(548,436).getChild(0).getText().substring(11).startsWith("Empty"));
                        Inventory.getItem(Globals.ITEMS_OPTIONAL[x]).getWidgetChild().interact("Empty");
                        int y = 0;
                        do{
                            Task.sleep(100,200);
                            y++;
                        }while(!Inventory.contains(Globals.ID_PURE_ESS) || y<=10);

                    }
                }
            }
    }
}
