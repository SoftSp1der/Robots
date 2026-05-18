package test.java.gui;

import main.java.gui.GameLogic;
import main.java.gui.GameMonitorPresenter;
import main.java.helper.*;
import java.util.*;

public class MockupView2 implements Observer {
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
    public void update(Observable ob, Object obj) {
        if (obj instanceof Triple<?, ?, ?>) {
            var triple = (Triple<Integer, Integer, Double>) obj;
            robotPositionX = triple.a;
            robotPositionY = triple.b;
            robotDirection = triple.c;
        } else {
            var pair = (Pair<Integer, Integer>) obj;
            targetPositionX = pair.a;
            targetPositionY = pair.b;
        }
    }

    public Pair<Integer, Integer> getTargetPos() {
        return new Pair<Integer, Integer>(targetPositionX, targetPositionY);
    }
    public Triple<Integer, Integer, Double> getRobotPos() {
        return new Triple<Integer, Integer, Double>(robotPositionX, robotPositionY, robotDirection);
    }
}
