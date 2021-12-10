package entities.move_entity.enemies;

import app.BombermanGame;
import entities.AnimatedImage;
import graphics.Sprite;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Enemy extends AnimatedImage {
    protected final static int SPEED_ENEMY = 8;
    private int score;
    private int numberOfFrames;
    private int countToDisappear = 0;
    private boolean disappeared = false;
    private int defaultIndex = 0;
    private Image[] dieFrames = {null};
    protected boolean moveDown = false;
    protected boolean moveUp = false;
    protected boolean moveRight = false;
    protected boolean moveLeft = false;
    protected int random = (int)(Math.random() * 4) + 1;
    protected int prevRandom;
    protected String[] stillSymbols = {"#", "*", "x", "s", "f", "d", "b", "F", "S", "D", "B", "m"};
    public Enemy(int x, int y, Image img) {
        super(x, y, img);
        this.setPosition(x * Sprite.SCALED_SIZE, y * Sprite.SCALED_SIZE);
        BombermanGame.textMap.get(y).set(x, " ");
        this.setSpeed(SPEED_ENEMY);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNumberOfFrames() {
        return numberOfFrames;
    }

    public int getDefaultIndex() {
        return defaultIndex;
    }

    public void setDefaultIndex(int defaultIndex) {
        this.defaultIndex = defaultIndex;
    }

    public void move() {
        if (random > 0) {
            prevRandom = random;
            if (random == 1) {
                moveRight = false;
                moveLeft = true;
                moveDown = false;
                moveUp = false;
                this.setDefaultIndex(0);
                this.setNumberOfFrames(2);
            } else if (random == 2) {
                moveRight = true;
                moveLeft = false;
                moveDown = false;
                moveUp = false;
                this.setDefaultIndex(3);
                this.setNumberOfFrames(5);
            } else if (random == 3) {
                moveRight = false;
                moveLeft = false;
                moveDown = true;
                moveUp = false;
            } else {
                moveRight = false;
                moveLeft = false;
                moveDown = false;
                moveUp = true;
            }

            random = 0;
        }
    }

    public void setNumberOfFrames(int numberOfFrames) {
        this.numberOfFrames = numberOfFrames;
    }

    public void setDieFrames(Image[] dieFrames) {
        this.dieFrames = dieFrames;
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        if (this.isDied()) {
            MediaPlayer enemyDeadSound = new MediaPlayer(new Media(BombermanGame.class.getResource("sound/enemy_die.wav").toExternalForm()));
            enemyDeadSound.play();
            this.setFrames(dieFrames);
            numberOfFrames = 10;
            defaultIndex = 11;
        } else {
            move();
        }
        if (this.getIndex() >= numberOfFrames) {
            this.setIndex(defaultIndex);
            if (this.isDied()) {
                countToDisappear++;
                if (countToDisappear == 1) {
                    disappeared = true;
                    BombermanGame.score.set(BombermanGame.score.get() + this.getScore());
                    BombermanGame.removeEntities.add(this);
                    BombermanGame.numberOfEnemies--;
                }
            }
        } else {
            if (!disappeared) {
                this.setIndex(this.getIndex() + 1);
            }
        }
    }
}
