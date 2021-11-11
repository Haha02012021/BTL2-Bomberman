package entities.move_entity;

import app.BombermanGame;
import entities.AnimatedImage;
import entities.static_entity.Bomb.Bomb;
import entities.static_entity.Bomb.Explosion;
import graphics.Sprite;
import javafx.scene.image.Image;

public class Bomberman extends AnimatedImage {
    private static final int SPEED = 8;
    private boolean moving;
    Bomb bomb = null;
    private Image[] leftMove = {Sprite.player_left.getFxImage(), Sprite.player_left_1.getFxImage(), Sprite.player_left_2.getFxImage()};
    private Image[] rightMove = {Sprite.player_right.getFxImage(), Sprite.player_right_1.getFxImage(), Sprite.player_right_2.getFxImage()};
    private Image[] downMove = {Sprite.player_down.getFxImage(), Sprite.player_down_1.getFxImage(), Sprite.player_down_2.getFxImage()};
    private Image[] upMove = {Sprite.player_up.getFxImage(), Sprite.player_up_1.getFxImage(), Sprite.player_up_2.getFxImage()};

    private Image[] topExplosionFrames = {Sprite.explosion_vertical_top_last.getFxImage(), Sprite.explosion_vertical_top_last1.getFxImage(), Sprite.explosion_vertical_top_last2.getFxImage()};
    private Image[] downExplosionFrames = {Sprite.explosion_vertical_down_last.getFxImage(), Sprite.explosion_vertical_down_last1.getFxImage(), Sprite.explosion_vertical_down_last2.getFxImage()};
    private Image[] centerExplosionFrames = {Sprite.bomb_exploded.getFxImage(), Sprite.bomb_exploded1.getFxImage(), Sprite.bomb_exploded2.getFxImage()};

    private int bombX, bombY;

    public Bomberman(int x, int y, Image fxImage) {
        super(x, y, fxImage);
        this.setPosition(x * Sprite.SCALED_SIZE, y * Sprite.SCALED_SIZE);
        this.setFrames(rightMove);
    }

    @Override
    public void update() {
        if (BombermanGame.input.contains("LEFT")
                && !this.checkCollision(BombermanGame.textMap, "#", "LEFT")
                && !this.checkCollision(BombermanGame.textMap, "*", "LEFT")
                && !this.checkCollision(BombermanGame.textMap, "x", "LEFT")) {
            this.move(leftMove, "LEFT");
        }

        if (BombermanGame.input.contains("RIGHT")
                && !this.checkCollision(BombermanGame.textMap, "#", "RIGHT")
                && !this.checkCollision(BombermanGame.textMap, "*", "RIGHT")
                && !this.checkCollision(BombermanGame.textMap, "x", "RIGHT")) {
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
            this.setIndex(0);
        } else this.setIndex(this.getIndex() + 1);
        if (type.equals("LEFT")) this.setX(this.getX() - SPEED);
        else if (type.equals("RIGHT")) this.setX(this.getX() + SPEED);
        else if (type.equals("DOWN")) this.setY(this.getY() + SPEED);
        else this.setY(this.getY() - SPEED);
        this.setPosition(this.getX(), this.getY());
    }
}
