
package scripts.AmphibiaAPI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;

/**
 *
 * @author Deividas Dunda
 */
public class AGUI {
    /** Draws shadowed text on the screen
     * @param g2d
     * @param font font to draw
     * @param text text to draw
     * @param x X position to draw on the screen
     * @param y Y position to draw on the screen
     */
    public static void drawShadowText(Graphics2D g2d, Font font, String text, int x, int y){
        TextLayout textLayout = new TextLayout(text, font, g2d.getFontRenderContext());
	    
        g2d.setPaint(Color.BLACK);
        textLayout.draw(g2d, x+1, y+1);

        g2d.setPaint(Color.WHITE);
        textLayout.draw(g2d, x, y);
    }
}
