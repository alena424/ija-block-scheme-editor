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
    public HashMap<String,Double> value ;
    private Integer type; // type 1 - input port, type 2 - output port
    private String name;
    private Block ownerBlock;
    private boolean free = true;
    private Integer id;
    private Connection connection;

    public Port( String name, HashMap<String,Double> hashvalue ) {
        this.name = name;
        value = new HashMap<String, Double>();
        for ( String nameVal : hashvalue.keySet()){
            value.put(nameVal, hashvalue.get(nameVal));

        }
        //System.out.println(hashvalue);
        //this.id = id;
        //this.type = type;
        //this.ownerBlock = ownerBlock;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Block getBlock() {
        return this.ownerBlock;
    }

    public void setOwnerBlock(Block ownerBlock) {
        this.ownerBlock = ownerBlock;
    }

    public boolean isFree() {
        return this.free;
    }

    public void setFree() {
        this.free = true;
    }
    public void setUnFree() {
        this.free = false;
    }


    public Integer getType() {
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

    public HashMap<String, Double> getHashOfValue() {
        return this.value;
    }

    public Block getOwnerBlock() {
        return this.ownerBlock;
    }

}
