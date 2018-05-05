/*
 * @file  Block.java
 * @brief Block class for blocks on right panel (non static)
 *
 *
 * @author Alena Tesarova (xtesar36)
 * @author Jan Sorm (xsormj00)
 */
package view;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class Block extends AbstractBlock {

    public Block(String identification, JLabel label) {
        super(identification, label);
        // defaultne se nachazi tam, kde se vygeneroval
        setX(20);
        setY(20);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        clickPosition.setLocation(event.getX(), event.getY());
    }
}
