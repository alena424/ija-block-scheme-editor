package controller;

import model.Block;
import model.FactoryBlock;
import model.Port;
import model.Scheme;
import org.junit.Assert;
import org.w3c.dom.events.Event;
import view.AbstractBlock;
import view.Connection;
import view.DashBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.lang.reflect.Array;
import java.util.*;

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
    //List<model.Block> modelWorkingBlocks; // blocks on main canvas to work with in MODEL

    List<view.AbstractBlock> selectedBlock; //selected blocks, maximum is 2 blocks
    //List<Connection> connectionsList = new ArrayList<>();
    HashMap<view.AbstractBlock,model.Block> viewToModelBlocks = new HashMap<AbstractBlock,Block>();
    model.Connection chosenConn = null;

    public Program(){

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
        System.out.println("Create:");
        System.out.println(blocksInputPorts);
        System.out.println(blocksOutputPorts);
        //List blocksOutputPorts = new ArrayList();

        // CREATING BLOCKS
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
     *
     * @param i
     * @param event
     * @param type 1 - we want output port, 2 - we want input port
     * @return
     */
    public model.Port getLineAccordingToCoordinates(Connection i, MouseEvent event, Integer type){
            if( i.getTypeFrom() == type){
                // from is input, we want output
                return viewToModelBlocks.get(i.getBlockTo()).getOutputPortById(i.getidToPort());
            } else {
                // from is output
                System.out.println( "! " +i.getTypeFrom());
                return viewToModelBlocks.get(i.getBlockFrom()).getOutputPortById(i.getIdFromPort());
            }
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
       // dashboard.inputFrameForm(newScheme.getfactoreBlockByName("Addition").getInputPorts());

        dashboard.panel2.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                System.out.println("entered");
                // did user enter a connection?
                for (Connection i: dashboard.getConnectionsOnDashboard()
                        ) {
                    System.out.println(e);
                    model.Port port = getLineAccordingToCoordinates(i,e, 1);
                    if ( port != null ){
                        System.out.println(port);
                    }
                }
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
        dashboard.bload.addActionListener(new Loader(dashboard.frame1));
        dashboard.bsave.addActionListener(new Saver(dashboard.frame1));

        for (JLabel lab: panelBlocks
                ) {
            String name_block = lab.getText();
            // get which block was clicked

            Block block = getBlockByName(name_block);
            // set event on click
            lab.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("Clicked block:" + block);
                    generateBlock(block);

                }

            });
        }
        dashboard.bcount.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                getResult();
            }
        });
        dashboard.setVisible();

    }
    protected void deleteModelBlock(model.Block block){
        System.out.println("deleting model ");
        newScheme.modelWorkingBlocks.remove(block);
        block = null;
    }

    /**
     * Method deletes a view block with all connections connected to block
     * @param block
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
                System.out.println("removeOUT");
                rem.getOutput().getBlock().removeOutputPort(rem.getOutput());
            }
            if ( rem.getInput().getBlock().static_block ){
                System.out.println("removeIN");
                rem.getInput().getBlock().removeInputPort(rem.getInput());
            }
            deleteConnectionSingleLineModel(rem);

        }
        return true;
    }
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
        //dashboard.panel2.repaint();

    }
    public void deleteConnectionSingleLineView(Connection r){
        // VIEW
        dashboard.getConnectionsOnDashboard().remove(r);
        // remove jlabel from panel
        dashboard.panel2.remove(r.centerLabelToolTip);
        r = null;
        dashboard.panel2.repaint();
    }
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
     * @param block
     */
    protected void deleteConnection(view.AbstractBlock block){

       deleteViewConnection(block);
       deleteConnectionModel(block);
    }

    /**
     * Inicialized start and end block
     * @param start start block
     * @param end end block
     */

    public void initFactoryBlock(Block start, Block end){
        generateBlock(start);
        generateBlock(end);
    }

    /**
     * Method initializes port according to given hash
     * @param port according to this hash we create real port usinf new
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
     */
    public void generateBlock(Block block){
        // new label for new block

        JLabel lblock = new JLabel();
        FactoryBlock factoryBlock = new FactoryBlock();

        //System.out.println(block.getName());

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
                    //System.out.println("Dragged");
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

                      addViewBlock.setX( event.getX()+event.getComponent().getX()-addViewBlock.getXBlock() );
                      addViewBlock.setY( event.getY()+event.getComponent().getY()-addViewBlock.getYBlock() );


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
        System.out.println(block);
        System.out.println("Init input port" + block.getInputPorts());
        System.out.println("Init output port" + block.getOutputPorts());
        HashMap<Integer, Port> inputPortsModel = createPortsToBlock( block.getInputPorts());
        //System.out.println("Init output port" + block.getOutputPorts());
        HashMap<Integer, Port> outputPortsModel = createPortsToBlock( block.getOutputPorts());
        model.Block addModelBlock = factoryBlock.getBlock
                (block.getName(), inputPortsModel, outputPortsModel);

        System.out.println("realblock: " + addModelBlock);
        if ( block.getName().equalsIgnoreCase("start") ){
            newScheme.startblock = addModelBlock;
        }

        newScheme.modelWorkingBlocks.add(addModelBlock);
        // we need to know which view block is model block
        viewToModelBlocks.put(addViewBlock,addModelBlock);

        // CREATE SELECTED LIST
        selectedBlock = new ArrayList<>();

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
                    //System.out.println("right click");
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
                    //System.out.println("left click");

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
            @Override
            public void mouseEntered(MouseEvent e) {
                lblock.setToolTipText( "\nInput ports: " + formatPortsToString( addModelBlock.getInputPorts()) + "\n\nOutput ports: " + formatPortsToString( addModelBlock.getOutputPorts())  );

            }
        });

    }
    public String formatPortsToString(HashMap<Integer,Port> ports){
        String final_str = "";
        for( Integer i : ports.keySet() ){
            final_str += ports.get(i).getHashOfValue() + "\n";
        }
        return final_str;
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

        Point clickPoint = new Point(
                event.getX() + selectedBlock.get(1).getXBlock(),
                event.getY() + selectedBlock.get(1).getYBlock());

        Integer typePortFirst = selectedBlock.get(0).getTypePort(firstPoint);
        Integer typePortSecond  = selectedBlock.get(1).getTypePort(clickPoint);

        if ( typePortFirst == typePortSecond ){
            System.err.println("You clicked on same type of port, port must be different type");
            JOptionPane.showMessageDialog(dashboard.frame1, "You clicked on same type of port, port must be different type");

        } else {
            Integer idFirst = selectedBlock.get(0).getPortId(firstPoint,typePortFirst);
            Integer idSecond = selectedBlock.get(1).getPortId(clickPoint, typePortSecond);
            JLabel labelConn = new JLabel();
            //labelConn.setBackground(new Color(61, 204, 199));
            //labelConn.setOpaque(true);
            //labelConn.setHorizontalAlignment(SwingConstants.CENTER);

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
            System.out.println(viewToModelBlocks);
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
                        Port outport = getLineAccordingToCoordinates(finalConn,e,1);
                        System.out.println(outport);
                        System.out.println(outport.getBlock());
                        System.out.println(outport.getHashOfValue());
                        if ( outport != null ){
                            //labelConn.setToolTipText("Values: " + outport.getHashOfValue() + "\nLevel: " + outport.getBlock().getLevel());
                            labelConn.setToolTipText("Values: " + modelConn.getOutput().getHashOfValue() + "\nLevel: " +  modelConn.getOutput().getBlock().getLevel());
                        }
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        //super.mouseClicked(e);
                        if ( SwingUtilities.isRightMouseButton(e) ){
                            System.out.println("Deleting connection");
                            deleteConnectionSingleLineView(finalConn);
                            deleteConnectionSingleLineModel(modelConn);
                            // pozor na start a end
                            if ( modelConn.getOutput().getBlock().static_block ){

                                modelConn.getOutput().getBlock().removeOutputPort(modelConn.getOutput());
                            }
                            if ( modelConn.getInput().getBlock().static_block ){
                                System.out.println("Deleting port: " + modelConn.getInput().getBlock() + " " + modelConn.getInput() + " " + modelConn.getInput().getBlock().getInputPorts());
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
    public boolean isConnectedToStart(model.Connection conn){
        if ( conn.getOutput().getBlock().getName().equalsIgnoreCase("start") ){
            return true;
        }
        return false;
    }
    public void newFrameInputData(){
        //dashboard.inputFrameForm();

    }
    public void executeConnectionToBlock(Block block){
        for ( model.Connection i : newScheme.getConnectionsOnModel() ){
            if ( i.getOutput().getBlock() == block ){
                // aktualizovali jsme vystupni
                i.getInput().setValue( i.getOutput().getHashOfValue() );
            }
        }
    }

    public void getResult(){
        // vysledky budou v end blocku
        if ( ! newScheme.checkScheme()){
            System.err.println("Scheme is uncomplete");
            JOptionPane.showMessageDialog(dashboard.frame1, "Scheme is uncomplete");
            return;
        }
        //getInputToBlock();

        Map<Integer, List> blocksForLevel = new TreeMap<>();
        newFrameInputData();
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

        // budeme pocitat po levlech
        int actLevel = 0;
        for ( Integer level : blocksForLevel.keySet() ){
            // jdeme podle levlu
            List<Block> blocks = blocksForLevel.get(level);
            for ( Block actBlock : blocks ){
                actBlock.execute();
                executeConnectionToBlock(actBlock);
            }
        }

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
    public Block getBlockByName(String name){
        for ( model.Block bl : factoreBlocks){
            if ( bl.getName().equalsIgnoreCase(name) ){
                return bl;
            }
        }
        return null;
    }
    public void sendData(){
        // sending data to model
        if(chosenConn == null){
            System.err.println("unchosen connection");

            return;
        }

        dashboard.lstate.setVisible(true);
        double val = 0;
        int i = 0;
        String valueStr ="";
       // Port port =

        for ( String name : chosenConn.getOutput().getHashOfValue().keySet() ){
            //System.out.println(name);
            // check if double
            valueStr = dashboard.textFieldValueGUI[i].getText();
            try{
                val = Double.parseDouble(valueStr);
                //System.out.println("pred: " + modelConn.getOutput().getHashOfValue());
                System.out.println("outport: "+chosenConn.getOutput());
                System.out.println("inport: "+chosenConn.getInput());
                chosenConn.getOutput().getHashOfValue().put(name, val);
                chosenConn.getInput().getHashOfValue().put(name, val);
                //System.out.println(val);
                System.out.println("prepisuju: " + chosenConn.getOutput().getHashOfValue());
                //dashboard.lstate.setText("saved");
                dashboard.labelIconsGUI[i].setIcon(dashboard.iconYes);
                dashboard.labelIconsGUI[i].setOpaque(true);


                dashboard.lstate.setForeground(new Color(000,200,81));
            } catch (NumberFormatException nfe){
                System.err.println( valueStr + " is not a number");
                JOptionPane.showMessageDialog(dashboard.frame1, "Value must be a number");
                //dashboard.lstate.setText("error");
                // to co tam bylo predtim
                dashboard.labelIconsGUI[i].setIcon(dashboard.iconNo);
                dashboard.labelIconsGUI[i].setOpaque(true);

                dashboard.textFieldValueGUI[i].setText(chosenConn.getOutput().getHashOfValue().get(name).toString());
                dashboard.lstate.setForeground(new Color(255,053,71));
                return;
            }
            i++;
           ;
        }
        dashboard.repaint();


    }
    public void inputPanel() {
        int i = 0;
        boolean start = true; // pripojeno ke start bloku
        Double value = 0.0;
        if(chosenConn == null){
            System.err.println("unchosen connection");
            return;
        }
        for ( String name : chosenConn.getInput().getHashOfValue().keySet() ){
            dashboard.textFieldValueGUI[i].setEditable(true);
            dashboard.textFieldNameGUI[i].setEditable(false);

            value = chosenConn.getInput().getHashOfValue().get(name);
            dashboard.textFieldNameGUI[i].setText(name);
            dashboard.textFieldValueGUI[i].setText(value.toString());

            dashboard.textFieldValueGUI[i].setVisible(true);
            dashboard.textFieldNameGUI[i].setVisible(true);
            dashboard.labelNameGUI[i].setVisible(true);
            dashboard.labelValueGUI[i].setVisible(true);
            i++;
        }

        if ( ! isConnectedToStart(chosenConn) ){
            // nejsme pripojeni ke start bloku, nemuzeme upravovat hodnoty
            i = 0;
            start = false;
            dashboard.bsavePorts.setVisible(false);
        } else {
            dashboard.bsavePorts.setVisible(true);
        }
        while ( i < 3){
            dashboard.textFieldValueGUI[i].setEditable(false);
            dashboard.textFieldNameGUI[i].setEditable(false);

            if (start){
                // pokud nejsme pripojeni ke start bloku, chceme videt aspon hodnoty vsech ostatnich portu po kliknuti na propoj
                dashboard.textFieldValueGUI[i].setText("");
                dashboard.textFieldNameGUI[i].setText("");

            }

            /*dashboard.textFieldValueGUI[i].setVisible(false);
            dashboard.textFieldNameGUI[i].setVisible(false);
            dashboard.labelValueGUI[i].setVisible(false);
            dashboard.labelNameGUI[i].setVisible(false);*/
            i++;
        }
        for ( int j = 0; i < 3; j++ ){
            dashboard.labelIconsGUI[j].setOpaque(false);
            dashboard.labelIconsGUI[j].setIcon(null);
        }
        dashboard.lstate.setVisible(false);

        //dashboard.panel4.setBounds(25, 410, 230, 145);
        dashboard.panel4.setBounds(15, 410, 280, 145);

        dashboard.bsavePorts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                sendData();
            }
        });
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
