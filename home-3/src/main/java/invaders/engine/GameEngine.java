package invaders.engine;

import java.util.ArrayList;
import java.util.List;

import invaders.ConfigReader;
import invaders.Observe.Observer;
import invaders.builder.BunkerBuilder;
import invaders.builder.Director;
import invaders.builder.EnemyBuilder;
import invaders.factory.EnemyProjectile;
import invaders.factory.Projectile;
import invaders.gameobject.Bunker;
import invaders.gameobject.Enemy;
import invaders.gameobject.GameObject;
import invaders.entities.Player;
import invaders.rendering.Renderable;
import invaders.strategy.FastProjectileStrategy;
import invaders.strategy.ProjectileStrategy;
import invaders.strategy.SlowProjectileStrategy;
import invaders.undo.Caretaker;
import invaders.undo.Memento;
import org.json.simple.JSONObject;

/**
 * This class manages the main loop and logic of the game
 */
public class GameEngine implements  ObserveableSubject {
	private List<GameObject> gameObjects = new ArrayList<>(); // A list of game objects that gets updated each frame
	private List<GameObject> pendingToAddGameObject = new ArrayList<>();
	private List<GameObject> pendingToRemoveGameObject = new ArrayList<>();

	private List<Renderable> pendingToAddRenderable = new ArrayList<>();
	private List<Renderable> pendingToRemoveRenderable = new ArrayList<>();

	private List<Renderable> renderables =  new ArrayList<>();
	private List<Observer> observers= new ArrayList<>(); //for implementation of observer pattern

	private Player player;

	private boolean left;
	private boolean right;
	private int gameWidth;
	private int gameHeight;
	private int timer = 45;

	private Caretaker caretaker=new Caretaker();

	//private Memento memento;

	public GameEngine(String config){
		// Read the config here
		ConfigReader.parse(config);

		// Get game width and height
		gameWidth = ((Long)((JSONObject) ConfigReader.getGameInfo().get("size")).get("x")).intValue();
		gameHeight = ((Long)((JSONObject) ConfigReader.getGameInfo().get("size")).get("y")).intValue();

		//Get player info
		this.player = new Player(ConfigReader.getPlayerInfo());
		renderables.add(player);


		Director director = new Director();
		BunkerBuilder bunkerBuilder = new BunkerBuilder();
		//Get Bunkers info
		for(Object eachBunkerInfo:ConfigReader.getBunkersInfo()){
			Bunker bunker = director.constructBunker(bunkerBuilder, (JSONObject) eachBunkerInfo);
			gameObjects.add(bunker);
			renderables.add(bunker);
		}


		EnemyBuilder enemyBuilder = new EnemyBuilder();
		//Get Enemy info
		for(Object eachEnemyInfo:ConfigReader.getEnemiesInfo()){
			Enemy enemy = director.constructEnemy(this,enemyBuilder,(JSONObject)eachEnemyInfo);
			gameObjects.add(enemy);
			renderables.add(enemy);
		}

	}

	/**
	 * Updates the game/simulation
	 */
	public void update(){
//		if(this.gameOver()){
//			System.out.println(this.gameOver());
//		}

		timer+=1;

		movePlayer();

		for(GameObject go: gameObjects){
			go.update(this);
		}

		for (int i = 0; i < renderables.size(); i++) {
			Renderable renderableA = renderables.get(i);
			for (int j = i+1; j < renderables.size(); j++) {
				Renderable renderableB = renderables.get(j);

				if((renderableA.getRenderableObjectName().equals("Enemy") && renderableB.getRenderableObjectName().equals("EnemyProjectile"))
						||
						(renderableA.getRenderableObjectName().equals("EnemyProjectile") && renderableB.getRenderableObjectName().equals("Enemy"))
						||
						(renderableA.getRenderableObjectName().equals("EnemyProjectile") && renderableB.getRenderableObjectName().equals("EnemyProjectile"))){
				}else{
					if(renderableA.isColliding(renderableB) && (renderableA.getHealth()>0 && renderableB.getHealth()>0)) {
						renderableA.takeDamage(1);
						renderableB.takeDamage(1);
						notifyObserver(renderableA, renderableB); //so we can notify observer score has changed and needs to be handled
					}
				}
			}
		}


		// ensure that renderable foreground objects don't go off-screen
		int offset = 1;
		for(Renderable ro: renderables){
			if(!ro.getLayer().equals(Renderable.Layer.FOREGROUND)){
				continue;
			}
			if(ro.getPosition().getX() + ro.getWidth() >= gameWidth) {
				ro.getPosition().setX((gameWidth - offset) -ro.getWidth());
			}

			if(ro.getPosition().getX() <= 0) {
				ro.getPosition().setX(offset);
			}

			if(ro.getPosition().getY() + ro.getHeight() >= gameHeight) {
				ro.getPosition().setY((gameHeight - offset) -ro.getHeight());
			}

			if(ro.getPosition().getY() <= 0) {
				ro.getPosition().setY(offset);
			}
		}

	}

	public List<Renderable> getRenderables(){
		return renderables;
	}

	public List<GameObject> getGameObjects() {
		return gameObjects;
	}
	public List<GameObject> getPendingToAddGameObject() {
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


	public void leftReleased() {
		this.left = false;
	}

	public void rightReleased(){
		this.right = false;
	}

	public void leftPressed() {
		this.left = true;
	}
	public void rightPressed(){
		this.right = true;
	}

	public boolean shootPressed(){
		if(timer>45 && player.isAlive()){
			Projectile projectile = player.shoot();
			gameObjects.add(projectile);
			renderables.add(projectile);
			timer=0;
			caretaker.updateMemento(this.save());
			return true;

		}
		return false;
	}

	//cheat code to remove slow Aliens when cheat button is pressed
	public void slowAlien(){
		if(player.isAlive()){
			for(int i=0; i<renderables.size(); i++){
				if((renderables.get(i) instanceof Enemy) && ( ((Enemy)renderables.get(i)).getStrategy()==0) ){
					renderables.get(i).takeDamage(1);
					notifyObserver(null, renderables.get(i));

				}

			}
		}

	}

	//cheat code code to remove fast Aliens
	public void fastAlien(){
		if(player.isAlive()){
			for(int i=0; i<renderables.size(); i++){
				if((renderables.get(i) instanceof Enemy) && ( ( (Enemy)renderables.get(i)).getStrategy()==1) ){
					renderables.get(i).takeDamage(1);
					notifyObserver(null, renderables.get(i));
				}
			}

		}


	}

	//cheat code to remove slowProjectile
	public void slowProjectile(){
		if(player.isAlive()){
			for(int i=0; i<renderables.size(); i++){
				if((renderables.get(i) instanceof EnemyProjectile)){
					ProjectileStrategy strategy= ((EnemyProjectile) renderables.get(i)).getStrategy();
					if(strategy instanceof SlowProjectileStrategy && renderables.get(i).isAlive()){
						renderables.get(i).takeDamage(1);
						notifyObserver(null, renderables.get(i));

					}

				}
			}
		}
	}

	//cheat code to remove fast projectiles
	public void fastProjectile(){
		if(player.isAlive()){
			for(int i=0; i<renderables.size(); i++){
				if((renderables.get(i) instanceof EnemyProjectile)){
					ProjectileStrategy strategy= ((EnemyProjectile) renderables.get(i)).getStrategy();
					if(strategy instanceof FastProjectileStrategy && renderables.get(i).isAlive()){
						renderables.get(i).takeDamage(1);
						notifyObserver(null, renderables.get(i));
						System.out.println("Fat Proj "+renderables.get(i).isAlive());

					}
				}
			}
		}
	}

	private void movePlayer(){
		if(left){
			player.left();
		}

		if(right){
			player.right();
		}
	}

	public int getGameWidth() {
		return gameWidth;
	}

	public int getGameHeight() {
		return gameHeight;
	}

	public Player getPlayer() {
		return player;
	}

	//for attaching observers to the engine
	@Override
	public void attachObserver(Observer o) {
		if (o!=null){
			observers.add(o);
		}


	}

	@Override
	public void notifyObserver(Renderable renderableA, Renderable renderableB) {

		if(observers!=null){
			for(Observer o: observers){
				o.observe(renderableA,renderableB);
			}
		}

	}

	@Override
	public void detachObserver(Observer o) {
		if (o!=null){
			observers.remove(o);
		}

	}
	//check if game is over
	public boolean gameOver(){
		if(!player.isAlive()){
			return true;
		}
		int countEnemyAlive=0;

		for(GameObject object: gameObjects){
			if(object instanceof Enemy){
				boolean enemyisAlive=((Enemy) object).isAlive();
				if(enemyisAlive){
					countEnemyAlive++;
				}
			}
		}
		if(countEnemyAlive==0){
			return true;
		}
		return false;
	}

	public Memento save(){
		return (new Memento(this.gameObjects,
				this.pendingToAddGameObject,
				this.pendingToRemoveGameObject,
				this.pendingToAddRenderable,
				this.pendingToRemoveRenderable,
				this.renderables,
				this.player,
				this.observers));
	}

	public void setState(List<GameObject> gameObjects,List<GameObject> pendingToAddGameObject,
						 List<GameObject> pendingToRemoveGameObject, List<Renderable> pendingToAddRenderable,
						 List<Renderable> pendingToRemoveRenderable,
						 List<Renderable>renderables, Player player, List<Observer> observers)
	{
		this.gameObjects=gameObjects;
		this.pendingToAddRenderable=pendingToAddRenderable;
		this.pendingToRemoveRenderable=pendingToRemoveRenderable;
		this.pendingToAddGameObject=pendingToAddGameObject;
		this.pendingToRemoveGameObject=pendingToRemoveGameObject;
		this.renderables=renderables;
		this.player=player;
		this.observers=observers;
	}

	public Memento  getMemento(){
		return caretaker.getMemento();
	}
}
