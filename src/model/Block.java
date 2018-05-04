/*
 * @file  Block.java
 * @brief Abstract class for block
 *
 * File containing functions for block
 *
 * @author Alena Tesarova (xtesar36)
 * @author Jan Sorm (xsormj00)
 */

package model;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;


public abstract class Block {

    protected Integer id;
    protected HashMap<Integer,Port> inputPorts; //= new HashMap<Integer, Port>();
    protected HashMap<Integer,Port> outputPorts; //= new HashMap<Integer, Port>();
    protected Integer level = 0;
    public Integer countInput;
    public Integer countOutput;
    protected String name;
    public Integer static_x = 0;
    public Integer static_y = 0;

    public boolean static_block = false; // flag, block can not be deleted and position is static
    // defaultIcon
    public String icon = "img/default.png";
    public String selectedIcon = "img/default.png";

    public Block (HashMap<Integer,Port> inputPortsPorts, HashMap<Integer,Port> outputPortsPorts) {
        inputPorts = new HashMap<Integer, Port>(inputPortsPorts);
        outputPorts = new HashMap<Integer, Port>(outputPortsPorts);
        // set all ports to this block
        for (Integer i : inputPortsPorts.keySet()){
            inputPortsPorts.get(i).setOwnerBlock(this);
        }
        for (Integer i : outputPorts.keySet()){
            outputPorts.get(i).setOwnerBlock(this);
        }
    }

    protected Block() {
    }

    public HashMap<Integer, Port> getInputPorts() {
        return inputPorts;
    }

    public HashMap<Integer, Port> getOutputPorts() {
        return outputPorts;
    }

    public void setCountInput(Integer countInput) {
        this.countInput = countInput;
    }

    public void setCountOutput(Integer countOutput) {
        this.countOutput = countOutput;
    }

    public Integer getCountInput() {
        return countInput;
    }

    public Integer getCountOutput() {
        return countOutput;
    }

    public String getSelectedIcon() {
        return selectedIcon;
    }

    public String getName() {
        return name;
    }

    public ImageIcon getIcon(){
        return new ImageIcon(icon);
    }
    public String getPathOfIcon(){
        return icon;
    }

    public Integer getId() {
        return this.id;
    }

    public Integer getLevel() {
        return this.level;
    }

    public void setLevel( Integer level ) {
        this.level = level;
    }

    public boolean addinputPort( Integer id, Port port ) {
        // check ID
        if ( inputPorts.get(id) == null){
            inputPorts.put(id,port);
        } else {
            System.err.println("clicked ID of input port is already used");
            System.err.println(inputPorts);
            return false;
        }
        port.setOwnerBlock(this);
        return true;
    }

    public boolean addoutputPort( Integer id, Port port) {
        if ( outputPorts.get(id) == null ){
            outputPorts.put(id,port);
        } else {
            System.err.println("clicked ID of output port is already used");
            System.err.println(outputPorts);
            return false;
        }
        port.setOwnerBlock(this);
        return true;
        //this.countOutput++;
    }

    public void setInputPorts( HashMap map, Integer order ) {
        if ( this.level == 0 && this.countInput > order && this.inputPorts.get( order ).isFree() ) {
            this.inputPorts.get( order ).setValue( map );
        }
    }

    public void setOutputPorts( HashMap map, Integer order ) {
            this.outputPorts.get( order ).setValue( map );
    }

    public boolean isFreeInput() {
        for ( Port port : this.inputPorts.values() ) {
            if ( port.isFree() ) {
                return true;
            }
        }
        return false;
    }
    public boolean isFreeOutput() {
        for ( Port port : this.outputPorts.values() ) {
            if ( port.isFree() ) {
                return true;
            }
        }
        return false;
    }
    public void removeInputPort(Port port){
        inputPorts.remove(port.getId());
    }
    public void removeOutputPort(Port port){
        outputPorts.remove(port.getId());
    }

    public void deleteBlockConnection() {
        for ( Port port : this.inputPorts.values() ) {
            port.unsetConnection();
        }
        for ( Port port : this.outputPorts.values() ) {
            port.unsetConnection();
        }
    }

    public Port getInputPortById( Integer order ) {
        return this.inputPorts.get( order );
    }

    public Port getOutputPortById( Integer order ) {
        return this.outputPorts.get( order );
    }
    public void execute() {
    }

}
