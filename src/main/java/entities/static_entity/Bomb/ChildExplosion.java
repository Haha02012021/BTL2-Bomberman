package entities.static_entity.Bomb;

import app.BombermanGame;
import entities.AnimatedImage;
import entities.static_entity.Brick;
import graphics.Sprite;
import javafx.scene.image.Image;

public class ChildExplosion extends AnimatedImage {
    private int cycle = 1, countToFinish = 0;
    private boolean tail = false;
    private boolean finished = false;
    public ChildExplosion(int x, int y, Image image, Image[] frames) {
        super(x, y, image);
        this.setPosition(x * Sprite.SCALED_SIZE, y * Sprite.SCALED_SIZE);
        this.setFrames(frames);
    }

    public ChildExplosion() {

    }

    public boolean isTail() {
        return tail;
    }

    public void setTail(boolean tail) {
        this.tail = tail;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void update() {
        if (this.getIndex() >= 3) {
            this.setIndex(3);
            countToFinish++;
            if (countToFinish == cycle) {
                finished = true;
            }
        } else {
            if (!finished) {
                if (!tail) {
                    if (this.checkCollision(BombermanGame.textMap, "*", "LEFT")
                            || this.checkCollision(BombermanGame.textMap, "x", "LEFT")) {
                        int x = (int)(this.getX() / Sprite.SCALED_SIZE) - 1;
                        int y = (int)(this.getY() / Sprite.SCALED_SIZE);
                        System.out.println(x + " " + y);
                        Brick brick = new Brick(x, y, Sprite.brick.getFxImage());
                        if (this.checkCollision(BombermanGame.textMap, "*", "LEFT")) BombermanGame.textMap.get(y).set(x, " ");
                        if (this.checkCollision(BombermanGame.textMap, "x", "LEFT")) {
                            BombermanGame.textMap.get(y).set(x, "X");
                            brick.setHidden(false);
                        }
                        brick.setExploded(true);
                        BombermanGame.stillObjects.set(y * BombermanGame.width + x, brick);
                    } else if (this.checkCollision(BombermanGame.textMap, "*", "RIGHT")
                            || this.checkCollision(BombermanGame.textMap, "x", "RIGHT")) {
                        int x = (int)(this.getX() / Sprite.SCALED_SIZE) + 1;
                        int y = (int)(this.getY() / Sprite.SCALED_SIZE);
                        System.out.println(x + " " + y);
                        Brick brick = new Brick(x, y, Sprite.brick.getFxImage());
                        if (this.checkCollision(BombermanGame.textMap, "*", "RIGHT")) BombermanGame.textMap.get(y).set(x, " ");
                        if (this.checkCollision(BombermanGame.textMap, "x", "RIGHT")) {
                            BombermanGame.textMap.get(y).set(x, "X");
                            brick.setHidden(false);
                        }
                        brick.setExploded(true);
                        BombermanGame.stillObjects.set(y * BombermanGame.width + x, brick);
                    } else if (this.checkCollision(BombermanGame.textMap, "*", "DOWN")
                                || this.checkCollision(BombermanGame.textMap, "x", "DOWN")) {
                        int x = (int)(this.getX() / Sprite.SCALED_SIZE);
                        int y = (int)(this.getY() / Sprite.SCALED_SIZE) + 1;
                        System.out.println(x + " " + y);
                        Brick brick = new Brick(x, y, Sprite.brick.getFxImage());
                        if (this.checkCollision(BombermanGame.textMap, "*", "DOWN")) BombermanGame.textMap.get(y).set(x, " ");
                        if (this.checkCollision(BombermanGame.textMap, "x", "DOWN")) {
                            BombermanGame.textMap.get(y).set(x, "X");
                            brick.setHidden(false);
                        }
                        brick.setExploded(true);
                        BombermanGame.stillObjects.set(y * BombermanGame.width + x, brick);
                    } else if (this.checkCollision(BombermanGame.textMap, "*", "UP")
                                || this.checkCollision(BombermanGame.textMap, "x", "UP")) {
                        int x = (int)(this.getX() / Sprite.SCALED_SIZE);
                        int y = (int)(this.getY() / Sprite.SCALED_SIZE) - 1;
                        System.out.println(x + " " + y);
                        Brick brick = new Brick(x, y, Sprite.brick.getFxImage());
                        if (this.checkCollision(BombermanGame.textMap, "*", "UP")) BombermanGame.textMap.get(y).set(x, " ");
                        if (this.checkCollision(BombermanGame.textMap, "x", "UP")) {
                            BombermanGame.textMap.get(y).set(x, "X");
                            brick.setHidden(false);
                        }
                        brick.setExploded(true);
                        BombermanGame.stillObjects.set(y * BombermanGame.width + x, brick);
                    }
                }


                this.setIndex(this.getIndex() + 1);
            }
        }
    }
}
