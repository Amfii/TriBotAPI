package scripts.AmphibiaAPI;

import java.util.HashMap;
import org.tribot.api.General;
import static org.tribot.api.General.sleep;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Game;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSItem;

/**
 * Static utility class with various methods that are related
 * to bank / banking.
 *
 * @author Deividas Dunda
 */
public class ABanking {
    
    /**
     * The master index for the banking Interface.
     */
    private static final int BANKING_INTERFACE = 12;

    /**
     * The child index for the bank space in the banking Interface.
     */
    private static final int BANK_AMOUNT_CHILD_INTERFACE = 5;

    /**
     * Gets the amount of space in the RSPlayers bank.
     *
     * @return The amount of space in the RSPlayers bank.
     */
    public static int getCurrentUsedBankSpace() {
        final RSInterface amount = Interfaces.get(BANKING_INTERFACE, BANK_AMOUNT_CHILD_INTERFACE);
        if (amount == null)
            return -1;

        String text = amount.getText();
        if (text == null)
            return -1;

        if (text.length() <= 0)
            return -1;

        int parse = Integer.parseInt(text);
        if (parse > 0)
            return parse;

        return -1;
    }

    /**
     * Checks if the bank is loaded.
     *
     * @return True if the bank is loaded; false otherwise.
     */
    public static boolean isBankItemsLoaded() {
        return getCurrentUsedBankSpace() == Banking.getAll().length;
    }
    
    /**
     * Returns whether the noted game setting is on
     * 
     * @return true if noted is on
     */
    public static boolean isNotedOn(){
        return Game.getSetting(115) == 1;
    }
    
    /**
     * Returns whether the noted game setting was 
     * set successfully
     * 
     * @param noted if we should set the setting on or off
     * @return true if noted game setting was changed successfully
     */
    public static boolean setNoted(boolean noted) {
        if(Banking.isBankScreenOpen()) {			
            if(noted && !isNotedOn()) { // turn note setting on				
                RSInterfaceChild notedInterfaceChild = Interfaces.get(12, 23);				
                if(notedInterfaceChild != null
                        && notedInterfaceChild.click("Note")) {			
                    Timing.waitCondition(new Condition() {
                        @Override
                        public boolean active() {
                            General.sleep(10, 30);
                            return isNotedOn();
                        }
                    }, General.random(800, 1200));
                    return isNotedOn();
                }
            } else if(!noted && isNotedOn()) {  // turn note setting off	
                RSInterfaceChild itemInterfaceChild = Interfaces.get(12, 21);
                if(itemInterfaceChild != null && itemInterfaceChild.click("Item")) {	
                    Timing.waitCondition(new Condition() {	
                        @Override
                        public boolean active() {
                            General.sleep(10, 30);
                            return !isNotedOn();
                        }
                    }, General.random(800, 1200));
                    return !isNotedOn();
                }
            }
        }
        return false;
    }
    

    /**
     * Deposits inventory to bank in a random fashion 
     * 
     */
    public static void depositInventory() {
        RSItem[] inventoryItems = Inventory.getAll();
        if (inventoryItems.length > 0 && inventoryItems.length < 5
                && General.random(1, 2) == 1) {
            for (RSItem a : inventoryItems) {
                Banking.depositItem(a, 1);
                sleep (300, 800);
            }
        } else if (inventoryItems.length > 0) {
            if (Banking.depositAll() > 0) {
                Timing.waitCondition(new Condition() {
                    @Override
                    public boolean active() {
                        General.sleep(100); // Add this in to reduce CPU usage
                        return Inventory.getAll().length == 0;
                    }
                }, General.random(3000, 8000));
            }
        }
    }
    
    /**
     * Withdraws items from bank in a random fashion
     *
     * @param amount the amount to withdraw
     * @param item item ID to withdraw
     * @return true if withdrew items successfully
     */
    public static boolean withdrawFromBank(int amount, int item) {
        if (amount > 0 && amount < 5 && General.random(1, 4) < 4) {
            for (int i = 0; i < amount; i++) {
                if (Banking.withdraw(1, item)) {
                    sleep (500, 800);
                } else {
                    return false;
                }
            }
            return true;
        } else if (amount == 0) {
//            if (General.random(1, 4) == 1) {
//                Banking.withdraw(-1, item);
//            } else {
                if (Banking.withdraw(amount, item))
                    return true;
//            }
        } else
            if (Banking.withdraw(amount, item))
                return true;
        
        return false;
    }
    
    /**
     * Withdraws items from bank in a random fashion
     * 
     * @param amount the amount to withdraw
     * @param item items ID's to withdraw
     * @return true if withdrew items successfully
     */
    public static boolean withdrawFromBank(int amount, int[] item) {
        if (amount > 0 && amount < 5 && General.random(1, 4) < 4) {
            for (int i = 0; i < amount; i++) {
                if (Banking.withdraw(1, item)) {
                    sleep (500, 800);
                } else {
                    return false;
                }
            }
            return true;
        } else if (amount == 0) {
//            if (General.random(1, 4) == 1)
//                if (Banking.withdraw(-1, item))
//                    return true;
//            else
                if (Banking.withdraw(amount, item))
                    return true;
        } else
            if (Banking.withdraw(amount, item))
                return true;
        
        return false;
    }
    
    /**
     * Withdraws items from bank in a random fashion
     * 
     * @param amount the amount to withdraw
     * @param item item name to withdraw
     * @return true if withdrew items successfully
     */
    public static boolean withdrawFromBank(int amount, String item) {
        if (amount > 0 && amount < 5 && General.random(1, 4) < 4) {
            for (int i = 0; i < amount; i++) {
                if (Banking.withdraw(1, item)) {
                    sleep (500, 800);
                } else {
                    return false;
                }
            }
            return true;
        } else if (amount == 0) {
//            if (General.random(1, 4) == 1)
//                if (Banking.withdraw(-1, item))
//                    return true;
//            else
                if (Banking.withdraw(amount, item))
                    return true;
        } else
            if (Banking.withdraw(amount, item))
                return true;
        
        return false;
    }
    
    /**
     * Returns true if bank has required supplies
     * 
     * @param set item names and amounts to check
     * @return true if supplies are in bank
     */
    public static boolean supplyCheck(final HashMap<String, Integer> set){
        if (!Banking.isBankScreenOpen()){
            General.println("Bank is not open. Could not determine supplies. Assuming we have sufficient supplies.");
            return true;
        }
        return Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(100);
                for (String item : set.keySet()){
                    RSItem[] bankItem = Banking.find(item);
                    if (bankItem.length <= 0 || bankItem[0].getStack() < set.get(item)){
                        if (!isBankItemsLoaded()){
                            General.println("Couldn't find bank items, but not fully loaded. Assuming we have sufficient supplies.");
                            return true;
                        }
                        return false;
                    }
                }
                return true;
            }
        }, General.random(1500, 2000));
    }
    
    /**
     * Returns true if bank has one of the
     * listed supplies
     * 
     * @param set item names and amounts to check
     * @return true if at least one of required supplies are in bank
     */
    public static boolean hasOneOf(final HashMap<String, Integer> set){
        if (!Banking.isBankScreenOpen()){
            General.println("Bank is not open. Could not determine supplies. Assuming we have sufficient supplies.");
            return true;
        }
        return Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(100);
                for (String item : set.keySet()){
                    RSItem[] bankItem = Banking.find(item);
                    if (bankItem.length > 0) {
                        if (bankItem[0].getStack() >= set.get(item)){
                            return true;
                        }
                    }
                }
                if (!isBankItemsLoaded()){
                    General.println("Couldn't find bank items, but not fully loaded. Assuming we have sufficient supplies.");
                    return true;
                }
                return false;
            }
        }, General.random(1500, 2000));
    }
    
    /**
     * Returns amount of item missing in bank
     * 
     * @param item item name to check
     * @param count amount needed in bank
     * @return amount of item missing in bank
     */
    public static int missingInBank(String item, int count) {
        if (!Banking.isBankScreenOpen()){
            General.println("Bank is not open. Could not determine supplies. Assuming we have sufficient supplies.");
            return 0;
        }
        RSItem[] bankItem = Banking.find(item);
        if (bankItem != null && bankItem.length > 0)
            return count - bankItem[0].getStack();
            
        return count;
    }
}
