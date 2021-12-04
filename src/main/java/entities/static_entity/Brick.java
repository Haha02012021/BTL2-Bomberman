package entities.static_entity;

import app.BombermanGame;
import entities.AnimatedImage;
import graphics.Sprite;
import javafx.scene.image.Image;

public class Brick extends AnimatedImage {
    private int countToDisappear = 0;
    private boolean isExploded = false;
    private boolean disappeared = false;
    private boolean hiddenPortal = true;
    private boolean hiddenPSpeed = true;
    private boolean hiddenPFlame = true;
    private boolean hiddenPDetonation = true;
    private Image[] brickFrames = {
            Sprite.brick_exploded.getFxImage(),
            Sprite.brick_exploded1.getFxImage(),
            Sprite.brick_exploded2.getFxImage(),
            Sprite.grass.getFxImage(),
            Sprite.brick.getFxImage(),
            Sprite.powerup_flamepass.getFxImage(),
            Sprite.powerup_flames.getFxImage(),
            Sprite.powerup_detonator.getFxImage()
    };

    public boolean isHiddenPortal() {
        return hiddenPortal;
    }

    public void setHiddenPortal(boolean hiddenPortal) {
        this.hiddenPortal = hiddenPortal;
    }

    public boolean isExploded() {
        return isExploded;
    }

    public void setExploded(boolean exploded) {
        isExploded = exploded;
    }

    public boolean isHiddenPSpeed() {
        return hiddenPSpeed;
    }

    public boolean isHiddenPFlame() {
        return hiddenPFlame;
    }

    public void setHiddenPFlame(boolean hiddenPFlame) {
        this.hiddenPFlame = hiddenPFlame;
    }

    public void setHiddenPSpeed(boolean hiddenPSpeed) {
        this.hiddenPSpeed = hiddenPSpeed;
    }

    public boolean isHiddenPDetonation() {
        return hiddenPDetonation;
    }

    public void setHiddenPDetonation(boolean hiddenPDetonation) {
        this.hiddenPDetonation = hiddenPDetonation;
    }

    public Brick(int x, int y, Image img) {
        super(x, y, img);
        this.setFrames(brickFrames);
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        if (isExploded) {
            if (this.getIndex() >= 3) {
                if (hiddenPortal && hiddenPSpeed && hiddenPFlame && hiddenPDetonation) {
                    this.setIndex(3);
                    this.setImg(brickFrames[3]);
                    BombermanGame.removeEntities.add(this);
                } else {
                    if (!hiddenPortal) {
                        BombermanGame.removeEntities.add(this);
                    }

                    if (!hiddenPSpeed) {
                        this.setIndex(5);
                        this.setImg(brickFrames[5]);
                    }

                    if (!hiddenPFlame) {
                        this.setIndex(6);
                        this.setImg(brickFrames[6]);
                    }

                    if (!hiddenPDetonation) {
                        this.setIndex(7);
                        this.setImg(brickFrames[7]);
                    }
                }

                countToDisappear++;
                if (countToDisappear == 1) {
                    disappeared = true;
                }
            } else {
                if (!disappeared) this.setIndex(this.getIndex() + 1);
            }
        } else {
            this.setIndex(4);
        }
    }
}
