/*
 * @file  Block.java
 * @brief Class for view of block
 *
 * File containing specific functions for view of block
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
import java.util.HashMap;

public class BlockPanel implements MouseListener, MouseMotionListener{
    private String path; // path to icon with represents block
    private HashMap<String,String> identification = new HashMap<String, String>(); // all paths to blocks
    private Integer x; //x position
    private Integer y; // y position
    private Integer height;
    private Integer width;
    private String name;
    JLabel labelBlock;

    public BlockPanel(String identification, JLabel label){
        // important identification of block
        this.identification.put( "multiply", "img/multiplication.png");
        this.identification.put( "devide", "img/division.png");
        this.identification.put( "add", "img/addition.png");
        this.identification.put( "random", "img/random.png");
        this.identification.put( "invert", "img/invert.png");
        //String path = this.identification.get(identification);
        setPath(this.identification.get(identification));
        this.name = identification;
        setHeight(65);
        setWidth(75);
        // defaultne se nachazi tam, kde se vygeneroval
        setX(310);
        setY(45);
        this.labelBlock = label;
        this.labelBlock.addMouseListener(this);
        this.labelBlock.addMouseMotionListener((MouseMotionListener) this);
    }

    // for future, creating new block
    public void setNewPath( String identif, String path ){
        this.identification.put( identif, path);
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getPath() {
        return path;
    }

    public Integer getHeightBlock() {
        return height;
    }

    public Integer getWidthBlock() {
        return width;
    }

    public Integer getXBlock() {
        return x;
    }

    public Integer getYBlock() {
        return y;
    }
    private void deleteBlock(){
        System.out.println("deleting label");
        Container parent = labelBlock.getParent();
        parent.remove(labelBlock);
        parent.validate();
        parent.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if ( SwingUtilities.isRightMouseButton(e) ){
            System.out.println("right click");
            deleteBlock();
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {
        System.out.println("Pressed");
        x = event.getX();
        y = event.getY();

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        labelBlock.setToolTipText(this.name);

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent event) {
        System.out.println("Dragged");
        event.getComponent().setLocation(event.getX()+event.getComponent().getX()-x,   event.getY()+event.getComponent().getY()-y);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

}
