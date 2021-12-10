package entities.static_entity.Bomb;

import app.BombermanGame;
import entities.AnimatedImage;
import graphics.Sprite;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;

public class Explosion extends AnimatedImage {
    private int x, y;
    private int numberofCells = 1;
    private boolean powerUp = false;
    private MediaPlayer bombEplode = new MediaPlayer(new Media(BombermanGame.class.getResource("sound/BOM_11_M.wav").toExternalForm()));

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
        bombEplode.play();

        centerExplosion = new ChildExplosion(x, y, null, centerExplosionFrames);
        BombermanGame.stillObjects.add(centerExplosion);

        powerUp();

        addTailOfExplosion();

        handleBodyOfExplosion(horizontalRightExplosion);
        handleBodyOfExplosion(horizontalLeftExplosion);
        handleBodyOfExplosion(verticalDownExplosion);
        handleBodyOfExplosion(verticalTopExplosion);

        handleTailOfExplosion(topExplosion);
        handleTailOfExplosion(downExplosion);
        handleTailOfExplosion(leftExplosion);
        handleTailOfExplosion(rightExplosion);
    }

    public void powerUp() {
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
    }

    public void addTailOfExplosion() {
        if (y - numberofCells >= 0
                && BombermanGame.textMap.get(y - numberofCells).get(x).equals(" ")) topExplosion = new ChildExplosion(x, y - numberofCells, null, topExplosionFrames);
        if (BombermanGame.textMap.get(y + numberofCells).get(x).equals(" ")) downExplosion = new ChildExplosion(x, y + numberofCells, null, downExplosionFrames);
        if (x - numberofCells >= 0
                && BombermanGame.textMap.get(y).get(x - numberofCells).equals(" ")) leftExplosion = new ChildExplosion(x - numberofCells, y, null, leftExplosionFrames);
        if (BombermanGame.textMap.get(y).get(x + numberofCells).equals(" ")) rightExplosion = new ChildExplosion(x + numberofCells, y, null, rightExplosionFramse);
    }

    public void handleTailOfExplosion(ChildExplosion tailExplosion) {
        if (tailExplosion != null) {
            tailExplosion.setTail(true);
            BombermanGame.stillObjects.add(tailExplosion);
            if (tailExplosion.isFinished()) {
                BombermanGame.stillObjects.remove(tailExplosion);
                tailExplosion = null;
            }
        }
    }

    public void handleBodyOfExplosion(ArrayList<ChildExplosion> bodyExplosion) {
        if (bodyExplosion.size() != 0) {
            BombermanGame.stillObjects.addAll(bodyExplosion);
            for (ChildExplosion childExplosion: bodyExplosion) {
                if (childExplosion.isFinished()) {
                    BombermanGame.stillObjects.remove(childExplosion);
                }
            }
        }
    }
}
