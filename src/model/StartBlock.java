package model;

import java.util.HashMap;

public class StartBlock extends Block {
    public StartBlock(HashMap<Integer, Port> inputPortsPorts, HashMap<Integer, Port> outputPortsPorts) {
        super(inputPortsPorts,outputPortsPorts);
        icon = "img/minus.png";
        selectedIcon = "img/selected/minus.png";
        name = "Start";
        static_block = true; // nemenny blok
        static_x = 300;
        static_y = 20;
        countInput = 0;
        countOutput = 100;
        level = 0;
    }
    public void execute(){
        // funkce by mela otevrit okno pro vyplneni udaju a vsechny je dat na vstup
        int i = 1;
        for ( Integer portId : inputPorts.keySet() ) {
            System.out.println("Portid: " + portId);
            //HashMap<Integer, Port> ports = new HashMap<Integer, Port>(this.inputPorts.get( i ));
            for ( String name : inputPorts.get(portId).getHashOfValue().keySet() ) {

                this.inputPorts.get(portId).getHashOfValue().put(name, 2.0 + i);
                i++;

            }
        }
    }

}
