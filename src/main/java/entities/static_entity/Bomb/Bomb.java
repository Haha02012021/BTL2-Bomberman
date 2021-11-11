package entities.static_entity.Bomb;

import entities.AnimatedImage;
import graphics.Sprite;
import javafx.scene.image.Image;

public class Bomb extends AnimatedImage {
    private int countToExplore = 0, cycle = 4;
    private boolean explosed = false;
    private boolean explosing = false;
    private Image[] bombFrames = {Sprite.bomb.getFxImage(), Sprite.bomb_1.getFxImage(), Sprite.bomb_2.getFxImage()};
    public Bomb(int x, int y, Image image) {
        super(x, y, image);
        this.setPosition(x * Sprite.SCALED_SIZE, y * Sprite.SCALED_SIZE);
        this.setFrames(bombFrames);
    }

    public boolean isExplosing() {
        return explosing;
    }

    public void setExplosing(boolean explosing) {
        this.explosing = explosing;
    }

    public boolean isExplosed() {
        return explosed;
    }

    public void setExplosed(boolean explosed) {
        this.explosed = explosed;
    }

    public void update() {
        if (this.getIndex() >= 2) {
            this.setIndex(0);
            countToExplore++;
            if (countToExplore == cycle) {
                explosed = true;
            }
        } else {
            if (!explosed) {
                this.setIndex(this.getIndex() + 1);
            }
        }
    }
}
