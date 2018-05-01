/*
 * @file  Division.java
 * @brief Class for division block
 *
 * File containing specific functions for divison block
 *
 * @author Alena Tesarova (xtesar36)
 * @author Jan Sorm (xsormj00)
 */

package model;

import java.util.HashMap;
import java.util.List;

public class DivisionBlock extends Block {

    public DivisionBlock(HashMap<Integer, Port> inputPortsPorts, HashMap<Integer, Port> outputPortsPorts) {
        super(inputPortsPorts,outputPortsPorts);
        /*super( scheme, level )*/;
        icon = "img/division.png";
        selectedIcon = "img/selected/division.png";
        name = "Division";
        countInput = 2;
        countOutput = 1;
    }
    public void execute() {

        // vezmeme hodnoty z input z portu 1 a vydelime touto hodnotou hodnoty v portu2
        Double divider = inputPorts.get(1).getHashOfValue().get("Name1");

        if ( divider == 0 ){
            System.err.println("Can not devide by zero");
            return;
        }
        Double val1 = inputPorts.get(2).getHashOfValue().get("Name2");
        Double val2 = inputPorts.get(2).getHashOfValue().get("Name3");

        // nastavim output port
        outputPorts.get(1).getHashOfValue().put("Name2", val1/divider);
        outputPorts.get(1).getHashOfValue().put("Name3", val2/divider);
    }
}
