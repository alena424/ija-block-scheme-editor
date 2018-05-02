/*
 * @file  RandomBlock.java
 * @brief Class for random block
 *
 * File containing specific functions for random block
 *
 * @author Alena Tesarova (xtesar36)
 * @author Jan Sorm (xsormj00)
 */

package model;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RandomBlock extends Block {
    public RandomBlock(HashMap<Integer, Port> inputPortsPorts, HashMap<Integer, Port> outputPortsPorts) {
        super(inputPortsPorts,outputPortsPorts);

        icon = "img/random.png";
        name = "Random";
        selectedIcon = "img/selected/random.png";
        countInput = 1;
        countOutput = 2;
    }
    public void execute() {
        Random rand = new Random();
        // vezmeme hodnoty z input z portu 1 a pristeme k ni random hodnotu od 1 - 10
        Double val = inputPorts.get(1).getHashOfValue().get("Name1");

        // nastavim output port
        // nastavim output port
        outputPorts.get(1).getHashOfValue().put("Name1",val+rand.nextInt(10) );

        outputPorts.get(2).getHashOfValue().put("Name2",val+rand.nextInt(10) );
        outputPorts.get(2).getHashOfValue().put("Name3", val+rand.nextInt(10) );

        //outputPorts.get(1).getHashOfValue().put("Name2", val+rand.nextInt(10) );
        //outputPorts.get(1).getHashOfValue().put("Name3", val+rand.nextInt(10));

    }
}
