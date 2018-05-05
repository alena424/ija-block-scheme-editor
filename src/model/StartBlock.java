package model;

import java.util.HashMap;

public class StartBlock extends Block {
    public StartBlock(HashMap<Integer, Port> inputPortsPorts, HashMap<Integer, Port> outputPortsPorts) {
        super(inputPortsPorts,outputPortsPorts);
        icon = "lib/img/minus.png";
        selectedIcon = "lib/img/selected/minus.png";
        name = "Start";
        static_block = true; // nemenny blok
        static_x = 300; // static coordinates (to give to model block)
        static_y = 20;
        countInput = 0;
        countOutput = 100; // maximum of block to be connected to start block
        level = 0;
    }

    /**
     * Start block:
     * nothing to execute
     */
    public void execute(){
    }
}
