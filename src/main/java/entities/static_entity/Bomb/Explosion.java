package entities.static_entity.Bomb;

import app.BombermanGame;
import entities.AnimatedImage;
import graphics.Sprite;
import javafx.scene.image.Image;

public class Explosion extends AnimatedImage {
    private int x, y;

    private Image[] topExplosionFrames = {Sprite.explosion_vertical_top_last.getFxImage(), Sprite.explosion_vertical_top_last1.getFxImage(), Sprite.explosion_vertical_top_last2.getFxImage(), null};
    private Image[] downExplosionFrames = {Sprite.explosion_vertical_down_last.getFxImage(), Sprite.explosion_vertical_down_last1.getFxImage(), Sprite.explosion_vertical_down_last2.getFxImage(), null};
    private Image[] centerExplosionFrames = {Sprite.bomb_exploded.getFxImage(), Sprite.bomb_exploded1.getFxImage(), Sprite.bomb_exploded2.getFxImage(), null};
    private Image[] leftExplosionFrames = {Sprite.explosion_horizontal_left_last.getFxImage(), Sprite.explosion_horizontal_left_last1.getFxImage(), Sprite.explosion_horizontal_left_last2.getFxImage(), null};
    private Image[] rightExplosionFramse = {Sprite.explosion_horizontal_right_last.getFxImage(), Sprite.explosion_horizontal1.getFxImage(), Sprite.explosion_horizontal_right_last2.getFxImage(), null};

    private ChildExplosion topExplosion;
    private ChildExplosion downExplosion;
    private ChildExplosion leftExplosion;
    private ChildExplosion rightExplosion;
    private ChildExplosion centerExplosion;

    public Explosion(int x, int y) {
        this.x = x;
        this.y = y;

        if (BombermanGame.textMap.get(y - 1).get(x).equals(" ")) topExplosion = new ChildExplosion(x, y - 1, null, topExplosionFrames);
        if (BombermanGame.textMap.get(y + 1).get(x).equals(" ")) downExplosion = new ChildExplosion(x, y + 1, null, downExplosionFrames);
        centerExplosion = new ChildExplosion(x, y, null, centerExplosionFrames);
        if (BombermanGame.textMap.get(y).get(x - 1).equals(" ")) leftExplosion = new ChildExplosion(x - 1, y, null, leftExplosionFrames);
        if (BombermanGame.textMap.get(y).get(x + 1).equals(" ")) rightExplosion = new ChildExplosion(x + 1, y, null, rightExplosionFramse);

        BombermanGame.addStillObjects(centerExplosion);
        BombermanGame.textMap.get(y).set(x, "-1");
    }

    public void update() {
        if (topExplosion != null) {
            topExplosion.setTail(true);
            BombermanGame.addStillObjects(topExplosion);
            BombermanGame.textMap.get(y - 1).set(x, "-1");
            if (topExplosion.isFinished()) {
                BombermanGame.removeStillObjects(topExplosion);
                topExplosion = null;
            }
        }

        if (downExplosion != null) {
            downExplosion.setTail(true);
            BombermanGame.addStillObjects(downExplosion);
            BombermanGame.textMap.get(y + 1).set(x, "-1");
            if (downExplosion.isFinished()) {
                BombermanGame.removeStillObjects(downExplosion);
                downExplosion = null;
            }
        }

        if (leftExplosion != null) {
            leftExplosion.setTail(true);
            BombermanGame.addStillObjects(leftExplosion);
            BombermanGame.textMap.get(y).set(x - 1, "-1");
            if (leftExplosion.isFinished()) {
                BombermanGame.removeStillObjects(leftExplosion);
                leftExplosion = null;
            }
        }

        if (rightExplosion != null) {
            rightExplosion.setTail(true);
            BombermanGame.addStillObjects(rightExplosion);
            BombermanGame.textMap.get(y).set(x + 1, "-1");
            if (rightExplosion.isFinished()) {
                BombermanGame.removeStillObjects(rightExplosion);
                rightExplosion = null;
            }
        }
    }
}
