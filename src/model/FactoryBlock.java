package model;

import java.util.HashMap;

/**
 * Class for creating new blocks
 */
public class FactoryBlock {
    public Block getBlock(String blockName, HashMap<Integer, Port> inputP, HashMap<Integer, Port> outputP){
        if ( blockName == null ){
            return null;
        }
        if ( blockName.equalsIgnoreCase("Addition" )){
            return new AdditionBlock(inputP,outputP);
        }
        if ( blockName.equalsIgnoreCase("Multiplication" )){
            return new MultiplicationBlock(inputP,outputP);
        }
        if ( blockName.equalsIgnoreCase("Division" )){
            return new DivisionBlock(inputP,outputP);
        }
        if ( blockName.equalsIgnoreCase("Random" )){
            return new RandomBlock(inputP,outputP);
        }
        if ( blockName.equalsIgnoreCase("Invert" )){
            return new InvertBlock(inputP,outputP);
        }
        if ( blockName.equalsIgnoreCase("Start" )){
            return new StartBlock(inputP,outputP);
        }
        if ( blockName.equalsIgnoreCase("End" )){
            return new EndBLock(inputP,outputP);
        }
        return null;

    }
}
