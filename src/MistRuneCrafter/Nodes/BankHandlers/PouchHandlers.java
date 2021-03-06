package MistRuneCrafter.Nodes.BankHandlers;

import MistRuneCrafter.MistRuneCrafter;
import MistRuneCrafter.Nodes.Globals;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

import java.util.LinkedHashSet;
import java.util.Set;

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

    public enum Pouch {
        GIANT(5514, 0xF, 18, 12), //30
        LARGE(5512, 0xF, 9, 9),  //12
        MEDIUM(5510, 0xF, 3, 6),
        SMALL(5509, 0xF, 0, 3);

        private final int id, mask, shift, maxEss;

        Pouch(final int id, final int mask, final int shift, final int maxEss) {
            this.id = id;
            this.mask = mask;
            this.shift = shift;
            this.maxEss = maxEss;
        }

        public int getId() {
            return this.id;
        }

        public Item getItem() {
            return Inventory.getItem(this.id);
        }

        public int getEssCount() {
            return (Settings.get(3214, this.shift, this.mask));
        }

        public int getMaxEss() {
            return this.maxEss;
        }

        public boolean isEmpty() {
            return(getEssCount() != this.maxEss);
        }

        public boolean isFull() {
            return(getEssCount() == this.maxEss);
        }

    public static boolean fillPouch(int pouchNum) {
        Pouch pouch = Pouch.GIANT;

        if(pouchNum==Globals.ID_GIANT_POUCH){pouch = Pouch.GIANT;}
        if(pouchNum==Globals.ID_LARGE_POUCH){pouch = Pouch.LARGE;}
        if(pouchNum==Globals.ID_MEDIUM_POUCH){pouch = Pouch.MEDIUM;}
        if(pouchNum==Globals.ID_SMALL_POUCH){pouch = Pouch.SMALL;}

        final Item item = pouch.getItem();
        if (item != null && !pouch.isFull()) {
            MistRuneCrafter.status="Filling " + item.getName();
            return item.getWidgetChild().interact("Fill", item.getName());
        }
        return false;
    }

    public static boolean emptyPouch(int pouchNum) {
        Pouch pouch = Pouch.GIANT;

        if(pouchNum==Globals.ID_GIANT_POUCH){pouch = Pouch.GIANT;}
        if(pouchNum==Globals.ID_LARGE_POUCH){pouch = Pouch.LARGE;}
        if(pouchNum==Globals.ID_MEDIUM_POUCH){pouch = Pouch.MEDIUM;}
        if(pouchNum==Globals.ID_SMALL_POUCH){pouch = Pouch.SMALL;}

        final Item item = pouch.getItem();

        if (item != null && !pouch.isEmpty() && (Inventory.getCount()-pouch.getEssCount()>=0)) {
            MistRuneCrafter.status="Emptying " + item.getName();
            return item.getWidgetChild().interact("Empty", item.getName());
        }
        return false;
    }

    public static Pouch[] getPouches() {
        Set<Pouch> used = new LinkedHashSet<Pouch>();
        for (Pouch p : Pouch.values()) {
            if (Inventory.contains(p.getId())) {
                used.add(p);
            }
        }
        return used.toArray(new Pouch[used.size()]);
    }

    public static boolean allEmpty() {
        for (Pouch p : getPouches()) {
            if (!p.isEmpty()) {
                MistRuneCrafter.status="All Empty Return false and has " + p.getEssCount() + " ess.";
                return false;
            }
        }
        MistRuneCrafter.status="All Empty Return true.";
        return true;

    }

    public static boolean allFull() {
        for (Pouch p : getPouches()) {
            System.out.println(p.getItem().getName()+" has " + p.getEssCount() + " and " + p.isFull());
            if (!p.isFull()) {
                MistRuneCrafter.status="All Full return false and has " + p.getEssCount() + " ess.";;
                return false;
            }
        }
        MistRuneCrafter.status="All Full return true.";
        return true;

    }

    public static Item getDegradedPouch() {
        return Inventory.getItem(5515);
    }

    public static boolean haveDegraded() {
        return getDegradedPouch() != null;
    }

    }

    private void waitGameTick(){
        Task.sleep(1200,1600);
    }


    SceneObject bankBooth;
    WidgetChild meSpeak;
    WidgetChild mageSpeak;
    WidgetChild specialSpeak;

    @Override
    public boolean activate(){
        return (PouchHandlers.Pouch.haveDegraded());
    }

    @Override
    public void execute(){

        bankBooth = SceneEntities.getNearest(Globals.BANK_BOOTH_IDS);

        if(!Bank.isOpen() && !Widgets.get(88,0).visible() && !Widgets.get(1191,17).visible()
                && !Widgets.get(1184,13).visible() ){
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

        if(Bank.isOpen() && !Widgets.get(88,0).visible() && !Widgets.get(1191,17).visible()
                && !Widgets.get(1184,13).visible()){
            MistRuneCrafter.status = "Depositing inventory.";
            Bank.depositInventory();
            MistRuneCrafter.status = "Withdrawing Astral Rune.";
            Bank.withdraw(Globals.ID_RUNE_ASTRAL, 1);
            Task.sleep(500,750);
            MistRuneCrafter.status = "Withdrawing Cosmic Rune.";
            Bank.withdraw(Globals.ID_RUNE_COSMIC, 1);
            Task.sleep(500,750);
            MistRuneCrafter.status = "Withdrawing Air Runes.";
            Bank.withdraw(Globals.ID_RUNE_AIR, 2);
            Task.sleep(500,750);
            for(int x = 0; x<= Globals.ITEMS_OPTIONAL.length-1; x++){
                MistRuneCrafter.status = "Withdrawing pouches.";
                if(Bank.withdraw(Globals.ITEMS_OPTIONAL[x], Globals.ITEMS_OPTIONAL_AMOUNTS[x])){
                    BankHandler.invChangeSleep();
                }
            }
            int x = 0;
            do{
                Bank.close();
                x++;
                Task.sleep(250,500);
            }while(Bank.isOpen() && x<=10);

        }

        Timer timeCheck2 = new Timer(10000);
        do{
            Task.sleep(20);
        }while(timeCheck2.isRunning() && !Widgets.get(640,108).visible());
        if(!Widgets.get(640,108).visible()){
            Widgets.get(540,3).click(true);
            Task.sleep(650,1000);
        }
        Widgets.get(640,108).click(true);


        Timer timeCheck = new Timer(30000);

        do{
            Task.sleep(75,125);
            MistRuneCrafter.status="Waiting for Choose NPC.";
        }while(!Widgets.get(88,0).visible() && timeCheck.isRunning());

        do{
            meSpeak = Widgets.get(1191,17);
            mageSpeak = Widgets.get(1184,13);
            specialSpeak = Widgets.get(1188,24);

            if(Widgets.get(88,6) != null){
                if(Widgets.get(88,6).getChild(14).getText().startsWith("Dark")){
                    for(int y =0; y<6; y++){
                        MistRuneCrafter.status="Scrolling down.";
                        if(Widgets.get(88,7) != null){
                            Widgets.get(88,7).getChild(5).click(true);
                            Task.sleep(250,500);
                        }
                    }
                    Widgets.get(88,6).getChild(14).click(true);
                }
                MistRuneCrafter.status="Waiting for Cast";
                Task.sleep(6000,8000);
            }
            waitGameTick();

            if(meSpeak != null){
                if(meSpeak.getText().substring(5).startsWith("Hello")){
                    MistRuneCrafter.status="Clicking Continue on Hello";
                    Widgets.get(1191,18).click(true);
                    Task.sleep(600,750);
                }
            }
            waitGameTick();
            if(mageSpeak != null){
                if(mageSpeak.getText().substring(5).startsWith("What")){
                    MistRuneCrafter.status="Clicking Continue on What";
                    Widgets.get(1184,18).click(true);
                    Task.sleep(600,750);
                }
            }
            waitGameTick();
            if(meSpeak != null){
                if(meSpeak.getText().substring(5).startsWith("It")){
                    MistRuneCrafter.status="Clicking Continue on It";
                    Widgets.get(1191,18).click(true);
                    Task.sleep(600,750);
                }
            }
            waitGameTick();
            if(mageSpeak != null){
                if(mageSpeak.getText().substring(5).startsWith("Are")){
                    MistRuneCrafter.status="Clicking Continue on Are";
                    Widgets.get(1184,18).click(true);
                    Task.sleep(600,750);
                }
            }
            waitGameTick();
            if(meSpeak != null){
                if(meSpeak.getText().substring(5).startsWith("Sorry")){
                    MistRuneCrafter.status="Clicking Continue on Sorry";
                    Widgets.get(1191,18).click(true);
                    Task.sleep(600,750);
                }
            }
            waitGameTick();
            if(mageSpeak != null){
                if(mageSpeak.getText().substring(5).startsWith("What?")){
                    MistRuneCrafter.status="Clicking Continue on What?";
                    Widgets.get(1184,18).click(true);
                    Task.sleep(600,750);
                }
            }
            waitGameTick();
            if(specialSpeak != null){
                if(specialSpeak.getText().startsWith("Can you repair")){
                    MistRuneCrafter.status="Clicking Repair";
                    Widgets.get(1188,24).click(true);
                    Task.sleep(600,750);
                }
            }
            waitGameTick();
            if(meSpeak != null){
                if(meSpeak.getText().substring(5).startsWith("Can you")){
                    MistRuneCrafter.status="Clicking Continue on Can you";
                    Widgets.get(1191,18).click(true);
                    Task.sleep(600,750);
                }
            }
            waitGameTick();
            if(mageSpeak != null){
                if(mageSpeak.getText().substring(5).startsWith("A simple")){
                    MistRuneCrafter.status="Clicking Continue on A simple";
                    Widgets.get(1184,18).click(true);
                    Task.sleep(600,750);
                }
            }
            waitGameTick();
        }while(PouchHandlers.Pouch.haveDegraded());
    }
}