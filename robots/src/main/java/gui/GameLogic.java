package main.java.gui;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameLogic {
    private final Timer m_timer = initTimer();
    private final ArrayList<GameChangeListener> m_listeners;
    
    private static Timer initTimer() {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    private volatile double m_robotPositionX = 100;
    private volatile double m_robotPositionY = 100; 
    private volatile double m_robotDirection = 0;
    private volatile double m_velocity = maxAngularVelocity;
    private volatile int m_counter = 0;

    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;
    
    private static final double maxVelocity = 0.1; 
    private static final double maxAngularVelocity = 0.001;

    public GameLogic() {
        m_listeners = new ArrayList<GameChangeListener>();
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onModelUpdateEvent();
            }
        }, 0, 10);
    }

    public void subscribe(GameChangeListener listener) {
        synchronized (m_listeners) {
            m_listeners.add(listener);
        }
    }

    protected void setTargetPosition(Point p) {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
        for (var listener : m_listeners) {
            listener.onTargetCoordsChanged(m_targetPositionX, m_targetPositionY);
        }
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }
    
    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    private static double angle_diff(double angle1, double angle2) {
        double ans = angle1 - angle2;
        if (ans >= Math.PI) {
            ans -= 2 * Math.PI;
        }
        if (ans <= -Math.PI) {
            ans += 2 * Math.PI;
        }
        return ans;
    }
    
    protected void onModelUpdateEvent() {
        double distance = distance(m_targetPositionX, m_targetPositionY, 
            m_robotPositionX, m_robotPositionY);
        if (distance < 0.5) {
            return;
        }
        double velocity = maxVelocity;
        double angleToTarget = angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY);
        double angularVelocity = 0;
        double angle = angle_diff(angleToTarget, m_robotDirection);
        m_counter += 1;
        if (m_counter == 1000) {
            m_velocity = -m_velocity;
            m_counter = 0;
        }
        if (-Math.PI / 3 < angle && angle < Math.PI / 3) {
            if (angle < 0) {
                angularVelocity = -maxAngularVelocity;
            } else {
                angularVelocity = maxAngularVelocity;
            }
        } else {
            angularVelocity = m_velocity;
        }

        moveRobot(velocity, angularVelocity, 10);
        for (var listener : m_listeners) {
            listener.onCoordsChanged(round(m_robotPositionX), round(m_robotPositionY), m_robotDirection);
        }
    }

    public static int round(double value) {
        return (int)(value + 0.5);
    }
    
    private static double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }
    
    private void moveRobot(double velocity, double angularVelocity, double duration) {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = m_robotPositionX + velocity / angularVelocity * 
            (Math.sin(m_robotDirection  + angularVelocity * duration) -
                Math.sin(m_robotDirection));
        if (!Double.isFinite(newX)) {
            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        }
        double newY = m_robotPositionY - velocity / angularVelocity * 
            (Math.cos(m_robotDirection  + angularVelocity * duration) -
                Math.cos(m_robotDirection));
        if (!Double.isFinite(newY)) {
            newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
        }
        m_robotPositionX = newX;
        m_robotPositionY = newY;
        double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration); 
        m_robotDirection = newDirection;
    }

    private static double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }
}
