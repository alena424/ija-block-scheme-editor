/*
 * @file  AdditionBlock.java
 * @brief Class for addition block
 *
 * File containing specific functions for addition block
 *
 * @author Alena Tesarova (xtesar36)
 * @author Jan Sorm (xsormj00)
 */

package model;

import java.util.HashMap;
import java.util.Iterator;

public class AdditionBlock extends Block {
    public AdditionBlock( Scheme scheme, Integer level ) {
        super( scheme, level );
        this.addInputPort( "Alena" );
        this.addInputPort( "Alena" );
        this.addOutputPort( "Alena" );
    }

    public void execute() {
        HashMap<String,Double> sum = new HashMap<String, Double>();
        //double[] sum = new double[ this.input[i].getHashOfValue().size() ];
        for ( int i = 0; i < this.countInput; i++ ) {
            for ( Iterator<String> key = this.input.get( i ).getHashOfValue().keySet().iterator(); key.hasNext(); ) {
                System.out.println("Enter a password: ");
                //sum. += this.input[i].getHashOfValue()[j];
            }
        }
        /*for ( Integer j : this.input[ i ].getHashOfValue().values() ) {
            sum[ j ] += this.input[ i ].getHashOfValue()[ j ];
        }*/
    }
}
