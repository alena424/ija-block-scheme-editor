/*
 * @file  Program.java
 * @brief Main Class for controlling all
 *
 * File containing specific functions to control view and model
 *
 * @author Alena Tesarova (xtesar36)
 * @author Jan Sorm (xsormj00)
 */
package controller;

import model.Block;
import model.FactoryBlock;
import model.Port;
import model.Scheme;
import view.AbstractBlock;
import view.Connection;
import view.DashBoard;
import view.Help;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.abs;

public class Program {

    private Scheme newScheme = new Scheme(1); // new shceme
    public DashBoard dashboard; // new dashboard
    private List <Block> factoreBlocks; // modelBlock created blocks
    private List<JLabel> panelBlocks; // blocks on the left side, generates working blocks
    private List<view.AbstractBlock> viewWorkingBlocks; // blocks on main canvas to work with in VIEW
    public List<view.AbstractBlock> selectedBlock; //selected blocks, maximum is 2 blocks
    private List<view.AbstractBlock> debugSelectedBlock; // selected block of one step
    public HashMap<view.AbstractBlock,model.Block> viewToModelBlocks = new HashMap<AbstractBlock,Block>();
    private model.Connection chosenConn = null; // clicked connection
    private Integer actualDebugLevel = 1;
    private Map<Integer, List> blocksForLevel = new TreeMap<>(); // all blocks with levels
    public Program(){
    }

    public HashMap<AbstractBlock, Block> getViewToModelBlocks() {
        return viewToModelBlocks;
    }

    /**
     * Method creates types of block (appears in left panel)
     */
    public void createBlocks(){
        // DEFINE 3 TYPES OF PORT
        HashMap<String,Double> pomHash = new HashMap<>();
        pomHash.put("Name1", 0.0);
        Port port1 = new Port("Port1", pomHash);

        HashMap<String,Double> pomHash2 = new HashMap<>();
        pomHash2.put("Name2", 0.0);
        pomHash2.put("Name3", 0.0);
        Port port2 = new Port("Port2", pomHash2);

        HashMap<String,Double> pomHash3 = new HashMap<>();
        pomHash3.put("Name4", 0.0);
        pomHash3.put("Name5", 0.0);
        pomHash3.put("Name6", 0.0);
        Port port3 = new Port("Port3", pomHash3);

        HashMap<Integer,Port> blocksInputPorts = new HashMap<Integer,Port>();
        HashMap<Integer,Port> blocksOutputPorts = new HashMap<Integer,Port>();

        // CREATING BLOCKS - 5 types of block
        newScheme.factorBlock("Start", blocksInputPorts, blocksOutputPorts);
        newScheme.factorBlock("End", blocksInputPorts, blocksOutputPorts);

        //3 INPUTS
        blocksInputPorts.put(1,port1);
        blocksInputPorts.put(2,port2);
        blocksInputPorts.put(3,port3);
        //1 OUTPUT
        blocksOutputPorts.put(1,port1);
        newScheme.factorBlock("Addition", blocksInputPorts, blocksOutputPorts);

        HashMap<Integer,Port> blocksInputPorts2 = new HashMap<Integer,Port>();
        HashMap<Integer,Port> blocksOutputPorts2 = new HashMap<Integer,Port>();

        // 2 IN, 2 OUT
        blocksInputPorts2.put(1,port1);
        blocksInputPorts2.put(2,port2);
        blocksOutputPorts2.put(1,port1);
        blocksOutputPorts2.put(2,port2);

        newScheme.factorBlock("Invert", blocksInputPorts2, blocksOutputPorts2);

        HashMap<Integer,Port> blocksInputPorts3 = new HashMap<Integer,Port>();
        HashMap<Integer,Port> blocksOutputPorts3 = new HashMap<Integer,Port>();
        // 1 IN 2, OUT
        blocksInputPorts3.put(1,port1);
        blocksOutputPorts3.put(1,port1);
        blocksOutputPorts3.put(2,port2);
        newScheme.factorBlock("Random",blocksInputPorts3,blocksOutputPorts3);

        HashMap<Integer,Port> blocksInputPorts4 = new HashMap<Integer,Port>();
        HashMap<Integer,Port> blocksOutputPorts4 = new HashMap<Integer,Port>();
        // 2 IN 2, OUT
        blocksInputPorts4.put(1,port1);
        blocksInputPorts4.put(2,port2);
        blocksOutputPorts4.put(1,port2);
        newScheme.factorBlock("Multiplication",blocksInputPorts4,blocksOutputPorts4);

        HashMap<Integer,Port> blocksInputPorts5 = new HashMap<Integer,Port>(blocksInputPorts4);
        HashMap<Integer,Port> blocksOutputPorts5 = new HashMap<Integer,Port>(blocksOutputPorts4);

        newScheme.factorBlock("Division",blocksInputPorts5,blocksOutputPorts5);
    }

    /**
     * Method returns output port by out block from connection
     * @param i connection from which we take ourput block
     * @param type 1 - we want output port, 2 - we want input port
     * @return output port by out block
     */
    public model.Port getLineAccordingToCoordinates(Connection i, Integer type){
            if( i.getTypeFrom() == type){
                // from is input, we want output
                return viewToModelBlocks.get(i.getBlockTo()).getOutputPortById(i.getidToPort());
            } else {
                // from is output
               // System.out.println( "! " +i.getTypeFrom());
                return viewToModelBlocks.get(i.getBlockFrom()).getOutputPortById(i.getIdFromPort());
            }
    }

    /**
     * Run program
     */
    public void run(){
        // blocks on left panel
        createBlocks();
        // creating dashboard
        dashboard = new DashBoard();
        // of we clicks whereever on panel2, all selected blocks or connections should be unselected
        // in addition to it, we stop debugging
        dashboard.panel2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                distroySelectedBlocks();
                distroyDebugSelectedBlocks();
                actualDebugLevel = 1;
                dashboard.panel4.setSize(0,0);
            }
        });

        // created default blocks we can use
        factoreBlocks = new ArrayList<>( newScheme.getFactoreBlock() );
        // put blocks on left panel1
        dashboard.getBlocksOnLeft(newScheme.getFactoreBlock());
        // save blocks in left panel in panelBlocks
        panelBlocks = dashboard.getBlocksOnPanel();
        //initialize array of view and model blocks
        viewWorkingBlocks = new ArrayList<>();

        // special inicialization for start and end block
        initFactoryBlock(newScheme.getfactoreBlockByName("Start"), newScheme.getfactoreBlockByName("End"));

        //LOAD, SAVE, NEW
        dashboard.bload.addActionListener(new Loader(dashboard.frame1, this));
        dashboard.bsave.addActionListener(new Saver(dashboard.frame1, this));
        dashboard.bdebug.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                debugProgramStep();
            }
        });
        dashboard.bnew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearCanvas();
            }
        });
        // HELP
        dashboard.lhelp.addActionListener(new Help(this));

        for (JLabel lab: panelBlocks
                ) {
            String name_block = lab.getText();
            // get which block was clicked

            Block block = getBlockByName(name_block);
            // set event on click
            lab.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    generateBlock(block);
                }
            });
        }
        // COUNT
        dashboard.bcount.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                getResult();
            }
        });
        dashboard.setVisible();
    }

    /**
     * One step of computation
     */
    public void debugProgramStep(){
        int end = 0;
        actualizeBlocksForLevel();
        distroyDebugSelectedBlocks();
        List<Block> blocks = blocksForLevel.get(actualDebugLevel);
        if ( blocks != null){
            for ( Block actBlock : blocks ){
                // chceme dostat view block
                view.AbstractBlock viewBlock = getViewBlock(actBlock);
                debugSelectedBlock.add(viewBlock);
                viewBlock.labelBlock.setIcon(viewBlock.getSelectedIconImg());

                dashboard.panel2.repaint();
                if ( actBlock.getName().equalsIgnoreCase("end") ){
                    end = 1;
                }

                try {
                    actBlock.execute();
                } catch (Exception ext){
                    // we catch error in case of for example dividing by zero
                    JOptionPane.showMessageDialog(dashboard.frame1,ext.getMessage());
                }
                // transfer output to input
                executeConnectionToBlock(actBlock);
            }
        }
        if ( end == 1){
            // debug or computing has ended
            actualDebugLevel = 0;
            JOptionPane.showMessageDialog(dashboard.frame1,"Computation has finished");
            distroyDebugSelectedBlocks();
        } else {
            // raise debug level
            actualDebugLevel++;
        }
    }

    /**
     * Method deletes a model block
     * @param block model to delete
     */
    protected void deleteModelBlock(model.Block block){
        newScheme.modelWorkingBlocks.remove(block);
        block = null;
    }

    /**
     * Method deletes a view block with all connections connected to block
     * @param block block to be deleted
     */
    protected void deleteViewBlock(view.AbstractBlock block){
        //System.out.println("deleting label view");
        dashboard.panel2.remove(block.labelBlock);

        // delete connection connected to block if there is just one
        deleteConnection(block);
        dashboard.panel2.validate();
        dashboard.panel2.repaint();
        viewWorkingBlocks.remove(block);
        // delete from hash
        viewToModelBlocks.remove(block);
        block = null;
    }

    /**
     * Method finds all connections belonging to block and deletes them
     * @param block block that will be deleted
     * @return true/false
     */
    public boolean deleteConnectionModel(AbstractBlock block){
        Block blockToDelete = viewToModelBlocks.get(block);
        List<model.Connection> toRemoveConnection = new ArrayList<>();
        for ( model.Connection i: newScheme.getConnectionsOnModel()
             ) {
            if ( i.getInput().getBlock() == blockToDelete || i.getOutput().getBlock() == blockToDelete){
                toRemoveConnection.add(i);
            }
        }
        for ( model.Connection rem: toRemoveConnection
             ) {
            rem.getInput().setFree();
            rem.getOutput().setFree();

            // co kdyz se jedna o start block nebo end block?
            if ( rem.getOutput().getBlock().static_block ){
                rem.getOutput().getBlock().removeOutputPort(rem.getOutput());
            }
            if ( rem.getInput().getBlock().static_block ){
                rem.getInput().getBlock().removeInputPort(rem.getInput());
            }
            deleteConnectionSingleLineModel(rem);
        }
        return true;
    }

    /**
     * Function finds all VIEW connections belonging to block and deletes them
     * @param block blong that will be deleted
     */
    public void deleteViewConnection(AbstractBlock block){
        List<Connection> toRemoveConnection = new ArrayList<>();
        for (Connection i: dashboard.getConnectionsOnDashboard()
                ) {
            if ( i.getBlockTo() == block || i.getBlockFrom() == block){
                toRemoveConnection.add(i);
            }
        }
        for ( Connection r: toRemoveConnection
                ) {
            deleteConnectionSingleLineView(r);
        }
    }

    /**
     * Method deletes one given VIEW connection
     * @param r view connection that will be deleted
     */
    public void deleteConnectionSingleLineView(Connection r){
        // VIEW
        dashboard.getConnectionsOnDashboard().remove(r);
        // remove jlabel from panel
        dashboard.panel2.remove(r.centerLabelToolTip);
        r = null;
        dashboard.panel4.setSize(0,0);
        dashboard.panel2.repaint();
    }
    /**
     * Method deletes one given MODEL connection
     * @param rem model connection that will be deleted
     */
    public void deleteConnectionSingleLineModel(model.Connection rem){
        // MODEL
        newScheme.getConnectionsOnModel().remove(rem);
        // free 2 ports
        rem.getOutput().setFree();
        rem.getInput().setFree();
        rem = null;
    }

    /**
     * Method deletes all connections relative to block
     * @param block block to be deleted
     */
    protected void deleteConnection(view.AbstractBlock block){

       deleteViewConnection(block);
       deleteConnectionModel(block);
        //dashboard.panel4.setVisible(false);
        dashboard.panel4.setSize(0,0);
    }

    /**
     * Method initializes start and end block
     * @param start start block
     * @param end end block
     */

    public void initFactoryBlock(Block start, Block end){
        generateBlock(start);
        generateBlock(end);
    }

    /**
     * Method initializes port according to given hash
     * @param port according to this hash we create real port using new
     * @return ports of block in hashmap
     */
    public HashMap<Integer,Port> createPortsToBlock(HashMap<Integer,Port> port){
        HashMap<Integer,Port> newPortModel = new HashMap<Integer, Port>();
        // String identification is unique for block port
        //System.out.println(port);
        if ( ! port.isEmpty() ){
            for (Map.Entry<Integer,Port> entry : port.entrySet()
                    ) {
                // important using new
                Port newPort = new Port(entry.getValue().getName(),entry.getValue().getHashOfValue());
                newPort.setId(entry.getKey());
                newPortModel.put(entry.getKey(),newPort);
            }
        }
        return newPortModel;
    }

    /**
     * Function generates block
     * @param block block we get information from, when we are creating view and model block
     * @return new view block
     */
    public view.AbstractBlock generateBlock(Block block){
        // new label for new block

        JLabel lblock = new JLabel();
        FactoryBlock factoryBlock = new FactoryBlock();

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
                return null;
            }

        } else {
            // VIEW BLOCK
            addViewBlock = new view.Block(block.getName(), lblock);
            // drag and drop
            lblock.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent event) {
                    Integer pomX = 0;
                    Integer pomY = 0;

                    if ( event.getComponent().getY() <= 0 && event.getComponent().getX() <= 0 ){
                        pomX =  abs(event.getX()+event.getComponent().getX()-addViewBlock.clickPosition.x);
                        pomY =  abs(event.getY()+event.getComponent().getY()-addViewBlock.clickPosition.y);
                    } else if ( event.getComponent().getX() > 660 ) {
                        pomX =  600;
                        pomY =  abs(event.getY()+event.getComponent().getY()-addViewBlock.clickPosition.y);
                    } else if ( event.getComponent().getY() > 550 ) {
                        pomX =  abs(event.getX()+event.getComponent().getX()-addViewBlock.clickPosition.x);
                        pomY =  550;
                    } else {
                        pomX =  abs(event.getX()+event.getComponent().getX()-addViewBlock.clickPosition.x);
                        pomY =  abs(event.getY()+event.getComponent().getY()-addViewBlock.clickPosition.y);
                    }
                    event.getComponent().setLocation(pomX,pomY);
                    addViewBlock.setX(pomX);
                    addViewBlock.setY(pomY);
                    // update connections (lines)
                    if ( ! dashboard.getConnectionsOnDashboard().isEmpty() ){
                        dashboard.actualizeAllConnection();
                        dashboard.panel2.repaint();
                    }
                }
            });

            // after release, we need to update coordinates of port and connections
            lblock.addMouseListener(new MouseAdapter() {
                  @Override
                  public void mouseReleased(MouseEvent event) {
                      addViewBlock.setX( event.getX()+event.getComponent().getX()-addViewBlock.clickPosition.x );
                      addViewBlock.setY( event.getY()+event.getComponent().getY()-addViewBlock.clickPosition.y );
                      if ( ! dashboard.getConnectionsOnDashboard().isEmpty() ){
                          dashboard.actualizeAllConnection();
                          dashboard.panel2.repaint();
                      }
                  }
              });
        }
        viewWorkingBlocks.add(addViewBlock);

        // CREATING MODEL BLOCK
        // define all ports to block
        HashMap<Integer, Port> inputPortsModel = createPortsToBlock( block.getInputPorts());
        HashMap<Integer, Port> outputPortsModel = createPortsToBlock( block.getOutputPorts());
        model.Block addModelBlock = factoryBlock.getBlock
                (block.getName(), inputPortsModel, outputPortsModel);

        // we want to remember start block
        if ( block.getName().equalsIgnoreCase("start") ){
            newScheme.startblock = addModelBlock;
        }

        newScheme.modelWorkingBlocks.add(addModelBlock);
        // we need to know which view block is model block
        viewToModelBlocks.put(addViewBlock,addModelBlock);

        // CREATE SELECTED LIST
        selectedBlock = new ArrayList<>();
        debugSelectedBlock = new ArrayList<>();

        // sets information to view block
        addViewBlock.setPath(block.getPathOfIcon());
        addViewBlock.setName(block.getName());

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
                    int indexM = newScheme.modelWorkingBlocks.indexOf(addModelBlock);
                    if ( indexM != -1 ){
                        // can not delete static objects!
                        if ( ! newScheme.modelWorkingBlocks.get(indexM).static_block )
                                deleteModelBlock( newScheme.modelWorkingBlocks.get(indexM));
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
                    changeIconToSelected(addViewBlock);
                    // we must save where was clicked
                    addViewBlock.setLeftClickedLast(e.getPoint());

                    if ( selectedBlock.size() < 2 ){
                        selectedBlock.add(addViewBlock);

                        // if it is a second chosen block, we make a line
                        if ( selectedBlock.size() == 2 ){
                            makeNewConnection();
                        }
                    } else {
                        // if we have 2 and more, it should not happen and we clear list
                        distroySelectedBlocks();
                    }
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                lblock.setToolTipText( "\nInput ports: " + formatPortsToString( addModelBlock.getInputPorts()) + "\n\nOutput ports: " + formatPortsToString( addModelBlock.getOutputPorts())  );

            }
        });
        return addViewBlock;

    }

    /**
     * Method format string with information about ports
     * @param ports port to get information from
     * @return string with information
     */
    public String formatPortsToString(HashMap<Integer,Port> ports){
        String final_str = "";
        for( Integer i : ports.keySet() ){
            final_str += ports.get(i).getHashOfValue() + "\n";
        }
        return final_str;
    }

    /**
     * Method deletes all model and view blocks and their connections (except start and end block)
     */
    public void clearCanvas(){
        // vsechno smazeme
        actualDebugLevel = 1;
        for (view.AbstractBlock vblock : viewToModelBlocks.keySet()){
                Block mblock = viewToModelBlocks.get(vblock);
                deleteModelBlock(mblock);
                dashboard.panel2.remove(vblock.labelBlock);
                // delete connection connected to block if there is just one
                deleteConnection(vblock);
        }
        dashboard.panel2.validate();
        dashboard.panel2.repaint();
        viewWorkingBlocks.clear();
        viewToModelBlocks.clear();
        // special inicialization for start and end block
        initFactoryBlock(newScheme.getfactoreBlockByName("Start"), newScheme.getfactoreBlockByName("End"));
    }

    /**
     * Method makes new view and model connection
     * 1. gets points where was clicked
     * 2. gets type of ports (input, output), 2 selected points can not have same port
     * 3. gets id of port
     * 4. from id we get the point of right port
     */
    public void makeNewConnection(){
        // we have 2 blocks to connect

        Point firstPoint = new Point(
                selectedBlock.get(0).getLeftClickedLast().x + selectedBlock.get(0).getXBlock(),
                selectedBlock.get(0).getLeftClickedLast().y +selectedBlock.get(0).getYBlock());

        Point clickPoint = new Point(
                selectedBlock.get(1).getLeftClickedLast().x + selectedBlock.get(1).getXBlock(),
                selectedBlock.get(1).getLeftClickedLast().y + selectedBlock.get(1).getYBlock());

        Integer typePortFirst = selectedBlock.get(0).getTypePort(firstPoint);
        Integer typePortSecond  = selectedBlock.get(1).getTypePort(clickPoint);

        if ( typePortFirst == typePortSecond ){
            System.err.println("You clicked on same type of port, port must be different type");
            JOptionPane.showMessageDialog(dashboard.frame1, "You clicked on same type of port, port must be different type");

        } else {
            Integer idFirst = selectedBlock.get(0).getPortId(firstPoint,typePortFirst);
            Integer idSecond = selectedBlock.get(1).getPortId(clickPoint, typePortSecond);
            JLabel labelConn = new JLabel();;

            // make new connection
            Connection conn = new Connection(selectedBlock.get(0), selectedBlock.get(1), labelConn);
            // set id of ports
            conn.setIdFromPort(idFirst);
            conn.setIdToPort(idSecond);
            // set types of ports
            conn.setTypeFrom(typePortFirst);
            conn.setTypeTo(typePortSecond);
            // actualize coordinates of ports
            conn.actualize();

            dashboard.panel2.add(labelConn);
            //System.out.println(viewToModelBlocks);
            model.Connection modelConn = newScheme.makeNewConnectionModel(

                    viewToModelBlocks.get(conn.getBlockFrom()),
                    viewToModelBlocks.get(conn.getBlockTo()),
                    conn.getIdFromPort(),
                    conn.getidToPort(),
                    conn.getTypeFrom());
            if ( modelConn == null){
                // if model says we can not create connection
                JOptionPane.showMessageDialog(dashboard.frame1, "Can not make connection, loot at error details");
                conn = null;
                dashboard.panel2.remove(labelConn);
            } else {
                Connection finalConn = conn;
                labelConn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        super.mouseEntered(e);
                        Port outport = getLineAccordingToCoordinates(finalConn,1);
                        if ( outport != null ){
                            //labelConn.setToolTipText("Values: " + outport.getHashOfValue() + "\nLevel: " + outport.getBlock().getLevel());
                            labelConn.setToolTipText("Values: " + modelConn.getOutput().getHashOfValue() + "\nLevel: " +  modelConn.getOutput().getBlock().getLevel());
                        }
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        //super.mouseClicked(e);
                        if ( SwingUtilities.isRightMouseButton(e) ){
                            //System.out.println("Deleting connection");
                            deleteConnectionSingleLineView(finalConn);
                            deleteConnectionSingleLineModel(modelConn);
                            // pozor na start a end
                            if ( modelConn.getOutput().getBlock().static_block ){
                                modelConn.getOutput().getBlock().removeOutputPort(modelConn.getOutput());
                            }
                            if ( modelConn.getInput().getBlock().static_block ){
                                modelConn.getInput().getBlock().removeInputPort(modelConn.getInput());
                            }
                            newScheme.actualizeLevel();
                        } else {
                            // can we enter input data?
                            chosenConn = modelConn;
                            inputPanel();
                        }
                    }
                });
                dashboard.addConnectionsOnDashboard(conn);
            }
        }
        distroySelectedBlocks();
    }

    /**
     * Method decides if connection contains start block
     * @param conn connection
     * @return true or false
     */
    public boolean isConnectedToStart(model.Connection conn){
        if ( conn.getOutput().getBlock().getName().equalsIgnoreCase("start") ){
            return true;
        }
        return false;
    }

    /**
     * Method actualizes input values of block
     * new values takes from output port of connected block
     * @param block block containing new values
     */
    public void executeConnectionToBlock(Block block){
        for ( model.Connection i : newScheme.getConnectionsOnModel() ){
            if ( i.getOutput().getBlock() == block ){
                // aktualizovali jsme vystupni
                i.getInput().setValue( i.getOutput().getHashOfValue() );
            }
        }
    }

    /**
     * Method actualizes levels to blocks
     */
    public void actualizeBlocksForLevel(){
        blocksForLevel.clear();
        for ( Block i : newScheme.modelWorkingBlocks ){
            if ( blocksForLevel.get(i.getLevel()) == null ||  blocksForLevel.get(i.getLevel()).isEmpty() ){
                List<Block> list = new ArrayList<Block>();
                list.add(i);
                blocksForLevel.put(i.getLevel(), list);

            } else {
                // uz zde jsou bloky
                blocksForLevel.get(i.getLevel()).add(i);
            }
        }
    }

    /**
     * Method called after clickong on solve
     * counts all connections
     */
    public void getResult()  {
        // vysledky budou v end blocku
        if ( ! newScheme.checkScheme()){
            System.err.println("Scheme is uncomplete");
            JOptionPane.showMessageDialog(dashboard.frame1, "Scheme is uncomplete");
            return;
        }
        actualizeBlocksForLevel();
        // budeme pocitat po levlech
        int actLevel = 0;
        Map<Integer, List> blocksForLevelOld = new TreeMap<>(blocksForLevel);

        for ( Integer level : blocksForLevelOld.keySet() ){
            actualDebugLevel = level;
            // jdeme podle levlu
            debugProgramStep();
        }
        // uklidime
        actualDebugLevel = 1;
        distroyDebugSelectedBlocks();
    }
    public view.AbstractBlock getViewBlock(model.Block block){
        for( view.AbstractBlock vBlock : viewToModelBlocks.keySet() ){
            if ( viewToModelBlocks.get(vBlock) == block ){
                return vBlock;
            }
        }
        return null;
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

    /**
     * Method distroys debug selected items
     */
    public void distroyDebugSelectedBlocks(){
        for (view.AbstractBlock vblock : debugSelectedBlock){
            changeIconToNormal(vblock);
        }
        debugSelectedBlock.clear();
    }


    public void changeIconToSelected( view.AbstractBlock vblock){
        vblock.labelBlock.setIcon( new ImageIcon(vblock.selectedPath));
    }
    public void changeIconToNormal( view.AbstractBlock vblock){
        vblock.labelBlock.setIcon( new ImageIcon(vblock.path));
    }

    /**
     * Method returns model block according to name
     * @param name name of block
     * @return model block
     */
    public Block getBlockByName(String name){
        for ( model.Block bl : factoreBlocks){
            if ( bl.getName().equalsIgnoreCase(name) ){
                return bl;
            }
        }
        return null;
    }

    /**
     * Method saves data on input, if data correct than it shows yes icon, otherwise error icon
     */
    public void sendData(){
        // sending data to model
        if(chosenConn == null){
            System.err.println("unchosen connection");
            return;
        }
        double val = 0;
        int i = 0;
        boolean correct = true;
        String valueStr ="";
        for ( String name : chosenConn.getOutput().getHashOfValue().keySet() ){
            // check if double
            valueStr = dashboard.textFieldValueGUI[i].getText().replaceAll(",","\\.");
            try{
                val = Double.parseDouble(valueStr);
                chosenConn.getOutput().getHashOfValue().put(name, val);
                chosenConn.getInput().getHashOfValue().put(name, val);
                correct = true;
            } catch (NumberFormatException nfe){
                dashboard.labelIconsGUI[i].setIcon(dashboard.iconNo);
                System.err.println( valueStr + " is not a number");
                correct = false;
                JOptionPane.showMessageDialog(dashboard.frame1, "Value must be a number");
                dashboard.textFieldValueGUI[i].setText(String.format("%.3f", (double) chosenConn.getOutput().getHashOfValue().get(name)));
            }

            if ( correct ){
                dashboard.labelIconsGUI[i].setIcon(dashboard.iconYes);
            } else {
                dashboard.labelIconsGUI[i].setIcon(dashboard.iconNo);
            }
            i++;
        }

        //dashboard.repaint();
    }

    /**
     * Method displays input form on left panel and fills data with actual data of chosen connection
     */
    public void inputPanel() {
        int i = 0;
        boolean start = true; // pripojeno ke start bloku
        Double value = 0.0;
        if(chosenConn == null){
            System.err.println("unchosen connection");
            return;
        }
        // vymazu vsude text
        for ( int j = 0; j < 3; j++ ){
            dashboard.textFieldValueGUI[j].setText("");
            dashboard.textFieldNameGUI[j].setText("");
        }
        if ( ! isConnectedToStart(chosenConn) ){
            start = false;
            dashboard.bsavePorts.setEnabled(false);
        } else {
            dashboard.bsavePorts.setEnabled(true);
        }
        // nastavim text podle connection
        for ( String name : chosenConn.getInput().getHashOfValue().keySet() ){
            dashboard.textFieldValueGUI[i].setEditable(true);
            dashboard.textFieldNameGUI[i].setEditable(false);

            value = chosenConn.getInput().getHashOfValue().get(name);
            dashboard.textFieldNameGUI[i].setText(name);
            dashboard.textFieldValueGUI[i].setText(String.format( "%.3f", (double) value ));


            dashboard.textFieldValueGUI[i].setVisible(true);
            dashboard.textFieldNameGUI[i].setVisible(true);
            dashboard.labelNameGUI[i].setVisible(true);
            dashboard.labelValueGUI[i].setVisible(true);
            if ( ! start ){
                dashboard.textFieldValueGUI[i].setEditable(false);
            }
            dashboard.textFieldValueGUI[i].setEnabled(true);
            dashboard.textFieldNameGUI[i].setEnabled(true);
            dashboard.labelNameGUI[i].setEnabled(true);
            dashboard.labelValueGUI[i].setEnabled(true);
            i++;
        }

        // not used values and names will ot be enable
        while ( i < 3){
            dashboard.textFieldValueGUI[i].setEnabled(false);
            dashboard.textFieldNameGUI[i].setEnabled(false);
            dashboard.labelNameGUI[i].setEnabled(false);
            dashboard.labelValueGUI[i].setEnabled(false);

            i++;
        }
        for ( int j = 0; j < 3; j++ ){
            // default icon - for keeping space next to input
            dashboard.labelIconsGUI[j].setIcon(dashboard.iconDef);
        }

        dashboard.panel4.setBounds(15, 410, 280, 145);
        dashboard.bsavePorts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendData();
            }
        });
    }

    /**
     * Method returns view block according to coordinates (Loader needs)
     * @param x coordinate x
     * @param y coordinate y
     * @return found abstract block
     */
    public AbstractBlock getViewBlockAccordingToCoordinate(Integer x, Integer y){
        for( AbstractBlock block : viewWorkingBlocks ){
            if ( block.getYBlock().equals( y) && block.getXBlock().equals(x )){
                return block;
            }
        }
        return null;
    }
    /**
     * Main runs program
     * @param args arguments
     * @throws IOException exceptions
     */
    public static void main( String[] args ) throws IOException {
        Program program = new Program();
        try{
            program.run();
        } catch (Exception exp){
            System.err.println(exp.getMessage());
        }
    }
}
