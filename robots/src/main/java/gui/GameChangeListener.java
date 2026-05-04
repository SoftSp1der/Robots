package main.java.gui;

public interface GameChangeListener {
    public void onCoordsChanged(int m_robotPositionX, int m_robotPositionY, double m_robotDirection);
    public void onTargetCoordsChanged(int m_targetPositionX, int m_targetPositionY);
}
