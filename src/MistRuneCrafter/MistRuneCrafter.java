package MistRuneCrafter;

import MistRuneCrafter.Nodes.BankHandlers.BankHandler;
import MistRuneCrafter.Nodes.BankHandlers.PouchHandlers;
import MistRuneCrafter.Nodes.CraftingHandlers.CraftingHandler;
import MistRuneCrafter.Nodes.CraftingHandlers.EmptyPouchHandler;
import MistRuneCrafter.Nodes.WalkingHandlers.WalkToAltar;
import MistRuneCrafter.Nodes.WalkingHandlers.WalkToBank;
import org.powerbot.core.Bot;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.widget.WidgetCache;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.Entity;
import org.powerbot.game.client.Client;

import java.awt.*;

@Manifest(name = "Mist Rune Crafter", authors = "tails111", description = "Creates Mist Runes at the Air Altar using Magic Imbue and Binding Necklaces", version = 1.0)
public class MistRuneCrafter extends ActiveScript implements PaintListener{

    private Client client = Bot.client();
    public static long startTime = System.currentTimeMillis();
    private int getPerHour(final int value){
        return (int) (value * 3600000D / (System.currentTimeMillis() - startTime));
    }

    private Tree script = new Tree(new Node[]{
            new BankHandler(),
            new WalkToAltar(),
            new CraftingHandler(),
            new WalkToBank()
    });

    public static int postedProfit = 0;
    public static int postedProfitPerMath = 0;
    public static int postedCrafts = 0;
    public static int postedCraftsPerMath = 0;
    public static int postedTimeRun = 0;

    public static String status = "Hello Stephen.";
    public static Entity interacting = null;

    @Override
    public void onRepaint(Graphics g1){

        postedProfitPerMath = getPerHour(postedProfit);
        postedCraftsPerMath = getPerHour(postedCrafts);
        postedTimeRun = getPerHour(1000);

        String second =""+ ((System.currentTimeMillis() - startTime) / 1000) % 60;
        String minute =""+ ((System.currentTimeMillis() - startTime) / (1000 * 60)) % 60;
        String hour = ""+((System.currentTimeMillis() - startTime) / (1000 * 60 * 60)) % 24;

        String time=hour+":"+minute+":"+second;
        Graphics2D g = (Graphics2D)g1;

        g.setColor(Color.ORANGE);
        int mouseY = (int) Mouse.getLocation().getY();
        int mouseX = (int) Mouse.getLocation().getX();
        g.drawLine(mouseX - 999, mouseY + 999, mouseX + 999, mouseY - 999);
        g.drawLine(mouseX + 999, mouseY + 999, mouseX - 999, mouseY - 999);
        g.drawOval(mouseX-4,mouseY-4, 8, 8);
        g.setColor(Color.GREEN);
        g.fillOval(mouseX-2,mouseY-2,5,5);

        Font fontNormal = new Font("Comic Sans MS", Font.PLAIN, 12);
        Font fontTitle = new Font("Comic Sans MS", Font.BOLD, 12);
        g.setFont(fontTitle);
        g.setColor(Color.BLACK);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g.fillRect(10, 75, 200, 84);
        g.setColor(Color.CYAN);
        g.drawLine(10, 75, 10, 159);//LEFT
        g.drawLine(10,75,210,75);//TOP
        g.drawLine(210,75,210,159);//RIGHT
        g.drawLine(10,159,210,159);//BOTTOM
        //x1,y1,x2,y2
        g.drawString("    Mist Rune Crafter",11,90);
        g.setFont(fontNormal);
        g.drawString(("Status: " + status), 15, 105);
        g.drawString(("Time Run: " + time), 15, 122);
        g.drawString(("Profit: " + postedProfit + "(" + postedProfitPerMath + ")"), 15, 139);
        g.drawString(("Runes/H: "+ postedCrafts + "(" + postedCraftsPerMath + ")"),15, 156);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));


        if(interacting != null){
            g.setColor(Color.GREEN);
            for(final Polygon p : interacting.getBounds()){
                g.fillPolygon(p);
            }
        }
    }

    @Override
    public void onStart(){
        Mouse.setSpeed(Mouse.Speed.VERY_FAST);
        status = "Hello Stephen";
    }

    @Override
    public int loop(){
        final Node stateNode = script.state();
        if(stateNode != null && Game.isLoggedIn()){
            script.set(stateNode);
            final Node setNode = script.get();
            if(setNode != null){
                getContainer().submit(setNode);
                setNode.join();
            }
        }

        if (client != Bot.client()) {
            WidgetCache.purge();
            Bot.context().getEventManager().addListener(this);
            client = Bot.client();
        }
        return Random.nextInt(250, 300);
    }

}
