package view;

import controller.Program;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class shows help
 */
public class Help implements ActionListener {
    Program program;
    public Help(Program program){
        this.program = program;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String message = "Block editor\n" +
                "version 1.0\n\n" +
                "Generating blocks: choose a block on the left panel, by left click on the block, block discovers in the left corner of canvas\n" +
                "Connecting blocks: left click first on the port of fitst block to be connected, then on the second block (chosen block will change background)\n" +
                "Deleting connections: right click on connection\n" +
                "Deleting blocks: right click on block\n" +
                "Debug: debug means one step, just click on debug button (scheme does not have to be complete)\n" +
                "Solve: left click on solve, you will see results on mouseenter the connection by end block";
        JOptionPane.showMessageDialog(program.dashboard.frame1,message);

    }
}
