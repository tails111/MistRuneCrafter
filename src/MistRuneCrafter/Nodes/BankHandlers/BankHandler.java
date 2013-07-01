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
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

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

    //SETTING 3125 is POUCHES.
    //Small +- 2, Medium +- 8,  large +- 32, giant +- 128
    //170 Total


    public boolean haveRequiredItem(){
        return (Inventory.containsAll(Globals.ITEMS_REQUIRED));
    }

    public boolean haveTiara(){
        if(Equipment.getCount(Globals.ID_TIARA_AIR)>=1){
            Globals.TIARA_AIR_AMOUNT = 1;
            return true;
        }
        if(Equipment.getCount(Globals.ID_TIARA_ELEMENTAL)>=1){
            Globals.TIARA_ELEMENTAL_AMOUNT = 1;
            return true;
        }
        if(Equipment.getCount(Globals.ID_TIARA_OMNI)>=1){
            Globals.TIARA_OMNI_AMOUNT = 1;
            return true;
        }
        return false;
    }

    public boolean haveStaff(){
        if(Equipment.getCount(Globals.ID_STEAM_STAFF)>=1){
            Globals.STEAM_STAFF_AMOUNT = 1;
            return true;
        }
        if(Equipment.getCount(Globals.ID_STEAM_BATTLESTAFF)>=1){
            Globals.STEAM_BATTLESTAFF_AMOUNT = 1;
            return true;
        }
        return false;
    }

    public boolean haveNecklace(){
        if(Equipment.getCount(Globals.ID_BINDING_NECKLACE)>=1){
            Globals.BINDING_NECKLACE_AMOUNT = 1;
            return true;
        }
        return false;
    }

    public static void invChangeSleep(){

        Timer timeCheck = new Timer(Random.nextInt(1200,1600));
        Task.sleep(50,75);
        int tempInvCount = Inventory.getCount();
        int newInvCount;
        do {
            MistRuneCrafter.status="Waiting for Inv Change.";
            Task.sleep(20,50);
            newInvCount = Inventory.getCount();
        }while(tempInvCount!=newInvCount && timeCheck.isRunning());

    }

    public boolean fillPouches(){

        do {
            for(int x =0;  x<=Globals.ITEMS_OPTIONAL.length-1; x++){
                MistRuneCrafter.status="Filling Pouches through BankHandler";
                PouchHandlers.fillPouch(Globals.ITEMS_OPTIONAL[x]);
                invChangeSleep();
                if(Inventory.getCount(Globals.ID_PURE_ESS)<=9){
                    Bank.withdraw(Globals.ID_PURE_ESS, 28-Inventory.getCount());
                    invChangeSleep();
                }
                if(!Inventory.isFull()){
                    Bank.withdraw(Globals.ID_PURE_ESS,28-Inventory.getCount());
                    invChangeSleep();
                }
            }
            Task.sleep(1250,1500);
        }while(!PouchHandlers.allFull());

        return false;
    }

    PouchHandlers pouchControl = new PouchHandlers();

    @Override
    public boolean activate(){
        return(!haveRequiredItem() && Calculations.distanceTo(BANK_TILE)<=8);
    }

    @Override
    public void execute(){
        boolean needTiara = false;
        boolean needStaff = false;
        boolean needNecklace = false;

        SceneObject bankBooth = SceneEntities.getNearest(Globals.BANK_BOOTH_IDS);

        if(pouchControl.activate()){
            pouchControl.execute();
        }
        if(!haveTiara()){
            needTiara   =  true;
        }
        if(!haveStaff()){
            needStaff = true;
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
                Bank.withdraw(Globals.ITEMS_REQUIRED[x],Globals.ITEMS_REQUIRED_AMOUNTS[x]);
                invChangeSleep();
            }
               if(needNecklace){
                   Bank.withdraw(Globals.ID_BINDING_NECKLACE, 1);
                   invChangeSleep();
                   if(Inventory.contains(Globals.ID_BINDING_NECKLACE)){
                       Inventory.getItem(Globals.ID_BINDING_NECKLACE).getWidgetChild().interact("Wear");
                       invChangeSleep();
                   }
               }
            //        for(int x = 0; x<= ITEMS_EQUIPMENT.length-1; x++){
            //           MistRuneCrafter.status = "Withdrawing equipment items.";
            //           if(ITEMS_EQUIPMENT_AMOUNT[x]==0){
            //               x++;
            //           }
            //          if(ITEMS_EQUIPMENT_AMOUNT[x]==1){
            //              Bank.withdraw(ITEMS_EQUIPMENT[x], ITEMS_EQUIPMENT_AMOUNT[x]);
            //             Task.sleep(500,1000);
            //            if(Inventory.contains(ITEMS_EQUIPMENT)){
            //               MistRuneCrafter.status= "Equipping items.";
            //              if(!Inventory.getItem(ITEMS_EQUIPMENT).getWidgetChild().interact("Wear")){
            //                  if(!Inventory.getItem(ITEMS_EQUIPMENT).getWidgetChild().interact("Equip")){
            //                      Inventory.getItem(ITEMS_EQUIPMENT).getWidgetChild().interact("Wield");
            //                 }
            //
            //            }
            //        }
            //        ITEMS_EQUIPMENT_AMOUNT[x]=0;
            //        Task.sleep(500,750);
            //    }
            // }
            // }
            for(int x = 0; x<= Globals.ITEMS_OPTIONAL.length-1; x++){
                MistRuneCrafter.status = "Withdrawing pouches.";
                Bank.withdraw(Globals.ITEMS_OPTIONAL[x], Globals.ITEMS_OPTIONAL_AMOUNTS[x]);
                invChangeSleep();
                Task.sleep(250,500);
            }
            if(pouchControl.activate()){
                pouchControl.execute();
            }

            if(!Inventory.isFull()) {Bank.withdraw(Globals.ID_PURE_ESS, 28-Inventory.getCount()); invChangeSleep();}
            fillPouches();
            if(!Inventory.isFull()) {Bank.withdraw(Globals.ID_PURE_ESS, 28-Inventory.getCount()); invChangeSleep();}
        }

        MistRuneCrafter.status = "Closing bank.";
        int x = 0;
        do{
            x++;
            Bank.close();
            Task.sleep(125,250);
        }while(Bank.isOpen() && x<=10);

    }

}