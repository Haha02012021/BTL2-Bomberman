package entities.move_entity.enemies;

import app.BombermanGame;
import graphics.Sprite;
import javafx.scene.image.Image;

import java.util.List;

public class Ghost extends Kondoria {
    private boolean enchentic = false;
    private boolean next = true;
    private int x;
    private int y;
    private int manX = 0;
    private int manY = 0;
    private int count;
    private List<FindPath.Point> path;
    private String[] symbolsCanPass = {" ", "X", "f", "s", "d", "*", "x"};
    Image[] ghostFrames = {
            Sprite.ghost_left1.getFxImage(),
            Sprite.ghost_left2.getFxImage(),
            Sprite.ghost_left3.getFxImage(),
            Sprite.ghost_right1.getFxImage(),
            Sprite.ghost_right2.getFxImage(),
            Sprite.ghost_right3.getFxImage()
    };

    Image[] dieGhostFrames = {
            Sprite.ghost_dead.getFxImage(),
            Sprite.ghost_dead.getFxImage(),
            Sprite.ghost_dead.getFxImage(),
            Sprite.ghost_dead.getFxImage(),
            Sprite.mob_dead1.getFxImage(),
            Sprite.mob_dead1.getFxImage(),
            Sprite.mob_dead1.getFxImage(),
            Sprite.mob_dead2.getFxImage(),
            Sprite.mob_dead2.getFxImage(),
            Sprite.mob_dead3.getFxImage(),
            Sprite.mob_dead3.getFxImage(),
            null
    };

    public Ghost(int x, int y, Image img) {
        super(x, y, img);
        this.setScore(800);
        this.setNumberOfFrames(5);
        this.setScore(SPEED_ENEMY + 4);
        this.setFrames(ghostFrames);
        this.setDieFrames(dieGhostFrames);
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
                || manY != (int) BombermanGame.bomberman.getX() / Sprite.SCALED_SIZE)
                || BombermanGame.bomberman.isDied()) {

            manX = (int) BombermanGame.bomberman.getY() / Sprite.SCALED_SIZE;
            manY = (int) BombermanGame.bomberman.getX() / Sprite.SCALED_SIZE;

            count = 0;

            List<FindPath.Point> shortestPath = FindPath.findShortestPath(
                    new FindPath.Point(x, y),
                    new FindPath.Point(manX, manY),
                    symbolsCanPass
            );

            List<FindPath.Point> aPath = FindPath.findAPath(
                    new FindPath.Point(x, y),
                    symbolsCanPass
            );

            if (shortestPath.size() <= 1 || BombermanGame.bomberman.isDied()) {
                path = aPath;
            } else {
                path = shortestPath;
                manX = 0;
                manY = 0;
            }
        }
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