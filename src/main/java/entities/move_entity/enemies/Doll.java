package entities.move_entity.enemies;

import graphics.Sprite;
import javafx.scene.image.Image;

public class Doll extends Enemy {
    Image[] dollFrames = {
            Sprite.doll_left1.getFxImage(),
            Sprite.doll_left2.getFxImage(),
            Sprite.doll_left3.getFxImage(),
            Sprite.doll_right1.getFxImage(),
            Sprite.doll_right2.getFxImage(),
            Sprite.doll_right3.getFxImage()
    };

    Image[] dieDollFrames = {
            Sprite.doll_dead.getFxImage(),
            Sprite.doll_dead.getFxImage(),
            Sprite.mob_dead1.getFxImage(),
            Sprite.mob_dead1.getFxImage(),
            Sprite.mob_dead2.getFxImage(),
            Sprite.mob_dead2.getFxImage(),
            Sprite.mob_dead3.getFxImage(),
            Sprite.mob_dead3.getFxImage(),
            null
    };

    public Doll(int x, int y, Image img) {
        super(x, y, img);
        this.setScore(200);
        this.setNumberOfFrames(5);
        this.setFrames(dollFrames);
        this.setDieFrames(dieDollFrames);
    }

    @Override
    public void move() {
        super.move();
        if (moveRight) {
            if (this.checkCollisionToStillObjects(stillSymbols, "RIGHT")) {
                this.setX(this.getX() + SPEED_ENEMY);
                random--;
                if (random == -100) {
                    random = (int)(Math.random() * 4) + 1;
                    while (random == prevRandom) {
                        random = (int)(Math.random() * 4) + 1;
                    }
                }
            } else if (this.checkCollisionToStillObjects(stillSymbols, "LEFT")) {
                this.setDefaultIndex(0);
                this.setNumberOfFrames(2);
                moveRight = false;
                moveLeft = true;
                moveDown = false;
                moveUp = false;
            } else {
                random = (int)(Math.random() * 4) + 1;
                while (random == prevRandom) {
                    random = (int)(Math.random() * 4) + 1;
                }
            }
        }

        if (moveLeft) {
            if (this.checkCollisionToStillObjects(stillSymbols, "LEFT")) {
                this.setX(this.getX() - SPEED_ENEMY);
                random--;
                if (random == -100) {
                    random = (int)(Math.random() * 4) + 1;
                    while (random == prevRandom) {
                        random = (int)(Math.random() * 4) + 1;
                    }
                }
            } else if (this.checkCollisionToStillObjects(stillSymbols, "RIGHT")) {
                this.setDefaultIndex(3);
                this.setNumberOfFrames(5);
                moveRight = true;
                moveLeft = false;
                moveDown = false;
                moveUp = false;
            } else {
                random = (int)(Math.random() * 4) + 1;
                while (random == prevRandom) {
                    random = (int)(Math.random() * 4) + 1;
                }
            }
        }

        if (moveDown) {
            if (this.checkCollisionToStillObjects(stillSymbols, "DOWN")) {
                this.setY(this.getY() + SPEED_ENEMY);
                random--;
                if (random == -100) {
                    random = (int)(Math.random() * 4) + 1;
                    while (random == prevRandom) {
                        random = (int)(Math.random() * 4) + 1;
                    }
                }
            } else if (this.checkCollisionToStillObjects(stillSymbols, "UP")) {
                moveRight = false;
                moveLeft = false;
                moveDown = false;
                moveUp = true;
            } else {
                random = (int)(Math.random() * 4) + 1;
                while (random == prevRandom) {
                    random = (int)(Math.random() * 4) + 1;
                }
            }
        }

        if (moveUp) {
            if (this.checkCollisionToStillObjects(stillSymbols, "UP")) {
                this.setY(this.getY() - SPEED_ENEMY);
                random--;
                if (random == -100) {
                    random = (int)(Math.random() * 4) + 1;
                    while (random == prevRandom) {
                        random = (int)(Math.random() * 4) + 1;
                    }
                }
            } else if (this.checkCollisionToStillObjects(stillSymbols, "DOWN")) {
                moveRight = false;
                moveLeft = false;
                moveDown = true;
                moveUp = false;
            } else {
                random = (int)(Math.random() * 4) + 1;
                while (random == prevRandom) {
                    random = (int)(Math.random() * 4) + 1;
                }
            }
        }
        this.setPosition(this.getX(), this.getY());
    }
}