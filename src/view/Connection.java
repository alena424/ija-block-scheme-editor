/*
 * @file  Connection.java
 * @brief Class for view of connection
 *
 * File containing specific functions for view of connection.
 *
 * @author Alena Tesarova (xtesar36)
 * @author Jan Sorm (xsormj00)
 */

package view;

import java.awt.*;

public class Connection{
    public Point cooFrom; // dynamic starting point
    public Point cooTo; // dynamic ending point
    
    private Integer idFromPort; // id of starting port (starts from 1)
    private Integer idToPort; // id of ending port (belongs to block)
    
    private Integer typeFrom; //input or output
    private Integer typeTo; //input or output
    
    private AbstractBlock blockFrom; // reference to starting block where was clicked
    private AbstractBlock blockTo; // reference to end block where was clicked

    /**
     * Make connection of 2 blocks
     * @param A starting block from
     * @param B end block to
     */
    public Connection(AbstractBlock A, AbstractBlock B){
        this.blockFrom = A;
        this.blockTo = B;
    }

    public AbstractBlock getBlockFrom() {
        return blockFrom;
    }

    public AbstractBlock getBlockTo() {
        return blockTo;
    }

    private void setCooTo(Point cooTo) {
        this.cooTo = cooTo;
    }

    private void setCooFrom(Point cooFrom) {
        this.cooFrom = cooFrom;
    }

    public Integer getIdFromPort() {
        return idFromPort;
    }

    public Integer getidToPort() {
        return idToPort;
    }

    public void setTypeFrom(Integer typeFrom) {
        this.typeFrom = typeFrom;
    }

    public void setTypeTo(Integer typeTo) {
        this.typeTo = typeTo;
    }

    public void setIdToPort(Integer idToPort) {
        this.idToPort = idToPort;
    }

    public void setIdFromPort(Integer idFromPort) {
        this.idFromPort = idFromPort;
    }

    /**
     * Method actualizes connection
     * 1. update all input and output ports
     * 2. count final coordinates of start and end point
     */
    public void actualize(){
        // must count position of port according to block and his id
        System.out.println("Update connection, port id: " + idFromPort + " and second port id:" + idToPort);
        blockFrom.updateInputPort();
        blockFrom.updateOutputPort();

        blockTo.updateInputPort();
        blockTo.updateOutputPort();
        setCooFrom( blockFrom.getPort(idFromPort, typeFrom) );
        setCooTo( blockTo.getPort(idToPort, typeTo) );
    }
}
