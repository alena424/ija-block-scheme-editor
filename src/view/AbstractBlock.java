package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;

public class AbstractBlock implements MouseListener, MouseMotionListener{
        protected String path; // path to icon with represents block
        protected String selectedPath; // path to icon with represents a selected block
        protected HashMap<String,String> identification = new HashMap<String, String>(); // all paths to blocks
        protected Integer x; //x position
        protected Integer y; // y position
        protected Integer height;
        protected Integer width;
        protected String name;
        protected JLabel labelBlock;
        protected Integer PortX;
        protected Integer PortY;

    public AbstractBlock(String identification, JLabel label){
            // important identification of block
            /*this.identification.put( "Multiplication", "img/multiplication.png");
            this.identification.put( "Division", "img/division.png");
            this.identification.put( "Addition", "img/addition.png");
            this.identification.put( "Random", "img/random.png");
            this.identification.put( "Invert", "img/invert.png");*/
            this.identification.put( "end", "img/equals.png");
            this.identification.put( "start", "img/minus.png");
            //String path = this.identification.get(identification);
            //setPath(this.identification.get(identification));
            this.name = identification;
            setHeight(65);
            setWidth(75);
            this.labelBlock = label;
            this.labelBlock.addMouseListener(this);
            this.labelBlock.addMouseMotionListener((MouseMotionListener) this);
     }

    // for future, creating new block
    public void setNewPath( String identif, String path ){
        this.identification.put( identif, path);
    }

    public void setSelectedPath(String selectedPath) {
        this.selectedPath = selectedPath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setX(Integer x) {
        this.x = x;
        // musime zmenit i souradnice portu

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
    protected void deleteBlock(){
        System.out.println("deleting label");
        Container parent = labelBlock.getParent();
        parent.remove(labelBlock);
        parent.validate();
        parent.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {


    }

    @Override
    public void mousePressed(MouseEvent e) {

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
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

}
