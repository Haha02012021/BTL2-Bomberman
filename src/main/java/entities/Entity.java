package entities;

import java.util.ArrayList;
import java.util.List;

import app.BombermanGame;
import entities.move_entity.Bomberman;
import entities.move_entity.Enemy;
import graphics.Sprite;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Entity {
    //Tọa độ X tính từ góc trái trên trong Canvas
    protected double x;

    //Tọa độ Y tính từ góc trái trên trong Canvas
    protected double y;

    protected Image img;

    protected Entity() {
    }


    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Image getImg() {
        return this.img;
    }

    public void setImg(Image img) {
        this.img = img;
    }


    //Khởi tạo đối tượng, chuyển từ tọa độ đơn vị sang tọa độ trong canvas
    public Entity( int xUnit, int yUnit, Image img) {
        this.x = xUnit * Sprite.SCALED_SIZE;
        this.y = yUnit * Sprite.SCALED_SIZE;
        this.img = img;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(img, x, y);
    }

    public boolean checkCollision(List<List<String>> map, String symbol, String type) {
        int size = Sprite.SCALED_SIZE;

        if (type.equals("RIGHT")) {
            int nextX = (int)(this.getX() + size) / size;
            double nextDoubleY = this.getY() / size;
            int nextY = (int)nextDoubleY;
            if (nextY < nextDoubleY) {
                return (map.get(nextY).get(nextX).equals(symbol) || map.get(nextY + 1).get(nextX).equals(symbol));
            } else {
                return map.get(nextY).get(nextX).equals(symbol);
            }
        }

        if (type.equals("LEFT")) {
            int nextX = (int)(this.getX() - 4) / size;
            double nextDoubleY = this.getY() / size;
            int nextY = (int)nextDoubleY;
            if (nextY < nextDoubleY) {
                return map.get(nextY).get(nextX).equals(symbol) || map.get(nextY + 1).get(nextX).equals(symbol);
            } else {
                return map.get(nextY).get(nextX).equals(symbol);
            }
        }

        if (type.equals("DOWN")) {
            double nextDoubleX = (this.getX()) / size;
            int nextX = (int)nextDoubleX;
            int nextY = (int)(this.getY() + size) / size;
            if (nextDoubleX - nextX > 0.2) {
                return map.get(nextY).get(nextX).equals(symbol) || map.get(nextY).get(nextX + 1).equals(symbol);
            } else {
                return map.get(nextY).get(nextX).equals(symbol);
            }
        }

        if (type.equals("UP")) {
            double nextDoubleX = (this.getX()) / size;
            int nextX = (int)nextDoubleX;
            int nextY = (int)(this.getY() - 1) / size;
            if (nextDoubleX - nextX > 0.2) {
                return map.get(nextY).get(nextX).equals(symbol) || map.get(nextY).get(nextX + 1).equals(symbol);
            } else {
                return map.get(nextY).get(nextX).equals(symbol);
            }
        }

        return false;
    }

    public void checkCollision(ArrayList<AnimatedImage> entities) {
        int size = Sprite.SCALED_SIZE;
        Rectangle2D e = new Rectangle2D(this.getX(), this.getY(), Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);
        if (this instanceof Bomberman) {
            for (AnimatedImage entity: entities) {
                if (!(entity instanceof Bomberman)) {
                    Rectangle2D c = new Rectangle2D(entity.getX(), entity.getY(), Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);
                    if (c.intersects(e)) {
                        ((Bomberman) this).setDied(true);
                    }
                }
            }
        } else {
            for (AnimatedImage entity: entities) {
                Rectangle2D c = new Rectangle2D(entity.getX(), entity.getY(), Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);
                if (c.intersects(e)) {
                    entity.setDied(true);
                }
            }
        }
    }


    public abstract void update();
}