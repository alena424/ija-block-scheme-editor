package controller;

import model.AdditionBlock;
import model.Block;
import model.Scheme;
import view.DashBoard;
import view.DashBoard_GUI;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;

public class Program {

    public static void main( String[] args ) throws IOException {

        // CREATING BLOCKS
        Scheme newScheme = new Scheme(1);
        newScheme.factorBlock("Start");
        newScheme.factorBlock("End");

        newScheme.factorBlock("Addition");
        newScheme.factorBlock("Invert");
        newScheme.factorBlock("Random");
        newScheme.factorBlock("Multiplication");
        newScheme.factorBlock("Division");
;

        // DASHBOARD with blocks
        DashBoard dashboard = new DashBoard(newScheme.getFactoreBlock());
        dashboard.setVisible();

        // get all blocks generated


        /*System.out.println("Enter a password: ");

        Scheme scheme = new Scheme( 1 );
        AdditionBlock first = new AdditionBlock( scheme, 0);
        AdditionBlock second = new AdditionBlock( scheme, 0);
        AdditionBlock third = new AdditionBlock( scheme, 0);
        System.out.println("first: " + first.getLevel() );
        System.out.println("second: " + second.getLevel() );
        System.out.println("third: " + third.getLevel() );
        System.out.println("cycle: " + scheme.isCycle() );
        System.out.println("levelFault: " + scheme.isLevelFault() );
        Integer maxId = scheme.getMaxId();
        System.out.println("Max id je: " + maxId );
        scheme.createConnection( third.getInputPort( 0 ), first.getOutputPort( 0 ) );

        System.out.println("first: " + first.getLevel() );
        System.out.println("second: " + second.getLevel() );
        System.out.println("third: " + third.getLevel() );
        System.out.println("cycle: " + scheme.isCycle() );
        System.out.println("levelFault: " + scheme.isLevelFault() );

        scheme.createConnection( third.getInputPort( 1 ), second.getOutputPort( 0 ) );

        System.out.println("first: " + first.getLevel() );
        System.out.println("second: " + second.getLevel() );
        System.out.println("third: " + third.getLevel() );
        System.out.println("cycle: " + scheme.isCycle() );
        System.out.println("levelFault: " + scheme.isLevelFault() );*/

       /* HashMap<Integer,Block> map = new HashMap<Integer, Block>();
        map.put( 'Alena', 42.0 );
        map.put( 'Alena', 42.0 );
        first.setInput( map, 0 );
        first.execute();*/
    }

}
