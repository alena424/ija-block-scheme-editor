/*
 * @file  AdditionBlock.java
 * @brief Class for addition block
 *
 * File containing specific functions for addition block
 *
 * @author Alena Tesarova (xtesar36)
 * @author Jan Sorm (xsormj00)
 */

package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class AdditionBlock extends Block {

    public AdditionBlock(HashMap<Integer, Port> inputPortsPorts, HashMap<Integer, Port> outputPortsPorts) {
        super(inputPortsPorts,outputPortsPorts);
        icon = "img/addition.png"; // locations of icon for view
        selectedIcon = "img/selected/addition-sel.png"; // location of selected icon for view
        name = "Addition"; // name of block
        setCountInput(3);
        setCountOutput(1);
    }
    public void execute() {
       HashMap<String,Double> sum_hash = new HashMap<String, Double>();
       Double sum = 0.0;
        for ( Integer portId : inputPorts.keySet() ) {
            for ( String name : inputPorts.get(portId).getHashOfValue().keySet() ) {
                sum += this.inputPorts.get(portId).getHashOfValue().get(name);
            }
        }
        // nastavim output port
        outputPorts.get(1).getHashOfValue().put("Name1", sum);
    }
}
