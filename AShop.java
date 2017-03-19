package scripts.AmphibiaAPI;

import org.tribot.api2007.Interfaces;

/**
 *
 * @author Deividas Dunda
 */
public class AShop {
    
    /** Returns true if shop is open
     * 
     * @return true if shop interface is open
     */
    public static boolean isShopScreenOpen() {
        return Interfaces.get(300) != null;
    }
    
    /** Returns true if shop has specific item
     * 
     * @param component item slot to check
     * @param item name of item to check
     * @return true if shops item name contains item
     */
    public static boolean itemInShopNameContains(int component, String item) {
        if (Interfaces.get(300, 2) != null) {
            if (Interfaces.get(300, 2).getChild(component) != null) {
                if (Interfaces.get(300, 2).getChild(component).getComponentName().contains(item))
                    return true;
            }
        }
        
        return false;
    }
    
    /** Returns component stack for shop item
     * 
     * @param component item slot to get
     * @return component stack for shop item
     */
    public static int getShopItemStack(int component) {
        if (Interfaces.get(300, 2) != null) {
            return Interfaces.get(300, 2).getChild(component).getComponentStack();
        }
        
        return 0;
    }
    
    /** Buys 10 of shop item
     * 
     * @param component shop item slot
     * @return true if item was bought successfully
     */
    public static boolean buy10Shop(int component) {
        if (Interfaces.get(300, 2) != null) {
            if (Interfaces.get(300, 2).getChild(component).click("Buy 10"))
                return true;
        }
        return false;
    }
    
    /** Buys 5 of shop item
     * 
     * @param component shop item slot
     * @return true if item was bought successfully
     */
    public static boolean buy5Shop(int component) {
        if (Interfaces.get(300, 2) != null) {
            if (Interfaces.get(300, 2).getChild(component).click("Buy 5"))
                return true;
        }
        return false;
    }
    
    /** Buys 1 of shop item
     * 
     * @param component shop item slot
     * @return true if item was bought successfully
     */
    public static boolean buy1Shop(int component) {
        if (Interfaces.get(300, 2) != null) {
            if (Interfaces.get(300, 2).getChild(component).click("Buy 1"))
                return true;
        }
        return false;
    }
    
    /** Closes shop window
     * 
     * @return true if shop window was closed successfully
     */
    public static boolean closeShop() {
        if (Interfaces.get(300, 1) != null) {
            if (Interfaces.get(300, 1).getChild(11).click("Close")) {
                return true;
            }
        }
        
        return false;
    }
}
