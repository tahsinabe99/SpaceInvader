package invaders.engine;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class KeyboardInputHandler {
    private final GameEngine model;
    private boolean left = false;
    private boolean right = false;
    private boolean slowAlien=false;
    private boolean fastAlien=false;
    private boolean slowProjectile=false;
    private boolean fastProjectile=false;
    private Set<KeyCode> pressedKeys = new HashSet<>();

    private Map<String, MediaPlayer> sounds = new HashMap<>();

    KeyboardInputHandler(GameEngine model) {
        this.model = model;

        // TODO (longGoneUser): Is there a better place for this code?
        URL mediaUrl = getClass().getResource("/shoot.wav");
        String jumpURL = mediaUrl.toExternalForm();

        Media sound = new Media(jumpURL);
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        sounds.put("shoot", mediaPlayer);
    }

    void handlePressed(KeyEvent keyEvent) {
        if (pressedKeys.contains(keyEvent.getCode())) {
            return;
        }
        pressedKeys.add(keyEvent.getCode());

        if (keyEvent.getCode().equals(KeyCode.SPACE)) {
            if (model.shootPressed()) {
                MediaPlayer shoot = sounds.get("shoot");
                shoot.stop();
                shoot.play();
            }
        }

        if (keyEvent.getCode().equals(KeyCode.LEFT)) {
            left = true;
        }
        if (keyEvent.getCode().equals(KeyCode.RIGHT)) {
            right = true;
        }
        if(keyEvent.getCode().equals(KeyCode.A)){
            slowAlien=true;
        }
        else if(keyEvent.getCode().equals(KeyCode.S)){
            fastAlien=true;
        }
        else if(keyEvent.getCode().equals(KeyCode.D)){slowProjectile=true;}
        else if(keyEvent.getCode().equals(KeyCode.F)){fastProjectile=true;}


        if (left) {
            model.leftPressed();
        }

        if(right){
            model.rightPressed();
        }

        if(slowAlien){
            model.slowAlien();
        }
        if(fastAlien){
            model.fastAlien();
        }
        if(slowProjectile){
            model.slowProjectile();
        }
        if(fastProjectile){
            model.fastProjectile();
        }
    }

    void handleReleased(KeyEvent keyEvent) {
        //ensuring  we can only remove fast and slow enemy only once
        if(keyEvent.getCode()==KeyCode.A || keyEvent.getCode()==KeyCode.S ){

        }
        else {pressedKeys.remove(keyEvent.getCode());}


        if (keyEvent.getCode().equals(KeyCode.LEFT)) {
            left = false;
            model.leftReleased();
        }
        if (keyEvent.getCode().equals(KeyCode.RIGHT)) {
            model.rightReleased();
            right = false;
        }
        if (keyEvent.getCode().equals(KeyCode.A)){
            slowAlien=false;
        }
        if (keyEvent.getCode().equals(KeyCode.S)){
            fastAlien=false;
        }
        if (keyEvent.getCode().equals(KeyCode.D)){
            slowProjectile=false;
        }
        if (keyEvent.getCode().equals(KeyCode.F)){
            fastProjectile=false;
        }
    }
}
