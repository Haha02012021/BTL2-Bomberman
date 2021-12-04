package entities.move_entity;

import app.BombermanGame;
import entities.AnimatedImage;
import entities.static_entity.Bomb.Bomb;
import entities.static_entity.Bomb.Explosion;
import graphics.Sprite;
import javafx.scene.image.Image;

public class Bomberman extends AnimatedImage {
    private boolean moving;
    private String status;
    private boolean disapeared = false;
    private boolean eatedFlames = false;
    private boolean eatedFlamePass = false;
    private boolean eatedDetonator = false;
    private int flamesEated = 0;
    private int flamePassEated = 0;
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
        this.setSpeed(12);
    }

    public boolean isEatedDetonator() {
        return eatedDetonator;
    }

    public void setEatedDetonator(boolean eatedDetonator) {
        this.eatedDetonator = eatedDetonator;
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

    public int getFlamePassEated() {
        return flamePassEated;
    }

    public void setFlamePassEated(int flamePassEated) {
        this.flamePassEated = flamePassEated;
    }

    @Override
    public void update() {
        this.checkCollision(BombermanGame.entities);

        if (!this.isDied()) {
            if (this.isEatedDetonator()) {
                souls += 1;
                BombermanGame.souls.set(souls);
                this.setEatedDetonator(false);
            }

            if (this.isEatedFlamePass()) {
                this.setFlamePassEated(this.getFlamesEated() + 3 * 3);
                this.setEatedFlamePass(false);
            }

            if (this.isEatedFlames()) {
                this.setFlamesEated(this.getFlamesEated() + 3);
                this.setEatedFlames(false);
            }

            if (BombermanGame.inputKey.contains("LEFT") && this.checkCollisionToStillObjects(stillSymbols, "LEFT")) {
                this.move(leftMove, "LEFT");
                status = "LEFT";
            }

            if (BombermanGame.inputKey.contains("RIGHT") && this.checkCollisionToStillObjects(stillSymbols, "RIGHT")) {
                this.move(rightMove, "RIGHT");
                status = "RIGHT";
            }

            if (BombermanGame.inputKey.contains("DOWN") && this.checkCollisionToStillObjects(stillSymbols, "DOWN")) {
                this.move(downMove, "DOWN");
                status = "DOWN";
            }

            if (BombermanGame.inputKey.contains("UP") && this.checkCollisionToStillObjects(stillSymbols, "UP")) {
                this.move(upMove, "UP");
                status = "UP";
            }

            if (BombermanGame.inputKey.contains("SPACE")) {
                int intX = (int) (this.getX() / Sprite.SCALED_SIZE);
                int intY = (int) (this.getY() / Sprite.SCALED_SIZE);
                if (bomb == null) {
                    bomb = new Bomb(intX, intY,
                            Sprite.bomb.getFxImage());
                    BombermanGame.stillObjects.add(bomb);
                }

            }
        } else {
            if (BombermanGame.inputKey.contains("R")) {
                this.setDied(false);
                disapeared = false;
                this.setFrames(rightMove);
                this.setIndex(0);
                this.setX(Sprite.SCALED_SIZE * BombermanGame.startManX);
                this.setY(Sprite.SCALED_SIZE * BombermanGame.startManY);
                this.setPosition(this.getX(), this.getY());
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
            if (flamePassEated <= 0) this.move(dieFrames, "");
            else {
                this.setDied(false);
                flamePassEated--;
            }
        }

        if (bomb != null) {
            if (bomb.isExplosed()) {
                bombX = (int) (bomb.getX() / Sprite.SCALED_SIZE);
                bombY = (int) (bomb.getY() / Sprite.SCALED_SIZE);
                BombermanGame.stillObjects.remove(bomb);
                bomb.setExplosing(true);
            }

            if (bomb.isExplosing()) {
                bomb.setExplosing(false);
                bomb = null;

                Explosion explosion = new Explosion(bombX, bombY);
                if (flamesEated > 0) {
                    System.out.println(flamesEated);
                    flamesEated--;
                    explosion.setPowerUp(true);
                    if (flamesEated / 3 > 0) {
                        explosion.setNumberofCells(flamesEated / 3 + 1);
                    }
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
        if (type.equals("LEFT")) this.setX(this.getX() - this.getSpeed());
        else if (type.equals("RIGHT")) this.setX(this.getX() + this.getSpeed());
        else if (type.equals("DOWN")) this.setY(this.getY() + this.getSpeed());
        else if (type.equals("UP")) this.setY(this.getY() - this.getSpeed());
        this.setPosition(this.getX(), this.getY());
    }
}
