package main.java.gui;

import java.util.Observer;
import java.util.Observable;

import main.java.helper.Pair;
import main.java.helper.Triple;

public class GameVisualizerPresenter <ViewType extends Observer,
        ModelType extends Observable & Observer & ICauseUpdate> extends Observable implements Observer {

    public GameVisualizerPresenter(ViewType view, ModelType model, int x, int y) {
        model.addObserver(this);
        addObserver(model);
        addObserver(view);
        model.updateView();
        updateVals(x, y);
    }

    @Override
    public void update(Observable ob, Object obj) {
        if (obj instanceof Triple<?, ?, ?>) {
            setChanged();
            notifyObservers((Triple<Integer, Integer, Double>) obj);
        }
    }

    public void updateVals(int x, int y) {
        setChanged();
        notifyObservers(new Pair<Integer, Integer>(x, y));
    }
}
