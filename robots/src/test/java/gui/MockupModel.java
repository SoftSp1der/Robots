package test.java.gui;

import main.java.gui.*;
import main.java.helper.*;
import java.util.*;

public class MockupModel extends Observable implements IModelUpd, ICauseUpdate {
    private volatile int robotPositionX = 2;
    private volatile int robotPositionY = 2;
    private volatile double robotDirection = 2;

    private volatile int targetPositionX = 2;
    private volatile int targetPositionY = 2;

    public MockupModel() {
    }

    public void updateCoords(int x, int y, double rot) {
        robotPositionX = x;
        robotPositionY = y;
        robotDirection = rot;
        updateView();
    }

    public void updateView() {
        setChanged();
        notifyObservers(new Triple<Integer, Integer, Double>(robotPositionX, robotPositionY, robotDirection));
    }
    public void updateModel() {
        setChanged();
        notifyObservers(new Pair<Integer, Integer>(targetPositionX, targetPositionY));
    }

    @Override
    public void receiveUpd(int x, int y) {
        targetPositionX = x;
        targetPositionY = y;
        updateModel();
    }

    public Pair<Integer, Integer> getTargetPos() {
        return new Pair<Integer, Integer>(targetPositionX, targetPositionY);
    }
    public Triple<Integer, Integer, Double> getRobotPos() {
        return new Triple<Integer, Integer, Double>(robotPositionX, robotPositionY, robotDirection);
    }
}
