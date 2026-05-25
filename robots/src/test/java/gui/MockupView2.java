package test.java.gui;

import main.java.gui.*;
import main.java.helper.*;

public class MockupView2 implements IModelUpd, IViewUpd {
    private volatile int robotPositionX = 4;
    private volatile int robotPositionY = 4;
    private volatile double robotDirection = 4;

    private volatile int targetPositionX = 4;
    private volatile int targetPositionY = 4;
    private final GameMonitorPresenter<MockupView2, MockupModel> presenter;

    public MockupView2(MockupModel model) {
        presenter = new GameMonitorPresenter<MockupView2, MockupModel>(this, model);
    }

    @Override
    public void receiveUpd(int x, int y, double rot) {
        robotPositionX = x;
        robotPositionY = y;
        robotDirection = rot;
    }

    @Override
    public void receiveUpd(int x, int y) {
        targetPositionX = x;
        targetPositionY = y;
    }

    public Pair<Integer, Integer> getTargetPos() {
        return new Pair<Integer, Integer>(targetPositionX, targetPositionY);
    }
    public Triple<Integer, Integer, Double> getRobotPos() {
        return new Triple<Integer, Integer, Double>(robotPositionX, robotPositionY, robotDirection);
    }
}
