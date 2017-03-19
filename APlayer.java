
package scripts.AmphibiaAPI;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Players;
import org.tribot.api2007.Skills;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.api2007.types.RSTile;

/**
 *
 * @author Deividas Dunda
 */
public class APlayer {
    /** Returns the percentage of HP remaining 
     * 
     * @return percentage of current HP remaining
     */
    public static double getHPPercent() {
        double currentHP = Skills.getCurrentLevel(Skills.SKILLS.HITPOINTS);
        double totalHP = Skills.getActualLevel(Skills.SKILLS.HITPOINTS);
        return currentHP / totalHP * 100;
    } 
    
    /** Returns the percentage of Special Attack remaining
     * 
     * @return percentage of special attack remaining
     */
    public static int specialAttackPercent() {
        return  (Game.getSetting(300)/10);
    }
    
    /** Returns true if Special Attack is active
     * 
     * @return true if Special Attack is active
     */
    public static boolean specialAttackActivated() {
        RSInterface specPercentInterface = Interfaces.get(593, 34);
        if (specPercentInterface.getTextColour() == 16776960) { // 16776960 is the yellow spec attack text color
            return true;
        }
        return false;
    }
    
    /** Activates Special Attack
     * 
     * @return true if Special Attack used successfully
     */
    public static boolean specialAttackUse() {
        RSInterface specClickInterface = Interfaces.get(593, 30);
        GameTab.TABS.COMBAT.open();
        if (GameTab.TABS.COMBAT.isOpen()) {
            specClickInterface.click("Use");
            General.sleep(50, 100);
            return true;
        }
        return false;
    }
    
    /** Returns true if player is poisoned
     * 
     * @return true if player is poisoned
     */
    public static boolean isPoisoned() {
        return Game.getSetting(102) > 0;
    }
    
    /** Returns true if we are next to a bank
     * 
     * @return true if we are near a bank
     */
    public static boolean isInBank() {
        final RSObject[] booths = Objects.findNearest(10, "Bank booth");
	if (booths != null && booths.length > 1) {
            if (booths[0].isOnScreen())
		return true;
	}
        
        final RSObject[] chests = Objects.findNearest(10, "Bank chest");
	if (chests != null && chests.length > 0) {
            if (chests[0].isOnScreen())
		return true;
	}
        
	final RSNPC[] bankers = NPCs.findNearest("Banker", "Magnus Gram", "Ghost banker");
	if (bankers != null && bankers.length > 0)
            if (bankers[0].isOnScreen()) {
                return true;
            } else {
                Camera.turnToTile(bankers[0].getPosition());
            }
        
	return false;
    }
    
    /** Returns true if player is in combat with an NPC
     * 
     * @return true if we are in combat with an NPC
     */
    public static boolean checkCombat() {
      RSNPC[] all = NPCs.getAll();
 
      for (RSNPC n: all) {
          if ((n.isInteractingWithMe() && Player.getRSPlayer().isInCombat()) || (Player.getRSPlayer().getInteractingCharacter() == n && n.isInCombat())) {
              return true;
          }
      }
      return false;
    }
    
    /** Returns true if another player is attacking us
     * 
     * @return true if a player is attacking us
     */
    public static boolean isPlayerAttackingMe() {
        RSPlayer[] players = Players.getAll();
 
        for (RSPlayer p: players) {
            if (p.isInteractingWithMe() && Player.getRSPlayer().isInCombat() && p.getAnimation() != 1) {
                return true;
            }
        }
        return false;
    }
    
    /** Returns true if a PKer is detected nearby
     * 
     * @return true if there is a potential PKer nearby
     */
    public static boolean inDanger() {
        RSPlayer[] players = Players.getAll();
 
        for (RSPlayer p: players) {
            if (!p.equals(Player.getRSPlayer()) && p.getSkullIcon() == 0 && Combat.getWildernessLevel() != 0
                    && Math.abs(p.getCombatLevel()-Player.getRSPlayer().getCombatLevel()) <= Combat.getWildernessLevel()) {
                return true;
            }
        }
        return false;
    }
    
    /** Returns true if a potential PKer is nearby
     * 
     * @param range range in which to check for PKers
     * @return true if a potential PKer was detected nearby
     */
    public static boolean potentialPKerNearby(int range) {
        RSPlayer[] players = Players.getAll();
        RSTile myPos = Player.getPosition();
        for (RSPlayer p: players) {
            if (!p.equals(Player.getRSPlayer()) && p.getPosition().distanceTo(myPos) <= range && Combat.getWildernessLevel() != 0 && Math.abs(p.getCombatLevel()-Player.getRSPlayer().getCombatLevel()) <= Combat.getWildernessLevel()) {
                return true;
            }
        }
        return false;
    }
}
