package MistRuneCrafter.Nodes.BankHandlers;

import MistRuneCrafter.MistRuneCrafter;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Equipment;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
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

    public static final int ID_RUNE_ASTRAL = 9075, ID_RUNE_WATER = 555, ID_RUNE_COSMIC = 564, ID_RUNE_AIR = 556, ID_PURE_ESS = 7936;
    public static final int ID_MASSIVE_POUCH = 24205, ID_GIANT_POUCH = 5514, ID_LARGE_POUCH = 5512, ID_MEDIUM_POUCH = 5510, ID_SMALL_POUCH = 5509;
    public final int ID_TIARA_AIR = 5527, ID_TIARA_ELEMENTAL = 0, ID_TIARA_OMNI = 13655;
    public final int ID_STEAM_STAFF = 11736, ID_STEAM_BATTLESTAFF = 11738;
    public final int ID_BINDING_NECKLACE = 5521;

    public static int RUNE_ASTRAL_AMOUNT = 2, RUNE_WATER_AMOUNT = 100, PURE_ESS_AMOUNT = 28;
    public static int MASSIVE_POUCH_AMOUNT = 0, GIANT_POUCH_AMOUNT = 0, LARGE_POUCH_AMOUNT = 0, MEDIUM_POUCH_AMOUNT = 0, SMALL_POUCH_AMOUNT = 0;
    public static int TIARA_AIR_AMOUNT = 0, TIARA_ELEMENTAL_AMOUNT = 0, TIARA_OMNI_AMOUNT = 0;
    public static int STEAM_STAFF_AMOUNT = 0, STEAM_BATTLESTAFF_AMOUNT = 0;
    public static int BINDING_NECKLACE_AMOUNT = 0;


    public static int[] ITEMS_REQUIRED = {ID_RUNE_ASTRAL, ID_RUNE_WATER, ID_PURE_ESS};
    public static int[] ITEMS_REQUIRED_AMOUNTS = {RUNE_ASTRAL_AMOUNT, RUNE_WATER_AMOUNT, 1};
    public static int[] ITEMS_OPTIONAL = {ID_GIANT_POUCH,ID_LARGE_POUCH,ID_MEDIUM_POUCH,ID_SMALL_POUCH};
    public static int[] ITEMS_OPTIONAL_AMOUNTS = {GIANT_POUCH_AMOUNT,LARGE_POUCH_AMOUNT,
            MEDIUM_POUCH_AMOUNT,SMALL_POUCH_AMOUNT};
    public int[] ITEMS_EQUIPMENT = {ID_TIARA_AIR, ID_TIARA_ELEMENTAL, ID_TIARA_OMNI, ID_STEAM_STAFF, ID_STEAM_BATTLESTAFF,
            ID_BINDING_NECKLACE};
    public int[] ITEMS_EQUIPMENT_AMOUNT = {TIARA_AIR_AMOUNT, TIARA_ELEMENTAL_AMOUNT, TIARA_OMNI_AMOUNT, STEAM_STAFF_AMOUNT,
            STEAM_BATTLESTAFF_AMOUNT, BINDING_NECKLACE_AMOUNT};

    public static final int[] BANK_BOOTH_IDS= {782};

    private final Tile BANK_TILE = new Tile(3186,3440,0);

    //SETTING 3125 is POUCHES.
    //Small +- 2, Medium +- 8,  large +- 32, giant +- 128
    //170 Total


    public boolean haveRequiredItem(){
        return (Inventory.containsAll(ITEMS_REQUIRED) && Inventory.getCount(ID_PURE_ESS)>=5);
    }

    public boolean haveTiara(){
        if(Equipment.getCount(ID_TIARA_AIR)>=1){
            BankHandler.TIARA_AIR_AMOUNT = 1;
            return true;
        }
        if(Equipment.getCount(ID_TIARA_ELEMENTAL)>=1){
            BankHandler.TIARA_ELEMENTAL_AMOUNT = 1;
            return true;
        }
        if(Equipment.getCount(ID_TIARA_OMNI)>=1){
            BankHandler.TIARA_OMNI_AMOUNT = 1;
            return true;
        }
        return false;
    }

    public boolean haveStaff(){
        if(Equipment.getCount(ID_STEAM_STAFF)>=1){
            BankHandler.STEAM_STAFF_AMOUNT = 1;
            return true;
        }
        if(Equipment.getCount(ID_STEAM_BATTLESTAFF)>=1){
            BankHandler.STEAM_BATTLESTAFF_AMOUNT = 1;
            return true;
        }
        return false;
    }

    public boolean haveNecklace(){
        if(Equipment.getCount(ID_BINDING_NECKLACE)>=1){
            BankHandler.BINDING_NECKLACE_AMOUNT = 1;
            return true;
        }
        return false;
    }

    public boolean fillPouches(){
        if(Inventory.contains(ITEMS_OPTIONAL)){
            for(int x =0;  x<= ITEMS_OPTIONAL.length-1; x++){
                if(Settings.get(3215) >= 170){
                    return true;
                }
                Task.sleep(75, 125);
                if(Inventory.getCount(ID_PURE_ESS)<=12){
                    Bank.withdraw(ID_PURE_ESS, PURE_ESS_AMOUNT);
                }
                Task.sleep(75,125);
                Inventory.getItem(ITEMS_OPTIONAL[x]).getWidgetChild().interact("Fill");
                Task.sleep(500,1000);
                MistRuneCrafter.status= "Filling pouches.";
            }
            if(!Inventory.isFull()){
                Bank.withdraw(ID_PURE_ESS,PURE_ESS_AMOUNT);
            }
            if(Settings.get(3215) >= 140){
                return true;
            }
        }
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

        SceneObject bankBooth = SceneEntities.getNearest(BANK_BOOTH_IDS);

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
            for(int x = 0; x<= ITEMS_REQUIRED.length-1; x++){
                MistRuneCrafter.status = "Withdrawing required items.";
                Bank.withdraw(ITEMS_REQUIRED[x],ITEMS_REQUIRED_AMOUNTS[x]);
                Task.sleep(500,750);
            }
               if(needNecklace){
                   Bank.withdraw(ID_BINDING_NECKLACE, 1);
                   Task.sleep(600,850);
                   if(Inventory.contains(ID_BINDING_NECKLACE)){
                       Inventory.getItem(ID_BINDING_NECKLACE).getWidgetChild().interact("Wear");
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
            for(int x = 0; x<= ITEMS_OPTIONAL.length-1; x++){
                MistRuneCrafter.status = "Withdrawing pouches.";
                Bank.withdraw(ITEMS_OPTIONAL[x], ITEMS_OPTIONAL_AMOUNTS[x]);
                Task.sleep(500,1000);
            }
            int x = 0;
            do{
                x++;
                MistRuneCrafter.status = "Withdrawing Ess";
                fillPouches();
                Bank.withdraw(ID_PURE_ESS, PURE_ESS_AMOUNT);
            }while(!fillPouches() && x<=10);
            if(!Inventory.isFull()) {Bank.withdraw(ID_PURE_ESS, 28-Inventory.getCount());}
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