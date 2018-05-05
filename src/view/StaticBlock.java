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
