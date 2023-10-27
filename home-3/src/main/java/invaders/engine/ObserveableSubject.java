package invaders.engine;

import invaders.Observe.Observer;
import invaders.rendering.Renderable;

public interface ObserveableSubject {
    public void attachObserver(Observer o);
    public void notifyObserver(Renderable renderableA, Renderable renderableB);
    public void detachObserver(Observer o);
}
