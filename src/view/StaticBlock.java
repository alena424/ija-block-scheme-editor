/*
 * @file  StaticBlock.java
 * @brief Class for StaticBlock
 *
 * File containing specific functions start and end block in view.
 *
 * @author Alena Tesarova (xtesar36)
 * @author Jan Sorm (xsormj00)
 */

package view;

import javax.swing.*;

/**
 * StaticBlock class was created for start and end block
 */
public class StaticBlock extends AbstractBlock {
    public StaticBlock(String identification, JLabel label) {
        super(identification, label);
        static_object = true;
    }
}
