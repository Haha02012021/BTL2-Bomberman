package entities.move_entity.enemies;

import graphics.Sprite;
import javafx.scene.image.Image;

public class Balloom extends Enemy {
    private boolean moveDown = true;
    private boolean moveUp = false;
    Image[] ballomFrames = {
            Sprite.balloom_left1.getFxImage(),
            Sprite.balloom_left2.getFxImage(),
            Sprite.balloom_left3.getFxImage(),
            Sprite.balloom_right1.getFxImage(),
            Sprite.balloom_right2.getFxImage(),
            Sprite.balloom_right3.getFxImage()
    };

    Image[] dieBallomFrames = {
            Sprite.balloom_dead.getFxImage(),
            Sprite.balloom_dead.getFxImage(),
            Sprite.mob_dead1.getFxImage(),
            Sprite.mob_dead1.getFxImage(),
            Sprite.mob_dead2.getFxImage(),
            Sprite.mob_dead2.getFxImage(),
            Sprite.mob_dead3.getFxImage(),
            Sprite.mob_dead3.getFxImage(),
            null
    };

    public Balloom(int x, int y, Image img) {
        super(x, y, img);
        this.setScore(100);
        this.setNumberOfFrames(5);
        this.setFrames(ballomFrames);
        this.setDieFrames(dieBallomFrames);
    }

    @Override
    public void move() {
        if (moveDown) {
            if (this.checkCollisionToStillObjects(stillSymbols, "DOWN")) {
                this.setY(this.getY() + SPEED_ENEMY);
            } else {
                moveDown = false;
                moveUp = true;
            }
        }

        if (moveUp) {
            if (this.checkCollisionToStillObjects(stillSymbols, "UP")) {
                this.setY(this.getY() - SPEED_ENEMY);
            } else {
                moveDown = true;
                moveUp = false;
            }
        }
        this.setPosition(this.getX(), this.getY());
    }
}