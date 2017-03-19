
package scripts.AmphibiaAPI;

import java.awt.Color;
import static org.tribot.api.General.println;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Login;
import org.tribot.api2007.Player;
import org.tribot.api2007.Players;
import org.tribot.api2007.Screen;
import org.tribot.api2007.WorldHopper;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.api2007.types.RSTile;


/**
 *
 * @author Deividas Dunda
 */
public class AGeneral {
    
    private final static Color WORLD_SWITCH_COLOUR = new Color(189, 152, 57);
    
    /** Returns true if any other players are detected nearby
     *
     * @param range range in which to check for players around
     * @return true if there are other players nearby
     */
    public static boolean peopleNearby(int range) {
        RSPlayer[] players = Players.getAll();
        RSTile myPos = Player.getPosition();
        if (players != null && players.length > 0
                && Login.getLoginState() != Login.STATE.WELCOMESCREEN
                && Login.getLoginState() == Login.STATE.INGAME) {
            for (RSPlayer player : players) {
                if (player.getPosition().distanceTo(myPos) <= range
                        && !player.equals(Player.getRSPlayer())) {
                    println(player.getName()+" has interrupted the script");
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /** Returns current wilderness level
     * 
     * @return current wilderness level
     */
    public static int getWildLevel() {
        if (Interfaces.get(90, 46) != null) {
            if (Interfaces.get(90, 46).getText() != null) {
                return Integer.parseInt(Interfaces.get(90, 46).getText().substring(7));
            }
        }
        
        return 0;
    }
    
    /** Returns true if select world is not blacklisted
     * 
     * @param rWorld world to check
     * @return true if world is not blacklisted
     */
    public static boolean isWorldGood(int rWorld) {
        if (rWorld == WorldHopper.getWorld() || rWorld == 0 ||
                rWorld == 11 || rWorld == 20 || rWorld == 25 || rWorld == 37 ||
                rWorld == 38 || rWorld == 45 || rWorld == 49 || rWorld == 53 ||
                rWorld == 57 || rWorld == 61 || rWorld == 66 || rWorld == 73 ||
                rWorld == 74 || rWorld == 78 || rWorld == 81 || rWorld == 85)
            return false;
        
        return true;
    }
    
    /** Returns true if we are currently on a world select screen
     * 
     * @return true if currently on a world select screen
     */
    public static final boolean isAtWorldHopScreen() {
        return Screen.getColorAt(11, 0).equals(WORLD_SWITCH_COLOUR);
    }
}
