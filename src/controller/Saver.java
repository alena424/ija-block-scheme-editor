/*
 * @file  Connection.java
 * @brief Class for saver
 *
 * File containing specific functions for saver.
 *
 * @author Alena Tesarova (xtesar36)
 * @author Jan Sorm (xsormj00)
 */

package controller;

import view.DashBoard;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Saver implements ActionListener {
    JFrame mainframe;
    FileOutputStream outFile = null;
    Program program;

    /*private JTextField filename = new JTextField(),
            dir = new JTextField();*/
    String filename;
    PrintWriter writer = null;

    public Saver(JFrame frame, Program program){
        mainframe = frame;
        this.program = program;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser c = new JFileChooser();
        // Demonstrate "Save" dialog:

        c.setSelectedFile(new File("BlockScheme.scheme"));

        int rVal = c.showSaveDialog(mainframe);
        if (rVal == JFileChooser.APPROVE_OPTION) {
            filename = c.getSelectedFile().getName();
           // dir.setText(c.getCurrentDirectory().toString());
            File file = c.getSelectedFile();

            try{
                saveBlockScheme(file);
            } catch( Exception ex ){

            }

        }
        if (rVal == JFileChooser.CANCEL_OPTION) {
            //filename.setText("You pressed cancel");
            //dir.setText("");
        }

    }
    public void saveBlockScheme(File file) throws FileNotFoundException, UnsupportedEncodingException {
        try ( PrintWriter writer = new PrintWriter(file, "UTF-8")) {
            //writer.print("blocks#");

            for ( view.AbstractBlock block :  program.viewToModelBlocks.keySet()){
                // projdeme vsechny bloky

                writer.print(program.viewToModelBlocks.get(block).getName() + "|");
                writer.print(block.getXBlock() + "|");
                writer.print(block.getYBlock()+ "|");
                writer.print(block.getHeightBlock() + "|");
                writer.print(block.getWidthBlock() + "#");
            }
            writer.println();

            //writer.print("\nconnections#");
            for ( view.Connection conn : program.dashboard.getConnectionsOnDashboard() ){
                writer.print(conn.cooFrom.x +"|"+ conn.cooFrom.y + "|" + conn.cooTo.x + "|" + conn.cooTo.y + "|");
                writer.print(conn.getIdFromPort() + "|" + conn.getidToPort() + "|");
                writer.print(conn.getBlockFrom().getXBlock() + "|"+conn.getBlockFrom().getYBlock() +  "|");
                writer.print(conn.getBlockTo().getXBlock() + "|"+conn.getBlockTo().getYBlock() +  "#");
            }
        }
    }
}
