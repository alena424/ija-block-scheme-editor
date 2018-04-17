/*
 * @file  Port.java
 * @brief Class for port
 *
 * File containing specific functions for port
 *
 * @author Alena Tesarova (xtesar36)
 * @author Jan Sorm (xsormj00)
 */

package model;

import java.util.HashMap;
import java.util.*;

public class Port {
    private HashMap<String,Double> value = new HashMap<String, Double>();
    private String type;
    private Block ownerBlock;
    private boolean free = true;
    private Integer id;
    private Connection connection;

    public Port( Integer id, String type, Block ownerBlock ) {
        this.id = id;
        this.type = type;
        this.ownerBlock = ownerBlock;
    }

    public Block getBlock() {
        return this.ownerBlock;
    }

    public boolean isFree() {
        return this.free;
    }

    public String getType() {
        return this.type;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void setConnection( Connection connection ) {
        this.connection = connection;
        this.free = false;
    }

    public void unsetConnection() {
        this.connection = null;
        this.free = true;
    }

    public void setValue( HashMap map ) {
        this.value.putAll( map );
    }

    public void addValue( String name, Double value ) {
        this.value.put( name, value );
    }

    public void popValue( String name, Double value ) {
        this.value.remove( name );
    }

    public HashMap getHashOfValue() {
        return this.value;
    }

    public Block getOwnerBlock() {
        return this.ownerBlock;
    }

}
