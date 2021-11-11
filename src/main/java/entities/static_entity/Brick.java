package entities.static_entity;

import entities.AnimatedImage;
import graphics.Sprite;
import javafx.scene.image.Image;

public class Brick extends AnimatedImage {
    private int countToDisappear = 0;
    private boolean isExploded = false;
    private boolean disappeared = false;
    private boolean hidden = true;
    private Image[] brickFrames = {
            Sprite.brick_exploded.getFxImage(),
            Sprite.brick_exploded1.getFxImage(),
            Sprite.brick_exploded2.getFxImage(),
            Sprite.grass.getFxImage(),
            Sprite.portal.getFxImage(),
            Sprite.brick.getFxImage()};

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isExploded() {
        return isExploded;
    }

    public void setExploded(boolean exploded) {
        isExploded = exploded;
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
                if (hidden) {
                    this.setIndex(3);
                    this.setImg(brickFrames[3]);
                } else {
                    this.setIndex(4);
                    this.setImg(brickFrames[4]);
                }

                countToDisappear++;
                if (countToDisappear == 1) {
                    disappeared = true;
                }
            } else {
                if (!disappeared) this.setIndex(this.getIndex() + 1);
            }
        } else {
            this.setIndex(5);
        }
    }
}
