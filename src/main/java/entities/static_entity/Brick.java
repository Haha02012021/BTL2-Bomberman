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
    private boolean hiddenPBomb = true;
    private Image[] brickFrames = {
            Sprite.brick_exploded.getFxImage(),
            Sprite.brick_exploded1.getFxImage(),
            Sprite.brick_exploded2.getFxImage(),
            Sprite.grass.getFxImage(),
            Sprite.portal.getFxImage(),
            Sprite.powerup_bombs.getFxImage(),
            Sprite.brick.getFxImage()};

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

    public boolean isHiddenPBomb() {
        return hiddenPBomb;
    }

    public void setHiddenPBomb(boolean hiddenPBomb) {
        this.hiddenPBomb = hiddenPBomb;
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
                if (hiddenPortal && hiddenPBomb) {
                    this.setIndex(3);
                    this.setImg(brickFrames[3]);
                    BombermanGame.removeEntities.add(this);
                } else {
                    if (!hiddenPortal) {
                        this.setIndex(4);
                        this.setImg(brickFrames[4]);
                    } else if (!hiddenPBomb) {
                        this.setIndex(5);
                        this.setImg(brickFrames[5]);
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
            this.setIndex(6);
        }
    }
}
