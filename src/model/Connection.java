/*
 * @file  Connection.java
 * @brief Class for connection
 *
 * File containing specific functions for connection.
 *
 * @author Alena Tesarova (xtesar36)
 * @author Jan Sorm (xsormj00)
 */

package model;

public class Connection {

    private Port input;
    private Port output;

    public Connection( Port input, Port output ) {
        this.input = input;
        this.output = output;
        input.setConnection( this );
        output.setConnection( this );
    }

    public Port getInput() {
        return this.input;
    }

    public Port getOutput() {
        return this.output;
    }

    public void deleteConnection() {
        this.input.unsetConnection();
        this.output.unsetConnection();
        this.input = null;
        this.output = null;
    }
}
