package MistRuneCrafter.Nodes.CraftingHandlers;


import MistRuneCrafter.MistRuneCrafter;
import MistRuneCrafter.Nodes.BankHandlers.BankHandler;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;

import java.util.Arrays;

//548, 436

public class EmptyPouchHandler extends Node {
    @Override
    public boolean activate(){
        return(Inventory.contains(BankHandler.ITEMS_OPTIONAL) && !Inventory.isFull());
    }

    @Override
    public void execute(){
            for(int x =0;  x<= BankHandler.ITEMS_OPTIONAL.length-1; x++){
                if(Inventory.getCount()<=27){
                    String tempArray = Inventory.getItem(BankHandler.ITEMS_OPTIONAL[x]).getWidgetChild().getWidget().getText();
                    Inventory.getItem(BankHandler.ITEMS_OPTIONAL[x]).getWidgetChild().hover();
                    Task.sleep(50,100);
                    if(Widgets.get(548,436).getChild(0).getText().substring(12).startsWith("Empty")){
                        MistRuneCrafter.status= "Emptying pouches." + (Widgets.get(548,436).getChild(0).getText().substring(12).startsWith("Empty"));
                        Inventory.getItem(BankHandler.ITEMS_OPTIONAL[x]).getWidgetChild().interact("Empty");
                        int y = 0;
                        do{
                            Task.sleep(100,200);
                            y++;
                        }while(!Inventory.contains(BankHandler.ID_PURE_ESS) || y<=10);

                    }
                }
            }
    }
}
