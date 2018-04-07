package model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;
import java.util.Arrays;

public class DisplayModel {
    public Integer numberOfBlock;
    Map<Integer, List <Integer>> connectionParent = new HashMap<Integer, List <Integer>>();
    // mapuje naslednik (klic) => predchudce

    Map<Integer, List <Integer>> connectionChildren = new HashMap<Integer, List <Integer>>();
    // mapuje predchudce (klic) => naslednik

    // schvavam si vsechny bloky a jejich id
    Map<Integer, Block> allBlocks = new HashMap<Integer, Block>();
    public Double result ;

    public Integer maxLevel; // maximalni level
    public Integer minLevel; // minimalni

    // generuje id
    WrapId generateId = new WrapId();
    Block startBlock;
    Block endBlock;

    //id
    private Integer id;

    public DisplayModel(){
        // nemame zatim zadne bloky
        this.numberOfBlock = 0;
        this.id = 1; // zacinam id 1, 0 je zarezervovana
        this.result = 0.0;
        this.maxLevel = 0;
        this.minLevel = 0;

        //pocatecni blok
        TypePort[] start_out = new TypePort[6]; //2 na vstupu
        start_out[0] = new TypePort("honza", 20.0);
        start_out[1] = new TypePort("honza", 30.0);
        Block start = new Block(null, start_out);
        start.id = 0;
        this.allBlocks.put(start.id , start);
        this.startBlock = start;


        // koncovy blok
        TypePort[] end_in = new TypePort[6]; //1 vystup
        end_in[0] = new TypePort("honza", 0.0);
        Block end = new Block(end_in, null);
        end.id = 999;
        this.allBlocks.put(end.id, end);
        this.endBlock = end;

    }
    private void actualizeLevel(Block block){
        List<Integer> toExpand = connectionChildren.get(block.id);
        //List <Integer> toExpand = new ArrayList<Integer>( connectionChildren.get(id) );
        // index prvniho je 0
        //System.out.println(toExpand);
        Integer max_level = 0; // maximalni level
        if ( toExpand.indexOf(999) == -1 || ! toExpand.isEmpty() ){
            for( Integer item : toExpand ){
                Block block1 = this.allBlocks.get(item);
                if ( block1 == null ){
                    System.out.println("Blok neexistuje");
                    return;
                }
                System.out.println("ID: " + item );
                System.out.println("LEVEL BEF: " + block1.level );

                // potrebuju predchudce a nejvetsi integer, podle toho zmenim level
                List<Integer> parrent = connectionParent.get(item) ;
                // pokud nema rodice, neni pripojeno, level nemam od ceho aktualizovat
                if( parrent == null ){
                    System.out.println("Nemam pripojeno, neni predek, nemuzu aktulizovat");
                    return;
                }
                for ( Integer level : parrent ){
                    if ( level > max_level ){
                        max_level = level;
                    }
                }
                // nasli jsme level predchudce
                block1.level = max_level;
                System.out.println("LEVEL AFTER: " + block1.level );
                getAllChildren(item);
            }
        }

    }

    public int connect( Block block_out, Block block_to ){
        // spojim je, az pri vypoctu zkontroluji typ
        // mame jeste volne nejake porty?
        if ( ( block_to.inputPorts != null && block_to.countInput >= block_to.inputPorts.length ) ||
                block_out.outputPorts != null && block_out.countOutput >= block_out.outputPorts.length ){
            // chci zapojit port, to uz ale nejde
            // hlaska nejsou volne porty
            System.out.println("Nejsu zadne volne porty\n");
            return 0;
        }
        List<Integer> children = connectionChildren.get(block_out.id);
        List<Integer> parents = connectionParent.get(block_to.id);
        if ( children == null ){
            List<Integer> x = new ArrayList<Integer>(1);
            x.add( block_to.id );
            connectionChildren.put(block_out.id, x );
            List<Integer> y = new ArrayList<Integer>(1);
            y.add( block_out.id );
            connectionParent.put(block_to.id, y);
        } else {
            children.add(block_to.id);
            connectionChildren.put(block_out.id, children);
        }
        if ( parents != null ) {
            parents.add(block_to.id);
            connectionParent.put(block_to.id, parents);
        }

        // musim aktualizovat rodice
        this.actualizeLevel(block_out);

        //connectionParent.put(block_in.id, Arrays.asList(new Integer[]{ block_out.id } ) );
        //children.add(block_to.id);
        //
        block_to.countInput++;
        block_out.countOutput++;
        /*
        if ( parents.isEmpty() ){
            parents.add(block_out.id); // pridam mu rodice

        }*/
        //List <Integer> parents= new ArrayList<Integer>(  );
        //
        //connectionParent.put(block_in.id, parents);

        return 1;
    }



    public void displayBlock( Block block ){
        // dostali jsme informaci o tom, ze mame novy blok na obrazovce
        if ( this.numberOfBlock == 0 ){
            // pridavam prvni blok
            block.level = 1;
        }
        // vygeneruji si unikatni ID
        Integer id = generateId.getId();
        block.id = id;

        // ulozim si id a blok
        this.allBlocks.put(id, block);
        this.numberOfBlock++;
        System.out.println("Novy blok: ");
        printBlok(block);
        // musim najit vsechny predchudce
        //List <Integer> parrent = this.connectionParent.get(block.id);
        // mam vsechny predchudce


    }

    private void printBlok( Block block){
        //Block block = this.allBlocks.get(id);
        //if ( block == null ){
          //  System.out.println("Blok neexistuje");
            //return;
        //}
        System.out.println("---- ID-" + block.id + " ----");
        if ( block.level != null){
            System.out.println("---- LEVEL-" + block.level + " ----");
        }


        if ( block.inputPorts != null ){
            System.out.println("Nazev vstupnich portu: " );
            for (int i = 0; i < block.inputPorts.length; i++){
                System.out.println( i + ". " + block.inputPorts[i].name + ", " + block.inputPorts[i].value + " ");

            }
            System.out.println("Pocet plny/celkem: " + block.countInput + '/' + block.inputPorts.length );

        }
        if ( block.outputPorts != null ) {
            System.out.println("Nazev vystupnich portu: " );
            for (int i = 0; i < block.outputPorts.length && block.outputPorts[i] != null; i++){
                System.out.println( i + ". " + block.outputPorts[i].name + ", " + block.outputPorts[i].value + " ");

            }
            System.out.println("Pocet plny/celkem: " + block.countOutput+ '/' + block.outputPorts.length );

        }
    }
    public void getAllChildren(Integer id){
        // budeme potrebovat 1 list
        List<Integer> toExpand = connectionChildren.get(id);
        //List <Integer> toExpand = new ArrayList<Integer>( connectionChildren.get(id) );
        // index prvniho je 0
        //System.out.println(toExpand);

        if ( toExpand != null && ( ! toExpand.isEmpty() ) && toExpand.indexOf(999) == -1 ){
            for( Integer item : toExpand ){

                //System.out.println("ID: " + item );
                getAllChildren(item);
            }
        }
        
    }
    /* funkce vytiskne vsechny rodice bloku
     */
    public void  getAllParents(Integer id){
        // budeme potrebovat 1 list
        List <Integer> toExpand = new ArrayList<Integer>( connectionParent.get(id) );
        // index prvniho je 0

        while( toExpand.indexOf(0) != -1 ){
            for( Integer item : toExpand ){
                getAllParents(item);
                System.out.println("ID: " + item );
            }
        }
    }
    public void count(){
        // projde vsechny urovne
        for (int i = this.minLevel; i < this.maxLevel; i++){
            //this.operate(i);
            getAllChildren(0);
        }
    }
}
