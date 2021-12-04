package entities.move_entity.enemies;

import app.BombermanGame;
import graphics.Sprite;
import javafx.scene.image.Image;

public class Minvo extends Enemy {
    private boolean enchentic = false;
    private String[] stillSymbols = {"#"};
    Image[] minvoFrames = {
            Sprite.minvo_left1.getFxImage(),
            Sprite.minvo_left2.getFxImage(),
            Sprite.minvo_left3.getFxImage(),
            Sprite.minvo_right1.getFxImage(),
            Sprite.minvo_right2.getFxImage(),
            Sprite.minvo_right3.getFxImage()
    };

    Image[] minvoFlyFrames = {
            Sprite.minvo_fly_left1.getFxImage(),
            Sprite.minvo_fly_left2.getFxImage(),
            Sprite.minvo_fly_left3.getFxImage(),
            Sprite.minvo_fly_right1.getFxImage(),
            Sprite.minvo_fly_right2.getFxImage(),
            Sprite.minvo_fly_right3.getFxImage()
    };

    Image[] dieMinvoFrames = {
            Sprite.minvo_dead.getFxImage(),
            Sprite.minvo_dead.getFxImage(),
            Sprite.mob_dead1.getFxImage(),
            Sprite.mob_dead1.getFxImage(),
            Sprite.mob_dead2.getFxImage(),
            Sprite.mob_dead2.getFxImage(),
            Sprite.mob_dead3.getFxImage(),
            Sprite.mob_dead3.getFxImage(),
            null
    };

    Image[] dieGhostFlyFrames = {
            Sprite.minvo_fly_dead.getFxImage(),
            Sprite.minvo_fly_dead.getFxImage(),
            Sprite.mob_dead1.getFxImage(),
            Sprite.mob_dead1.getFxImage(),
            Sprite.mob_dead2.getFxImage(),
            Sprite.mob_dead2.getFxImage(),
            Sprite.mob_dead3.getFxImage(),
            Sprite.mob_dead3.getFxImage(),
            null
    };

    public Minvo(int x, int y, Image img) {
        super(x, y, img);
        this.setScore(600);
        this.setNumberOfFrames(5);
        this.setFrames(minvoFrames);
        this.setDieFrames(dieMinvoFrames);
    }

    @Override
    public void move() {
        if (!enchentic) {
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
            if (BombermanGame.time.get() == 0) enchentic = true;
        }

        if (enchentic) {
            this.setFrames(minvoFlyFrames);
            this.setDieFrames(dieMinvoFrames);
        }
        this.setPosition(this.getX(), this.getY());
    }
}