package model;


import java.util.HashMap;

public class EndBLock extends Block {
    public EndBLock(HashMap<Integer, Port> inputPortsPorts, HashMap<Integer, Port> outputPortsPorts) {
        super(inputPortsPorts,outputPortsPorts);
        icon = "img/equals.png";
        selectedIcon = "img/selected/equals.png";
        name = "End";
        static_block = true; // nemenny blok
        static_x = 300;
        static_y = 546;
        countInput = 100;
        countOutput = 0;
    }
    public void execute(){
        String finalStr = "";
        // funkce by mela otevrit ukazat vysledek
        for ( Integer i : inputPorts.keySet() ){
            finalStr += inputPorts.get(i).value + "\n";
        }
    }
}
