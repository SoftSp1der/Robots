package test.java.gui;

import org.junit.jupiter.api.Test;

import main.java.gui.GameVisualizerPresenter;
import main.java.helper.Pair;
import main.java.helper.Triple;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    MockupModel model = new MockupModel();
    MockupView1 view1 = new MockupView1(model);
    MockupView2 view2 = new MockupView2(model);

    boolean theyAgree() {
        return model.getTargetPos().equals(view1.getTargetPos()) && model.getTargetPos().equals(view2.getTargetPos()) &&
            model.getRobotPos().equals(view1.getRobotPos()) && model.getRobotPos().equals(view2.getRobotPos());
    }
    boolean theyAgree(int x, int y, double rot, int tx, int ty) {
        return theyAgree() && model.getTargetPos().equals(tx, ty) && model.getRobotPos().equals(x, y, rot);
    }

    @Test
    void presenterTest() {
        assertTrue(theyAgree());
        view1.updateCoords(1, 1);
        assertTrue(theyAgree());
        model.updateCoords(5, 5, 5.0);
        assertTrue(theyAgree());

        view1.updateCoords(6, 6);
        assertTrue(theyAgree(5, 5, 5.0, 6, 6));

        model.updateCoords(7, 7, 7.0);
        assertTrue(theyAgree(7, 7, 7.0, 6, 6));
    }
}
