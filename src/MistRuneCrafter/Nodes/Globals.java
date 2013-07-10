package MistRuneCrafter.Nodes;

import MistRuneCrafter.Nodes.BankHandlers.BankHandler;

public class Globals {
    public static int idleImbueSetting = 0;
    public static int activeImbueSetting=0;

    public static final int ID_RUNE_ASTRAL = 9075, ID_RUNE_WATER = 555, ID_RUNE_COSMIC = 564, ID_RUNE_AIR = 556, ID_RUNE_MIST = 4695;
    public static final int ID_PURE_ESS = 7936;
    public static final int ID_GIANT_POUCH = 5514, ID_LARGE_POUCH = 5512, ID_MEDIUM_POUCH = 5510, ID_SMALL_POUCH = 5509;
    public static final int ID_GIANT_POUCH_DEG = 5515;
    public static final int ID_BINDING_NECKLACE = 5521;

    public static int RUNE_ASTRAL_AMOUNT = 2, RUNE_WATER_AMOUNT = 43;
    public static int GIANT_POUCH_AMOUNT = 1, LARGE_POUCH_AMOUNT = 1, MEDIUM_POUCH_AMOUNT = 1, SMALL_POUCH_AMOUNT = 1;
    public static int GIANT_POUCH_DEG_AMOUNT = 1;
    public static int BINDING_NECKLACE_AMOUNT = 0;

    public static final int RUNE_MIST_PRICE = BankHandler.getGEValues(ID_RUNE_MIST);


    public static final int[] ITEMS_REQUIRED = {ID_RUNE_ASTRAL, ID_RUNE_WATER, ID_PURE_ESS};
    public static final int[] ITEMS_REQUIRED_AMOUNTS = {RUNE_ASTRAL_AMOUNT, RUNE_WATER_AMOUNT, 1};
    public static final int[] ITEMS_OPTIONAL = {ID_GIANT_POUCH,ID_LARGE_POUCH,ID_MEDIUM_POUCH,ID_SMALL_POUCH, ID_GIANT_POUCH_DEG};
    public static final int[] ITEMS_OPTIONAL_AMOUNTS = {GIANT_POUCH_AMOUNT,LARGE_POUCH_AMOUNT,
            MEDIUM_POUCH_AMOUNT,SMALL_POUCH_AMOUNT, GIANT_POUCH_DEG_AMOUNT,};

    public static final int[] BANK_BOOTH_IDS= {782};
}
