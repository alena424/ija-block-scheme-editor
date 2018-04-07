package tests;

import model.*;
import org.junit.Assert;
import java.util.HashMap;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

public class ProgramTest {

    Scheme scheme;
    private AdditionBlock first;
    private AdditionBlock second;
    private AdditionBlock third;

    @Before
    public void setUp() throws Exception {

        first = new AdditionBlock( scheme, 0);
        second = new AdditionBlock( scheme, 0);
        third = new AdditionBlock( scheme, 0);

    }

    /**
     * Zakladni testy bloku
     */
    @Test
    public void test01() {

        Assert.assertEquals( "Get level of first block", 0, (long) first.getLevel());
        Assert.assertEquals("Get level of second block", 0,(long) second.getLevel());
        Assert.assertEquals("Get level of third block",0, (long) third.getLevel());
        Assert.assertEquals("Maximum block ID", 3,  (long) scheme.getMaxId());

        Assert.assertTrue(first.isFreeInput());
        Assert.assertTrue(second.isFreeInput());
        Assert.assertTrue(third.isFreeInput());

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
     * Testy tridu Port
     */
    @Test
    public void test02() {

        /** PORT */
        // vytvorime port k bloku1
        Port portFirst = new Port( 1, "port1", first);

        HashMap<String, Double> port_def = new HashMap<String, Double>();
        port_def.put("teplota", 21.2);
        port_def.put("hustota", 51.7);
        portFirst.setValue(port_def);

        Assert.assertEquals( portFirst.getBlock(), first);
        Assert.assertTrue( portFirst.isFree() );
        Assert.assertFalse(portFirst.getHashOfValue().isEmpty());

        first.addOutputPort("port1");
        Assert.assertEquals( "Port type", portFirst.getType(), "port1");


    }

    /**
     * Testy na tridu connection
     */
    @Test
    public void test03() {
        // nejake hodnoty do portu
        HashMap<String, Double> port_def = new HashMap<String, Double>();
        port_def.put("teplota", 21.2);
        port_def.put("hustota", 51.7);

        /** PRIPOJIME OUTPUT PORT K PRVNIMU BLOKU */

        // vytvorime port s typem Type1
        second.addInputPort("Type1");
        second.setInput( port_def, 0 );


        /** PRIPOJIME INPUT PORT K DRUHEMU BLOKU */
        // vytvorime port s typem Type1
        first.addOutputPort("Type1");
        first.setOutput( port_def, 0 );
        Assert.assertTrue(first.isFreeInput());

        /** NOVE SPOJENI */
        Connection connFirstSecond = new Connection(  second.getInputPort(0), first.getOutputPort(0) );
        Assert.assertEquals( connFirstSecond.getInput(),  second.getInputPort(0) );
        Assert.assertEquals( connFirstSecond.getOutput(),  first.getOutputPort(0) );

        // smazeme spojeni
        connFirstSecond.deleteConnection();
        Assert.assertNull(connFirstSecond.getOutput());
        Assert.assertNull(connFirstSecond.getInput());

    }
}