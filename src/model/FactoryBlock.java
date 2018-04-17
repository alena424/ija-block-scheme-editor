package model;

import view.AbstractBlock;

public class FactoryBlock {
    public Block getBlock(String blockName){
        if ( blockName == null ){
            return null;
        }
        if ( blockName.equalsIgnoreCase("Addition" )){
            return new AdditionBlock();
        }
        if ( blockName.equalsIgnoreCase("Multiplication" )){
            return new MultiplicationBlock();
        }
        if ( blockName.equalsIgnoreCase("Division" )){
            return new DivisionBlock();
        }
        if ( blockName.equalsIgnoreCase("Random" )){
            return new RandomBlock();
        }
        if ( blockName.equalsIgnoreCase("Invert" )){
            return new InvertBlock();
        }
        if ( blockName.equalsIgnoreCase("Start" )){
            return new StartBlock();
        }
        if ( blockName.equalsIgnoreCase("End" )){
            return new EndBLock();
        }
        return null;

    }
}
