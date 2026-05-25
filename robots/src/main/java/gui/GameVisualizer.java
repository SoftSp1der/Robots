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

public class GameVisualizer extends JPanel implements IViewUpd {
    private final Timer m_timer = initTimer();
    private final GameVisualizerPresenter<GameVisualizer, GameLogic> presenter;

    private volatile int m_robotPositionX;
    private volatile int m_robotPositionY; 
    private volatile double m_robotDirection;

    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;
    
    private static Timer initTimer() {
        Timer timer = new Timer("events generator", true);
        return timer;
    }
    
    public GameVisualizer(GameLogic logic) {
        presenter = new GameVisualizerPresenter<GameVisualizer, GameLogic>(this, logic, m_targetPositionX, m_targetPositionY);
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 50);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                m_targetPositionX = e.getPoint().x;
                m_targetPositionY = e.getPoint().y;
                presenter.updateVals(m_targetPositionX, m_targetPositionY);
                repaint();
            }
        });
        setDoubleBuffered(true);
    }

    @Override
    public void receiveUpd(int x, int y, double rot) {
        m_robotPositionX = x;
        m_robotPositionY = y;
        m_robotDirection = rot;
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
