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


public abstract class Block {

    protected Integer id;
    protected HashMap<Integer,Port> input = new HashMap<Integer, Port>();
    protected HashMap<Integer,Port> output = new HashMap<Integer, Port>();
    protected Integer level = 0;
    protected Integer countInput = 0;
    protected Integer countOutput = 0;
    protected String name;
    public Integer static_x = 0;
    public Integer static_y = 0;

    public boolean static_block = false; // flag, block can not be deleted and position is static
    // defaultIcon
    String icon = "img/default.png";
    String selectedIcon = "img/default.png";

   // protected Scheme scheme;

    public Block () {

        //Integer newId = scheme.getMaxId();
        //this.id = newId;
        //this.level = level;
       // this.scheme = scheme;

        //scheme.addBlock( this );
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

    public void addInputPort( String type ) {
        Port port = new Port( this.countInput, type, this );
        this.input.put( this.countInput, port );
        this.countInput++;
    }

    public void addOutputPort( String type ) {
        Port port = new Port( this.countOutput, type, this );
        this.output.put( this.countOutput, port );
        this.countOutput++;
    }

    public void setInput( HashMap map, Integer order ) {
        if ( this.level == 0 && this.countInput > order && this.input.get( order ).isFree() ) {
            this.input.get( order ).setValue( map );
        }
    }

    public void setOutput( HashMap map, Integer order ) {
            this.output.get( order ).setValue( map );
    }

    public boolean isFreeInput() {
        for ( Port port : this.input.values() ) {
            if ( port.isFree() ) {
                return true;
            }
        }
        return false;
    }

    public void deleteBlockConnection() {
        for ( Port port : this.input.values() ) {
            port.unsetConnection();
        }
        for ( Port port : this.output.values() ) {
            port.unsetConnection();
        }
    }

    public Port getInputPort( Integer order ) {
        return this.input.get( order );
    }

    public Port getOutputPort( Integer order ) {
        return this.output.get( order );
    }



    public void execute() {
    }

}
