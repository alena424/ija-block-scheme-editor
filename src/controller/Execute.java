/*
 * @file  Execute.java
 * @brief Class for execute
 *
 * File containing specific functions for execute.
 *
 * @author Alena Tesarova (xtesar36)
 * @author Jan Sorm (xsormj00)
 */

package controller;

import model.Scheme;

public class Execute {

    private Scheme scheme;

    public void Execute( Scheme scheme ) {
        this.scheme = scheme;
    }

    public boolean execute() {
        if ( ! this.scheme.isReadyForExecute() ) {
            return false;
        }

        //TODO

        return true;
    }

}
