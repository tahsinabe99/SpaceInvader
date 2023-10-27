package invaders;

import invaders.strategyGame.DifficultyLevel;
import invaders.strategyGame.EasyGame;
import invaders.strategyGame.HardGame;
import invaders.strategyGame.MediumGame;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import invaders.engine.GameEngine;
import invaders.engine.GameWindow;

public class App extends Application {
    private DifficultyLevel difficultyLevel;

    //App constructors are overloaded so we can have one app that starts with easy method by default
    // and another that starts with which ever difficulty strategy we choose
    public App(DifficultyLevel difficultyLevel){
        this.difficultyLevel=difficultyLevel; // started with easy one
    }

    public App(){
        this.difficultyLevel=new EasyGame();
    }

    public void setDifficultyLevel(DifficultyLevel differentLevel){
        this.difficultyLevel=differentLevel;
    }
    public DifficultyLevel getDifficultyLevel(){
        return this.difficultyLevel;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GameEngine model=difficultyLevel.loadGame();
       //GameEngine model = new GameEngine("src/main/resources/config_easy.json");
        GameWindow window = new GameWindow(model);
        //attaching the functions of the buttons to them
        buttonFunction(window.getEasyButton(),window.getNormalButton(), window.getHardButton(),primaryStage);
        window.run();
        primaryStage.setTitle("Space Invaders");
        primaryStage.setScene(window.getScene());
        primaryStage.show();

        window.run();
    }

    //function to restart the app with different game strategy
    public void restartGame(Stage primaryStage, DifficultyLevel gamedifficulty ){
        primaryStage.close();

        Platform.runLater(() ->{
            new App(gamedifficulty).start(new Stage());
        });
    }
    //function to add functionality to buttons
    //takes the button and adds the function to restart the game based on level
    public void buttonFunction(Button easyButton, Button mediumButton, Button hardButton, Stage primaryStage){
        easyButton.setOnAction(e ->restartGame(primaryStage, new EasyGame()));
        mediumButton.setOnAction(e ->restartGame(primaryStage, new MediumGame()));
        hardButton.setOnAction(e-> restartGame(primaryStage, new HardGame()));
    }


}
