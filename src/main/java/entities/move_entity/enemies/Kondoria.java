package entities.move_entity.enemies;

import app.BombermanGame;
import entities.move_entity.Bomberman;
import graphics.Sprite;
import javafx.scene.image.Image;

import java.util.*;

public class Kondoria extends Doll {
    private boolean next = true;
    private int x;
    private int y;
    private int manX = 0;
    private int manY = 0;
    private int count;
    private List<FindMan.Point> path;
    private String[] symbolsCanPass = {" ", "X"};

    Image[] kondoriaFrames = {
            Sprite.kondoria_left1.getFxImage(),
            Sprite.kondoria_left2.getFxImage(),
            Sprite.kondoria_left3.getFxImage(),
            Sprite.kondoria_right1.getFxImage(),
            Sprite.kondoria_right2.getFxImage(),
            Sprite.kondoria_right3.getFxImage()
    };

    Image[] dieKondoriaFrames = {
            Sprite.kondoria_dead.getFxImage(),
            Sprite.kondoria_dead.getFxImage(),
            Sprite.mob_dead1.getFxImage(),
            Sprite.mob_dead1.getFxImage(),
            Sprite.mob_dead2.getFxImage(),
            Sprite.mob_dead2.getFxImage(),
            Sprite.mob_dead3.getFxImage(),
            Sprite.mob_dead3.getFxImage(),
            null
    };

    public Kondoria(int x, int y, Image img) {
        super(x, y, img);
        this.setSpeed(SPEED_ENEMY + 4);
        this.setScore(400);
        this.setNumberOfFrames(5);
        this.setFrames(kondoriaFrames);
        this.setDieFrames(dieKondoriaFrames);
    }

    public void moveLeft(int nextY) {
        this.setDefaultIndex(0);
        this.setNumberOfFrames(2);
        if (this.getX() > nextY * Sprite.SCALED_SIZE) {
            if (this.getX() - this.getSpeed() > nextY * Sprite.SCALED_SIZE) {
                this.setX(this.getX() - this.getSpeed());
            } else {
                this.setX(nextY * Sprite.SCALED_SIZE + 1);
                y = nextY;
                count++;
                findNextPath();
            }
        }
    }

    public void moveRight(int nextY) {
        if (this.getX() < nextY * Sprite.SCALED_SIZE) {
            if (this.getX() + this.getSpeed() < nextY * Sprite.SCALED_SIZE) {
                this.setX(this.getX() + this.getSpeed());
            } else {
                this.setX(nextY * Sprite.SCALED_SIZE - 1);
                y = nextY;
                count++;
                findNextPath();
            }
        }
    }

    public void moveDown(int nextX) {
        if (this.getY() < nextX * Sprite.SCALED_SIZE) {
            if (this.getY() + this.getSpeed() < nextX * Sprite.SCALED_SIZE) {
                this.setY(this.getY() + this.getSpeed());
            } else {
                this.setY(nextX * Sprite.SCALED_SIZE);
                x = nextX;
                count++;
                findNextPath();
            }
        }
    }

    public void moveUp(int nextX) {
        if (this.getY() > nextX * Sprite.SCALED_SIZE) {
            if (this.getY() - this.getSpeed() > nextX * Sprite.SCALED_SIZE) {
                this.setY(this.getY() - this.getSpeed());
            } else {
                this.setY(nextX * Sprite.SCALED_SIZE);
                x = nextX;
                count++;
                findNextPath();
            }
        }
    }

    public void findNextPath() {
        if ((manX != (int) BombermanGame.bomberman.getY() / Sprite.SCALED_SIZE
                || manY != (int) BombermanGame.bomberman.getX() / Sprite.SCALED_SIZE)) {

            manX = (int) BombermanGame.bomberman.getY() / Sprite.SCALED_SIZE;
            manY = (int) BombermanGame.bomberman.getX() / Sprite.SCALED_SIZE;

            count = 0;

            path = FindMan.findShortestPath(
                    new FindMan.Point(x, y),
                    new FindMan.Point(manX, manY),
                    symbolsCanPass
            );
        }
    }

    @Override
    public void move() {
        if (next) {
            x = (int) this.getY() / Sprite.SCALED_SIZE;
            y = (int) this.getX() / Sprite.SCALED_SIZE;
            System.out.println(x + "," + y);
            if (moveLeft) {
                this.setX(y * Sprite.SCALED_SIZE);
            }

            if (moveRight) {
                if (this.getX() / Sprite.SCALED_SIZE - y < 0) {
                    y += 1;
                }
                this.setX(y * Sprite.SCALED_SIZE);
            }

            if (moveUp) {
                this.setY(x * Sprite.SCALED_SIZE);
            }

            if (moveDown) {
                if (this.getY() / Sprite.SCALED_SIZE - x < 0) {
                    x += 1;
                }
                this.setY(x * Sprite.SCALED_SIZE);
            }
            findNextPath();
            next = false;
        } else {
            if (path.size() <= 1 || count >= path.size()) {
                super.move();
                if (!BombermanGame.bomberman.isDied()) findNextPath();
            }
        }

        if (!BombermanGame.bomberman.isDied()) {
            if (path.size() > 1 && count < path.size()) {
                int nextX = path.get(count).x;
                int nextY = path.get(count).y;

                if (nextX - x > 0 && !moveDown) {
                    moveLeft = false;
                    moveRight = false;
                    moveDown = true;
                    moveUp = false;
                }

                if (moveDown) {
                    moveDown(nextX);
                }

                if (nextX - x < 0 && !moveUp) {
                    moveLeft = false;
                    moveRight = false;
                    moveDown = false;
                    moveUp = true;
                }

                if (moveUp) {
                    moveUp(nextX);
                }

                if (nextY - y > 0 && !moveRight) {
                    this.setDefaultIndex(3);
                    this.setNumberOfFrames(5);
                    moveLeft = false;
                    moveRight = true;
                    moveDown = false;
                    moveUp = false;
                }

                if (moveRight) {
                    moveRight(nextY);
                }

                if (nextY - y < 0 && !moveLeft) {
                    this.setDefaultIndex(0);
                    this.setNumberOfFrames(2);
                    moveLeft = true;
                    moveRight = false;
                    moveDown = false;
                    moveUp = false;
                }

                if (moveLeft) {
                    moveLeft(nextY);
                }
            }
        } else {
            super.move();
        }

        this.setPosition(this.getX(), this.getY());
    }
}