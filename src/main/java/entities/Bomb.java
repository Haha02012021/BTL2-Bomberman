package entities;

import app.BombermanGame;
import graphics.Sprite;
import javafx.animation.Animation;
import javafx.animation.Interpolatable;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

public class Bomb extends Entity {

    public Bomb(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update(Scene scene) {
        
    }
}