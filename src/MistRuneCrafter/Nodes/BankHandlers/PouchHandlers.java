package MistRuneCrafter.Nodes.BankHandlers;

import MistRuneCrafter.MistRuneCrafter;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

//Giant Pouch degrade 5515
//NPC Contact screen = WIDGET 88
//Widget Child 0, check visible.
//Widget Child 6, Child 14, get text = Dark Mage
//Widget Child 7, Child 5, click 5 times.

//WIDGET 1191, Child 17, "<p=3>Hello?"
//Child 18, Click
//WIDGET 1184, Child 13, "<p=4>What is this?!"
//Child 18, Click
//WIDGET 1191, Child 17, "<p=3>It"
//Child 18, Click
//WIDGET 1184, Child 13, "<p=4>Are"
//Child 18, Click
//WIDGET 1191, Child 17, "<p=2>Sorry"
//Child 18, Click
//WIDGET 1184, Child 13, "<p=4>What?"
//Child 18, Click
//WIDGET 1188, Child 24, "Can you repair my pouches?", CLICK IT
//WIDGET 1191, Child 17, "<p=3>Can you?"
//Child 18, Click
//WIDGET 1184, Child 13, "<p=2>A simple"
//Child 18, Click


public class PouchHandlers extends Node{

    Tile bankTile = new Tile(3186,3438,0);

    @Override
    public boolean activate(){
        return (Inventory.contains(5155) && Calculations.distanceTo(bankTile)<=8);
    }

    @Override
    public void execute(){
        SceneObject bankBooth = SceneEntities.getNearest(BankHandler.BANK_BOOTH_IDS);

        if(!Bank.isOpen()){
            MistRuneCrafter.status = "Opening bank booth.";
            int x = 0;
            do{
                x++;
                bankBooth.interact("Bank");
                Task.sleep(750, 1000);
                do{
                    Task.sleep(20,30);
                }while(Players.getLocal().isMoving());
            }while(!Bank.isOpen() && x<=10);

        }

        if(Bank.isOpen()){
            MistRuneCrafter.status = "Depositing inventory.";
            Bank.depositInventory();
            MistRuneCrafter.status = "Withdrawing Astral Rune.";
            Bank.withdraw(BankHandler.ID_RUNE_ASTRAL, 1);
            Task.sleep(500,750);
            MistRuneCrafter.status = "Withdrawing Cosmic Rune.";
            Bank.withdraw(BankHandler.ID_RUNE_COSMIC, 1);
            Task.sleep(500,750);
            MistRuneCrafter.status = "Withdrawing Air Runes.";
            Bank.withdraw(BankHandler.ID_RUNE_AIR, 2);
            Task.sleep(500,750);
            for(int x = 0; x<= BankHandler.ITEMS_OPTIONAL.length-1; x++){
                MistRuneCrafter.status = "Withdrawing pouches.";
                Bank.withdraw(BankHandler.ITEMS_OPTIONAL[x], BankHandler.ITEMS_OPTIONAL_AMOUNTS[x]);
                Task.sleep(500,1000);
            };
            int x = 0;
            do{
                Bank.close();
                x++;
                Task.sleep(250,500);
            }while(Bank.isOpen() && x<=10);

        }
        //Giant Pouch degrade 5515
//NPC Contact screen = WIDGET 88
//Widget Child 0, check visible.
//Widget Child 6, Child 14, get text = Dark Mage
//Widget Child 7, Child 5, click 5 times.

//WIDGET 1191, Child 17, "<p=3>Hello?"
//Child 18, Click
//WIDGET 1184, Child 13, "<p=4>What is this?!"
//Child 18, Click
//WIDGET 1191, Child 17, "<p=3>It"
//Child 18, Click
//WIDGET 1184, Child 13, "<p=4>Are"
//Child 18, Click
//WIDGET 1191, Child 17, "<p=2>Sorry"
//Child 18, Click
//WIDGET 1184, Child 13, "<p=4>What?"
//Child 18, Click
//WIDGET 1188, Child 24, "Can you repair my pouches?", CLICK IT
//WIDGET 1191, Child 17, "<p=3>Can you?"
//Child 18, Click
//WIDGET 1184, Child 13, "<p=2>A simple"
//Child 18, Click
        Keyboard.sendText("0", false);
        int x = 0;
        do{
            x++;
            Task.sleep(75,125);
        }while(!Widgets.get(88,0).visible() && x<=100);
        if(Widgets.get(88,6).getChild(14).getText().startsWith("Dark")){
            for(int y =0; y<6; y++){
                Widgets.get(88,7).getChild(5).click(true);
                Task.sleep(250,500);
            }
            Widgets.get(88,6).click(true);
        }
        if(Widgets.get(1191,17).getText().substring(4).startsWith("Hello")){
            Widgets.get(1191,18).click(true);
            Task.sleep(600,750);
        }
        if(Widgets.get(1184,13).getText().substring(4).startsWith("What")){
            Widgets.get(1191,18).click(true);
            Task.sleep(600,750);
        }
        if(Widgets.get(1191,17).getText().substring(4).startsWith("It")){
            Widgets.get(1191,18).click(true);
            Task.sleep(600,750);
        }
        if(Widgets.get(1184,13).getText().substring(4).startsWith("Are")){
            Widgets.get(1191,18).click(true);
            Task.sleep(600,750);
        }
        if(Widgets.get(1191,17).getText().substring(0).startsWith("Sorry")){
            Widgets.get(1191,18).click(true);
            Task.sleep(600,750);
        }
        if(Widgets.get(1184,13).getText().substring(4).startsWith("What?")){
            Widgets.get(1191,18).click(true);
            Task.sleep(600,750);
        }
        if(Widgets.get(1188,24).getText().substring(4).startsWith("Can you repair")){
            Widgets.get(1188,24).click(true);
            Task.sleep(600,750);
        }
        if(Widgets.get(1191,17).getText().substring(0).startsWith("Cam you")){
            Widgets.get(1191,18).click(true);
            Task.sleep(600,750);
        }
        if(Widgets.get(1184,13).getText().substring(4).startsWith("A simple")){
            Widgets.get(1191,18).click(true);
            Task.sleep(600,750);
        }
    }
}
