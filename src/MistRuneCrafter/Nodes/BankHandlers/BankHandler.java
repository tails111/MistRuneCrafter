package MistRuneCrafter.Nodes.BankHandlers;

import MistRuneCrafter.MistRuneCrafter;
import MistRuneCrafter.Nodes.Globals;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Equipment;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BankHandler extends Node {
    public static int getGEValues(final int id) {
        int price = 0;
        String add = "http://scriptwith.us/api/?return=text&item=";
        add += id;
        try {
            final BufferedReader in = new BufferedReader(new InputStreamReader(
                    new URL(add).openConnection().getInputStream()));
            String line = in.readLine();
            if (line == null) {
                line = in.readLine();
            }
            String[] subset = line.split("[:]");
            price = Integer.parseInt(subset[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return price;
    }

    private final Tile BANK_TILE = new Tile(3186,3440,0);

    public boolean haveRequiredItem(){
        return (Inventory.containsAll(Globals.ITEMS_REQUIRED));
    }

    public boolean haveNecklace(){
        if(Equipment.getCount(Globals.ID_BINDING_NECKLACE)>=1){
            Globals.BINDING_NECKLACE_AMOUNT = 1;
            return true;
        }
        return false;
    }

    public static boolean invChangeSleep(){
        Timer timeCheck = new Timer(Random.nextInt(1200,1600));
        int tempInvCount = Inventory.getCount();
        int newInvCount;
        do {
            MistRuneCrafter.status="Waiting for Inv Change: " +timeCheck.getRemaining();
            Task.sleep(20,50);
            newInvCount = Inventory.getCount();
            if(timeCheck.getRemaining()<=100){return false;}
        }while(tempInvCount==newInvCount && timeCheck.isRunning());
        return true;

    }

    public boolean fillPouches(){
        for(int x =0;  x<=Globals.ITEMS_OPTIONAL.length-1; x++){
            if(Inventory.getCount(Globals.ID_PURE_ESS)<9){
                Bank.withdraw(Globals.ID_PURE_ESS, 28-Inventory.getCount());
                invChangeSleep();
            }
            MistRuneCrafter.status="Filling Pouches through BankHandler";
            if(PouchHandlers.Pouch.fillPouch(Globals.ITEMS_OPTIONAL[x])){invChangeSleep();}
            if(PouchHandlers.Pouch.allFull()){ System.out.println("PouchHandlers.allFull{} = true "); return false;}
        }
        return true;
    }

    PouchHandlers pouchControl = new PouchHandlers();

    @Override
    public boolean activate(){
        return(!haveRequiredItem() && Calculations.distanceTo(BANK_TILE)<=8);
    }

    @Override
    public void execute(){
        boolean needNecklace = false;
        SceneObject bankBooth = SceneEntities.getNearest(Globals.BANK_BOOTH_IDS);

        if(pouchControl.activate()){
            pouchControl.execute();
        }
        if(!haveNecklace()){
            needNecklace = true;
        }

        if(!Bank.isOpen()){
            MistRuneCrafter.status = "Opening bank booth.";
            int x = 0;
            do{
                x++;
                MistRuneCrafter.interacting=bankBooth;
                Camera.turnTo(bankBooth, 25);
                bankBooth.interact("Bank");
                Task.sleep(750,1000);
                do{
                    Task.sleep(20,30);
                }while(Players.getLocal().isMoving());
            }while(!Bank.isOpen() && x<=10);

        }
        if(Bank.isOpen()){
            MistRuneCrafter.status = "Depositing inventory.";
            Bank.depositInventory();
            invChangeSleep();
            for(int x = 0; x<= Globals.ITEMS_REQUIRED.length-2; x++){
                MistRuneCrafter.status = "Withdrawing required items.";
                if(Bank.withdraw(Globals.ITEMS_REQUIRED[x],Globals.ITEMS_REQUIRED_AMOUNTS[x])){invChangeSleep();}
            }
            if(needNecklace){
                Bank.withdraw(Globals.ID_BINDING_NECKLACE, 1);
                invChangeSleep();
                if(Inventory.contains(Globals.ID_BINDING_NECKLACE)){
                    Inventory.getItem(Globals.ID_BINDING_NECKLACE).getWidgetChild().interact("Wear");
                    invChangeSleep();
                }
            }
            for(int x = 0; x<= Globals.ITEMS_OPTIONAL.length-1; x++){
                MistRuneCrafter.status = "Withdrawing pouches.";
                if(Bank.withdraw(Globals.ITEMS_OPTIONAL[x], Globals.ITEMS_OPTIONAL_AMOUNTS[x])){
                    invChangeSleep();
                }
                Task.sleep(250,500);
            }
            if(pouchControl.activate()){
                pouchControl.execute();
            }

            Timer timeCheck2 = new Timer(25000);
            while (timeCheck2.isRunning() && !PouchHandlers.Pouch.allFull()){
                fillPouches();
                if(Inventory.getCount()<=12){Bank.withdraw(Globals.ID_PURE_ESS, 28-Inventory.getCount()); invChangeSleep();}
            }
            if(!Inventory.isFull()){Bank.withdraw(Globals.ID_PURE_ESS, 28-Inventory.getCount()); invChangeSleep();}
        }

        MistRuneCrafter.status = "Closing bank.";
        int x = 0;
        do{
            x++;
            Bank.close();
            Task.sleep(125,250);
        }while(Bank.isOpen() && x<=10);
        Globals.idleImbueSetting = Settings.get(4);
        System.out.println("Idle Imbue = " + Globals.idleImbueSetting);
    }

}