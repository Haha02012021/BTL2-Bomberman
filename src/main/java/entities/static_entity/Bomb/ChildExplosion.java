package entities.static_entity.Bomb;

import app.BombermanGame;
import entities.AnimatedImage;
import entities.static_entity.Brick;
import entities.static_entity.Portal;
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
        this.setSpeed(0);
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
                BombermanGame.textMap.get((int)(this.getY() / Sprite.SCALED_SIZE)).set((int)(this.getX() / Sprite.SCALED_SIZE), " ");
            }
        } else {
            if (!finished) {
                this.checkCollision(BombermanGame.entities);
                if (!tail) {
                    if (checkCollisionToItem("LEFT")) {
                        int x = (int)(this.getX() / Sprite.SCALED_SIZE) - 1;
                        int y = (int)(this.getY() / Sprite.SCALED_SIZE);

                        handleCollisionToItem(x, y, "LEFT");
                    } else if (checkCollisionToItem("RIGHT")) {
                        int x = (int)(this.getX() / Sprite.SCALED_SIZE) + 1;
                        int y = (int)(this.getY() / Sprite.SCALED_SIZE);

                        handleCollisionToItem(x, y, "RIGHT");
                    } else if (checkCollisionToItem("DOWN")) {
                        int x = (int)(this.getX() / Sprite.SCALED_SIZE);
                        int y = (int)(this.getY() / Sprite.SCALED_SIZE) + 1;

                        handleCollisionToItem(x, y, "DOWN");
                    } else if (checkCollisionToItem("UP")) {
                        int x = (int)(this.getX() / Sprite.SCALED_SIZE);
                        int y = (int)(this.getY() / Sprite.SCALED_SIZE) - 1;

                        handleCollisionToItem(x, y, "UP");
                    }
                }
                this.setIndex(this.getIndex() + 1);
            }
        }
    }

    public boolean checkCollisionToItem(String type) {
        return (this.checkCollision(BombermanGame.textMap, "*", type, 0)
                || this.checkCollision(BombermanGame.textMap, "x", type, 0)
                || this.checkCollision(BombermanGame.textMap, "s", type, 0)
                || this.checkCollision(BombermanGame.textMap, "f", type, 0)
                || this.checkCollision(BombermanGame.textMap, "d", type, 0)
                || this.checkCollision(BombermanGame.textMap, "b", type, 0));
    }

    public void handleCollisionToItem(int x, int y, String type) {
        Brick brick = new Brick(x, y, Sprite.brick.getFxImage());
        Portal grass = new Portal(x, y, Sprite.grass.getFxImage());

        if (this.checkCollision(BombermanGame.textMap, "*", type, 0)) BombermanGame.textMap.get(y).set(x, " ");
        if (this.checkCollision(BombermanGame.textMap, "x", type, 0)) {
            BombermanGame.textMap.get(y).set(x, "X");
            brick.setHiddenPortal(false);
            BombermanGame.addStillObjects.add(new Portal(x, y, Sprite.portal.getFxImage()));
        }
        if (this.checkCollision(BombermanGame.textMap, "s", type, 0)) {
            BombermanGame.textMap.get(y).set(x, "S");
            brick.setHiddenPSpeed(false);
        }
        if (this.checkCollision(BombermanGame.textMap, "f", type, 0)) {
            BombermanGame.textMap.get(y).set(x, "F");
            brick.setHiddenPFlame(false);
        }
        if (this.checkCollision(BombermanGame.textMap, "d", type, 0)) {
            BombermanGame.textMap.get(y).set(x, "D");
            brick.setHiddenPDetonation(false);
        }
        brick.setExploded(true);

        BombermanGame.stillObjects.set(y * BombermanGame.width + x, grass);
        BombermanGame.entities.add(brick);
    }
}