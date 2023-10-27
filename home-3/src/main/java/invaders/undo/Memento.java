package invaders.undo;

import invaders.Observe.Observer;
import invaders.engine.GameEngine;
import invaders.entities.Player;
import invaders.gameobject.GameObject;
import invaders.rendering.Renderable;

import java.util.ArrayList;
import java.util.List;

public class Memento {
    private List<GameObject> gameObjects = new ArrayList<>(); // A list of game objects that gets updated each frame
    private List<GameObject> pendingToAddGameObject = new ArrayList<>();
    private List<GameObject> pendingToRemoveGameObject = new ArrayList<>();
    private List<Renderable> pendingToAddRenderable = new ArrayList<>();
    private List<Renderable> pendingToRemoveRenderable = new ArrayList<>();
    private List<Renderable> renderables =  new ArrayList<>();
    //private List<Observer> observers= new ArrayList<>();
    private Player player;
    private List<Observer> observers;

    String time;

    public Memento(List<GameObject> gameObjects,List<GameObject> pendingToAddGameObject,
                   List<GameObject> pendingToRemoveGameObject, List<Renderable> pendingToAddRenderable,
                   List<Renderable> pendingToRemoveRenderable,
                   List<Renderable>renderables, Player player, List<Observer> observers  ){
        this.gameObjects=gameObjects;
        this.pendingToAddRenderable=pendingToAddRenderable;
        this.pendingToRemoveRenderable=pendingToRemoveRenderable;
        this.pendingToAddGameObject=pendingToAddGameObject;
        this.pendingToRemoveGameObject=pendingToRemoveGameObject;
        this.renderables=renderables;
        this.player=player;
        this.observers=observers;
    }


    public List<GameObject> getGameObjects() {
        return gameObjects;
    }
    public List<GameObject> getPendingToAddGameObject(){
        return pendingToAddGameObject;
    }

    public List<GameObject> getPendingToRemoveGameObject() {
        return pendingToRemoveGameObject;
    }

    public List<Renderable> getPendingToAddRenderable() {
        return pendingToAddRenderable;
    }

    public List<Renderable> getPendingToRemoveRenderable() {
        return pendingToRemoveRenderable;
    }

    public List<Renderable> getRenderables() {
        return renderables;
    }

    public Player getPlayer(){
        return this.player;
    }
    public List<Observer> getObserbvers(){
        return this.observers;
    }


    public void setTime(String time){
        this.time=time;
    }
    public String getTIme(){
        return this.time;
    }

}
