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

        //super( scheme, level );
        icon = "img/addition.png";
        selectedIcon = "img/selected/addition-sel.png";
        name = "Addition";
        setCountInput(3);
        setCountOutput(1);
    }

    public void execute() {
       HashMap<String,Double> sum_hash = new HashMap<String, Double>();
       Double sum = 0.0;
        //double[] sum = new double[ this.input[i].getHashOfValue().size() ];
        for ( Integer portId : inputPorts.keySet() ) {
            System.out.println("Portid: " + portId);
            //HashMap<Integer, Port> ports = new HashMap<Integer, Port>(this.inputPorts.get( i ));
            for ( String name : inputPorts.get(portId).getHashOfValue().keySet() ) {

                sum += this.inputPorts.get(portId).getHashOfValue().get(name);
                System.out.println("Sum: " + sum);
            }
        }
        // nastavim output port
        outputPorts.get(1).getHashOfValue().put("Name1", sum);

    }
}
