package entities.move_entity.enemies;

import app.BombermanGame;
import graphics.Sprite;
import javafx.scene.image.Image;

import java.util.List;

public class Minvo extends Enemy {
    private boolean next = true;
    private int x;
    private int y;
    private int count;
    private List<FindPath.Point> path;
    private String[] symbolsCanPass = {" ", "X", "f", "s", "d", "*", "x"};
    private String[] stillSymbols = {"#"};
    Image[] minvoFrames = {
            Sprite.minvo_left1.getFxImage(),
            Sprite.minvo_left2.getFxImage(),
            Sprite.minvo_left3.getFxImage(),
            Sprite.minvo_right1.getFxImage(),
            Sprite.minvo_right2.getFxImage(),
            Sprite.minvo_right3.getFxImage()
    };

    Image[] dieMinvoFrames = {
            Sprite.minvo_dead.getFxImage(),
            Sprite.minvo_dead.getFxImage(),
            Sprite.minvo_dead.getFxImage(),
            Sprite.minvo_dead.getFxImage(),
            Sprite.mob_dead1.getFxImage(),
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
        count = 0;

        List<FindPath.Point> aPath = FindPath.findAPath(
                new FindPath.Point(x, y),
                symbolsCanPass
        );

        path = aPath;
    }

    @Override
    public void move() {
        if (next) {
            x = (int) this.getY() / Sprite.SCALED_SIZE;
            y = (int) this.getX() / Sprite.SCALED_SIZE;

            findNextPath();
            next = false;
        } else {
            if (path.size() <= 1 || count >= path.size()) {
                findNextPath();
            }
        }

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

            this.setPosition(this.getX(), this.getY());
        }

        this.setPosition(this.getX(), this.getY());
    }
}