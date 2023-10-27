package invaders.engine;

import java.util.List;
import java.util.ArrayList;

import invaders.Observe.Score;
import invaders.entities.EntityViewImpl;
import invaders.entities.SpaceBackground;
import invaders.Observe.Timer;
import invaders.undo.Caretaker;
import invaders.undo.Memento;
import javafx.scene.control.Button;
import javafx.util.Duration;
import javafx.scene.text.Text;


import invaders.entities.EntityView;
import invaders.rendering.Renderable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;


public class GameWindow {
	private final int width;
    private final int height;
	private Scene scene;
    private Pane pane;
    private GameEngine model;
    private List<EntityView> entityViews =  new ArrayList<EntityView>();
    private Renderable background;

    private double xViewportOffset = 0.0;
    private double yViewportOffset = 0.0;
    // private static final double VIEWPORT_MARGIN = 280.0;

    /** **/
    Timer timer;
    Text text=new Text();

    /** Score observer **/
    Score scoreObserver=new Score();
    Label score;

    /** Setting buttons for different difficulty **/
    private final Button hardButton = new Button( "Hard Mode" );
    private final Button easyButton = new Button( "Easy Mode" );
    private final Button mediumButton = new Button( "Medium Mode" );
    private final Button undoButton= new Button("Undo");




	public GameWindow(GameEngine model){
        this.model = model;
		this.width =  model.getGameWidth();
        this.height = model.getGameHeight();
        timer=new Timer(model);

        pane = new Pane();
        scene = new Scene(pane, width, height);
        this.background = new SpaceBackground(model, pane);

        /**TIMER FOR GAME **/
        model.attachObserver(timer);
        text.setX(0);
        text.setY(50);
        text.setFill(Color.WHITE);
        pane.getChildren().add(text);

        /**Attching score to model **/
        model.attachObserver(scoreObserver);
        score=scoreObserver.getLabel();
        score.setTextFill(Color.WHITE);
        score.setLayoutX(0);
        score.setLayoutY(0);
        score.setText(scoreObserver.getLabel().getText());
        pane.getChildren().add(score);

        /**Setting Undo Button action **/
        undoButton.setOnAction(e->{
            //Memento memento=model.getMemento();
            //this.model.setState(memento.getGameObjects(), memento.getPendingToAddGameObject(), memento.getPendingToRemoveGameObject(), memento.getPendingToAddRenderable(), memento.getPendingToRemoveRenderable(), memento.getRenderables(),memento.getPlayer());
            model.setState(model.getMemento().getGameObjects(), model.getMemento().getPendingToAddGameObject(), model.getMemento().getPendingToRemoveGameObject(), model.getMemento().getPendingToAddRenderable(), model.getMemento().getPendingToRemoveRenderable(), model.getMemento().getRenderables(),model.getMemento().getPlayer(), model.getMemento().getObserbvers());
            System.out.println("Undo!!!!");
        });

        /**Attaching buttons to pane**/
        undoButton.setLayoutX(width-295);
        easyButton.setLayoutX(width-247);
        mediumButton.setLayoutX(width-170);
        hardButton.setLayoutX(width-80);
        //previously adding button hindered key board input
        //adding setFocustransversable shifts the focus from these buttons
        easyButton.setFocusTraversable(false);
        hardButton.setFocusTraversable(false);
        mediumButton.setFocusTraversable(false);
        undoButton.setFocusTraversable(false);
        pane.getChildren().addAll(easyButton,mediumButton,hardButton, undoButton);

        KeyboardInputHandler keyboardInputHandler = new KeyboardInputHandler(this.model);

        scene.setOnKeyPressed(keyboardInputHandler::handlePressed);
        scene.setOnKeyReleased(keyboardInputHandler::handleReleased);

    }

	public void run() {
         Timeline timeline = new Timeline(new KeyFrame(Duration.millis(17), t -> this.draw()));

         timeline.setCycleCount(Timeline.INDEFINITE);
         timeline.play();
    }


    private void draw(){
        model.update();


        List<Renderable> renderables = model.getRenderables();
        for (Renderable entity : renderables) {
            boolean notFound = true;
            for (EntityView view : entityViews) {
                if (view.matchesEntity(entity)) {
                    notFound = false;
                    view.update(xViewportOffset, yViewportOffset);
                    break;
                }
            }
            if (notFound) {
                EntityView entityView = new EntityViewImpl(entity);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
            }
        }

        for (Renderable entity : renderables){
            if (!entity.isAlive()){
                for (EntityView entityView : entityViews){
                    if (entityView.matchesEntity(entity)){
                        entityView.markForDelete();
                    }
                }
            }
        }

        for (EntityView entityView : entityViews) {
            if (entityView.isMarkedForDelete()) {
                pane.getChildren().remove(entityView.getNode());
            }
        }

        model.getGameObjects().removeAll(model.getPendingToRemoveGameObject());
        model.getGameObjects().addAll(model.getPendingToAddGameObject());
        model.getRenderables().removeAll(model.getPendingToRemoveRenderable());
        model.getRenderables().addAll(model.getPendingToAddRenderable());

        model.getPendingToAddGameObject().clear();
        model.getPendingToRemoveGameObject().clear();
        model.getPendingToAddRenderable().clear();
        model.getPendingToRemoveRenderable().clear();

        entityViews.removeIf(EntityView::isMarkedForDelete);
        /**  **/
        if(!model.gameOver()){
            text.setText(timer.getTime());
        }
        else{

        }

        score.setText(scoreObserver.getLabel().getText());
    }

	public Scene getScene() {
        return scene;
    }
    public Button getEasyButton(){return easyButton;}
    public Button getHardButton(){return hardButton;}
    public Button getNormalButton(){return mediumButton;}
}
