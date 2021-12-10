package entities.move_entity;

import java.io.FileReader;
import java.sql.Time;

import app.BombermanGame;
import entities.AnimatedImage;
import entities.static_entity.Bomb.Bomb;
import entities.static_entity.Bomb.Explosion;
import graphics.Sprite;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

public class Bomberman extends AnimatedImage {
    private boolean disapeared = false;
    private boolean eatedFlames = false;
    private boolean eatedFlamePass = false;
    private boolean eatedDetonator = false;
    private boolean eatedBombs = false;
    private int flamesEated = 0;
    private int flamePassEated = 0;
    private long flamePassStart = 900_000;
    private int souls = 3;
    private Bomb bomb = null;
    private boolean playMusic = true;
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
            Sprite.player_dead1.getFxImage(),
            Sprite.player_dead1.getFxImage(),
            Sprite.player_dead1.getFxImage(),
            Sprite.player_dead2.getFxImage(),
            Sprite.player_dead2.getFxImage(),
            Sprite.player_dead2.getFxImage(),
            Sprite.player_dead3.getFxImage(),
            Sprite.player_dead3.getFxImage(),
            null
    };
    private int bombX, bombY;

    public Bomberman(int x, int y, Image fxImage) {
        super(x, y, fxImage);
        this.setPosition(x * Sprite.SCALED_SIZE, y * Sprite.SCALED_SIZE);
        this.setFrames(rightMove);
        this.setSpeed(12);
    }

    public boolean isEatedDetonator() {
        return eatedDetonator;
    }

    public void setEatedDetonator(boolean eatedDetonator) {
        this.eatedDetonator = eatedDetonator;
    }

    public boolean isEatedBombs() {
        return eatedBombs;
    }

    public void setEatedBombs(boolean eatedBombs) {
        this.eatedBombs = eatedBombs;
    }

    public boolean isEatedFlamePass() {
        return eatedFlamePass;
    }

    public void setEatedFlamePass(boolean eatedFlamePass) {
        this.eatedFlamePass = eatedFlamePass;
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

    @Override
    public void update() {
        this.checkCollision(BombermanGame.entities);

        if (!this.isDied() || flamePassEated != 0) {
            //handle eat item
            eatItem();

            //handle move follow direct
            moveFollowDirect("LEFT", leftMove);

            moveFollowDirect("RIGHT", rightMove);

            moveFollowDirect("DOWN", downMove);

            moveFollowDirect("UP", upMove);

            //put bomb
            putBomb();
        } else {
            //handle main dead
            mainDead();

            //main revival
            mainReviral();

            //you lose
            if (BombermanGame.lose) {
                BombermanGame.removeEntities.add(this);
            }
        }

        //handle meet portal
        meetPortal();

        if (bomb != null) {
            //handle active bomb
            activeBomb();
        }
        
    }

    //move
    public void move(Image[] moves, String type) {
        this.setFrames(moves);
        if (this.getIndex() == moves.length - 2) {
            if (type.equals("")) {
                this.setIndex(moves.length - 1);
                souls--;
                BombermanGame.souls.set(souls);
                disapeared = true;
            } else {
                this.setIndex(0);
            }
        } else {
            if (!disapeared) {
                this.setIndex(this.getIndex() + 1);
            }
        }
        if (type.equals("LEFT")) this.setX(this.getX() - this.getSpeed());
        else if (type.equals("RIGHT")) this.setX(this.getX() + this.getSpeed());
        else if (type.equals("DOWN")) this.setY(this.getY() + this.getSpeed());
        else if (type.equals("UP")) this.setY(this.getY() - this.getSpeed());
        this.setPosition(this.getX(), this.getY());
    }

    public void moveFollowDirect(String type, Image[] move) {
        if (BombermanGame.inputKey.contains(type) && this.checkCollisionToStillObjects(stillSymbols, type)) {
            MediaPlayer footStepSound = new MediaPlayer(new Media(BombermanGame.class.getResource("sound/footstep.mp3").toExternalForm()));
            footStepSound.play();
            this.move(move, type);
        }
    }

    //bomb
    public void putBomb() {
        if (BombermanGame.inputKey.contains("SPACE")) {
            int intX = (int) (this.getX() / Sprite.SCALED_SIZE);
            int intY = (int) (this.getY() / Sprite.SCALED_SIZE);
            if (bomb == null && !BombermanGame.textMap.get(intY).get(intX).equals("X")) {
                MediaPlayer putBombSound = new MediaPlayer(new Media(BombermanGame.class.getResource("sound/setbom.wav").toExternalForm()));
                putBombSound.play();
                bomb = new Bomb(intX, intY,
                        Sprite.bomb.getFxImage());
                BombermanGame.stillObjects.add(bomb);
                BombermanGame.textMap.get(intY).set(intX, "m");
            }
        }
    }

    public void activeBomb() {
        if (bomb.isExplosed()) {
            bombX = (int) (bomb.getX() / Sprite.SCALED_SIZE);
            bombY = (int) (bomb.getY() / Sprite.SCALED_SIZE);
            BombermanGame.stillObjects.remove(bomb);
            BombermanGame.textMap.get(bombY).set(bombX, " ");
            bomb.setExplosing(true);
        }

        if (bomb.isExplosing()) {
            bomb.setExplosing(false);
            bomb = null;

            Explosion explosion = new Explosion(bombX, bombY);
            if (flamesEated > 0) {
                flamesEated--;
                explosion.setPowerUp(true);
                if (flamesEated / 3 > 0) {
                    explosion.setNumberofCells(flamesEated / 3 + 1);
                }
            }

            explosion.update();
        }
    }

    //main
    public void mainReviral() {
        if (BombermanGame.inputKey.contains("R") && disapeared && !playMusic) {
            this.setDied(false);
            disapeared = false;
            playMusic = true;
            this.setFrames(rightMove);
            this.setIndex(0);
            this.setX(Sprite.SCALED_SIZE * BombermanGame.startManX);
            this.setY(Sprite.SCALED_SIZE * BombermanGame.startManY);
            this.setPosition(this.getX(), this.getY());
        }
    }

    public void mainDead() {
        if (this.isDied()) {
            if (System.currentTimeMillis() - flamePassStart >= 20_000) {
                if (playMusic) {
                    MediaPlayer mainDeadSound = new MediaPlayer(new Media(BombermanGame.class.getResource("sound/bomber_die.wav").toExternalForm()));
                    mainDeadSound.play();
                    playMusic = false;
                }
                flamePassEated = 0;
                this.move(dieFrames, "");
                flamePassStart = 900_000;
            } else {
                this.setDied(false);
            }
        }
    }

    //action
    public void meetPortal() {
        if (BombermanGame.textMap.get((int) this.getY() / Sprite.SCALED_SIZE).get((int) this.getX() / Sprite.SCALED_SIZE).equals("X")
            && BombermanGame.numberOfEnemies == 0) {
            BombermanGame.win = true;
            this.setFrames(upMove);
            this.setIndex(0);
            MediaPlayer winSound = new MediaPlayer(new Media(BombermanGame.class.getResource("sound/wingame.mp3").toExternalForm()));
            winSound.play();
        }
    }

    public void eatItem() {
        MediaPlayer eatItemSound = new MediaPlayer(new Media(BombermanGame.class.getResource("sound/Item.wav").toExternalForm()));
        if (this.isEatedDetonator()) {
            eatItemSound.play();
            souls += 1;
            BombermanGame.souls.set(souls);
            this.setEatedDetonator(false);
        }

        if (this.isEatedFlamePass()) {
            eatItemSound.play();
            flamePassStart = System.currentTimeMillis();
            flamePassEated = 1;
            this.setEatedFlamePass(false);
        }

        if (this.isEatedFlames()) {
            eatItemSound.play();
            this.setFlamesEated(this.getFlamesEated() + 3);
            this.setEatedFlames(false);
        }

        if (this.isEatedBombs()) {
            eatItemSound.play();
            this.setEatedBombs(false);
        }
    }

}
