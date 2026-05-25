package main.java.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameMonitorWindow extends JInternalFrame implements IViewUpd, IModelUpd {
    private TextArea m_logContent;
    private final GameMonitorPresenter presenter;

    private volatile int m_robotPositionX;
    private volatile int m_robotPositionY; 
    private volatile int m_robotDirection;
    private volatile int m_targetPositionX;
    private volatile int m_targetPositionY;
    
    public GameMonitorWindow(GameLogic logic) {
        super("Статистика игрового поля", true, true, true, true);
        m_logContent = new TextArea("");
        m_logContent.setSize(300, 500);
        presenter = new GameMonitorPresenter<GameMonitorWindow, GameLogic>(this, logic);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        setName("gameMonitor");
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

    @Override
    public void receiveUpd(int x, int y, double rot) {
        m_robotPositionX = x;
        m_robotPositionY = y;
        m_robotDirection = GameLogic.round(rot * 180 / Math.PI);
        EventQueue.invokeLater(this::update_text);
    }

    @Override
    public void receiveUpd(int x, int y) {
        m_targetPositionX = x;
        m_targetPositionY = y;
        EventQueue.invokeLater(this::update_text);
    }
}
