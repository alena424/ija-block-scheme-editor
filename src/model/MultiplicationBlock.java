/*
 * @file  MultiplicationBlock.java
 * @brief Class for addition block
 *
 * File containing specific functions for multiplication block
 *
 * @author Alena Tesarova (xtesar36)
 * @author Jan Sorm (xsormj00)
 */

package model;

import java.util.HashMap;
import java.util.List;

public class MultiplicationBlock extends Block {

    public MultiplicationBlock(HashMap<Integer, Port> inputPortsPorts, HashMap<Integer, Port> outputPortsPorts) {
        super(inputPortsPorts,outputPortsPorts);

        name = "Multiplication";
        icon = "lib/img/multiplication.png";
        selectedIcon = "lib/img/selected/multiplication.png";
        countInput = 2;
        countOutput = 1;
    }

    /**
     * Multiplication block:
     * Numbers from second input port are multiplied by number from first input port
     */
    public void execute() {
        // vezmeme hodnoty z input z portu 1 a vynasobime touto hodnotou hodnoty v portu2
        Double multiply = inputPorts.get(1).getHashOfValue().get("Name1");

        Double val1 = inputPorts.get(2).getHashOfValue().get("Name2");
        Double val2 = inputPorts.get(2).getHashOfValue().get("Name3");

        // nastavim output port
        outputPorts.get(1).getHashOfValue().put("Name2", val1*multiply);
        outputPorts.get(1).getHashOfValue().put("Name3", val2*multiply);

    }
}
