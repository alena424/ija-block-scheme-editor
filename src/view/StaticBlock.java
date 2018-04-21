package view;

import javax.swing.*;

public class StaticBlock extends AbstractBlock {
    public StaticBlock(String identification, JLabel label) {
        super(identification, label);
        static_object = true;
    }
}
