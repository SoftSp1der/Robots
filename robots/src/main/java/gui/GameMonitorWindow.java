package main.java.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameMonitorWindow extends JInternalFrame implements GameChangeListener {
    private TextArea m_logContent;

    private volatile int m_robotPositionX;
    private volatile int m_robotPositionY; 
    private volatile int m_robotDirection;
    private volatile int m_targetPositionX;
    private volatile int m_targetPositionY;
    
    public GameMonitorWindow(GameLogic logic) {
        super("Статистика игрового поля", true, true, true, true);
        m_logContent = new TextArea("");
        m_logContent.setSize(300, 500);
        logic.subscribe(this);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        update_text();
    }

    private void update_text() {
        StringBuilder content = new StringBuilder();
        content.append("Позиция робота: (" + String.valueOf(m_robotPositionX) + ", " + String.valueOf(m_robotPositionY) + ")\n");
        content.append("Направление движение робота: " + String.valueOf(m_robotDirection) + "°\n");
        content.append("Позиция цели: (" + String.valueOf(m_targetPositionX) + ", " + String.valueOf(m_targetPositionY) + ")\n");
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }

    public void onCoordsChanged(int robotPositionX, int robotPositionY, double robotDirection) {
        m_robotPositionX = robotPositionX;
        m_robotPositionY = robotPositionY;
        m_robotDirection = GameLogic.round(robotDirection * 180 / Math.PI);
        EventQueue.invokeLater(this::update_text);
    }
    public void onTargetCoordsChanged(int targetPositionX, int targetPositionY) {
        m_targetPositionX = targetPositionX;
        m_targetPositionY = targetPositionY;
        EventQueue.invokeLater(this::update_text);
    }
}
