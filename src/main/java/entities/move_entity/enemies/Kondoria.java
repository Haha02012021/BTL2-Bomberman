package entities.move_entity.enemies;

import app.BombermanGame;
import graphics.Sprite;
import javafx.scene.Parent;
import javafx.scene.image.Image;

import java.util.*;

public class Kondoria extends Doll {
    private int manX = 0;
    private int manY = 0;
    private boolean canToMain = false;

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
            Sprite.kondoria_dead.getFxImage(),
            Sprite.kondoria_dead.getFxImage(),
            Sprite.mob_dead1.getFxImage(),
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

    @Override
    public void findNextPath() {
        if (((manX != (int) BombermanGame.bomberman.getY() / Sprite.SCALED_SIZE
                && manY != (int) BombermanGame.bomberman.getX() / Sprite.SCALED_SIZE))
                || BombermanGame.bomberman.isDied() || path.size() <= 1) {

        }
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
                manX = 0;
                manY = 0;
            } else {
                path = shortestPath;
            }
        }
    }

    @Override
    public void move() {
        super.move();
    }
}