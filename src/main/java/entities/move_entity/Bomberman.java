package entities.move_entity;

import app.BombermanGame;
import entities.AnimatedImage;
import entities.static_entity.Bomb.Bomb;
import entities.static_entity.Bomb.Explosion;
import graphics.Sprite;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;

public class Bomberman extends AnimatedImage {
    private static final int SPEED = 8;
    private boolean moving;
    private boolean disapeared = false;
    private int countToDisappeared = 0;
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
    }

    @Override
    public void update() {
        this.checkCollision(BombermanGame.entities);

        if (!this.isDied()) {
            if (BombermanGame.input.contains("LEFT")
                    && !this.checkCollision(BombermanGame.textMap, "#", "LEFT")
                    && !this.checkCollision(BombermanGame.textMap, "*", "LEFT")
                    && !this.checkCollision(BombermanGame.textMap, "x", "LEFT")
                    && !this.checkCollision(BombermanGame.textMap, "b", "LEFT")) {
                this.move(leftMove, "LEFT");
            }

            if (BombermanGame.input.contains("RIGHT")
                    && !this.checkCollision(BombermanGame.textMap, "#", "RIGHT")
                    && !this.checkCollision(BombermanGame.textMap, "*", "RIGHT")
                    && !this.checkCollision(BombermanGame.textMap, "x", "RIGHT")
                    && !this.checkCollision(BombermanGame.textMap, "b", "RIGHT")) {
                this.move(rightMove, "RIGHT");
            }

            if (BombermanGame.input.contains("DOWN")
                    && !this.checkCollision(BombermanGame.textMap, "#", "DOWN")
                    && !this.checkCollision(BombermanGame.textMap, "*", "DOWN")
                    && !this.checkCollision(BombermanGame.textMap, "x", "DOWN")) {
                this.move(downMove, "DOWN");
            }

            if (BombermanGame.input.contains("UP")
                    && !this.checkCollision(BombermanGame.textMap, "#", "UP")
                    && !this.checkCollision(BombermanGame.textMap, "*", "UP")
                    && !this.checkCollision(BombermanGame.textMap, "x", "UP")) {
                this.move(upMove, "UP");
            }

            if (BombermanGame.input.contains("SPACE")) {
                if (bomb == null) {
                    bomb = new Bomb((int)(this.getX() / Sprite.SCALED_SIZE), (int)(this.getY() / Sprite.SCALED_SIZE), Sprite.bomb.getFxImage());
                    BombermanGame.addStillObjects(bomb);
                }

            }
        } else {
            if (BombermanGame.input.contains("R")) {
                this.setDied(false);
                countToDisappeared = 0;
                this.setFrames(rightMove);
                this.setIndex(0);
                this.setX(Sprite.SCALED_SIZE);
                this.setY(Sprite.SCALED_SIZE);
                this.setPosition(Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);
            }
        }

        if ((this.checkCollision(BombermanGame.textMap, "X", "LEFT")
                || this.checkCollision(BombermanGame.textMap, "X", "RIGHT")
                || this.checkCollision(BombermanGame.textMap, "X", "DOWN")
                || this.checkCollision(BombermanGame.textMap, "X", "UP"))
            /*&& BombermanGame.entities.size() == 1*/) {
            BombermanGame.win = true;
        }

        if (this.isDied()) {
                this.move(dieFrames, "");
                disapeared = false;
        }

        if (bomb != null) {
            if (bomb.isExplosed()) {
                bombX = (int)(bomb.getX() / Sprite.SCALED_SIZE);
                bombY = (int)(bomb.getY() / Sprite.SCALED_SIZE);
                BombermanGame.removeStillObjects(bomb);
                bomb.setExplosing(true);
            }

            if (bomb.isExplosing()) {
                bomb.setExplosing(false);
                bomb = null;

                Explosion explosion = new Explosion(bombX, bombY);
                explosion.update();
            }
        }
    }

    public void move(Image[] moves, String type) {
        this.setFrames(moves);
        if (this.getIndex() >= 2) {
            if (type.equals("")) {
                countToDisappeared++;
                if (countToDisappeared == 1) {
                    disapeared = true;
                    this.setIndex(3);
                    BombermanGame.souls.set(BombermanGame.souls.getValue() - 1);
                }
            } else {
                this.setIndex(0);
            }
        } else {
            if (!disapeared) this.setIndex(this.getIndex() + 1);
        }
        if (type.equals("LEFT")) this.setX(this.getX() - SPEED);
        else if (type.equals("RIGHT")) this.setX(this.getX() + SPEED);
        else if (type.equals("DOWN")) this.setY(this.getY() + SPEED);
        else if (type.equals("UP")) this.setY(this.getY() - SPEED);
        this.setPosition(this.getX(), this.getY());
    }
}
