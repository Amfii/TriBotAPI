package scripts.AmphibiaAPI;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.GrandExchange;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.types.RSGEOffer;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.script.Script;

/**
 *
 * @author Deividas Dunda
 */
public class AGrandExchange {
    
    /** Opens Grand Exchange using a
     *  Grand Exchange Clerk NPC
     */
    public static void openGE() {
        if (GrandExchange.getWindowState() != null) {   // already open
            GrandExchange.close();
            General.sleep(200, 400);
        }
        final RSNPC[] geNPC = NPCs.findNearest("Grand Exchange Clerk");
        if (geNPC.length > 0) {
            if (DynamicClicking.clickRSNPC(geNPC[0], "Exchange Grand Exchange Clerk")) {
                Timing.waitCondition(new Condition() {
                    @Override
                    public boolean active() {
                        General.sleep(100);
                        return GEMainWindowOpen();
                    }
                }, General.random(3000, 8000));
            }
        }
    }
    
    /** Puts select item on sale at -5% guide price
     * 
     * @param item name of item to put on sale
     * @return true if item successfully placed on sale
     */
    public static boolean sellAtMinus5(String item) {
        if (!GEMainWindowOpen()) {
            GrandExchange.close();
            General.sleep (200, 700);
            openGE();
        }
        final RSItem[] items = Inventory.find(item);
        if (items.length > 0) {
            if (items[0].click("Offer")) {
                General.sleep (300, 800);
                if (Interfaces.get(465, 24).getChild(50).click("-5%")) {
                    General.sleep (200, 700);
                    if (GrandExchange.confirmOffer()) {
                        General.sleep (300, 800);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /** Puts select item on sale at -15% guide price
     * 
     * @param item name of item to put on sale
     * @return true if item successfully placed on sale
     */
    public static boolean sellAtMinus15(int item) {
        if (!GEMainWindowOpen()) {
            GrandExchange.close();
            General.sleep (200, 700);
            openGE();
        }
        final RSItem[] items = Inventory.find(item);
        if (items != null && items.length > 0) {
            if (items[0].click("Offer")) {
                General.sleep (300, 800);
                if (Interfaces.get(465, 24).getChild(50).click("-5%")) {
                    if (Interfaces.get(465, 24).getChild(50).click("-5%")) {
                        if (Interfaces.get(465, 24).getChild(50).click("-5%")) {
                            General.sleep (200, 700);
                            if (GrandExchange.confirmOffer()) {
                                General.sleep (300, 800);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    /** Places an offer to buy select item at +5% guide price
     * 
     * @param item name of item to buy
     * @param amount amount of item to buy
     * @return true if buy offer placed successfully
     */
    public static boolean buyAtPlus5(String item, int amount) {
        if (amount < 1)
            return true;
        if (!GEMainWindowOpen()) {
            GrandExchange.close();
            General.sleep (200, 700);
            openGE();
        }
        for (int i = 7; i < 15; i++) {
            if (Interfaces.get(465, i) != null) {
                if (!Interfaces.get(465, i).getChild(3).isHidden()) {
                    if (Interfaces.get(465, i).getChild(3).click("Create Buy offer")) {
                        General.sleep(300, 800);
                        if (setItem(item)) {
                            General.sleep(300, 800);
                            if (GrandExchange.setQuantity(amount)) {
                                General.sleep (300, 800);
                                if (Interfaces.get(465, 24).getChild(53).click("+5%")) {
                                    General.sleep (300, 800);
                                    if (GrandExchange.confirmOffer()) {
                                        General.sleep (300, 800);
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    /** Sets an item for buying on New Offer window
     * 
     * @param item name of item to set
     * @return true if item set successfully
     */
    public static boolean setItem(String item) {
        if (!newOfferWindowOpen())
            return false;
        
        if (Interfaces.get(162, 33) != null) {  // What would you like to buy? text
            if (!Interfaces.get(162, 33).isHidden() 
                    && Interfaces.get(162, 38).getChild(1) == null) {
                Keyboard.typeString(item.toLowerCase());
                if (Timing.waitCondition(new Condition() {
                    @Override
                    public boolean active() {
                        General.sleep(100); // Add this in to reduce CPU usage
                        return Interfaces.get(162, 38).getChild(1) != null;
                    }
                }, General.random(5000, 8000))) {
                    for (int i = 1; i < Interfaces.get(162, 38).getChildren().length-1; i += 3) {
                        if (Interfaces.get(162, 38).getChild(i).getText().equals(item)) {
                            if (Interfaces.get(162, 38).getChild(i-1).click()) {
                                return true;
                            }
                            break;
                        }
                    }
                }
            } else { // open choose item menu
                if (Interfaces.get(465, 24).getChild(0) != null) {
                    if (!Interfaces.get(465, 24).getChild(0).isHidden()) {
                        if (Interfaces.get(465, 24).getChild(0).click("Choose item")) {
                            General.sleep(200, 400);
                            return setItem(item);
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    /** Places an offer to buy select item at +5x% guide price
     * 
     * @param item name of item to buy
     * @param amount amount of item to buy
     * @param x price percentage multiplier
     * @return true if buy offer placed successfully
     */
    public static boolean buyAtPlusX(String item, int amount, int x) {
        if (amount < 1) // already bought
            return true;
        if (!GEMainWindowOpen()) {
            GrandExchange.close();
            General.sleep (200, 700);
            openGE();
        }
        for (int i = 7; i < 15; i++) {
            if (Interfaces.get(465, i) != null) {   // empty offer
                if (!Interfaces.get(465, i).getChild(3).isHidden()) {
                    if (Interfaces.get(465, i).getChild(3).click("Create Buy offer")) {
                        General.sleep(300, 800);
                        while (!GrandExchange.getItemName().equals(item)) {
                            if (!newOfferWindowOpen())
                                return false;
                            setItem(item);
                            General.sleep(300, 800);
                        }
                        if (GrandExchange.setQuantity(amount)) {
                            General.sleep (300, 800);
                            for (int k = 0; k < x; k++) {
                                Interfaces.get(465, 24).getChild(53).click("+5%");
                                General.sleep (200, 400);
                            }
                            General.sleep (300, 800);
                            if (GrandExchange.confirmOffer()) {
                                General.sleep (300, 800);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    /** Returns true if New Offer window is open
     * 
     * @return true if New Offer window is open
     */
    private static boolean newOfferWindowOpen() {
        return GrandExchange.getWindowState() == GrandExchange.WINDOW_STATE.NEW_OFFER_WINDOW;
    }
    
    /** Returns true if main window is open
     * 
     * @return true if main window is open
     */
    public static boolean GEMainWindowOpen() {
        return GrandExchange.getWindowState() == GrandExchange.WINDOW_STATE.SELECTION_WINDOW;
    }
    
    /** Collects complete Grand Exchange offers
     * 
     * @return true if offers collected successfully
     */
    public static boolean collectCompleteOffers() {
        if (!GEMainWindowOpen())
            openGE();
        if (GEMainWindowOpen()) {
            final RSGEOffer[] offers = GrandExchange.getOffers();
            for (RSGEOffer offer : offers) {
                if (offer != null) {
                    if (offer.getStatus() == RSGEOffer.STATUS.COMPLETED) {
                        if (Interfaces.get(465, 6) != null) {
                            if (Interfaces.get(465, 6).getChild(1).click("Collect")) {
                                General.sleep (400, 800);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    /** Returns true if there are empty offers available
     * 
     * @return true if empty offers are available
     */
    public static boolean emptyOfferAvailable() {
        boolean completed = false;
        if (!GEMainWindowOpen())
                openGE();
        if (GEMainWindowOpen()) {
            final RSGEOffer[] offers = GrandExchange.getOffers();
            for (RSGEOffer offer : offers) {
                if (offer != null) {
                    if (offer.getStatus() == RSGEOffer.STATUS.COMPLETED) {
                        completed = true;
                    } else if (offer.getStatus() == RSGEOffer.STATUS.EMPTY 
                            || offer.getStatus() == RSGEOffer.STATUS.CANCELLED) {
                        return true;
                    }
                }
            }
            if (completed) {    // collect complete offers
                if (Interfaces.get(465, 6) != null) {
                    if (Interfaces.get(465, 6).getChild(1).click("Collect")) {
                        General.sleep (400, 800);
                        return emptyOfferAvailable();
                    }
                }
            }
        }
        
        return false;
    }
    
    /** Returns true if select item is on offer
     * 
     * @param item ID of item to check
     * @return true if select item is on offer
     */
    public static boolean itemOnOffer(int item) {
        if (GEMainWindowOpen()) {
            final RSGEOffer[] offers = GrandExchange.getOffers();
            for (RSGEOffer offer : offers) {
                if (offer != null) {
                    if (offer.getItemName() != null) {
                        if (offer.getItemID() == item || offer.getItemID() == item-1) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }
    
    /** Returns true if select item is on offer
     * 
     * @param item name of item to check
     * @return true if select item is on offer
     */
    public static boolean itemOnOffer(String item) {
        if (GEMainWindowOpen()) {
            final RSGEOffer[] offers = GrandExchange.getOffers();
            for (RSGEOffer offer : offers) {
                if (offer != null) {
                    if (offer.getItemName() != null) {
                        if (offer.getItemName().equals(item)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }
    
    /** Waits for offer to complete on a specific item
     * 
     * @param item name of item to wait for
     * @return true if offer for select item was complete
     */
    public static boolean waitForOfferToComplete(String item) {
        boolean incomplete = true;
        while (incomplete) {
            if (!GEMainWindowOpen())
                openGE();
            if (!itemOnOffer(item)) {
                incomplete = false;
                return true;
            }
            final RSGEOffer[] offers = GrandExchange.getOffers();
            for (RSGEOffer offer : offers) {
                if (offer != null) {
                    if (offer.getItemName() != null) {
                        if (offer.getItemName().equals(item)) {
                            if (offer.getStatus() == RSGEOffer.STATUS.COMPLETED) {
                                int itemsGot = Inventory.getCount(item);
                                if (Interfaces.get(465, 6) != null) {
                                    if (Interfaces.get(465, 6).getChild(1).click("Collect")) {
                                        if (offer.getType() == RSGEOffer.TYPE.BUY) {
                                            int loops = 0;
                                            while (Inventory.getCount(item) == itemsGot) {
                                                General.sleep (200, 500);
                                                loops++;
                                                if (loops > 10)
                                                    break;
                                            }
                                        }
                                        incomplete = false;
                                        return true;
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
            General.sleep (1500, 3000);
        }
        return false;
    }
    
    /** Waits for offer to complete on a specific item
     * 
     * @param item ID of item to wait for
     * @return true if offer for select item was complete
     */
    public static boolean waitForOfferToComplete(int item) {
        boolean incomplete = true;
        while (incomplete) {
            if (!GEMainWindowOpen())
                openGE();
            if (!itemOnOffer(item)) {
                incomplete = false;
                return true;
            }
            final RSGEOffer[] offers = GrandExchange.getOffers();
            for (RSGEOffer offer : offers) {
                if (offer != null) {
                    if (offer.getItemName() != null) {
                        if (offer.getItemID() == item || offer.getItemID() == item-1) {
                            if (offer.getStatus() == RSGEOffer.STATUS.COMPLETED) {
                                int itemsGot = Inventory.getCount(item);
                                if (Interfaces.get(465, 6) != null) {
                                    if (Interfaces.get(465, 6).getChild(1).click("Collect")) {
                                        if (offer.getType() == RSGEOffer.TYPE.BUY) {
                                            int loops = 0;
                                            while (Inventory.getCount(item) == itemsGot) {
                                                General.sleep (200, 500);
                                                loops++;
                                                if (loops > 10)
                                                    break;
                                            }
                                        }
                                        incomplete = false;
                                        return true;
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
            General.sleep (1500, 3000);
        }
        return false;
    }
    
    /** Waits for offer to complete on a specific item
     *  while taking breaks 
     * 
     * @param f instance of a script to break
     * @param item name of item to wait for
     * @param minBreak minimum time of break to take
     * @param maxBreak maximum time of break to take
     * @return true if offer for select item was complete
     */
    public static boolean waitForOfferToComplete(Script f, String item, int minBreak, int maxBreak) {
        boolean incomplete = true;
        while (incomplete) {
            if (!GEMainWindowOpen())
                openGE();
            if (!itemOnOffer(item)) {
                incomplete = false;
                return true;
            }
            if (GEMainWindowOpen()) {
                final RSGEOffer[] offers = GrandExchange.getOffers();
                for (RSGEOffer offer : offers) {
                    if (offer != null) {
                        if (offer.getItemName() != null) {
                            if (offer.getItemName().equals(item)) {
                                if (offer.getStatus() == RSGEOffer.STATUS.COMPLETED) {
                                    int itemsGot = Inventory.getCount(item);
                                    if (Interfaces.get(465, 6) != null) {
                                        if (Interfaces.get(465, 6).getChild(1).click("Collect")) {
                                           if (offer.getType() == RSGEOffer.TYPE.BUY) {
                                                int loops = 0;
                                                while (Inventory.getCount(item) == itemsGot) {
                                                    General.sleep (200, 500);
                                                    loops++;
                                                    if (loops > 10)
                                                        break;
                                                }
                                            }
                                            incomplete = false;
                                            return true;
                                        }
                                    }
                                } else {
                                    General.sleep (2000, 2500);
                                    if (GrandExchange.close()) {
                                        f.setLoginBotState(false);
                                        Login.logout();
                                        General.println ("Taking a sleep login bot off");
                                        General.sleep(minBreak, maxBreak);
                                        f.setLoginBotState(true);
                                        General.println ("Sleep is over login bot on");
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
            General.sleep (1500, 3000);
        }
        return false;
    }
}
