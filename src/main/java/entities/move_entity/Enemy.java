package entities.move_entity;

import entities.AnimatedImage;
import graphics.Sprite;
import javafx.scene.image.Image;

public class Enemy extends AnimatedImage {
    private int numberOfFrames;
    public Enemy(int x, int y, Image img) {
        super(x, y, img);
        this.setPosition(x * Sprite.SCALED_SIZE, y * Sprite.SCALED_SIZE);
    }

    public void setNumberOfFrames(int numberOfFrames) {
        this.numberOfFrames = numberOfFrames;
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        if (this.getIndex() >= numberOfFrames) {
            this.setIndex(0);
        } else this.setIndex(this.getIndex() + 1);
    }
}
