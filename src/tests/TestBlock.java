/*
 * @file  ProgramTest.java
 * @brief Test class for program
 *
 * File containing tests for project.
 *
 * @author Jan Sorm (xsormj00)
 */

package tests;

import model.AdditionBlock;
import model.Connection;
import model.Port;
import model.Scheme;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class TestBlock {

    Scheme scheme;
    private AdditionBlock first;
    private AdditionBlock second;
    private AdditionBlock third;

    @Before
    public void setUp() throws Exception {

        scheme = new Scheme( 1 );
        first = new AdditionBlock( scheme, 0);
        second = new AdditionBlock( scheme, 0);
        third = new AdditionBlock( scheme, 0);

    }

    /**
     * Zakladni testy bloku - test funkce getLevel, getMaxId, getId
     */
    @Test
    public void test01() {

        Assert.assertEquals( "Get level of first block", 0, (long) first.getLevel());
        Assert.assertEquals("Get level of second block", 0,(long) second.getLevel());
        Assert.assertEquals("Get level of third block",0, (long) third.getLevel());
        Assert.assertEquals("Maximum block ID", 3,  (long) scheme.getMaxId());
        Assert.assertEquals( "Get id of first block", 0, (long) first.getId());
        Assert.assertEquals( "Get id of second block", 1, (long) second.getId());
        Assert.assertEquals( "Get id of third block", 2, (long) third.getId());


    }

    /**
     * Zakladni testy bloku - test funkce isFreeInput
     */
    @Test
    public void test02() {

        Assert.assertTrue(first.isFreeInput());
        Assert.assertTrue(second.isFreeInput());
        Assert.assertTrue(third.isFreeInput());

    }


    /**
     * Zakladni testy bloku - test setLevel
     */
    @Test
    public void test03() {

        // nastaveni levelu bloku
        first.setLevel( 3 );
        second.setLevel( 4 );

        Assert.assertEquals( "Get level of first block", 3, (long) first.getLevel());
        Assert.assertEquals("Get level of second block",4,(long) second.getLevel());
        Assert.assertEquals("Get level of third block",0, (long) third.getLevel());


        // zmena nastaveni levelu
        second.setLevel( 5 );
        third.setLevel( 2 );

        Assert.assertEquals( "Get level of first block", 3, (long) first.getLevel());
        Assert.assertEquals("Get level of second block",5,(long) second.getLevel());
        Assert.assertEquals("Get level of third block",2, (long) third.getLevel());

    }

    /**
     * Zakladni testy bloku - test addInputPort a addOutputPort
     */
    @Test
    public void test04() {


        Assert.assertEquals( "Get count of input port", 2, (long) first.getCountInput());
        Assert.assertEquals("Get count of output port",1,(long) first.getCountOutput());

        // zmena vychoziho poctu bloku
        first.addInputPort("Honza");
        first.addOutputPort("Honza");

        Assert.assertEquals( "Get count of input port", 3, (long) first.getCountInput());
        Assert.assertEquals("Get count of output port",2,(long) first.getCountOutput());

    }

    /**
     * Zakladni testy bloku a scheme - test funkce createConnection, getInputPort a getOutputPort
     */
    @Test
    public void test05() {

        // spojeni druheho a prvniho  bloku
        scheme.createConnection( second.getInputPort( 0 ), first.getOutputPort( 0 ) );

        Assert.assertEquals( "Get level of first block", 0, (long) first.getLevel());
        Assert.assertEquals("Get level of second block",1,(long) second.getLevel());
        Assert.assertEquals("Get level of third block",0, (long) third.getLevel());

        // spojeni druheho a  tretiho bloku
        scheme.createConnection( third.getInputPort( 0 ), second.getOutputPort( 0 ) );

        Assert.assertEquals( "Get level of first block", 0, (long) first.getLevel());
        Assert.assertEquals("Get level of second block",1,(long) second.getLevel());
        Assert.assertEquals("Get level of third block",2, (long) third.getLevel());

    }

    /**
     * Zakladni testy scheme - test funkce isCycle a actualizeLevel
     */
    @Test
    public void test06() {

        Assert.assertTrue( scheme.isCycle() == false );

        // cyklicky zapojime
        scheme.createConnection( second.getInputPort( 0 ), first.getOutputPort( 0 ) );
        Assert.assertTrue( scheme.isCycle() == false );
        scheme.createConnection( third.getInputPort( 0 ), second.getOutputPort( 0 ) );
        Assert.assertTrue( scheme.isCycle() == false );
        scheme.createConnection( first.getInputPort( 0 ), third.getOutputPort( 0 ) );

        Assert.assertTrue( scheme.isCycle() == true );
    }

    /**
     * Zakladni testy scheme - test funkce isLevelFault a actualizeLevel
     */
    @Test
    public void test07() {

        Assert.assertTrue( scheme.isLevelFault() == false );

        // cyklicky zapojime
        scheme.createConnection( second.getInputPort( 0 ), first.getOutputPort( 0 ) );
        Assert.assertTrue( scheme.isLevelFault() == true );
        scheme.createConnection( second.getInputPort( 1 ), third.getOutputPort( 0 ) );

        Assert.assertTrue( scheme.isLevelFault() == false );
    }

}
