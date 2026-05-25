package main.java.gui;

import java.util.Observer;
import java.util.Observable;

import main.java.helper.Triple;

public class GameVisualizerPresenter <ViewType extends IViewUpd,
        ModelType extends Observable & IModelUpd & ICauseUpdate> implements Observer {
    private final ViewType view;
    private final ModelType model;

    public GameVisualizerPresenter(ViewType v, ModelType m, int x, int y) {
        view = v;
        model = m;

        model.addObserver(this);
        model.updateView();
        updateVals(x, y);
    }

    @Override
    public void update(Observable ob, Object obj) {
        if (obj instanceof Triple<?, ?, ?>) {
            var triple = (Triple<Integer, Integer, Double>) obj;
            view.receiveUpd(triple.a, triple.b, triple.c);
        }
    }

    public void updateVals(int x, int y) {
        model.receiveUpd(x, y);
    }
}
