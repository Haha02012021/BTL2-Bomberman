package entities.move_entity.enemies;

import app.BombermanGame;
import graphics.Sprite;
import javafx.scene.image.Image;

import java.util.List;

public class Ghost extends Minvo {
    private boolean enchentic = false;
    private boolean next = true;
    private int x;
    private int y;
    private int manX = 0;
    private int manY = 0;
    private int count;
    private List<FindMan.Point> path;
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
            Sprite.mob_dead1.getFxImage(),
            Sprite.mob_dead1.getFxImage(),
            Sprite.mob_dead2.getFxImage(),
            Sprite.mob_dead2.getFxImage(),
            Sprite.mob_dead3.getFxImage(),
            Sprite.mob_dead3.getFxImage(),
            null
    };

    Image[] ghostFlyFrames = {
            Sprite.ghost_fly_left1.getFxImage(),
            Sprite.ghost_fly_left2.getFxImage(),
            Sprite.ghost_fly_left3.getFxImage(),
            Sprite.ghost_fly_right1.getFxImage(),
            Sprite.ghost_fly_right2.getFxImage(),
            Sprite.ghost_fly_right3.getFxImage()
    };

    Image[] dieGhostFlyFrames = {
            Sprite.ghost_fly_dead.getFxImage(),
            Sprite.ghost_fly_dead.getFxImage(),
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
        this.setScore(SPEED_ENEMY);
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
        if (!enchentic) {
            if (next) {
                x = (int) this.getY() / Sprite.SCALED_SIZE;
                y = (int) this.getX() / Sprite.SCALED_SIZE;
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
                next = true;
            }

            if (BombermanGame.time.get() == 0) enchentic = true;
        }

        if (enchentic) {
            this.setFrames(ghostFlyFrames);
            this.setDieFrames(dieGhostFlyFrames);
        }
        this.setPosition(this.getX(), this.getY());
    }
}