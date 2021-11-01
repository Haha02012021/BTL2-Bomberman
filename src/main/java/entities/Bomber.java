package entities;

import java.security.KeyStore.Entry;
import java.util.List;

import app.BombermanGame;
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
    private static final int SPEED = 4;
    private static final String RIGHT_KEY = "RIGHT_KEY";
    private static final String LEFT_KEY = "LEFT_KEY";
    private static final String DOWN_KEY = "DOWN_KEY";
    private static final String UP_KEY = "UP_KEY";

    public Bomber(int x, int y, Image img) {
        super( x, y, img);
    }

    public boolean checkPosition(List<Entity> stillObjects, List<Entity> entities, String pressedKey) {
        for (Entity e: stillObjects) {
            if (e instanceof Wall) {
                if (pressedKey.equals(RIGHT_KEY) || pressedKey.equals(LEFT_KEY)) {
                    if ((this.getY() >= e.getY() && this.getY() < e.getY() + Sprite.SCALED_SIZE)
                        || (this.getY() + Sprite.SCALED_SIZE < e.getY() + Sprite.SCALED_SIZE && this.getY() + Sprite.SCALED_SIZE > e.getY())) {
                        if (this.getX() + 36 == e.getX()
                            && pressedKey.equals(RIGHT_KEY)) return false;
                        if (this.getX() == e.getX() + Sprite.SCALED_SIZE
                            && pressedKey.equals(LEFT_KEY)) return false;
                    }
                }

                if (pressedKey.equals(DOWN_KEY) || pressedKey.equals(UP_KEY)) {
                    if ((this.getX() >= e.getX() && this.getX() < e.getX() + Sprite.SCALED_SIZE)
                    || (this.getX() + 36 < e.getX() + Sprite.SCALED_SIZE && this.getX() + 36 > e.getX())) {
                        if (this.getY() + Sprite.SCALED_SIZE == e.getY()
                            && pressedKey.equals(DOWN_KEY)) return false;
                        if (this.getY() - Sprite.SCALED_SIZE == e.getY()
                            && pressedKey.equals(UP_KEY)) return false;
                    }
                }
            }
        }

        for (Entity e: entities) {
            if (e instanceof Brick) {
                if (pressedKey.equals(RIGHT_KEY) || pressedKey.equals(LEFT_KEY)) {
                    if ((this.getY() >= e.getY() && this.getY() < e.getY() + Sprite.SCALED_SIZE)
                        || (this.getY() + Sprite.SCALED_SIZE < e.getY() + Sprite.SCALED_SIZE && this.getY() + Sprite.SCALED_SIZE > e.getY())) {
                        if (this.getX() + 36 == e.getX()
                            && pressedKey.equals(RIGHT_KEY)) return false;
                        if (this.getX() == e.getX() + Sprite.SCALED_SIZE
                            && pressedKey.equals(LEFT_KEY)) return false;
                    }
                }

                if (pressedKey.equals(DOWN_KEY) || pressedKey.equals(UP_KEY)) {
                    if ((this.getX() >= e.getX() && this.getX() < e.getX() + Sprite.SCALED_SIZE)
                    || (this.getX() + 36 < e.getX() + Sprite.SCALED_SIZE && this.getX() + 36 > e.getX())) {
                        if (this.getY() + Sprite.SCALED_SIZE == e.getY()
                            && pressedKey.equals(DOWN_KEY)) return false;
                        if (this.getY() - Sprite.SCALED_SIZE == e.getY()
                            && pressedKey.equals(UP_KEY)) return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public void update(Scene scene) {
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.B) {
                BombermanGame.stillObjects.add(new Bomb(this.getX() / Sprite.SCALED_SIZE, this.getY() / Sprite.SCALED_SIZE, Sprite.bomb.getFxImage()));
            } else if (e.getCode() == KeyCode.RIGHT) {
                
                Timeline t = new Timeline();
                t.setCycleCount(1);

                t.getKeyFrames().add(new KeyFrame(
                    Duration.millis(400), 
                    (ActionEvent event) -> {
                        this.setImg(Sprite.player_right.getFxImage());
                        Boolean b = this.checkPosition(BombermanGame.stillObjects, BombermanGame.entities, RIGHT_KEY);
                        if (b) this.setX(this.getX() + SPEED);
                    }
                ));

                t.getKeyFrames().add(new KeyFrame(
                    Duration.millis(800), 
                    (ActionEvent event) -> {
                        this.setImg(Sprite.player_right_1.getFxImage());
                        Boolean b = this.checkPosition(BombermanGame.stillObjects, BombermanGame.entities, RIGHT_KEY);
                        if (b) this.setX(this.getX() + SPEED);
                    }
                ));

                t.getKeyFrames().add(new KeyFrame(
                    Duration.millis(1200), 
                    (ActionEvent event) -> {
                        this.setImg(Sprite.player_right_2.getFxImage());
                        Boolean b = this.checkPosition(BombermanGame.stillObjects, BombermanGame.entities, RIGHT_KEY);
                        if (b) this.setX(this.getX() + SPEED);
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
                        Boolean b = this.checkPosition(BombermanGame.stillObjects, BombermanGame.entities, LEFT_KEY);
                        if (b) this.setX(this.getX() - SPEED);
                    }
                ));

                t.getKeyFrames().add(new KeyFrame(
                    Duration.millis(800), 
                    (ActionEvent event) -> {
                        this.setImg(Sprite.player_left_1.getFxImage());
                        Boolean b = this.checkPosition(BombermanGame.stillObjects, BombermanGame.entities, LEFT_KEY);
                        if (b) this.setX(this.getX() - SPEED);
                    }
                ));

                t.getKeyFrames().add(new KeyFrame(
                    Duration.millis(1200), 
                    (ActionEvent event) -> {
                        this.setImg(Sprite.player_left_2.getFxImage());
                        Boolean b = this.checkPosition(BombermanGame.stillObjects, BombermanGame.entities, LEFT_KEY);
                        if (b) this.setX(this.getX() - SPEED);
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
                        Boolean b = this.checkPosition(BombermanGame.stillObjects, BombermanGame.entities, DOWN_KEY);
                        if (b) this.setY(this.getY() + SPEED);
                    }
                ));

                t.getKeyFrames().add(new KeyFrame(
                    Duration.millis(800), 
                    (ActionEvent event) -> {
                        this.setImg(Sprite.player_down_1.getFxImage());
                        Boolean b = this.checkPosition(BombermanGame.stillObjects, BombermanGame.entities, DOWN_KEY);
                        if (b) this.setY(this.getY() + SPEED);
                    }
                ));

                t.getKeyFrames().add(new KeyFrame(
                    Duration.millis(1200), 
                    (ActionEvent event) -> {
                        this.setImg(Sprite.player_down_2.getFxImage());
                        Boolean b = this.checkPosition(BombermanGame.stillObjects, BombermanGame.entities, DOWN_KEY);
                        if (b) this.setY(this.getY() + SPEED);
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
                        Boolean b = this.checkPosition(BombermanGame.stillObjects, BombermanGame.entities, UP_KEY);
                        if (b) this.setY(this.getY() - SPEED);
                    }
                ));

                t.getKeyFrames().add(new KeyFrame(
                    Duration.millis(800), 
                    (ActionEvent event) -> {
                        this.setImg(Sprite.player_up_1.getFxImage());
                        Boolean b = this.checkPosition(BombermanGame.stillObjects, BombermanGame.entities, UP_KEY);
                        if (b) this.setY(this.getY() - SPEED);
                    }
                ));

                t.getKeyFrames().add(new KeyFrame(
                    Duration.millis(1200), 
                    (ActionEvent event) -> {
                        this.setImg(Sprite.player_up_2.getFxImage());
                        Boolean b = this.checkPosition(BombermanGame.stillObjects, BombermanGame.entities, UP_KEY);
                        if (b) this.setY(this.getY() - SPEED);
                    }
                ));

                t.play();
            }
        });
    }
}