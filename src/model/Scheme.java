/*
 * @file  Scheme.java
 * @brief Class for scheme
 *
 * File containing specific functions for scheme. Main class to work with model.
 *
 * @author Alena Tesarova (xtesar36)
 * @author Jan Sorm (xsormj00)
 */

package model;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Scheme {
    private Integer id; // id of scheme
    private HashMap<Integer,Block> allBlocks = new HashMap<Integer, Block>(); // hash
    public List<model.Block> modelWorkingBlocks; // array
    private boolean cycle = false;
    private boolean levelFault = false;
    protected FactoryBlock blockFactory = new FactoryBlock();
    List<Block> factoreBlock = new ArrayList<Block>();
    // generated block to work with
    List<Connection> connectionsOnModel = new ArrayList<>();
    public Block startblock; // start block is good to know

    public Scheme( Integer id ) {
        modelWorkingBlocks = new ArrayList<>();
        this.id = id;
    }

    /**
     * Class for creating objects
     * @param name identification of the block (p.e. Addition, Invert)
     * @param inputP input port
     * @param outputP output port
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
     * numberFactorBlock
     * @return how many block we have at the moment
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

    /**
     * Method make new model connection
     * @param blockFrom block that was clicked first
     * @param blockTo block that was clicked second
     * @param idFromPort id of port from (belongs to blockfrom)
     * @param idToPort id of port to  (belongs to blockfrom)
     * @param typePortFrom input - 1, output - 2 (belong toblock from)
     * @return model connection or null
     */
    public model.Connection makeNewConnectionModel(Block blockFrom, Block blockTo, Integer idFromPort, Integer idToPort, Integer typePortFrom){
        // connecting start block with end block
        Port startPort = null;
        Port endPort = null;
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
                startPort = new Port(blockTo.getInputPortById(idToPort).getName(),
                        blockTo.getInputPortById(idToPort).getHashOfValue());
                startPort.setOwnerBlock(blockFrom);
                startPort.setId(idFromPort);

                if ( blockFrom.addoutputPort(idFromPort,startPort) == false){
                    // id of port is used
                    return null;
                }
            } else if(  blockTo.getClass().getName().equalsIgnoreCase("model.startblock") ){
                // startblock was clicked second, need info from blockFrom

                startPort = new Port(blockFrom.getInputPortById(idFromPort).getName(),
                        blockFrom.getInputPortById(idFromPort).getHashOfValue());
                startPort.setOwnerBlock(blockTo);
                startPort.setId(idToPort);

                if ( blockTo.addoutputPort(idToPort,startPort  ) == false ){
                    // id of port is used
                    return null;
                }
            }

            if ( blockFrom.getClass().getName().equalsIgnoreCase("model.endblock") ){
                // get information from blockTo
                endPort = new Port(blockTo.getOutputPortById(idToPort).getName(),
                        blockTo.getOutputPortById(idToPort).getHashOfValue());
                endPort.setOwnerBlock(blockTo);
                endPort.setId(idFromPort);

                if ( blockFrom.addinputPort(idFromPort,endPort) == false){
                    // id of port is used
                    return null;
                }

            } else if  ( blockTo.getClass().getName().equalsIgnoreCase("model.endblock") ){
                // info from blockFrom
                endPort = new Port(blockFrom.getOutputPortById(idFromPort).getName(),
                        blockFrom.getOutputPortById(idFromPort).getHashOfValue());
                endPort.setOwnerBlock(blockFrom);
                endPort.setId(idToPort);

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
            if( checkPortsToBeConnected(blockFrom.getInputPortById(idFromPort), blockTo.getOutputPortById(idToPort)) ){
                connModel = new model.Connection(blockFrom.getInputPortById(idFromPort),
                        blockTo.getOutputPortById(idToPort));
            } else {
                //musime odstranit vyrvorene porty, pokud byly nejake vytvorene
                if ( startPort != null ){
                    Block portblock = startPort.getBlock();
                    portblock.removeOutputPort(startPort);
                    startPort = null;
                }
                if ( endPort != null ){
                    Block portblock = endPort.getBlock();
                    portblock.removeInputPort(endPort);
                    endPort = null;
                }
                return null;
            }

        } else {
            if ( checkPortsToBeConnected(blockTo.getInputPortById(idToPort),
                    blockFrom.getOutputPortById(idFromPort) ) ){
                connModel = new model.Connection(blockTo.getInputPortById(idToPort),
                        blockFrom.getOutputPortById(idFromPort));

            }
            else {
                //musime odstranit vyrvorene porty, pokud byly nejake vytvorene
                if ( startPort != null ){
                    Block portblock = startPort.getBlock();
                    portblock.removeOutputPort(startPort);
                    startPort = null;
                }
                if ( endPort != null ){
                    Block portblock = endPort.getBlock();
                    portblock.removeInputPort(endPort);
                    endPort = null;
                }
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

    /**
     * Method finds max level of blocks that are input blocks for block in arguments
     * @param block trying to find max level of this block
     * @return max level
     */
    public Integer getMaxLevelOfOutputPort(Block block){
        Integer maxLevel = 0;
        // projdeme vsechny spojeni, kde je tento block jako input block
        for( Connection i : connectionsOnModel ){

            if ( i.getInput().getBlock() == block ){
                // block je jako input block
                Block connectedBlock = i.getOutput().getBlock();
                if ( connectedBlock.getLevel() > maxLevel ){
                    maxLevel = connectedBlock.getLevel();
                }
            }
        }
        return maxLevel;
    }

    /**
     * Method goes throw all blocks and actualizes all levels
     */
    public void actualizeLevel(){
        for ( Block i : modelWorkingBlocks ){
            i.setLevel(getMaxLevelOfOutputPort(i) + 1);
        }
        for ( Block i : modelWorkingBlocks ){
            i.setLevel(getMaxLevelOfOutputPort(i) + 1);
        }
    }

    /**
     * Method detects cycle in scheme
     * @return true - correct, false - cycle detected
     */
    public boolean detectCycle(){
        // cyklus detekujeme v pripade, ze se vracime bloku, ktery je spojen s podezrelym blokem
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

    /**
     * Method checks that all input and output ports of all blocks are not free
     * @return true - OK, false - scheme is uncomplete
     */
    public boolean checkScheme(){
        // zkontrolujeme, jestli vsechny vstupy maji vystupy a obracene
        for ( Block i : modelWorkingBlocks ){
            if ( i.isFreeInput() ){
                return false;
            }
            if ( i.isFreeOutput() ){
                return false;
            }
        }
        return true;
    }
}
