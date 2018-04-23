package controller;

import model.Block;
import model.FactoryBlock;
import model.Scheme;
import view.Connection;
import view.DashBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.IOException;

import java.util.ArrayList;

import java.util.List;

import static java.lang.Math.abs;

public class Program {

    Scheme newScheme = new Scheme(1); // new shceme
    DashBoard dashboard; // new dashboard
    List <Block> factoreBlocks; // modelBlock created blocks
    List<JLabel> panelBlocks; // blocks on the left side, generates working blocks

    List<view.AbstractBlock> viewWorkingBlocks; // blocks on main canvas to work with in VIEW
    List<model.Block> modelWorkingBlocks; // blocks on main canvas to work with in MODEL

    List<view.AbstractBlock> selectedBlock; //selected blocks, maximum is 2 blocks
    List<Connection> connectionsList = new ArrayList<>();

    public Program(){

    }

    /**
     * Method creates types of block (appears in left panel)
     */
    public void createBlocks(){
        //newScheme = new Scheme(1);
        // CREATING BLOCKS
        newScheme.factorBlock("Start");
        newScheme.factorBlock("End");

        newScheme.factorBlock("Addition");
        newScheme.factorBlock("Invert");
        newScheme.factorBlock("Random");
        newScheme.factorBlock("Multiplication");
        newScheme.factorBlock("Division");

    }
    public void run(){
        createBlocks();

        dashboard = new DashBoard();
        dashboard.panel2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                distroySelectedBlocks();

            }
        });

        // created default blocks we can use
        factoreBlocks = new ArrayList<>( newScheme.getFactoreBlock() );
        System.out.println(factoreBlocks);
        // put blocks on left panel1
        dashboard.getBlocksOnLeft(newScheme.getFactoreBlock());
        // save blocks in left panel in panelBlocks
        panelBlocks = dashboard.getBlocksOnPanel();

        //initialize array of view and model blocks
        viewWorkingBlocks = new ArrayList<>();
        modelWorkingBlocks = new ArrayList<>();

        // special inicialization for start and end block
        initFactoryBlock(newScheme.getfactoreBlockByName("Start"), newScheme.getfactoreBlockByName("End"));

        for (JLabel lab: panelBlocks
                ) {
            String name_block = lab.getText();
            // get which block was clicked
            Block block = getBlockByName(name_block, factoreBlocks);
            // set event on click
            lab.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    generateBlock(block);

                }
            });
        }
        dashboard.setVisible();

    }
    protected void deleteModelBlock(model.Block block){
        System.out.println("deleting model ");
        modelWorkingBlocks.remove(block);
        block = null;
    }

    /**
     * Method deletes a view block with all connections connected to block
     * @param block
     */
    protected void deleteViewBlock(view.AbstractBlock block){
        System.out.println("deleting label view");
        dashboard.panel2.remove(block.labelBlock);

        // delete connection connected to block if there is just one
        deleteConnection(block);
        dashboard.panel2.validate();
        dashboard.panel2.repaint();
        viewWorkingBlocks.remove(block);
        block = null;
    }

    /**
     * Method deletes all connections relative to block
     * @param block
     */
    protected void deleteConnection(view.AbstractBlock block){
       // System.out.println(dashboard.getConnectionsOnDashboard());
        List<Connection> toRemoveConnection = new ArrayList<>();
        for (Connection i: dashboard.getConnectionsOnDashboard()
             ) {
            if ( i.getBlockTo() == block || i.getBlockFrom() == block){

                toRemoveConnection.add(i);

            }
        }
        for ( Connection r: toRemoveConnection
             ) {
            dashboard.getConnectionsOnDashboard().remove(r);
            r = null;
        }
        dashboard.panel2.repaint();
    }

    /**
     * Inicialized start and end block
     * @param start start block
     * @param end end block
     */

    public void initFactoryBlock(Block start, Block end){
        System.out.println(start);
        generateBlock(start);
        System.out.println(end);
        generateBlock(end);
    }

    /**
     * Function generates block
     * @param block block we get information from, when we are creating view and model block
     */
    public void generateBlock(Block block){
        // new label for new block

        JLabel lblock = new JLabel();
        FactoryBlock factoryBlock = new FactoryBlock();

        System.out.println(block.getName());

        // CREATING VIEW BLOCK
        view.AbstractBlock addViewBlock;
        if ( block.static_block ){
            // we get static coordinates from model
            addViewBlock = new view.StaticBlock(block.getName(), lblock);
            if ( block.static_x != 0 && block.static_y != 0){
                addViewBlock.setX(block.static_x);
                addViewBlock.setY(block.static_y);
            } else {
                System.err.println("Static blocks must contain their static coordinates\n");
                return;
            }
        } else {
            addViewBlock = new view.Block(block.getName(), lblock);
            // drag and drop
            lblock.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent event) {
                    //super.mouseDragged(e);
                    System.out.println("Dragged");
                    if ( event.getComponent().getY() <= 0 && event.getComponent().getX() <= 0 ){
                        event.getComponent().setLocation(abs(event.getX()+event.getComponent().getX()-addViewBlock.getXBlock()),
                                abs(event.getY()+event.getComponent().getY()-addViewBlock.getYBlock()) );
                    } else if ( event.getComponent().getX() > 660 ) {
                        event.getComponent().setLocation(660,   event.getY()+event.getComponent().getY()-addViewBlock.getYBlock());
                    } else if ( event.getComponent().getY() > 550 ) {
                        event.getComponent().setLocation(event.getX()+event.getComponent().getX()-addViewBlock.getXBlock(),   550);
                    } else {
                        event.getComponent().setLocation(abs(event.getX()+event.getComponent().getX()-addViewBlock.getXBlock()),
                                abs(event.getY()+event.getComponent().getY()-addViewBlock.getYBlock()) );
                    }
                }

            });
            // after release, we need to update coordinates of port and connections
            lblock.addMouseListener(new MouseAdapter() {
                  @Override
                  public void mouseReleased(MouseEvent event) {
                      System.out.println("release");
                      addViewBlock.setX( event.getX()+event.getComponent().getX()-addViewBlock.getXBlock() );
                      addViewBlock.setY( event.getY()+event.getComponent().getY()-addViewBlock.getYBlock() );


                      if ( ! dashboard.getConnectionsOnDashboard().isEmpty() ){
                          actualizeAllConnection();
                          System.out.println("Actualize");
                          dashboard.panel2.repaint();
                      }
                  }
              });
        }

        viewWorkingBlocks.add(addViewBlock);

        // CREATING MODEL BLOCK
        model.Block addModelBlock = factoryBlock.getBlock(block.getName());
        modelWorkingBlocks.add(addModelBlock);

        // CREATE SELECTED LIST
        selectedBlock = new ArrayList<>();

        addViewBlock.setPath(block.getPathOfIcon());
        addViewBlock.setName(block.getName());

        System.out.println("Name: " + block.getName());
        System.out.println("Inputs: " + block.getCountInput());
        System.out.println("Outputs: " + block.getCountOutput());

        addViewBlock.setNPortIn(block.getCountInput());
        addViewBlock.setNPortOut(block.getCountOutput());
        addViewBlock.setSelectedPath(block.getSelectedIcon());

        addViewBlock.labelBlock.setIcon(new ImageIcon( addViewBlock.getPath() ) );

        dashboard.panel2.add(addViewBlock.labelBlock);
        if ( block.static_block ){
            // if it is a static block, can not be moved and it has special coordinates
            addViewBlock.labelBlock.setBounds(block.static_x, block.static_y, addViewBlock.getWidthBlock(), addViewBlock.getHeightBlock());
        } else {
            addViewBlock.labelBlock.setBounds(addViewBlock.getXBlock(), addViewBlock.getYBlock(), addViewBlock.getWidthBlock(), addViewBlock.getHeightBlock());
        }

        lblock.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if ( SwingUtilities.isRightMouseButton(e) ){
                    System.out.println("right click");
                    int indexM = modelWorkingBlocks.indexOf(addModelBlock);
                    if ( indexM != -1 ){
                        // can not delete static objects!
                        if ( ! modelWorkingBlocks.get(indexM).static_block )
                                deleteModelBlock( modelWorkingBlocks.get(indexM));
                    } else {
                        System.out.println("Object does not exist!");
                    }

                    int indexV = viewWorkingBlocks.indexOf(addViewBlock);
                    if ( indexV != -1 ){
                        if (! viewWorkingBlocks.get(indexV).isStatic_object())
                                deleteViewBlock( viewWorkingBlocks.get(indexV));
                    } else {
                        System.out.println("Object does not exist!");
                    }
                } else {
                    System.out.println("left click");

                    changeIconToSelected(addViewBlock);
                    // we must save where was clicked
                    addViewBlock.setLeftClickedLast(e.getPoint());

                    if ( selectedBlock.size() < 2 ){
                        selectedBlock.add(addViewBlock);
                        // if it is a second chosen block, we make a line
                        if ( selectedBlock.size() == 2 ){
                            makeNewConnection(e);
                        }
                    } else {
                        // if we have 2 and more, it should not happen and we clear list
                        distroySelectedBlocks();
                    }
                }
            }
        });
    }

    /**
     * Method actualizes all connection
     * all connections are found in dashboard.getConnectionsOnDashboard()
     */
    public void actualizeAllConnection (){
        for ( Connection i: dashboard.getConnectionsOnDashboard()
             ) {
            i.actualize();
        }
    }

    /**
     * Method makes new connection
     * 1. gets points where was clicked
     * 2. gets type of ports (input, output), 2 selected points can not have same port
     * 3. gets id of port
     * 4. from id we get the point of right port
     * @param event click point
     */
    public void makeNewConnection(MouseEvent event){
        // we have 2 blocks to connect

        Point firstPoint = new Point(
                selectedBlock.get(0).getLeftClickedLast().x + selectedBlock.get(0).getXBlock(),
                selectedBlock.get(0).getLeftClickedLast().y +selectedBlock.get(0).getYBlock());
        System.out.println("First point:");
        System.out.println(firstPoint);

        Point clickPoint = new Point(
                event.getX() +selectedBlock.get(1).getXBlock(),
                event.getY() +selectedBlock.get(1).getYBlock());

        System.out.println("Second point:");
        System.out.println(clickPoint);

        System.out.println("Clicked place:");
        System.out.println(clickPoint);

        Integer typePortFirst = selectedBlock.get(0).getTypePort(firstPoint);
        Integer typePortSecond  = selectedBlock.get(1).getTypePort(clickPoint);

        if ( typePortFirst == typePortSecond ){
            System.err.println("You clicked on same type of port, port must be different type");

        } else {
            Integer idFirst = selectedBlock.get(0).getPortId(firstPoint,typePortFirst);
            Integer idSecond = selectedBlock.get(1).getPortId(clickPoint, typePortSecond);

            //Point portfirst = selectedBlock.get(0).getPort(idFirst, typePortFirst);
            //Point portsecond = selectedBlock.get(1).getPort(idSecond,typePortSecond);
            // make new connection
            Connection conn = new Connection(selectedBlock.get(0), selectedBlock.get(1));
            // set id of ports
            conn.setIdFromPort(idFirst);
            conn.setIdToPort(idSecond);
            // set types of ports
            conn.setTypeFrom(typePortFirst);
            conn.setTypeTo(typePortSecond);
            // actualize coordinates of ports
            conn.actualize();

            dashboard.addConnectionsOnDashboard(conn);
        }

        distroySelectedBlocks();

    }

    /**
     * Method clears selectedBlock array and change icon to normal
     */
    public void distroySelectedBlocks(){
        for (view.AbstractBlock vblock : selectedBlock){
            changeIconToNormal(vblock);
        }
        selectedBlock.clear();
    }
    public void changeIconToSelected( view.AbstractBlock vblock){
        vblock.labelBlock.setIcon( new ImageIcon(vblock.selectedPath));
    }
    public void changeIconToNormal( view.AbstractBlock vblock){
        vblock.labelBlock.setIcon( new ImageIcon(vblock.path));
    }
    public Block getBlockByName(String name, List <model.Block> factoreBlocks){
        for ( model.Block bl : factoreBlocks){
            if ( bl.getName().equalsIgnoreCase(name) ){
                return bl;
            }
        }
        return null;
    }

    /**
     * Main runs program
     * @param args
     * @throws IOException
     */
    public static void main( String[] args ) throws IOException {
        Program program = new Program();
        program.run();

    }
}
