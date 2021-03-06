package planning.view;


import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.plaf.basic.BasicLabelUI;

/**
 * @author Torben Zeine
 *
 *         Ermöglicht die Darstellung eins Labels mit Vertikaler Schrift
 */
class VerticalLabelUI extends BasicLabelUI {

    static {
        labelUI = new VerticalLabelUI(false);
    }
    protected boolean clockwise;// Uhrzeigerrichtung?
    // Kompnenten des Labels das gedreht wird
    private static Rectangle paintIconR = new Rectangle();
    private static Rectangle paintTextR = new Rectangle();
    private static Rectangle paintViewR = new Rectangle();
    private static Insets paintViewInsets = new Insets(0, 0, 0, 0);

    /**
     * Konstruktor
     *
     * @param clockwise = im Uhrzeigerrichtung drehen, false = gegen
     *        Uhrzeigerrichtung
     */
    VerticalLabelUI(boolean clockwise) {
        super();
        this.clockwise = clockwise;
    }

    @Override
    /*
     * Gibt Größe zurück, gedreht
     */
    public Dimension getPreferredSize(JComponent c) {
        Dimension dim = super.getPreferredSize(c);
        return new Dimension(dim.height, dim.width);
    }

    /**
     *
     * Zeichnet das JLabel gedreht neu
     */
    @Override
    public void paint(Graphics g, JComponent c) {
        JLabel label = (JLabel) c;
        String text = label.getText();
        Icon icon = (label.isEnabled()) ? label.getIcon() : label.getDisabledIcon();
        // Leeres Label brauch nicht gedreht werden
        if ((icon == null) && (text == null)) {
            return;
        }

        FontMetrics fm = g.getFontMetrics();
        paintViewInsets = c.getInsets(paintViewInsets);
        paintViewR.x = paintViewInsets.left;
        paintViewR.y = paintViewInsets.top;
        // Höhe und Breite invertieren
        paintViewR.height = c.getWidth() - (paintViewInsets.left + paintViewInsets.right);
        paintViewR.width = c.getHeight() - (paintViewInsets.top + paintViewInsets.bottom);
        paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
        paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;
        String clippedText = layoutCL(label, fm, text, icon, paintViewR, paintIconR, paintTextR);
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform tr = g2.getTransform();
        // Im Uhrzeigersinn drehen
        if (clockwise) {
            g2.rotate(Math.PI / 2);
            g2.translate(0, -c.getWidth());
        } // Gegen den Uhrzeigersinn drehen
        else {
            g2.rotate(-Math.PI / 2);
            g2.translate(-c.getHeight(), 0);
        }
        // Icon drehen
        if (icon != null) {
            icon.paintIcon(c, g, paintIconR.x, paintIconR.y);
        }
        if (text != null) {
            int textX = paintTextR.x;
            int textY = paintTextR.y + fm.getAscent();
            // Enabledes Label
            if (label.isEnabled()) {
                paintEnabledText(label, g, clippedText, textX, textY);
            } // Diabletes Label
            else {
                paintDisabledText(label, g, clippedText, textX, textY);
            }
        }
        // Label neu setzen
        g2.setTransform(tr);
    }
}