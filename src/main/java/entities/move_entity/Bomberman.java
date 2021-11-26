package entities.move_entity;

import app.BombermanGame;
import entities.AnimatedImage;
import entities.static_entity.Bomb.Bomb;
import entities.static_entity.Bomb.Explosion;
import graphics.Sprite;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class Bomberman extends AnimatedImage {
    private int speed = 8;
    private boolean moving;
    private boolean disapeared = false;
    private boolean eatedFlames = false;
    private boolean eatedSpeed = false;
    private int bombsEated = 0;
    private int flamesEated = 0;
    private int souls = 3;
    Bomb bomb = null;
    private Image[] leftMove = {
            Sprite.player_left.getFxImage(),
            Sprite.player_left_1.getFxImage(),
            Sprite.player_left_2.getFxImage(),
            null
    };
    private Image[] rightMove = {
            Sprite.player_right.getFxImage(),
            Sprite.player_right_1.getFxImage(),
            Sprite.player_right_2.getFxImage(),
            null
    };
    private Image[] downMove = {
            Sprite.player_down.getFxImage(),
            Sprite.player_down_1.getFxImage(),
            Sprite.player_down_2.getFxImage(),
            null
    };
    private Image[] upMove = {
            Sprite.player_up.getFxImage(),
            Sprite.player_up_1.getFxImage(),
            Sprite.player_up_2.getFxImage(),
            null
    };

    private Image[] dieFrames = {
            Sprite.player_dead1.getFxImage(),
            Sprite.player_dead2.getFxImage(),
            Sprite.player_dead3.getFxImage(),
            null
    };
    private int bombX, bombY;

    public Bomberman(int x, int y, Image fxImage) {
        super(x, y, fxImage);
        this.setPosition(x * Sprite.SCALED_SIZE, y * Sprite.SCALED_SIZE);
        this.setFrames(rightMove);
        this.setSpeed(speed);
    }

    public boolean isEatedSpeed() {
        return eatedSpeed;
    }

    public void setEatedSpeed(boolean eatedSpeed) {
        this.eatedSpeed = eatedSpeed;
    }

    public boolean isEatedFlames() {
        return eatedFlames;
    }

    public void setEatedFlames(boolean eatedFlames) {
        this.eatedFlames = eatedFlames;
    }

    public int getFlamesEated() {
        return flamesEated;
    }

    public void setFlamesEated(int flamesEated) {
        this.flamesEated = flamesEated;
    }

    public int getBombsEated() {
        return bombsEated;
    }

    public void setBombsEated(int bombsEated) {
        this.bombsEated = bombsEated;
    }

    @Override
    public void update() {
        this.checkCollision(BombermanGame.entities);

        if (!this.isDied()) {

            if (this.isEatedSpeed()) {
                speed += 4;
                this.setEatedSpeed(false);
            }

            if (this.isEatedFlames()) {
                this.setFlamesEated(this.getFlamesEated() + 3);
                this.setEatedFlames(false);
            }

            if (BombermanGame.input.contains("LEFT") && this.checkCollisionToStillObjects(stillSymbols, "LEFT")) {
                this.move(leftMove, "LEFT");
            }

            if (BombermanGame.input.contains("RIGHT") && this.checkCollisionToStillObjects(stillSymbols, "RIGHT")) {
                this.move(rightMove, "RIGHT");
            }

            if (BombermanGame.input.contains("DOWN") && this.checkCollisionToStillObjects(stillSymbols, "DOWN")) {
                this.move(downMove, "DOWN");
            }

            if (BombermanGame.input.contains("UP") && this.checkCollisionToStillObjects(stillSymbols, "UP")) {
                this.move(upMove, "UP");
            }

            if (BombermanGame.input.contains("SPACE")) {
                if (bomb == null) {
                    bomb = new Bomb((int) (this.getX() / Sprite.SCALED_SIZE), (int) (this.getY() / Sprite.SCALED_SIZE), Sprite.bomb.getFxImage());
                    BombermanGame.addStillObjects(bomb);
                }

            }
        } else {
            if (BombermanGame.input.contains("R")) {
                this.setDied(false);
                disapeared = false;
                this.setFrames(rightMove);
                this.setIndex(0);
                this.setX(Sprite.SCALED_SIZE);
                this.setY(Sprite.SCALED_SIZE);
                this.setPosition(Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);
            }

            if (BombermanGame.lose) {
                BombermanGame.removeEntities.add(this);
            }
        }

        if ((this.checkCollision(BombermanGame.textMap, "X", "LEFT", 0)
                || this.checkCollision(BombermanGame.textMap, "X", "RIGHT", 0)
                || this.checkCollision(BombermanGame.textMap, "X", "DOWN", 0)
                || this.checkCollision(BombermanGame.textMap, "X", "UP", 0))
            /*&& BombermanGame.numberOfEnemies == 0*/) {
            BombermanGame.win = true;
        }

        if (this.isDied()) {
            this.move(dieFrames, "");
        }

        if (bomb != null) {
            if (bomb.isExplosed()) {
                bombX = (int) (bomb.getX() / Sprite.SCALED_SIZE);
                bombY = (int) (bomb.getY() / Sprite.SCALED_SIZE);
                BombermanGame.removeStillObjects(bomb);
                bomb.setExplosing(true);
            }

            if (bomb.isExplosing()) {
                bomb.setExplosing(false);
                bomb = null;

                Explosion explosion = new Explosion(bombX, bombY);
                if (flamesEated > 0) {
                    flamesEated--;
                    explosion.setPowerUp(true);
                }

                explosion.update();
            }
        }
    }

    public void move(Image[] moves, String type) {
        this.setFrames(moves);
        if (this.getIndex() == 2) {
            if (type.equals("")) {
                disapeared = true;
                this.setIndex(3);
                souls--;
                BombermanGame.souls.set(souls);
            } else {
                this.setIndex(0);
            }
        } else {
            if (!disapeared) this.setIndex(this.getIndex() + 1);
        }
        if (type.equals("LEFT")) this.setX(this.getX() - speed);
        else if (type.equals("RIGHT")) this.setX(this.getX() + speed);
        else if (type.equals("DOWN")) this.setY(this.getY() + speed);
        else if (type.equals("UP")) this.setY(this.getY() - speed);
        this.setPosition(this.getX(), this.getY());
    }
}
