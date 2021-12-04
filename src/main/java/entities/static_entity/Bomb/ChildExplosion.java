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
                BombermanGame.textMap.get((int)(this.getY() / Sprite.SCALED_SIZE)).set((int)(this.getX() / Sprite.SCALED_SIZE), " ");
            }
        } else {
            if (!finished) {
                this.checkCollision(BombermanGame.entities);
                if (!tail) {
                    if (this.checkCollision(BombermanGame.textMap, "*", "LEFT", 0)
                            || this.checkCollision(BombermanGame.textMap, "x", "LEFT", 0)
                            || this.checkCollision(BombermanGame.textMap, "s", "LEFT", 0)
                            || this.checkCollision(BombermanGame.textMap, "f", "LEFT", 0)
                            || this.checkCollision(BombermanGame.textMap, "d", "LEFT", 0)) {
                        int x = (int)(this.getX() / Sprite.SCALED_SIZE) - 1;
                        int y = (int)(this.getY() / Sprite.SCALED_SIZE);
                        System.out.println(x + " " + y);
                        Brick brick = new Brick(x, y, Sprite.brick.getFxImage());
                        Portal grass = new Portal(x, y, Sprite.grass.getFxImage());
                        if (this.checkCollision(BombermanGame.textMap, "*", "LEFT", 0)) BombermanGame.textMap.get(y).set(x, " ");
                        if (this.checkCollision(BombermanGame.textMap, "x", "LEFT", 0)) {
                            BombermanGame.textMap.get(y).set(x, "X");
                            brick.setHiddenPortal(false);
                            BombermanGame.addStillObjects.add(new Portal(x, y, Sprite.portal.getFxImage()));
                        }
                        if (this.checkCollision(BombermanGame.textMap, "s", "LEFT", 0)) {
                            BombermanGame.textMap.get(y).set(x, " ");
                            brick.setHiddenPSpeed(false);
                        }
                        if (this.checkCollision(BombermanGame.textMap, "f", "LEFT", 0)) {
                            BombermanGame.textMap.get(y).set(x, " ");
                            brick.setHiddenPFlame(false);
                        }
                        if (this.checkCollision(BombermanGame.textMap, "d", "LEFT", 0)) {
                            BombermanGame.textMap.get(y).set(x, " ");
                            brick.setHiddenPDetonation(false);
                        }
                        brick.setExploded(true);
                        BombermanGame.stillObjects.set(y * BombermanGame.width + x, grass);
                        BombermanGame.entities.add(brick);
                    } else if (this.checkCollision(BombermanGame.textMap, "*", "RIGHT", 0)
                            || this.checkCollision(BombermanGame.textMap, "x", "RIGHT", 0)
                            || this.checkCollision(BombermanGame.textMap, "s", "RIGHT", 0)
                            || this.checkCollision(BombermanGame.textMap, "f", "RIGHT", 0)
                            || this.checkCollision(BombermanGame.textMap, "d", "RIGHT", 0)
                    ) {
                        int x = (int)(this.getX() / Sprite.SCALED_SIZE) + 1;
                        int y = (int)(this.getY() / Sprite.SCALED_SIZE);
                        System.out.println(x + " " + y);
                        Brick brick = new Brick(x, y, Sprite.brick.getFxImage());
                        Portal grass = new Portal(x, y, Sprite.grass.getFxImage());
                        if (this.checkCollision(BombermanGame.textMap, "*", "RIGHT", 0)) BombermanGame.textMap.get(y).set(x, " ");
                        if (this.checkCollision(BombermanGame.textMap, "x", "RIGHT", 0)) {
                            BombermanGame.textMap.get(y).set(x, "X");
                            brick.setHiddenPortal(false);
                            BombermanGame.addStillObjects.add(new Portal(x, y, Sprite.portal.getFxImage()));
                        }
                        if (this.checkCollision(BombermanGame.textMap, "s", "RIGHT", 0)) {
                            BombermanGame.textMap.get(y).set(x, " ");
                            brick.setHiddenPSpeed(false);
                        }
                        if (this.checkCollision(BombermanGame.textMap, "f", "RIGHT", 0)) {
                            BombermanGame.textMap.get(y).set(x, " ");
                            brick.setHiddenPFlame(false);
                        }
                        if (this.checkCollision(BombermanGame.textMap, "d", "RIGHT", 0)) {
                            BombermanGame.textMap.get(y).set(x, " ");
                            brick.setHiddenPDetonation(false);
                        }
                        brick.setExploded(true);
                        BombermanGame.stillObjects.set(y * BombermanGame.width + x, grass);
                        BombermanGame.entities.add(brick);
                    } else if (this.checkCollision(BombermanGame.textMap, "*", "DOWN", 0)
                            || this.checkCollision(BombermanGame.textMap, "x", "DOWN", 0)
                            || this.checkCollision(BombermanGame.textMap, "s", "DOWN", 0)
                            || this.checkCollision(BombermanGame.textMap, "f", "DOWN", 0)
                            || this.checkCollision(BombermanGame.textMap, "d", "DOWN", 0)
                    ) {
                        int x = (int)(this.getX() / Sprite.SCALED_SIZE);
                        int y = (int)(this.getY() / Sprite.SCALED_SIZE) + 1;
                        System.out.println(x + " " + y);
                        Brick brick = new Brick(x, y, Sprite.brick.getFxImage());
                        Portal grass = new Portal(x, y, Sprite.grass.getFxImage());
                        if (this.checkCollision(BombermanGame.textMap, "*", "DOWN", 0)) BombermanGame.textMap.get(y).set(x, " ");
                        if (this.checkCollision(BombermanGame.textMap, "x", "DOWN", 0)) {
                            BombermanGame.textMap.get(y).set(x, "X");
                            brick.setHiddenPortal(false);
                            BombermanGame.addStillObjects.add(new Portal(x, y, Sprite.portal.getFxImage()));
                        }
                        if (this.checkCollision(BombermanGame.textMap, "s", "DOWN", 0)) {
                            BombermanGame.textMap.get(y).set(x, " ");
                            brick.setHiddenPSpeed(false);
                        }
                        if (this.checkCollision(BombermanGame.textMap, "f", "DOWN", 0)) {
                            BombermanGame.textMap.get(y).set(x, " ");
                            brick.setHiddenPFlame(false);
                        }
                        if (this.checkCollision(BombermanGame.textMap, "d", "DOWN", 0)) {
                            BombermanGame.textMap.get(y).set(x, " ");
                            brick.setHiddenPDetonation(false);
                        }
                        brick.setExploded(true);
                        BombermanGame.stillObjects.set(y * BombermanGame.width + x, grass);
                        BombermanGame.entities.add(brick);
                    } else if (this.checkCollision(BombermanGame.textMap, "*", "UP", 0)
                            || this.checkCollision(BombermanGame.textMap, "x", "UP", 0)
                            || this.checkCollision(BombermanGame.textMap, "s", "UP", 0)
                            || this.checkCollision(BombermanGame.textMap, "f", "UP", 0)
                            || this.checkCollision(BombermanGame.textMap, "d", "UP", 0)
                    ) {
                        int x = (int)(this.getX() / Sprite.SCALED_SIZE);
                        int y = (int)(this.getY() / Sprite.SCALED_SIZE) - 1;
                        System.out.println(x + " " + y);
                        Brick brick = new Brick(x, y, Sprite.brick.getFxImage());
                        Portal grass = new Portal(x, y, Sprite.grass.getFxImage());
                        if (this.checkCollision(BombermanGame.textMap, "*", "UP", 0)) BombermanGame.textMap.get(y).set(x, " ");
                        if (this.checkCollision(BombermanGame.textMap, "x", "UP", 0)) {
                            BombermanGame.textMap.get(y).set(x, "X");
                            brick.setHiddenPortal(false);
                            BombermanGame.addStillObjects.add(new Portal(x, y, Sprite.portal.getFxImage()));
                        }
                        if (this.checkCollision(BombermanGame.textMap, "s", "UP", 0)) {
                            BombermanGame.textMap.get(y).set(x, " ");
                            brick.setHiddenPSpeed(false);
                        }
                        if (this.checkCollision(BombermanGame.textMap, "f", "UP", 0)) {
                            BombermanGame.textMap.get(y).set(x, " ");
                            brick.setHiddenPFlame(false);
                        }
                        if (this.checkCollision(BombermanGame.textMap, "d", "UP", 0)) {
                            BombermanGame.textMap.get(y).set(x, " ");
                            brick.setHiddenPDetonation(false);
                        }
                        brick.setExploded(true);
                        BombermanGame.stillObjects.set(y * BombermanGame.width + x, grass);
                        BombermanGame.entities.add(brick);
                    }
                }
                this.setIndex(this.getIndex() + 1);
            }
        }
    }
}