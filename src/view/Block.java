package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;

import static java.lang.Math.abs;

public class Block extends AbstractBlock {
    //GeneralPath path = null;
    private boolean twoPoints = false;
    /*Point previousPoint;
    Graphics g= null;*/

    public Block(String identification, JLabel label) {
        super(identification, label);
        // defaultne se nachazi tam, kde se vygeneroval
        setX(20);
        setY(20);

    }

    /*@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.blue);
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(8,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        if (path!=null) {
            g2d.draw(path);
        }
    }
    public void drawline(Point A, Point B){
        Graphics2D g2d = (Graphics2D)g;
        int width = 8;
        g2d.setStroke(new BasicStroke(width));
        g2d.drawLine(A.x, A.y, B.x, B.y);

    }*/

    @Override
    public void mouseClicked(MouseEvent e) {

        //e.
       /* if ( SwingUtilities.isLeftMouseButton( e)){
            Point actualPoint = e.getPoint();
            if ( twoPoints ){
                drawline(previousPoint, actualPoint);
                twoPoints = false;
            } else {
                previousPoint = actualPoint;
            }

            /*Point p = e.getPoint();
            if ( drawing ){
                path = new GeneralPath();
                path.moveTo(p.x, p.y);
                drawing = true;

            } else {
                path.lineTo(p.x, p.y);
            }
            System.out.println("left click");
            labelBlock.setIcon(new ImageIcon(selectedPath));
            labelBlock.repaint();

        }*/


    }


    @Override
    public void mousePressed(MouseEvent event) {
        System.out.println("Pressed");
        x = event.getX();
        y = event.getY();
        System.out.println(x);
        System.out.println(y);

    }
    @Override
    public void mouseReleased(MouseEvent event) {
        System.out.println("release");
        x = event.getX()+event.getComponent().getX()-x;
        y = event.getY()+event.getComponent().getY()-y;

        System.out.println(x);
        System.out.println(y);
        //updateOutputPort();
        //updateInputPort();
        //System.out.println(inputPortPoints);
        //System.out.println(outputPortPoints);

    }
    @Override
    public void mouseDragged(MouseEvent event) {
        System.out.println("Dragged");
        System.out.println(event.getPoint());
        if ( event.getComponent().getY() <= 0 && event.getComponent().getX() <= 0 ){
            event.getComponent().setLocation(abs(event.getX()+event.getComponent().getX()-x),abs(event.getY()+event.getComponent().getY()-y) );
        } else if ( event.getComponent().getX() > 660 ) {
            event.getComponent().setLocation(660,   event.getY()+event.getComponent().getY()-y);
        } else if ( event.getComponent().getY() > 550 ) {
            event.getComponent().setLocation(event.getX()+event.getComponent().getX()-x,   550);
        } else {
            event.getComponent().setLocation(abs(event.getX()+event.getComponent().getX()-x),abs(event.getY()+event.getComponent().getY()-y) );

        }
    }


}
