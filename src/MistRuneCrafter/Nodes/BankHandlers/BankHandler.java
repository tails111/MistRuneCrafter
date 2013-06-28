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
        return (Inventory.containsAll(Globals.ITEMS_REQUIRED) && Inventory.getCount(Globals.ID_PURE_ESS)>=5);
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

    public boolean fillPouches(){
            for(int x =0;  x<= Globals.ITEMS_OPTIONAL.length-1; x++){
                if(pouchControl.activate()){
                    pouchControl.execute();
                }
                if(Settings.get(3215) >= 160){
                    MistRuneCrafter.status = "Pouches are full.";
                    return true;
                }
                Task.sleep(75, 125);
                if(Inventory.getCount(Globals.ID_PURE_ESS)<=12){
                    Bank.withdraw(Globals.ID_PURE_ESS, Globals.PURE_ESS_AMOUNT);
                }
                Task.sleep(75,125);
                Inventory.getItem(Globals.ITEMS_OPTIONAL[x]).getWidgetChild().interact("Fill");
                Task.sleep(500,1000);
                MistRuneCrafter.status= "Filling pouches.";
            }
            if(!Inventory.isFull()){
                Bank.withdraw(Globals.ID_PURE_ESS,Globals.PURE_ESS_AMOUNT);
            }
            if(Settings.get(3215) >= 160){
                return true;
            }
        MistRuneCrafter.status="Pouches are NOT full.";
        Task.sleep(1000);
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
            for(int x = 0; x<= Globals.ITEMS_REQUIRED.length-1; x++){
                MistRuneCrafter.status = "Withdrawing required items.";
                Bank.withdraw(Globals.ITEMS_REQUIRED[x],Globals.ITEMS_REQUIRED_AMOUNTS[x]);
                Task.sleep(500,750);
            }
               if(needNecklace){
                   Bank.withdraw(Globals.ID_BINDING_NECKLACE, 1);
                   Task.sleep(600,850);
                   if(Inventory.contains(Globals.ID_BINDING_NECKLACE)){
                       Inventory.getItem(Globals.ID_BINDING_NECKLACE).getWidgetChild().interact("Wear");
                       Task.sleep(600);
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
                Task.sleep(500,1000);
            }
            if(pouchControl.activate()){
                pouchControl.execute();
            }

            Timer timeCheck = new Timer(15000);
            while(!fillPouches() && timeCheck.isRunning()){
                fillPouches();
                MistRuneCrafter.status = "Withdrawing Ess";
                Bank.withdraw(Globals.ID_PURE_ESS, 28-Inventory.getCount());
            }
            if(!Inventory.isFull()) {Bank.withdraw(Globals.ID_PURE_ESS, 28-Inventory.getCount());}
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