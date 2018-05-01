/*
 * @file  Scheme.java
 * @brief Class for scheme
 *
 * File containing specific functions for scheme.
 *
 * @author Alena Tesarova (xtesar36)
 * @author Jan Sorm (xsormj00)
 */

package model;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Scheme {
    private Integer id;
    private HashMap<Integer,Block> allBlocks = new HashMap<Integer, Block>(); // hash
    public List<model.Block> modelWorkingBlocks; // array
    private Integer maxId = 0;
    private boolean cycle = false;
    private boolean levelFault = false;
    protected FactoryBlock blockFactory = new FactoryBlock();
    //protected AbstractBlock[] factoreBlock;
    List<Block> factoreBlock = new ArrayList<Block>();
    // generated block to work with
    List<Connection> connectionsOnModel = new ArrayList<>();
    public Block startblock;
    public Block endblock;

    public Scheme( Integer id ) {
        modelWorkingBlocks = new ArrayList<>();
        this.id = id;
    }

    public Integer getMaxId() {
        return this.maxId;
    }

    public boolean isCycle() {
        return this.cycle;
    }

    public boolean isLevelFault() {
        return this.levelFault;
    }

   /* public void addBlock( Block newBlock ) {
        this.allBlocks.put( maxId, newBlock );
        this.maxId++;
    }*/

    /**
     * Class for creating objects
     * @param name identification of the block (p.e. Addition, Invert)
     * @param inputP
     * @param outputP
     *
     */
    public void factorBlock(String name, HashMap<Integer, Port> inputP, HashMap<Integer, Port> outputP){
        factoreBlock.add( blockFactory.getBlock(name, inputP, outputP ));
    }

    /**
     * Of the block is defined in the scheme, return block by name
     * @param name name of block we want to find
     * @return found Block
     */
    public Block getfactoreBlockByName(String name){
        for ( Block i : factoreBlock
             ) {

            if ( i.getName().equalsIgnoreCase(name) ){
                return i;
            }
        }
        return null;
    }

    /**
     * Checks if ports can be connected
     * @param port1 first port
     * @param port2 second port
     * @return true or false
     */
    public boolean checkPortsToBeConnected(Port port1, Port port2){
        // 1. same type
        //System.out.println(port1.getName());
        //System.out.println(port2.getName());
        if ( ! port1.getName().equals(port2.getName())){
            System.err.println("Ports must be same type otherwise can not be connected\n");
            return false;
        }
        // 2. must be both free
        if ( ! port1.isFree() ){
            System.err.println("First port is not free");
            return false;
        }
        if ( ! port2.isFree() ){
            System.err.println("Second port is not free");
            return false;
        }
        return true;

    }


    /**
     * how many block we have at the moment
     */
    public Integer numberFactorBlock(){
        return factoreBlock.size();
    }

    public List<Block> getFactoreBlock() {
        return factoreBlock;
    }

    public void removeBlock(Block delBlock ) {
        delBlock.deleteBlockConnection();
        this.allBlocks.remove( delBlock.id );
    }

    public HashMap<Integer, Block> getBlocks() {
        return this.allBlocks;
    }

    public boolean isReadyForExecute() {
        for ( Block block : this.allBlocks.values() ) {
            if ( block.getLevel() != 0 && block.isFreeInput() ) {
                return false;
            }
        }
        return true;
    }
    // todo nahradit ! za makenewconnectionmodel
    public boolean createConnection( Port input, Port output ) {
        if ( ! input.isFree() || ! output.isFree() || input.getType() != output.getType() ) {
            return true;
        }
        Connection newConnection = new Connection( input, output );
        connectionsOnModel.add(newConnection);
        this.actualizeLevel();
        return true;
    }


    public model.Connection makeNewConnectionModel(Block blockFrom, Block blockTo, Integer idFromPort, Integer idToPort, Integer typePortFrom){
        // connecting start block with end block
        //System.out.println("new con block from: "+ blockFrom);
        //System.out.println("new con block to: "+ blockTo);
        if( blockFrom.static_block && blockTo.static_block ){
            System.err.println("Can not connect start block with end block");
            return null;
        }

        // create connection beetween start or end block
        if ( blockFrom.static_block || blockTo.static_block ){
            // static block behaves different way
            // end block - can not have any output ports
            // start block - no input ports
            if ( blockFrom.getClass().getName().equalsIgnoreCase("model.startblock") ){
                // startblock was clicked first, need information from blockTo
                // create new input port, type of connected port
                Port startPort = new Port(blockTo.getInputPortById(idToPort).getName(),
                        blockTo.getInputPortById(idToPort).getHashOfValue());
                startPort.setOwnerBlock(blockFrom);
                startPort.setId(idFromPort);

                //System.out.println("Creating from port of id" + idFromPort);
                if ( blockFrom.addoutputPort(idFromPort,startPort) == false){
                    //System.out.println("End");
                    // id of port is used
                    return null;
                }
            } else if(  blockTo.getClass().getName().equalsIgnoreCase("model.startblock") ){
                // startblock was clicked second, need info from blockFrom

                Port startPort = new Port(blockFrom.getInputPortById(idFromPort).getName(),
                        blockFrom.getInputPortById(idFromPort).getHashOfValue());
                startPort.setOwnerBlock(blockTo);
                startPort.setId(idToPort);

                //System.out.println("Creating to port of id " + idToPort);
                if ( blockTo.addoutputPort(idToPort,startPort  ) == false ){
                    // id of port is used
                    //System.out.println("End");
                    return null;
                }
            }

            if ( blockFrom.getClass().getName().equalsIgnoreCase("model.endblock") ){
                // get information from blockTo
                Port endPort = new Port(blockTo.getOutputPortById(idToPort).getName(),
                        blockTo.getOutputPortById(idToPort).getHashOfValue());
                endPort.setOwnerBlock(blockTo);
                endPort.setId(idFromPort);

                //System.out.println("Creating from port of id" + idFromPort);
                if ( blockFrom.addinputPort(idFromPort,endPort) == false){
                    // id of port is used
                    return null;
                }

            } else if  ( blockTo.getClass().getName().equalsIgnoreCase("model.endblock") ){
                // info from blockFrom
                Port endPort = new Port(blockFrom.getOutputPortById(idFromPort).getName(),
                        blockFrom.getOutputPortById(idFromPort).getHashOfValue());
                endPort.setOwnerBlock(blockFrom);
                endPort.setId(idFromPort);

                //System.out.println("Creating to port of id " + idToPort);
                if ( blockTo.addinputPort(idToPort,endPort) == false){
                    // id of port is used
                    return null;
                }
            }
        }
        // vime id a typy napojenych portu
        model.Connection connModel;
        if ( typePortFrom == 1 ){
            // input port from
            //System.out.println("id of to port " + idToPort);
            if( checkPortsToBeConnected(blockFrom.getInputPortById(idFromPort), blockTo.getOutputPortById(idToPort)) ){
                connModel = new model.Connection(blockFrom.getInputPortById(idFromPort),
                        blockTo.getOutputPortById(idToPort));
            } else {
                //System.err.println("Ports have different type, can not be connected");
                return null;
            }

        } else {
            if ( checkPortsToBeConnected(blockTo.getInputPortById(idToPort),
                    blockFrom.getOutputPortById(idFromPort) ) ){
                connModel = new model.Connection(blockTo.getInputPortById(idToPort),
                        blockFrom.getOutputPortById(idFromPort));

            }
            else {
                // System.err.println("Ports are different types, can not be connected");
                return null;
            }
        }
        connModel.getInput().setUnFree();
        connModel.getOutput().setUnFree();
        // adding connection to array on scheme
        getConnectionsOnModel().add(connModel);
        actualizeLevel();
        // cyklus?
        if ( detectCycle() ){
            connModel.deleteConnection();
            getConnectionsOnModel().remove(connModel);
            return null;
        }
        return connModel;
    }

    public List<Connection> getConnectionsOnModel() {
        return connectionsOnModel;
    }

    public boolean removeConnectionFromModel(Connection conn){
        return connectionsOnModel.remove(conn);
    }

    public Integer getMaxLevelOfOutputPort(Block block){
        Integer maxLevel = 0;
        // projdeme vsechny spojeni, kde je tento block jako input block
        System.out.println("BB:"+block);
        for( Connection i : connectionsOnModel ){

            System.out.println( "Connin: "+i.getInput().getBlock());
            if ( i.getInput().getBlock() == block ){
                // block je jako input block
                System.out.println("Block: "+ block);
                Block connectedBlock = i.getOutput().getBlock();
                System.out.println("Connected block: "+ connectedBlock);
                if ( connectedBlock.getLevel() > maxLevel ){
                    maxLevel = connectedBlock.getLevel();
                }
            }
        }
        return maxLevel;
    }
    public void actualizeLevel(){
        System.out.println("Actuale level: ");
        for ( Block i : modelWorkingBlocks ){
            i.setLevel(getMaxLevelOfOutputPort(i) + 1);
        }
        for ( Block i : modelWorkingBlocks ){
            i.setLevel(getMaxLevelOfOutputPort(i) + 1);
        }
    }
    public boolean detectCycle(){
        // cyklus detekujeme v pripade, ze se vracime bloku, ktery je spojen s podezrelym blokem
        System.out.println("Detect cycle: ");
        //Stack<Block> stack = new Stack<Block>();
        //stack.push(startblock);
        for ( Block actBlock : modelWorkingBlocks ){
            // zjistime nasledniky

            Stack<Block> stack = new Stack<Block>();
            for (Port port : actBlock.outputPorts.values()) {
                Connection connection = port.getConnection();
                if (connection == null) {
                    continue;
                }
                stack.push(connection.getInput().getOwnerBlock());
            }
            Integer depth = 0;
            while (! stack.isEmpty() && depth < 10){
                Block block = stack.pop();
                System.out.println(block);
                if ( actBlock == block ){
                    System.err.println("Cycle detected");
                    return true;
                }

                for (Port port : block.outputPorts.values()) {
                    Connection connection = port.getConnection();
                    if (connection == null) {
                        continue;
                    }
                    stack.push(connection.getInput().getOwnerBlock());
                }
                depth++;
            }
        }
        return false;
    }


    public void actualizeLevel2() {
        // toto jsou uzly, na ktere uz jsme nekdy sahali
        LinkedList<Integer> closedAll = new LinkedList<Integer>();
        for ( Integer idBlock : this.allBlocks.keySet() ) {
            // preskocime ty bloky, ktere nemaji volny zadny vstup
            Block rootBlock = this.allBlocks.get( idBlock );
            if ( ! rootBlock.isFreeInput() ) {
                continue;
            }

            // zasobniky na nove expandovane bloky a jejich predpokladane levely
            Stack<Block> stack = new Stack<Block>();
            Stack<Integer> stackLastLevel = new Stack<Integer>();
            // toto jsou uzly, na ktere jsme sahali u tohoto konkretniho korenoveho bloku
            LinkedList<Integer> closed = new LinkedList<Integer>();

            Integer actLevel = 0;

            stack.push( rootBlock );
            stackLastLevel.push( actLevel );
            while ( ! stackLastLevel.isEmpty() ) {
                Block actBlock = stack.pop();
                // jestlize, jsme narazili na nejaky jiz prochazeny uzel, tak jsme nasli cyklus
                if ( closed.contains( actBlock.getId() ) ) {
                    this.cycle = true;
                    return;
                }

                // vytahnu si level, ktery bych chtel uzlu pridelit
                actLevel = stackLastLevel.pop();
                // pokud se na blok uz nekdy sahalo a level neodpovida, tak je chyba v levelovani
                if ( actBlock.getLevel() != actLevel && closedAll.contains( actBlock.getId() ) ) {
                    this.levelFault = true;
                    return;
                } else if ( actBlock.getLevel() != actLevel ) {
                    // levely se ruzni a na blok se jeste nesahalo, tak mu nastavime predpokladany level
                    actBlock.setLevel( actLevel );
                }
                // dame blok do closed
                closed.add( actBlock.getId() );
                closedAll.add( actBlock.getId() );
                // expandujeme o vsechny nasledujici bloky
                for ( Port port : actBlock.outputPorts.values() ) {
                    Connection connection = port.getConnection();
                    if ( connection == null ) {
                        continue;
                    }
                    stack.push( connection.getInput().getOwnerBlock() );
                    stackLastLevel.push( actLevel + 1 );
                }
            }
        }
        // pokud jsme dosli az sem, tak je vse bez chybicky
        this.cycle = false;
        this.levelFault = false;
    }
    public boolean checkScheme(){
        // zkontrolujeme, jestli vsechny vstupy maji vystupy a obracene
        for ( Block i : modelWorkingBlocks ){
            if ( i.isFreeInput() ){
                System.out.println("IN" + i);
                return false;
            }
            if ( i.isFreeOutput() ){
                System.out.println("OUT" + i);
                return false;
            }
        }
        return true;
    }


}
