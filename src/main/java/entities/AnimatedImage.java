package entities;

import app.BombermanGame;
import graphics.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class AnimatedImage extends Entity {
    private Image[] frames = {null};
    private double positionX;
    private double positionY;
    private double width;
    private double height;
    private int speed;
    private int index = 0;
    private boolean died = false;
    protected String[] stillSymbols = {"#", "*", "x", "s", "f", "d"};

    public AnimatedImage() {
        positionX = 0;
        positionY = 0;
    }

    public AnimatedImage(int x, int y, Image fxImage) {
        super(x, y, fxImage);
        this.setPosition(x * Sprite.SCALED_SIZE, y * Sprite.SCALED_SIZE);
    }
    public int getIndex() {
        return this.index;
    }
    public void setIndex(int i) {
        this.index = i;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isDied() {
        return died;
    }

    public void setDied(boolean died) {
        this.died = died;
    }

    public void setFrames(Image[] frames) {
        this.frames = frames;
        this.width = frames[0].getWidth();
        this.height = frames[0].getHeight();
    }

    public Image getFrame(int index)
    {
        return frames[index];
    }

    public void render(GraphicsContext gc, int index) {
        gc.drawImage(this.getFrame(index), positionX, positionY);
    }

    public void setPosition(double x, double y)
    {
        positionX = x;
        positionY = y;
    }

    public boolean checkCollisionToStillObjects(String[] symbols, String type) {
        for (String s: symbols) {
            if (this.checkCollision(BombermanGame.textMap, s, type, speed)) return false;
        }
        return true;
    }

    @Override
    public void update() {

    }
}