package test.java.gui;

import main.java.gui.GameVisualizerPresenter;
import main.java.helper.*;
import java.util.*;

public class MockupView1 implements Observer {
    private volatile int robotPositionX = 3;
    private volatile int robotPositionY = 3;
    private volatile double robotDirection = 3;

    private volatile int targetPositionX = 3;
    private volatile int targetPositionY = 3;
    private final GameVisualizerPresenter<MockupView1, MockupModel> presenter;

    public MockupView1(MockupModel model) {
        presenter = new GameVisualizerPresenter<MockupView1, MockupModel>(this, model, targetPositionX, targetPositionY);
    }

    public void updateCoords(int x, int y) {
        targetPositionX = x;
        targetPositionY = y;
        presenter.updateVals(x, y);
    }

    @Override
    public void update(Observable ob, Object obj) {
        if (obj instanceof Triple<?, ?, ?>) {
            var triple = (Triple<Integer, Integer, Double>) obj;
            robotPositionX = triple.a;
            robotPositionY = triple.b;
            robotDirection = triple.c;
        }
    }

    public Pair<Integer, Integer> getTargetPos() {
        return new Pair<Integer, Integer>(targetPositionX, targetPositionY);
    }
    public Triple<Integer, Integer, Double> getRobotPos() {
        return new Triple<Integer, Integer, Double>(robotPositionX, robotPositionY, robotDirection);
    }
}
