package invaders.Observe;

import invaders.rendering.Renderable;

public interface Observer {
    public void observe(Renderable renderableA, Renderable renderableB);
}
