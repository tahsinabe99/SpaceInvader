package invaders.Observe;

import invaders.engine.GameEngine;
import invaders.rendering.Renderable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Timer implements Observer{
    private String time;
    private GameEngine model;
    private int min;
    private int sec;
    private Timeline timeLine;


    public Timer(GameEngine model){
        this.model=model;
        this.min=0;
        this.sec=0;
        this.setTime();
        this.start();
    }

    public void start(){
        KeyFrame keyframe= new KeyFrame(Duration.millis(1000), e->{
            sec++;
            setTime();
        });
        timeLine= new Timeline(keyframe);
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    public void setTime(){

        //when seconds turn 60 we turn it to a minute
        if(sec==60){
            sec=0;
            min=min+1;
        }
        String minutes;
        String seconds;
        if(min>=10){
            minutes=String.valueOf(min);
        }
        else{
            minutes="0"+String.valueOf(min);
        }

        if(sec>=10){
            seconds=String.valueOf(sec);
        }
        else{
            seconds="0"+String.valueOf(sec);
        }

        time=minutes+":"+seconds;

    }

    public String getTime(){
        return time;
    }

    @Override
    public void observe(Renderable renderableA, Renderable renderableB) {
        if(timeLine!=null && model.gameOver()){
            timeLine.stop();
        }

    }
}
