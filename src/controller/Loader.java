/*
 * @file  Loader.java
 * @brief Class for loader
 *
 * File containing specific functions for loader.
 *
 * @author Alena Tesarova (xtesar36)
 * @author Jan Sorm (xsormj00)
 */

package controller;

import model.Block;
import model.Port;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.Charset;

public class Loader implements ActionListener {
    JFrame mainframe;
    private JTextField filename = new JTextField(), dir = new JTextField();
    Program program;

    public Loader(JFrame frame, Program program){
        mainframe = frame;
        this.program = program;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser c = new JFileChooser();
        // Demonstrate "Open" dialog:
        int rVal = c.showOpenDialog(mainframe);
        if (rVal == JFileChooser.APPROVE_OPTION) {
            filename.setText(c.getSelectedFile().getName());
            dir.setText(c.getCurrentDirectory().toString());
            File file = c.getSelectedFile();
            try{
                loadBlockScheme(file);
            } catch( Exception ex ){
                System.err.print(ex.getMessage());
            }
        }
        if (rVal == JFileChooser.CANCEL_OPTION) {
            filename.setText("You pressed cancel");
            dir.setText("");
        }
    }
    public void loadBlockScheme(File file) throws FileNotFoundException{
        try{

            InputStream suborStream = new FileInputStream(file);
            InputStreamReader suborReader = new InputStreamReader(suborStream, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(suborReader);
            String blocks = br.readLine();
            program.clearCanvas();

            String[] myBlocks = blocks.split("#");
            //System.out.println("Block: " + myBlocks[0]);
            //System.out.println("Block1: " + myBlocks[1]);
            //System.out.println(myBlocks.toString());
            for (String strIn : myBlocks){

                String line[] = strIn.split("\\|");
                /** 1. nazev
                 * 2. x
                 * 3. y
                 * height
                 * width
                 */
                model.Block block = program.getBlockByName(line[0]);
                //System.out.println("Loader: generatng block" + block);
                if ( ! block.getName().equalsIgnoreCase("start") && ! block.getName().equalsIgnoreCase("end") ) {

                    view.AbstractBlock blockView = program.generateBlock(block);
                    //System.out.println(blockView.getYBlock());

                    blockView.setX(Integer.parseInt(line[1]));
                    blockView.setY(Integer.parseInt(line[2]));
                    blockView.setHeight(Integer.parseInt(line[3]));
                    blockView.setWidth(Integer.parseInt(line[4]));
                    blockView.labelBlock.setBounds(blockView.getXBlock(), blockView.getYBlock(), blockView.getWidthBlock(), blockView.getHeightBlock());
                    program.dashboard.panel2.repaint();
                }
            }

            String connections = br.readLine();
            String[] myConn = connections.split("#");
            for (String strIn : myConn){
                String line[] = strIn.split("\\|");
                /** 1. from souradnice x
                 * 2. from souradnice y
                 * 3. to souradnice x
                 * 4. to souradnice y
                 * 5. id port from
                 * 6. id port to
                 * 7. from block x
                 * 8. from block y
                 * 9. to block x
                 * 10. to block y
                 */
                // musime najit view block, ktery odpovida souradnicim

                view.AbstractBlock vBlockFrom = program.getViewBlockAccrodingToCoordinate(Integer.parseInt(line[6]), Integer.parseInt(line[7]));

                view.AbstractBlock vBlockTo = program.getViewBlockAccrodingToCoordinate(Integer.parseInt(line[8]), Integer.parseInt(line[9]));
                //System.out.println("Block from: " + vBlockFrom + " Block to: " + vBlockTo);

                Point connPointFrom = new Point(Integer.parseInt(line[0]) - vBlockFrom.getXBlock() ,
                        Integer.parseInt(line[1]) - vBlockFrom.getYBlock() );
               // System.out.println(connPointFrom);

                Point connPointTo = new Point(Integer.parseInt(line[2]) - vBlockTo.getXBlock() ,
                         Integer.parseInt(line[3]) - vBlockTo.getYBlock()  );
                //System.out.println(connPointTo);

                vBlockFrom.setLeftClickedLast(connPointFrom);
                vBlockTo.setLeftClickedLast(connPointTo);
                program.selectedBlock.add(vBlockFrom);
                program.selectedBlock.add(vBlockTo);
                program.makeNewConnection();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
