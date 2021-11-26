package entities.move_entity.enemies;

import app.BombermanGame;
import entities.AnimatedImage;
import graphics.Sprite;
import javafx.scene.image.Image;

public class Oneal extends Enemy {
    private boolean moveRight = true;
    private boolean moveLeft = false;
    Image[] dieOnealFrames = {
            Sprite.oneal_dead.getFxImage(),
            Sprite.mob_dead1.getFxImage(),
            Sprite.mob_dead2.getFxImage(),
            Sprite.mob_dead3.getFxImage(),
            null
    };

    Image[] onealFrames = {
            Sprite.oneal_right1.getFxImage(),
            Sprite.oneal_left1.getFxImage(),
            Sprite.oneal_right2.getFxImage(),
            Sprite.oneal_left2.getFxImage(),
            Sprite.oneal_right3.getFxImage(),
            Sprite.oneal_left3.getFxImage()
    };

    public Oneal(int x, int y, Image img) {
        super(x, y, img);
        this.setFrames(onealFrames);
        this.setDieFrames(dieOnealFrames);
        this.setNumberOfFrames(5);
    }

    @Override
    public void move() {
        if (moveRight) {
            if (this.checkCollisionToStillObjects(stillSymbols, "RIGHT")) {
                this.setX(this.getX() + SPEED_ENEMY);
            } else {
                moveRight = false;
                moveLeft = true;
            }
        }

        if (moveLeft) {
            if (this.checkCollisionToStillObjects(stillSymbols, "LEFT")) {
                this.setX(this.getX() - SPEED_ENEMY);
            } else {
                moveRight = true;
                moveLeft = false;
            }
        }
        this.setPosition(this.getX(), this.getY());

    }
}
