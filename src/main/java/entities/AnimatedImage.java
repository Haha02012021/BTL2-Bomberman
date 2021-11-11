package entities;

import graphics.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class AnimatedImage extends Entity {
    private Image[] frames = {null};
    private double positionX;
    private double positionY;
    private double width;
    private double height;
    private int index = 0;

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

    @Override
    public void update() {

    }
}