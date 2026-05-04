package main.java.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class GameVisualizer extends JPanel implements GameChangeListener {
    private final Timer m_timer = initTimer();

    private volatile int m_robotPositionX;
    private volatile int m_robotPositionY; 
    private volatile double m_robotDirection;

    private volatile int m_targetPositionX;
    private volatile int m_targetPositionY;
    
    private static Timer initTimer() {
        Timer timer = new Timer("events generator", true);
        return timer;
    }
    
    public GameVisualizer(GameLogic logic) {
        logic.subscribe(this);
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 50);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logic.setTargetPosition(e.getPoint());
            }
        });
        setDoubleBuffered(true);
    }

    public void onCoordsChanged(int robotPositionX, int robotPositionY, double robotDirection) {
        m_robotPositionX = robotPositionX;
        m_robotPositionY = robotPositionY;
        m_robotDirection = robotDirection;
    }
    public void onTargetCoordsChanged(int targetPositionX, int targetPositionY) {
        m_targetPositionX = targetPositionX;
        m_targetPositionY = targetPositionY;
        repaint();
    }
    
    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g; 
        drawRobot(g2d, m_robotPositionX, m_robotPositionY, m_robotDirection);
        drawTarget(g2d, m_targetPositionX, m_targetPositionY);
    }
    
    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private void drawRobot(Graphics2D g, int x, int y, double direction) {
        AffineTransform t = AffineTransform.getRotateInstance(direction, m_robotPositionX, m_robotPositionY); 
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, m_robotPositionX, m_robotPositionY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, m_robotPositionX, m_robotPositionY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, m_robotPositionX + 10, m_robotPositionY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, m_robotPositionX + 10, m_robotPositionY, 5, 5);
    }
    
    private void drawTarget(Graphics2D g, int x, int y) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0); 
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }
}
