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

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Connection  {
    public Point cooFrom; // dynamic starting point
    public Point cooTo; // dynamic ending point
    
    private Integer idFromPort; // id of starting port (starts from 1)
    private Integer idToPort; // id of ending port (belongs to block)
    
    private Integer typeFrom; //input or output
    private Integer typeTo; //input or output
    
    private AbstractBlock blockFrom; // reference to starting block where was clicked
    private AbstractBlock blockTo; // reference to end block where was clicked

    public JLabel centerLabelToolTip;

    /**
     * Make connection of 2 blocks
     * @param A starting block from
     * @param B end block to
     */
    public Connection(AbstractBlock A, AbstractBlock B, JLabel label){
        this.blockFrom = A;
        this.blockTo = B;
        centerLabelToolTip = label;

    }

    /**
     * Label is put in center of connection
     * @return point where the label is
     */
    public Point positionOfLabel(){
        // krok bude 1 pokud cooFrom.x je mensi nez cooTo.x, jinak -1
        Integer stepX = (cooFrom.x - cooTo.x)/2 + cooTo.x;
        Integer stepY = (cooFrom.y - cooTo.y)/2 + cooTo.y;
        Point centerLine = new Point( stepX,  stepY);
        // 4 is because of height and width of label (we use width and height of block)
        // 2 make it even smaller
        centerLine.setLocation(centerLine.x-blockFrom.getWidthBlock()/4/2, centerLine.y-blockFrom.getHeightBlock()/4/2);
        return centerLine;
    }

    public Integer getTypeFrom() {
        return typeFrom;
    }

    public Integer getTypeTo() {
        return typeTo;
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
        blockFrom.updateInputPort();
        blockFrom.updateOutputPort();

        blockTo.updateInputPort();
        blockTo.updateOutputPort();
        setCooFrom( blockFrom.getPort(idFromPort, typeFrom) );
        setCooTo( blockTo.getPort(idToPort, typeTo) );

        centerLabelToolTip.setBounds(positionOfLabel().x, positionOfLabel().y,
                blockFrom.getWidthBlock()/4, blockFrom.getHeightBlock()/4);
    }
}
