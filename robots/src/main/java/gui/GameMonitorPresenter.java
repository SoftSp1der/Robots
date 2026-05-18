package main.java.gui;

import java.util.Observer;
import java.util.Observable;

import main.java.helper.Pair;
import main.java.helper.Triple;

public class GameMonitorPresenter <ViewType extends Observer,
        ModelType extends Observable & Observer & ICauseUpdate> extends Observable implements Observer {

    public GameMonitorPresenter(ViewType view, ModelType model) {
        model.addObserver(this);
        addObserver(view);
        model.updateModel();
        model.updateView();
    }

    @Override
    public void update(Observable ob, Object obj) {
        if (obj instanceof Triple<?, ?, ?>) {
            setChanged();
            notifyObservers((Triple<Integer, Integer, Double>) obj);
        } else {
            setChanged();
            notifyObservers((Pair<Integer, Integer>) obj);
        }
    }
}
