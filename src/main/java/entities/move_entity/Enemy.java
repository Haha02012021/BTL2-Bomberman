package entities.move_entity;

import app.BombermanGame;
import entities.AnimatedImage;
import graphics.Sprite;
import javafx.scene.image.Image;

public class Enemy extends AnimatedImage {
    private int numberOfFrames;
    private int countToDisappear = 0;
    private boolean disappeared = false;
    private int defaultIndex = 0;
    private Image[] dieFrames = {null};
    public Enemy(int x, int y, Image img) {
        super(x, y, img);
        this.setPosition(x * Sprite.SCALED_SIZE, y * Sprite.SCALED_SIZE);
        BombermanGame.textMap.get(y).set(x, " ");
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
            this.setFrames(dieFrames);
            numberOfFrames = 3;
        }
        if (this.getIndex() >= numberOfFrames) {
            this.setIndex(defaultIndex);
            if (this.isDied()) {
                countToDisappear++;
                if (countToDisappear == 1) {
                    disappeared = true;
                    BombermanGame.removeEntities.add(this);
                }
            }
        } else {
            if (!disappeared) this.setIndex(this.getIndex() + 1);
        }
    }
}
