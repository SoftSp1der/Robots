package main.java.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import main.java.helper.Pair;
import main.java.helper.Triple;

import java.util.Observable;
import java.util.Observer;

public class GameMonitorWindow extends JInternalFrame implements Observer {
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
    public void update(Observable ob, Object obj) {
        if (obj instanceof Triple<?, ?, ?>) {
            var triple = (Triple<Integer, Integer, Double>) obj;
            m_robotPositionX = triple.a;
            m_robotPositionY = triple.b;
            m_robotDirection = GameLogic.round(triple.c * 180 / Math.PI);
        } else {
            var pair = (Pair<Integer, Integer>) obj;
            m_targetPositionX = pair.a;
            m_targetPositionY = pair.b;
        }
        EventQueue.invokeLater(this::update_text);
    }
}
