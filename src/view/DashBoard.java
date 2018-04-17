/*
 * @file  DashBoard.java
 * @brief Class for DashBoard
 *
 * File containing specific functions for dashboard.
 *
 * @author Alena Tesarova (xtesar36)
 * @author Jan Sorm (xsormj00)
 */

package view;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class DashBoard extends JPanel {
    JFrame frame1 = new JFrame("Block editor");
    JPanel panel1 = new JPanel();
    JPanel panel2 = new JPanel();
    JButton lhelp = new JButton();
    JLabel lwelcome = new JLabel();
    JLabel laddion = new JLabel();
    JLabel linvert = new JLabel();
    JLabel lmulti = new JLabel();
    JLabel ldivision = new JLabel();
    JLabel lrand = new JLabel();
    JLabel lchoose = new JLabel();
    JButton bcount = new JButton();
    JButton bload = new JButton();
    JButton bsave = new JButton();
    JLabel lend = new JLabel();
    JLabel lstart = new JLabel();
    JLabel ldown = new JLabel();
    JLabel lup = new JLabel();
    JLabel lleftpan = new JLabel();
    JLabel lback = new JLabel();
   // Container frame1ContentPane;

    /**
     * Method displays a dashboard, needs to know which blocks should be displayed
     * @param factoreblock array of blocks to work with
     */
    public DashBoard(List<model.Block> factoreblock){
       // Container frame1ContentPane = frame1.getContentPane();
        //frame1ContentPane.setLayout(null);
        frame1.setResizable(false);

        panel2.setOpaque(false);
        panel2.setLayout(null);
        panel1.add(panel2);
        panel2.setBounds(295, 25, 705, 625);

        panel1.setLayout(null);

        //---- lwelcome ----
        lwelcome.setText("Welcome to Block Editor!");
        lwelcome.setFont(lwelcome.getFont().deriveFont(lwelcome.getFont().getStyle() | Font.BOLD, lwelcome.getFont().getSize() + 6f));
        lwelcome.setHorizontalAlignment(SwingConstants.CENTER);
        lwelcome.setForeground(new Color(247, 247, 247));
        panel1.add(lwelcome);
        lwelcome.setBounds(15, 20, 255, 60);

        // default height and width of block
        Integer width = 65;
        Integer height = 70;

        // position of first block
        Integer X = 30;
        Integer Y = 200;

        Integer i = 0; // counter
        JLabel [] labelblock = new JLabel[factoreblock.size()];
        for ( model.Block block : factoreblock ){
            if ( i % 3 == 0 && i != 0){
                Y += 85;
                X = 30;
            }
            //JLabel label = new JLabel();
            labelblock[i] = new JLabel();
            labelblock[i].setIcon(block.getIcon());
            panel1.add(labelblock[i]);

            Integer finalI = i;
            labelblock[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    labelblock[finalI].setToolTipText( block.getName());
                }
            });


            if ( block.static_block){
                labelblock[i].setBounds(block.static_x, block.static_y, width, height);
            } else{
                labelblock[i].setBounds(X, Y, width, height);
                labelblock[i].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        JLabel lblock = new JLabel();
                        System.out.println(block.getName());
                        Block addBlock = new Block(block.getName(), lblock);
                        addBlock.setPath(block.getPathOfIcon());
                        addBlock.setName(block.getName());
                        addBlock.setSelectedPath(block.getSelectedIcon());
                        generateBlock(addBlock);

                        lblock.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                super.mouseClicked(e);
                                System.out.println(" click22");
                            }
                        });

                    }
                });
                X += 85;
                i++;
            }

        }

        /*
        //---- laddion ----
        laddion.setIcon(new ImageIcon("img/addition.png"));
        panel1.add(laddion);
        laddion.setBounds(30, 197, 65, 70);
        laddion.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JLabel lblock = new JLabel();
                Block addBlock = new Block("add", lblock);
                generateBlock(addBlock);
            }
        });
        laddion.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                laddion.setToolTipText("Addition block");
            }
        });

        //---- linvert ----
        linvert.setIcon(new ImageIcon("img/invert.png"));
        panel1.add(linvert);
        linvert.setBounds(new Rectangle(new Point(200, 200), linvert.getPreferredSize()));
        linvert.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JLabel lblock = new JLabel();
                Block invertBlock = new Block("invert", lblock);
                generateBlock(invertBlock);
            }
        });
        linvert.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                linvert.setToolTipText("Invert block");
            }
        });

        //---- lmulti ----
        lmulti.setIcon(new ImageIcon("img/multiplication.png"));
        panel1.add(lmulti);
        lmulti.setBounds(new Rectangle(new Point(115, 200), lmulti.getPreferredSize()));
        lmulti.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JLabel lblock = new JLabel();
                Block multiBlock = new Block("multiply", lblock);
                generateBlock(multiBlock);
            }
        });
        lmulti.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                lmulti.setToolTipText("Multiplication block");
            }
        });


        //---- ldivision ----
        ldivision.setIcon(new ImageIcon("img/division.png"));
        panel1.add(ldivision);
        ldivision.setBounds(new Rectangle(new Point(115, 290), ldivision.getPreferredSize()));
        ldivision.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JLabel lblock = new JLabel();
                Block divisionBlock = new Block("devide", lblock);
                generateBlock(divisionBlock);
            }
        });
        ldivision.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ldivision.setToolTipText("Division block");
            }
        });


        //---- lrand ----
        lrand.setIcon(new ImageIcon("img/random.png"));
        panel1.add(lrand);
        lrand.setBounds(30, 285, 65, 75);
        lrand.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JLabel lblock = new JLabel();
                Block randomBlock = new Block("random", lblock);
                generateBlock(randomBlock);
            }
        });
        lrand.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                lrand.setToolTipText("Random block");
            }
        });*/

        //---- lchoose ----
        lchoose.setText("Choose a block:");
        lchoose.setForeground(new Color(247, 247, 247));
        lchoose.setFont(lchoose.getFont().deriveFont(lchoose.getFont().getSize() + 3f));
        panel1.add(lchoose);
        lchoose.setBounds(35, 155, 115, 30);

        //---- bcount ----
        bcount.setText("COUNT");
        bcount.setForeground(new Color(247, 247, 247));
        bcount.setBackground(new Color(61, 204, 199));
        bcount.setFont(bcount.getFont().deriveFont(bcount.getFont().getSize() + 5f));
        panel1.add(bcount);
        bcount.setBounds(25, 575, 230, 60);

        //---- bload ----
        bload.setText("LOAD");
        bload.setBackground(new Color(61, 204, 199));
        bload.setFont(bload.getFont().deriveFont(bload.getFont().getSize() + 3f));
        bload.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //bloadMouseClicked(e);
            }
        });
        panel1.add(bload);
        bload.setBounds(55, 90, 80, 45);

        //---- bsave ----
        bsave.setText("SAVE");
        bsave.setBackground(new Color(61, 204, 199));
        bsave.setFont(bsave.getFont().deriveFont(bsave.getFont().getSize() + 3f));
        panel1.add(bsave);
        bsave.setBounds(150, 90, 80, 45);
        bsave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //bloadMouseClicked(e);
            }
        });

        //---- lend ----
        //lend.setIcon(new ImageIcon("img/equals.png"));
        //panel1.add(lend);
        //lend.setBounds(new Rectangle(new Point(600, 575), lend.getPreferredSize()));
        EndBlock endblock = new EndBlock("end", lend);
        generateBlock(endblock );

        //---- lstart ----
        //lstart.setIcon(new ImageIcon( "img/minus.png") );
        //panel1.add(lstart);
        //lstart.setBounds(new Rectangle(new Point(600, 40), lstart.getPreferredSize()));
        StartBlock startblock = new StartBlock("start", lstart);
        generateBlock(startblock );

        //---- lhelp ----
        lhelp.setText("HELP");
        lhelp.setBackground(new Color(61, 204, 199));
        lhelp.setOpaque(true);
        lhelp.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lhelp.setForeground(new Color(30, 30, 30));
        lhelp.setHorizontalAlignment(SwingConstants.CENTER);
        lhelp.setBorder(null);
        panel1.add(lhelp);
        lhelp.setBounds(940, 0, 60, 25);

        //---- ldown ----
        ldown.setOpaque(true);
        ldown.setBackground(new Color(51, 51, 51));
        panel1.add(ldown);
        ldown.setBounds(0, 650, 1000, 20);

        //---- lup ----
        lup.setOpaque(true);
        lup.setBackground(new Color(51, 51, 51));
        panel1.add(lup);
        lup.setBounds(0, 0, 1000, 25);

        //---- lleftpan ----
        lleftpan.setOpaque(true);
        lleftpan.setFont(lleftpan.getFont().deriveFont(lleftpan.getFont().getSize() + 4f));
        lleftpan.setBackground(new Color(94, 94, 94));
        panel1.add(lleftpan);
        lleftpan.setBounds(0, 0, 295, 670);


    }

    public void generateBlock(AbstractBlock block){

        block.labelBlock.setIcon(new ImageIcon( block.getPath() ) );
        System.out.println(block.getPath());

        panel1.add(block.labelBlock);
        block.labelBlock.setBounds(block.getXBlock(), block.getYBlock(), block.getWidthBlock(), block.getHeightBlock());
        System.out.println( getComponentZOrder(block.labelBlock) );
    }

    public void setVisible() {
        //---- lback ----
        /*lback.setText("text");
        lback.setOpaque(true);
        lback.setBackground(new Color(252, 250, 249));
        panel1.add(lback);
        lback.setBounds(0, 0, 1000, 670);*/

        Container frame1ContentPane = frame1.getContentPane();
        frame1ContentPane.setLayout(null);

        frame1ContentPane.add(panel1);
        panel1.setBounds(0, 0, 1000, 670);

        Dimension preferredSize = new Dimension();
        for(int i = 0; i < frame1ContentPane.getComponentCount(); i++) {
            Rectangle bounds = frame1ContentPane.getComponent(i).getBounds();
            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
        }
        Insets insets = frame1ContentPane.getInsets();
        preferredSize.width += insets.right;
        preferredSize.height += insets.bottom;
        frame1ContentPane.setMinimumSize(preferredSize);
        frame1ContentPane.setPreferredSize(preferredSize);

        frame1.pack();
        frame1.setLocationRelativeTo(frame1.getOwner());
        frame1.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame1.setVisible(true);
    }
}
