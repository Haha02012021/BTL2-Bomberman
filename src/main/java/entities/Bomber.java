package entities;

import graphics.Sprite;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.LoadException;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Bomber extends Entity {
    private static final int SPEED = 5;

    public Bomber(int x, int y, Image img) {
        super( x, y, img);
    }

    @Override
    public void update(Scene scene) {
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.RIGHT) {
                
                Timeline t = new Timeline();
                t.setCycleCount(1);

                t.getKeyFrames().add(new KeyFrame(
                    Duration.millis(400), 
                    (ActionEvent event) -> {
                        this.setImg(Sprite.player_right.getFxImage());
                        this.setX(this.getX() + SPEED);
                    }
                ));

                t.getKeyFrames().add(new KeyFrame(
                    Duration.millis(800), 
                    (ActionEvent event) -> {
                        this.setImg(Sprite.player_right_1.getFxImage());
                        this.setX(this.getX() + SPEED);
                    }
                ));

                t.getKeyFrames().add(new KeyFrame(
                    Duration.millis(1200), 
                    (ActionEvent event) -> {
                        this.setImg(Sprite.player_right_2.getFxImage());
                        this.setX(this.getX() + SPEED);
                    }
                ));

                t.play();
            } else if (e.getCode() == KeyCode.LEFT) {
                
                Timeline t = new Timeline();
                t.setCycleCount(1);

                t.getKeyFrames().add(new KeyFrame(
                    Duration.millis(400), 
                    (ActionEvent event) -> {
                        this.setImg(Sprite.player_left.getFxImage());
                        this.setX(this.getX() - SPEED);
                    }
                ));

                t.getKeyFrames().add(new KeyFrame(
                    Duration.millis(800), 
                    (ActionEvent event) -> {
                        this.setImg(Sprite.player_left_1.getFxImage());
                        this.setX(this.getX() - SPEED);
                    }
                ));

                t.getKeyFrames().add(new KeyFrame(
                    Duration.millis(1200), 
                    (ActionEvent event) -> {
                        this.setImg(Sprite.player_left_2.getFxImage());
                        this.setX(this.getX() - SPEED);
                    }
                ));

                t.play();
            } else if (e.getCode() == KeyCode.DOWN) {
                
                Timeline t = new Timeline();
                t.setCycleCount(1);

                t.getKeyFrames().add(new KeyFrame(
                    Duration.millis(400), 
                    (ActionEvent event) -> {
                        this.setImg(Sprite.player_down.getFxImage());
                        this.setY(this.getY() + SPEED);
                    }
                ));

                t.getKeyFrames().add(new KeyFrame(
                    Duration.millis(800), 
                    (ActionEvent event) -> {
                        this.setImg(Sprite.player_down_1.getFxImage());
                        this.setY(this.getY() + SPEED);
                    }
                ));

                t.getKeyFrames().add(new KeyFrame(
                    Duration.millis(1200), 
                    (ActionEvent event) -> {
                        this.setImg(Sprite.player_down_2.getFxImage());
                        this.setY(this.getY() + SPEED);
                    }
                ));

                t.play();
            } else if (e.getCode() == KeyCode.UP) {
                
                Timeline t = new Timeline();
                t.setCycleCount(1);

                t.getKeyFrames().add(new KeyFrame(
                    Duration.millis(400), 
                    (ActionEvent event) -> {
                        this.setImg(Sprite.player_up.getFxImage());
                        this.setY(this.getY() - SPEED);
                    }
                ));

                t.getKeyFrames().add(new KeyFrame(
                    Duration.millis(800), 
                    (ActionEvent event) -> {
                        this.setImg(Sprite.player_up_1.getFxImage());
                        this.setY(this.getY() - SPEED);
                    }
                ));

                t.getKeyFrames().add(new KeyFrame(
                    Duration.millis(1200), 
                    (ActionEvent event) -> {
                        this.setImg(Sprite.player_up_2.getFxImage());
                        this.setY(this.getY() - SPEED);
                    }
                ));

                t.play();
            }
        });
    }
}