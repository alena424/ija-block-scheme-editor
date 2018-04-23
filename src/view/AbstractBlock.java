/*
 * @file  AbstractBlock.java
 * @brief Abstract class for  block
 *
 * File containing all functions to operate with a block
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


import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;

public class AbstractBlock implements MouseListener, MouseMotionListener{
        public String path; // path to icon with represents block
        public String selectedPath; // path to icon with represents a selected block
        public HashMap<String,String> identification = new HashMap<String, String>(); // all paths to blocks

        protected Integer x; //x position
        protected Integer y; // y position
        private Integer height;
        private Integer width;
        private String name;
        public JLabel labelBlock;
        protected Integer NPortIn;
        protected Integer NPortOut;

        // input ports of block identified by id, id starts from 1
        public HashMap <Integer,Point> inputPortPointsHash = new HashMap<Integer, Point>();

        // output ports of block identified by id
        public HashMap <Integer,Point> outputPortPointsHash = new HashMap<Integer, Point>();

        // stores information about which point of block was clicked, then count port
        Point leftClickedLast;

        // true - static, means can not be moved, deleted (start, end block)
        // false - non static, all other blocks
        boolean static_object = false; // static or nonstatic object

    public AbstractBlock(String identification, JLabel label){

            this.name = identification;
            // default width and height of block
            setHeight(64);
            setWidth(65);

            this.labelBlock = label;
            this.labelBlock.addMouseListener(this);
            this.labelBlock.addMouseMotionListener((MouseMotionListener) this);
     }

    public boolean isStatic_object() {
        return static_object;
    }

    public void setLeftClickedLast(Point leftClickedLast) {
        this.leftClickedLast = leftClickedLast;
    }

    public Point getLeftClickedLast() {
        return leftClickedLast;
    }

    /**
     * Method sets number of input port
     * @param NPortX number of input port
     */
    public void setNPortIn(int NPortX) {
        this.NPortIn = NPortX;
        System.out.println("It has " + NPortX + "input ports\n");
        updateInputPort();
    }

    /**
     * Method updates input ports according to position of block
     */
    public void updateInputPort(){
        System.out.println("Updation input ports...\n");
        Double divX = ((double)width)/(NPortIn+1);

        if ( inputPortPointsHash.isEmpty() ){
            for ( int i = 1; i <= NPortIn; i++ ) {
                Point bod = new Point();
                bod.y = y;
                bod.x = (int) (x + i*divX);

                inputPortPointsHash.put(i,bod);
            }
        } else{
            System.out.println("List is not empty");
            for ( int i = 1; i <= NPortIn; i++ ) {

                // index from 1
                Point bod = inputPortPointsHash.get(i);
                bod.y = y;
                bod.x = (int) (x + i*divX);
                inputPortPointsHash.get(i).setLocation( bod);
                System.out.println("Actualizing to:");
                System.out.println(bod);
            }
        }
    }
    /**
     * Method updates output ports according to position of block
     */
    public void updateOutputPort(){
        System.out.println("Updation output ports...\n");
        Double div = ((double)width)/(NPortOut+1);
        // adding new port
        if ( outputPortPointsHash.isEmpty() ){
            for ( int i = 1; i <= NPortOut; i++ ) {
                Point bod = new Point();
                bod.y = y + height;
                bod.x = (int) (x + i*div);
                outputPortPointsHash.put(i,bod);
            }
        // updating port
        } else{
            for ( int i = 1; i <= NPortOut; i++ ) {
                // index from 1
                Point bod = outputPortPointsHash.get(i);
                bod.y = y + height;
                bod.x = (int) (x + i * div);
                outputPortPointsHash.get(i).setLocation( bod);
            }
        }
    }
    public void setNPortOut(int NPortY) {
        this.NPortOut = NPortY;
        updateOutputPort();
    }
    public Point getPort(Integer idPort, Integer type){
        if ( type == 1 ){
            // input port
            return inputPortPointsHash.get(idPort);
        } else {
            // outputport
            return outputPortPointsHash.get(idPort);
        }
    }

    /**
     * Method decides if it is a input or output port
     * @param point point
     * @return 1 - inputport, 2 - outputport
     */
    public Integer getTypePort(Point point){
        if ( point.y < (this.y + height/2) ){
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * Method gets id of port
     * @param point point relative to block
     * @param type input or output
     * @return port id
     */
     public Integer getPortId(Point point, Integer type){
         double min = 1000000; // min that can not happen

         // first we need to update
         updateOutputPort();
         updateInputPort();

         Point minPoint = new Point(x,y);

         System.out.println("Inputports: ");
         System.out.println(inputPortPointsHash);

         System.out.println("Outputports: ");

         Integer idPort = 0;
         Integer poc = 1;
         System.out.println(outputPortPointsHash);
         if ( type == 1 ){
             //input port
             System.out.println("Input was clicked\n");
             for (Point i : inputPortPointsHash.values()){
                 double dest = lengthPath(point, i);
                 if (  dest < min ){
                     min = dest;
                     //minPoint = i;
                     idPort = poc;
                 }
                 poc++;
             }
         } else {
             //output port
             System.out.println("Output was clicked\n");
             for (Point i : outputPortPointsHash.values()){
                 double dest = lengthPath(point, i);
                 if (  dest < min ){
                     min = dest;
                     //minPoint = i;
                     idPort = poc;
                 }
                 poc++;
             }
         }
         return idPort;
    }
    private double lengthPath(Point A, Point B){
         return sqrt( pow(A.x - B.x, 2) + pow(A.y - B.y, 2) );
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
