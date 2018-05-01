/*
 * @file  InvertBlock.java
 * @brief Class for addition block
 *
 * File containing specific functions for invert block
 *
 * @author Alena Tesarova (xtesar36)
 * @author Jan Sorm (xsormj00)
 */

package model;

import java.util.HashMap;
import java.util.List;

public class InvertBlock extends Block {

    public InvertBlock(HashMap<Integer, Port> inputPortsPorts, HashMap<Integer, Port> outputPortsPorts) {
        super(inputPortsPorts,outputPortsPorts);
        icon = "img/invert.png";
        name = "Invert";
        selectedIcon = "img/selected/invert.png";
        countInput = 2;
        countOutput = 2;
    }
    public void execute() {
        // vezmeme hodnoty z input z portu 1 a zinvertujeme je na hodnoty v portu2
        Double val1 = inputPorts.get(1).getHashOfValue().get("Name1");

        Double val2 = inputPorts.get(2).getHashOfValue().get("Name2");
        Double val3 = inputPorts.get(2).getHashOfValue().get("Name3");

        // nastavim output port
        outputPorts.get(1).getHashOfValue().put("Name1", -val1);

        outputPorts.get(2).getHashOfValue().put("Name2", -val2);
        outputPorts.get(2).getHashOfValue().put("Name2", -val3);

    }
}
