package controller;

import model.AdditionBlock;
import model.Block;
import model.FactoryBlock;
import model.Scheme;
import org.w3c.dom.events.Event;
import view.DashBoard;
import view.DashBoard_GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Program {
    //List <Block> factoreBlocks = new ArrayList<>();

    Scheme newScheme = new Scheme(1); // new shceme
    DashBoard dashboard; // new dashboard
    List <Block> factoreBlocks; // modelBlock created blocks
    List<JLabel> panelBlocks; // blocks on the left side, generates working blocks

    List<view.AbstractBlock> viewWorkingBlocks; // blocks on main canvas to work with in VIEW
    List<model.Block> modelWorkingBlocks; // blocks on main canvas to work with in MODEL

    List<view.AbstractBlock> selectedBlock; //selected blocks

    public Program(){

    }

    /**
     * Method creates types of block (appears in left panel)
     */
    public void createBlocks(){
        //newScheme = new Scheme(1);
        // CREATING BLOCKS
        newScheme.factorBlock("Start");
        newScheme.factorBlock("End");

        newScheme.factorBlock("Addition");
        newScheme.factorBlock("Invert");
        newScheme.factorBlock("Random");
        newScheme.factorBlock("Multiplication");
        newScheme.factorBlock("Division");

    }
    public void run(){
        createBlocks();

        dashboard = new DashBoard();
        dashboard.panel2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                distroySelectedBlocks();

            }
        });

        factoreBlocks = new ArrayList<>( newScheme.getFactoreBlock() );
        System.out.println(factoreBlocks);

        dashboard.getBlocksOnLeft(newScheme.getFactoreBlock());



        panelBlocks = dashboard.getBlocksOnPanel();
        viewWorkingBlocks = new ArrayList<>();
        modelWorkingBlocks = new ArrayList<>();

        initFactoryBlock(newScheme.getfactoreBlockByName("Start"), newScheme.getfactoreBlockByName("End"));



        for (JLabel lab: panelBlocks
                ) {
            String name_block = lab.getText();
            // get which block was clicked
            Block block = getBlockByName(name_block, factoreBlocks);
            // set event on click
            lab.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    generateBlock(block);

                }
            });
        }
        dashboard.setVisible();

    }
    protected void deleteModelBlock(model.Block block){
        System.out.println("deleting model ");

        modelWorkingBlocks.remove(block);
        block = null;

    }

    protected void deleteViewBlock(view.AbstractBlock block){
        System.out.println("deleting label view");
        dashboard.panel2.remove(block.labelBlock);
        dashboard.panel2.validate();
        dashboard.panel2.repaint();
        viewWorkingBlocks.remove(block);
        block = null;
    }

    /**
     * Inicialized start and end block
     * @param start start block
     * @param end end block
     */

    public void initFactoryBlock(Block start, Block end){
        System.out.println(start);
        generateBlock(start);
        System.out.println(end);
        generateBlock(end);
    }

    /**
     * Function generates block of type block
     * @param block block we get information from when we re creating view and model bblock
     */
    public void generateBlock(Block block){
        // new label for new block

        JLabel lblock = new JLabel();
        FactoryBlock factoryBlock = new FactoryBlock();

        System.out.println(block.getName());

        // CREATING VIEW BLOCK
        view.AbstractBlock addViewBlock;
        if ( block.static_block ){
            addViewBlock = new view.StaticBlock(block.getName(), lblock);
            if ( block.static_x != 0 && block.static_y != 0){
                addViewBlock.setX(block.static_x);
                addViewBlock.setY(block.static_y);

            } else {
                System.err.println("Static blocks must contain their static coordinates\n");
                return;
            }

        } else {
            addViewBlock = new view.Block(block.getName(), lblock);
        }

        System.out.println(addViewBlock);
        viewWorkingBlocks.add(addViewBlock);

        // CREATING MODEL BLOCK
        model.Block addModelBlock = factoryBlock.getBlock(block.getName());
        modelWorkingBlocks.add(addModelBlock);

        // CREATE SELECTED LIST
        selectedBlock = new ArrayList<>();

        addViewBlock.setPath(block.getPathOfIcon());
        addViewBlock.setName(block.getName());

        System.out.println("Name: " + block.getName());
        System.out.println("Inputs: " + block.getCountInput());
        System.out.println("Outputs: " + block.getCountOutput());

        addViewBlock.setNPortIn(block.getCountInput());
        addViewBlock.setNPortOut(block.getCountOutput());
        addViewBlock.setSelectedPath(block.getSelectedIcon());

        addViewBlock.labelBlock.setIcon(new ImageIcon( addViewBlock.getPath() ) );
        System.out.println(addViewBlock.getPath());

        dashboard.panel2.add(addViewBlock.labelBlock);
        if ( block.static_block ){
            // if it is a static block, can not be moved and it has special coorinates
            addViewBlock.labelBlock.setBounds(block.static_x, block.static_y, addViewBlock.getWidthBlock(), addViewBlock.getHeightBlock());
        } else {
            addViewBlock.labelBlock.setBounds(addViewBlock.getXBlock(), addViewBlock.getYBlock(), addViewBlock.getWidthBlock(), addViewBlock.getHeightBlock());
        }


        lblock.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if ( SwingUtilities.isRightMouseButton(e) ){
                    System.out.println("right click");
                    int indexM = modelWorkingBlocks.indexOf(addModelBlock);
                    if ( indexM != -1 ){
                        // cano not delete static objects!
                        if ( ! modelWorkingBlocks.get(indexM).static_block )
                                deleteModelBlock( modelWorkingBlocks.get(indexM));
                    } else {
                        System.out.println("Object does not exist!");
                    }

                    int indexV = viewWorkingBlocks.indexOf(addViewBlock);
                    if ( indexV != -1 ){
                        if (! viewWorkingBlocks.get(indexV).isStatic_object())
                                deleteViewBlock( viewWorkingBlocks.get(indexV));
                    } else {
                        System.out.println("Object does not exist!");
                    }
                } else {
                    System.out.println("left click");

                    changeIconToSelected(addViewBlock);
                    // we must save where was clicked
                    addViewBlock.setLeftClickedLast(e.getPoint());

                    if ( selectedBlock.size() < 2 ){
                        selectedBlock.add(addViewBlock);
                        // if it is a second chosen block, we make a line
                        if ( selectedBlock.size() == 2 ){
                            makeNewConnection(e);
                        }
                    } else {
                        // if we have 2 nad more, it should not happen and we clear list
                        distroySelectedBlocks();
                    }

                }
            }
        });
    }
    public void drawPoint(Point a){
        Graphics g = dashboard.panel2.getGraphics();
        Graphics2D g2d = (Graphics2D)g;

        //g2d.setStroke(new BasicStroke(10));
        g2d.drawLine(a.x, a.y, a.x, a.y);
    }

    public void makeNewConnection(MouseEvent event){
        // we have 2 blocks to connect
       Graphics g = dashboard.panel2.getGraphics();
        Graphics2D g2d = (Graphics2D)g;
        int width = 4;
        g2d.setStroke(new BasicStroke(width));

        g2d.setColor(new Color(61,204,199));
        System.out.println("Block1 " + selectedBlock.get(0).getPath() + ": x = " + selectedBlock.get(0).getXBlock() + "y = " +selectedBlock.get(0).getYBlock());
        System.out.println("Block2 "+ selectedBlock.get(1).getPath() + ":  x = " + selectedBlock.get(1).getXBlock() + "y = " +selectedBlock.get(1).getYBlock());

        Point firstPoint = new Point(
                selectedBlock.get(0).getLeftClickedLast().x + selectedBlock.get(1).getXBlock(),
                selectedBlock.get(0).getLeftClickedLast().y +selectedBlock.get(1).getYBlock());
        drawPoint(firstPoint);

        Point clickPoint = new Point(
                event.getX()+selectedBlock.get(1).getXBlock(),
                event.getY()+selectedBlock.get(1).getYBlock());
        drawPoint(clickPoint);

        System.out.println("Fist click:");
        System.out.println(firstPoint);

        System.out.println("Clicked place:");
        System.out.println(clickPoint);
        //Point portfirst = selectedBlock.get(0).getPort(selectedBlock.get(0).getLeftClickedLast());
        Point portfirst = selectedBlock.get(0).getPort(firstPoint);
        Point portsecond = selectedBlock.get(1).getPort(clickPoint);
        drawPoint(portfirst);
        drawPoint(portsecond);
        System.out.println("Port on second block found:");
        System.out.println(portsecond);

        //g2d.drawLine(selectedBlock.get(0).getXBlock(), selectedBlock.get(0).getYBlock(), selectedBlock.get(1).getXBlock(), selectedBlock.get(1).getYBlock());
        //g2d.drawLine(selectedBlock.get(0).getXBlock(), selectedBlock.get(0).getYBlock(), selectedBlock.get(1).getXBlock(), selectedBlock.get(1).getYBlock());
        //g2d.drawLine(0,0,500,250);
        g2d.drawLine(portfirst.x,portfirst.y, portsecond.x, portsecond.y);
        distroySelectedBlocks();
    }

    public void distroySelectedBlocks(){
        for (view.AbstractBlock vblock : selectedBlock){
            changeIconToNormal(vblock);
        }
        selectedBlock.clear();
    }
    public void changeIconToSelected( view.AbstractBlock vblock){
        vblock.labelBlock.setIcon( new ImageIcon(vblock.selectedPath));
    }
    public void changeIconToNormal( view.AbstractBlock vblock){
        vblock.labelBlock.setIcon( new ImageIcon(vblock.path));
    }
    public Block getBlockByName(String name, List <model.Block> factoreBlocks){
        for ( model.Block bl : factoreBlocks){
            if ( bl.getName().equalsIgnoreCase(name) ){
                return bl;
            }
        }
        return null;
    }

    public static void main( String[] args ) throws IOException {
        Program program = new Program();
        program.run();

    }



}
