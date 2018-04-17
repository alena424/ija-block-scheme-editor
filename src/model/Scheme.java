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

import view.AbstractBlock;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Scheme {
    private Integer id;
    private HashMap<Integer,Block> allBlocks = new HashMap<Integer, Block>();
    private Integer maxId = 0;
    private boolean cycle = false;
    private boolean levelFault = false;
    protected FactoryBlock blockFactory = new FactoryBlock();
    //protected AbstractBlock[] factoreBlock;
    List<Block> factoreBlock = new ArrayList<Block>();
    // generated block to work with

    public Scheme( Integer id ) {

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

    public void addBlock( Block newBlock ) {
        this.allBlocks.put( maxId, newBlock );
        this.maxId++;
    }

    /**
     * Class for creating objects
     * @param name identification of the block (p.e. Addition, Invert)
     *
     */
    public void factorBlock(String name){
        factoreBlock.add( blockFactory.getBlock(name ));
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

    public boolean createConnection( Port input, Port output ) {
        if ( ! input.isFree() || ! output.isFree() || input.getType() != output.getType() ) {
            return false;
        }
        Connection newConnection = new Connection( input, output );
        this.actualizeLevel();
        return true;
    }
/*
    // TODO - toto bude funkce, ktera bude urcovat, zda je potreba provadet celkovou aktualizaci
    public void actualizeLevelNewConnection( Connection connection ) {
        Block inputBlock = connection.getInput().getOwnerBlock();
        Block outputBlock = connection.getOutput().getOwnerBlock();
        if ( inputBlock.getLevel() + 1 == outputBlock.getLevel() ) {
            if ( this.cycle && this.levelFault ) {
                // nebyla zadna chyba a nove spojeni nam novou urcite nepridalo, neni potreba ani nic aktualizovat
            }
        } else if ( inputBlock.getLevel() + 1 > outputBlock.getLevel() ) {
            // TODO je potreba provadet aktualizaci od outBlocku pomoci DFS treba
        } else {
            /
        }
    }
*/
    public void actualizeLevel() {
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
                for ( Port port : actBlock.output.values() ) {
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
}
