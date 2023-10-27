package invaders.Observe;

import invaders.factory.EnemyProjectile;
import invaders.factory.PlayerProjectile;
import invaders.factory.Projectile;
import invaders.gameobject.Enemy;
import invaders.rendering.Renderable;
import invaders.strategy.FastProjectileStrategy;
import invaders.strategy.ProjectileStrategy;
import invaders.strategy.SlowProjectileStrategy;
import javafx.scene.control.Label;


public class Score implements Observer{
    int currentScore=0;
    private Label label;

    public Score(){
        this.label=new Label();
        label.setText("Score: "+String.valueOf(currentScore));

    }

    public Label getLabel(){
        return label;
    }

    /**This also acts as score handler besides following Observer pattern **/
    @Override
    public void observe(Renderable renderableA, Renderable renderableB) {
        if(renderableA!=null){
            if(renderableA.getClass()== PlayerProjectile.class){
                //if renderableB is Enemy
                if(renderableB.getClass()== Enemy.class){
                    int strategy=((Enemy) renderableB).getStrategy();
                    if(strategy==0){
                        currentScore+=3;
                    } else if (strategy==1) {
                        currentScore+=4;
                    }
                    else{
                        System.exit(3);
                    }
                }
                //if renderableB is EnemyProjectile
                else if (renderableB.getClass()== EnemyProjectile.class) {
                    ProjectileStrategy strategy= ((EnemyProjectile) renderableB).getStrategy();
                    if(strategy instanceof FastProjectileStrategy){
                        currentScore+=2;
                    } else if (strategy instanceof SlowProjectileStrategy) {
                        currentScore+=1;
                    }
                }
            }

            //if renderableB is projectile
            else if(renderableB.getClass()== PlayerProjectile.class){
                //renderableA is enemy
                if(renderableA.getClass()== Enemy.class){
                    int strategy=((Enemy) renderableA).getStrategy();
                    if(strategy==0){
                        currentScore+=3;
                    } else if (strategy==1) {
                        currentScore+=4;
                    }
                    else{
                        System.exit(3);
                    }
                }
                //if renderableA is EnemyProjectile
                else if (renderableA.getClass()== EnemyProjectile.class) {
                    ProjectileStrategy strategy= ((EnemyProjectile) renderableA).getStrategy();
                    if(strategy instanceof FastProjectileStrategy){
                        currentScore+=2;
                    } else if (strategy instanceof SlowProjectileStrategy) {
                        currentScore+=1;
                    }
                }

            }
        }

        else if( (renderableB instanceof Enemy)){
            if( ((Enemy)renderableB).getStrategy()==0){
                currentScore+=3;
            }
            else if( ((Enemy)renderableB).getStrategy()==1){
                currentScore+=4;
            }
        }
        else if((renderableB instanceof  EnemyProjectile)){
            ProjectileStrategy strategy= ((EnemyProjectile) renderableB).getStrategy();
            if(strategy instanceof FastProjectileStrategy){
                currentScore+=2;
            } else if (strategy instanceof SlowProjectileStrategy) {
                currentScore+=1;
            }
        }

        label.setText("Score: "+String.valueOf(currentScore));
    }


}
