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

import model.Port;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashBoard extends JPanel {
    public JFrame frame1 = new JFrame("Block editor");
    public JPanel panel1 = new JPanel();
    public JPanel panel2 ;

    JPanel panel3 = new JPanel();
    public JPanel panel4 = new JPanel();

    public JButton lhelp = new JButton();
    JLabel lwelcome = new JLabel();
    JLabel laddion = new JLabel();
    JLabel linvert = new JLabel();
    JLabel lmulti = new JLabel();
    JLabel ldivision = new JLabel();
    JLabel lrand = new JLabel();
    JLabel lchoose = new JLabel();
    public JButton bcount = new JButton();
    public JButton bload = new JButton();
    public JButton bsave = new JButton();
    public JButton bnew = new JButton();
    public JButton bdebug = new JButton();
    JLabel lend = new JLabel();
    JLabel lstart = new JLabel();
    JLabel ldown = new JLabel();
    JLabel lup = new JLabel();
    JLabel lleftpan = new JLabel();
    JLabel lback = new JLabel();
    public ImageIcon iconNo = new ImageIcon("img/if_close_13493.png");
    public ImageIcon iconYes = new ImageIcon("img/if_check_13491.png");
    public ImageIcon iconDef = new ImageIcon("img/default_check.png");

    // INPUT FORM - MIG LAYOUT
    public JTextField[] textFieldNameGUI = new JTextField[3];
    public JTextField[] textFieldValueGUI = new JTextField[3];
    public JLabel[] labelNameGUI = new JLabel[3];
    public JLabel[] labelValueGUI = new JLabel[3];
    public JLabel[] labelIconsGUI = new JLabel[3];

    JLabel lname = new JLabel();
    public JTextField tname = new JTextField();
    JLabel lval = new JLabel();
    public JTextField tval = new JTextField();
    JLabel label3 = new JLabel();
    public JTextField textField2 = new JTextField();
    JLabel label6 = new JLabel();
    public JTextField textField5 = new JTextField();
    JLabel label4 = new JLabel();
    public JTextField textField3 = new JTextField();
    JLabel label7 = new JLabel();
    public JTextField textField6 = new JTextField();
    public JButton bsavePorts = new JButton();
    public JLabel lstate = new JLabel();

    // block on the left, maximum now is 10
    List <JLabel> labelblock = new ArrayList<>();
    //all connections
    List <Connection> connectionsOnDashboard = new ArrayList<>();
    /**
     * Method displays a dashboard, needs to know which blocks should be displayed
     */
    public DashBoard(){
       // Container frame1ContentPane = frame1.getContentPane();
        //frame1ContentPane.setLayout(null);
        frame1.setResizable(false);
        panel2 = new JPanel(){
            @Override
            public void paintComponent(Graphics g)
            {
                // draw all connections
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = 4;
                g2d.setStroke(new BasicStroke(width));

                g2d.setColor(new Color(61,204,199));
                for (Connection i: connectionsOnDashboard
                     ) {
                    g2d.drawLine(i.cooFrom.x,i.cooFrom.y, i.cooTo.x, i.cooTo.y);
                }
            }
        };
        //inputPanel();
        panel4.setBackground(new Color(94, 94, 94));
        panel4.setLayout(new MigLayout(
                "fill,hidemode 3",
                // columns
                "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]",
                // rows
                "[]" +
                        "[]" +
                        "[]" +
                        "[]"));
        panel1.add(panel4);

        int row = 0;
        int col = 0;
        for (Integer i = 0; i < 3; i++){
            textFieldNameGUI[i] = new JTextField();
            textFieldValueGUI[i] = new JTextField();
            labelNameGUI[i] = new JLabel();
            labelValueGUI[i] = new JLabel();
            labelIconsGUI[i] = new JLabel();

            labelNameGUI[i].setText("Name:");
           // panel4.add(labelNameGUI[i], "cell " + row + " " + col + " 2 1");
            panel4.add(labelNameGUI[i], "cell " + row + " " + col );
            panel4.add(textFieldNameGUI[i], "cell 1 " + i + " 2 1");

            labelValueGUI[i].setText("Value:");
            panel4.add(labelValueGUI[i], "cell 3 " + i + " 2 1");
            panel4.add(textFieldValueGUI[i], "cell 5 " + i + " 4 1");

            //label5.setDisabledIcon(new ImageIcon("D:\\Dokumenty_D\\FIT_VUT\\2017-2018\\letni_semestr\\IJA\\projekt\\img\\if_close_13493.png"));
            //label5.setOpaque(true);
            //label5.setEnabled(false);
            labelIconsGUI[i].setBackground(new Color(94, 94, 94));
            //labelIconsGUI[i].setEnabled(false);
            panel4.add(labelIconsGUI[i], "cell 9 " + i);

            col += 1;

        }
        //---- bsavePorts ----
        bsavePorts.setText("OK");
        bsavePorts.setBackground(new Color(243, 211, 189));

        panel4.add(bsavePorts, "cell 0 3 2 1");

        lstate.setText("");
        panel4.add(lstate, "cell 6 3 4 1");

        panel1.add(panel3);

        panel2.setOpaque(true);
        panel2.setBackground(new Color(238, 238, 238));
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

        //---- lchoose ----
        lchoose.setText("Choose a block:");
        lchoose.setForeground(new Color(247, 247, 247));
        lchoose.setFont(lchoose.getFont().deriveFont(lchoose.getFont().getSize() + 3f));
        panel1.add(lchoose);
        lchoose.setBounds(35, 155, 240, 30);

        //---- bcount ----
        bcount.setText("SOLVE");
        bcount.setForeground(new Color(247, 247, 247));
        bcount.setBackground(new Color(61, 204, 199));
        bcount.setFont(bcount.getFont().deriveFont(bcount.getFont().getSize() + 5f));
        panel1.add(bcount);
        bcount.setBounds(25, 575, 230, 60);

        //---- bnew ----
        bnew.setText("NEW");
        bnew.setBackground(new Color(61, 204, 199));
        //bnew.setFont(bnew.getFont().deriveFont(bnew.getFont().getSize() + 1f));
        panel1.add(bnew);
        bnew.setBounds(15, 90, 63, 40);

        //---- bload ----
        bload.setText("OPEN");
        bload.setBackground(new Color(61, 204, 199));
        bload.setFont(bload.getFont().deriveFont(bload.getFont().getSize() - 1f));
        bload.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //bloadMouseClicked(e);
            }
        });
        panel1.add(bload);
        bload.setBounds(82, 90, 63, 40);

        //---- bsave ----
        bsave.setText("SAVE");
        bsave.setBackground(new Color(61, 204, 199));
        bsave.setFont(bsave.getFont().deriveFont(bsave.getFont().getSize() - 1f));
        panel1.add(bsave);
        bsave.setBounds(149, 90, 63, 40);


        //---- bdebug ----
        bdebug.setText("DEBUG");
        bdebug.setBackground(new Color(61, 204, 199));
        bdebug.setFont(bdebug.getFont().deriveFont(bdebug.getFont().getSize() - 4f));
        panel1.add(bdebug);
        bdebug.setBounds(216, 90, 63, 40);



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
        ldown.setBounds(0, 650, 1000, 25);

        //---- lup ----
        lup.setOpaque(true);
        lup.setBackground(new Color(51, 51, 51));
        panel1.add(lup);
        lup.setBounds(0, 0, 1000, 25);

    }
    public void getBlocksOnLeft(List<model.Block> factoreblock){
        // default height and width of block
        Integer width = 65;
        Integer height = 70;

        // position of first block
        Integer X = 30;
        Integer Y = 200;

        Integer i = 0; // counter for block placed in left panel
        int counter = 0; // counter global from 0 ..
        for ( model.Block block : factoreblock ){
            if ( i % 3 == 0 && i != 0){
                Y += 85;
                X = 30;
            }
            //JLabel label = new JLabel();
            labelblock.add( new JLabel() );
            //System.out.println(block.getName());

            labelblock.get(counter).setIcon(block.getIcon());
            labelblock.get(counter).setText( block.getName());
            panel1.add(labelblock.get(counter));

            Integer finalI = counter;
            labelblock.get(counter).addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    labelblock.get(finalI).setToolTipText( block.getName());
                }
            });

            if ( block.static_block){
                //labelblock.get(counter).setBounds(block.static_x, block.static_y, width, height);
            } else{
                labelblock.get(counter).setBounds(X, Y, width, height);

                X += 85;
                i++;

            }
            counter++;


        }

        //---- lleftpan ----
        lleftpan.setOpaque(true);
        lleftpan.setFont(lleftpan.getFont().deriveFont(lleftpan.getFont().getSize() + 4f));
        lleftpan.setBackground(new Color(94, 94, 94));
        panel1.add(lleftpan);
        lleftpan.setBounds(0, 0, 295, 670);

    }
    /**
     * Method actualizes all connection
     * all connections are found in dashboard.getConnectionsOnDashboard()
     */
    public void actualizeAllConnection (){
        for ( Connection i: getConnectionsOnDashboard()
                ) {
            i.actualize();
        }
    }

    public List<JLabel> getBlocksOnPanel(){
        return this.labelblock;
    }

    public List<Connection> getConnectionsOnDashboard() {
        return connectionsOnDashboard;
    }

    public void addConnectionsOnDashboard(Connection conn) {
        this.connectionsOnDashboard.add(conn);
        panel2.repaint();
    }
    public void removeConnectionOnDashboard(Connection conn){
        Integer index = this.connectionsOnDashboard.indexOf(conn);
        if ( index == -1 ){
            // unexisting connection
            System.err.println("You want to delete unexisting connection");
        } else {
            connectionsOnDashboard.remove(conn);
            System.out.println("Connection was deleted\n");
        }
    }

    public void setVisible() {

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
