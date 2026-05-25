package main.java.gui;

import java.util.Observer;
import java.util.Observable;

import main.java.helper.Pair;
import main.java.helper.Triple;

public class GameMonitorPresenter <ViewType extends IViewUpd & IModelUpd,
        ModelType extends Observable & ICauseUpdate> implements Observer {
    private final ViewType view;
    private final ModelType model;

    public GameMonitorPresenter(ViewType v, ModelType m) {
        view = v;
        model = m;

        model.addObserver(this);
        model.updateModel();
        model.updateView();
    }

    @Override
    public void update(Observable ob, Object obj) {
        if (obj instanceof Triple<?, ?, ?>) {
            var triple = (Triple<Integer, Integer, Double>) obj;
            view.receiveUpd(triple.a, triple.b, triple.c);
        } else {
            var pair = (Pair<Integer, Integer>) obj;
            view.receiveUpd(pair.a, pair.b);
        }
    }
}
