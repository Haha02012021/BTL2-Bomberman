package entities.static_entity.Bomb;

import app.BombermanGame;
import entities.AnimatedImage;
import graphics.Sprite;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class Explosion extends AnimatedImage {
    private int x, y;
    private int numberofCells = 1;
    private boolean powerUp = false;

    private Image[] topExplosionFrames = {Sprite.explosion_vertical_top_last.getFxImage(), Sprite.explosion_vertical_top_last1.getFxImage(), Sprite.explosion_vertical_top_last2.getFxImage(), null};
    private Image[] downExplosionFrames = {Sprite.explosion_vertical_down_last.getFxImage(), Sprite.explosion_vertical_down_last1.getFxImage(), Sprite.explosion_vertical_down_last2.getFxImage(), null};
    private Image[] centerExplosionFrames = {Sprite.bomb_exploded.getFxImage(), Sprite.bomb_exploded1.getFxImage(), Sprite.bomb_exploded2.getFxImage(), null};
    private Image[] leftExplosionFrames = {Sprite.explosion_horizontal_left_last.getFxImage(), Sprite.explosion_horizontal_left_last1.getFxImage(), Sprite.explosion_horizontal_left_last2.getFxImage(), null};
    private Image[] rightExplosionFramse = {Sprite.explosion_horizontal_right_last.getFxImage(), Sprite.explosion_horizontal_right_last1.getFxImage(), Sprite.explosion_horizontal_right_last2.getFxImage(), null};

    private Image[] explosionHorizontalFrames = {Sprite.explosion_horizontal.getFxImage(), Sprite.explosion_horizontal1.getFxImage(), Sprite.explosion_horizontal2.getFxImage(), null};
    private Image[] explosionVerticalFrames = {Sprite.explosion_vertical.getFxImage(), Sprite.explosion_vertical1.getFxImage(), Sprite.explosion_vertical2.getFxImage(), null};

    private ArrayList<ChildExplosion> horizontalLeftExplosion = new ArrayList<>();
    private ArrayList<ChildExplosion> horizontalRightExplosion = new ArrayList<>();
    private ArrayList<ChildExplosion> verticalTopExplosion = new ArrayList<>();
    private ArrayList<ChildExplosion> verticalDownExplosion = new ArrayList<>();

    private ChildExplosion topExplosion;
    private ChildExplosion downExplosion;
    private ChildExplosion leftExplosion;
    private ChildExplosion rightExplosion;
    private ChildExplosion centerExplosion;

    public Explosion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isPowerUp() {
        return powerUp;
    }

    public void setPowerUp(boolean powerUp) {
        this.powerUp = powerUp;
    }

    public int getNumberofCells() {
        return numberofCells;
    }

    public void setNumberofCells(int numberofCells) {
        this.numberofCells = numberofCells;
    }

    public void update() {
        centerExplosion = new ChildExplosion(x, y, null, centerExplosionFrames);
        if (this.isPowerUp()) {
            for (int i = 1; i <= numberofCells; i++) {
                if (x - i >= 0
                        && BombermanGame.textMap.get(y).get(x - i).equals(" "))
                    horizontalLeftExplosion.add(new ChildExplosion(x - i, y, null, explosionHorizontalFrames));
                if (BombermanGame.textMap.get(y).get(x + i).equals(" "))
                    horizontalRightExplosion.add(new ChildExplosion(x + i, y, null, explosionHorizontalFrames));
                if (BombermanGame.textMap.get(y + i).get(x).equals(" "))
                    verticalDownExplosion.add(new ChildExplosion(x, y + i, null, explosionVerticalFrames));
                if (y - i >= 0
                        && BombermanGame.textMap.get(y - i).get(x).equals(" "))
                    verticalTopExplosion.add(new ChildExplosion(x, y - i, null, explosionVerticalFrames));
            }
            numberofCells++;
        }
        if (y - numberofCells >= 0
                && BombermanGame.textMap.get(y - numberofCells).get(x).equals(" ")) topExplosion = new ChildExplosion(x, y - numberofCells, null, topExplosionFrames);
        if (BombermanGame.textMap.get(y + numberofCells).get(x).equals(" ")) downExplosion = new ChildExplosion(x, y + numberofCells, null, downExplosionFrames);
        if (x - numberofCells >= 0
                && BombermanGame.textMap.get(y).get(x - numberofCells).equals(" ")) leftExplosion = new ChildExplosion(x - numberofCells, y, null, leftExplosionFrames);
        if (BombermanGame.textMap.get(y).get(x + numberofCells).equals(" ")) rightExplosion = new ChildExplosion(x + numberofCells, y, null, rightExplosionFramse);

        BombermanGame.stillObjects.add(centerExplosion);

        if (horizontalRightExplosion.size() != 0) {
            BombermanGame.stillObjects.addAll(horizontalRightExplosion);
            for (ChildExplosion childExplosion: horizontalRightExplosion) {
                if (childExplosion.isFinished()) {
                    BombermanGame.stillObjects.remove(childExplosion);
                    childExplosion = null;
                }
            }
        }

        if (horizontalLeftExplosion.size() != 0) {
            BombermanGame.stillObjects.addAll(horizontalLeftExplosion);
            for (ChildExplosion childExplosion: horizontalLeftExplosion) {
                if (childExplosion.isFinished()) {
                    BombermanGame.stillObjects.remove(childExplosion);
                    childExplosion = null;
                }
            }
        }

        if (verticalDownExplosion.size() != 0) {
            BombermanGame.stillObjects.addAll(verticalDownExplosion);
            for (ChildExplosion childExplosion: verticalDownExplosion) {
                if (childExplosion.isFinished()) {
                    BombermanGame.stillObjects.remove(childExplosion);
                    childExplosion = null;
                }
            }
        }

        if (verticalTopExplosion.size() != 0) {
            BombermanGame.stillObjects.addAll(verticalTopExplosion);
            for (ChildExplosion childExplosion: verticalTopExplosion) {
                if (childExplosion.isFinished()) {
                    BombermanGame.stillObjects.remove(childExplosion);
                    childExplosion = null;
                }
            }
        }

        if (topExplosion != null) {
            topExplosion.setTail(true);
            BombermanGame.stillObjects.add(topExplosion);
            BombermanGame.textMap.get(y - numberofCells).set(x, "-1");
            if (topExplosion.isFinished()) {
                BombermanGame.stillObjects.remove(topExplosion);
                topExplosion = null;
            }
        }

        if (downExplosion != null) {
            downExplosion.setTail(true);
            BombermanGame.stillObjects.add(downExplosion);
            BombermanGame.textMap.get(y + numberofCells).set(x, "-1");
            if (downExplosion.isFinished()) {
                BombermanGame.stillObjects.remove(downExplosion);
                downExplosion = null;
            }
        }

        if (leftExplosion != null) {
            leftExplosion.setTail(true);
            BombermanGame.stillObjects.add(leftExplosion);
            BombermanGame.textMap.get(y).set(x - numberofCells, "-1");
            if (leftExplosion.isFinished()) {
                BombermanGame.stillObjects.remove(leftExplosion);
                leftExplosion = null;
            }
        }

        if (rightExplosion != null) {
            rightExplosion.setTail(true);
            BombermanGame.stillObjects.add(rightExplosion);
            BombermanGame.textMap.get(y).set(x + numberofCells, "-1");
            if (rightExplosion.isFinished()) {
                BombermanGame.stillObjects.remove(rightExplosion);
                rightExplosion = null;
            }
        }
    }
}
