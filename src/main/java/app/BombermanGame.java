package app;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import entities.AnimatedImage;
import entities.Entity;
import entities.move_entity.Bomberman;
import entities.move_entity.Enemy;
import entities.static_entity.Brick;
import entities.static_entity.Grass;
import entities.static_entity.Wall;
import entities.static_entity.Bomb.Bomb;
import entities.static_entity.Bomb.ChildExplosion;
import graphics.Sprite;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

// Collect the Money Bags!
public class BombermanGame extends Application
{
    public static int width;
    public static int height;

    private int level;
    public static List<List<String>> textMap = new ArrayList<>();

    public static ArrayList<String> input = new ArrayList<String>();
    private GraphicsContext gc;
    public static ArrayList<Entity> stillObjects = new ArrayList<>();
    public static ArrayList<entities.AnimatedImage> entities = new ArrayList<>();

    public static ArrayList<Entity> getStillObjects() {
        return stillObjects;
    }
    public static void addStillObjects(Entity entity) {
        stillObjects.add(entity);
    }

    public static void removeStillObjects(Entity entity) {
        stillObjects.remove(entity);
    }

    public static void main(String[] args) 
    {
        launch(args);
    }

    @Override
    public void start(Stage theStage) 
    {
        readMap();

        theStage.setTitle( "Bomberman Game!" );

        Group root = new Group();
        Scene theScene = new Scene( root );
        theStage.setScene( theScene );

        Canvas canvas = new Canvas(Sprite.SCALED_SIZE * width, Sprite.SCALED_SIZE * height);
        root.getChildren().add( canvas );

        theScene.setOnKeyPressed(
            new EventHandler<KeyEvent>()
            {
                public void handle(KeyEvent e)
                {
                    String code = e.getCode().toString();
                    if ( !input.contains(code) )
                        input.add( code );
                }
            });

        theScene.setOnKeyReleased(
            new EventHandler<KeyEvent>()
            {
                public void handle(KeyEvent e)
                {
                    String code = e.getCode().toString();
                    input.remove( code );
                }
            });

        gc = canvas.getGraphicsContext2D();

        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdateMove = 0;
            private long getLastUpdateStatic = 0;
            @Override
            public void handle(long l) {
                render();
                if (l - lastUpdateMove >= 50000000) {
                    updateMove();
                    lastUpdateMove = l;
                }
                if (l - getLastUpdateStatic >= 100000000) {
                    updateStatic();
                    getLastUpdateStatic = l;
                }

            }
        };
        timer.start();

        createMap();

        AnimatedImage bomberman = new Bomberman(1, 1, Sprite.player_right.getFxImage());
        entities.add(bomberman);

        theStage.show();
    }

    public void updateMove() {
        entities.forEach(e -> e.update());
    }

    public void updateStatic() {
        stillObjects.forEach(e -> e.update());
    }

    public void render() {
        gc.clearRect(0, 0, width * Sprite.SCALED_SIZE, height * Sprite.SCALED_SIZE);
        stillObjects.forEach(g -> {
            if (g instanceof Bomb) {
                ((Bomb) g).render(gc,((Bomb) g).getIndex());
            } else if (g instanceof ChildExplosion) {
                ((ChildExplosion) g).render(gc, ((ChildExplosion) g).getIndex());
            } else if (g instanceof Brick) {
                ((Brick) g).render(gc, ((Brick) g).getIndex());
            } else g.render(gc);
        });
        entities.forEach(g -> g.render(gc, g.getIndex()));
    }

    private void createMap() {
        for (int i = 0; i < textMap.size(); i++) {
            List<String> lineMap = textMap.get(i);
            for (int j = 0; j < lineMap.size(); j++) {
                Entity object = null;
                String entity = lineMap.get(j);
                if (entity.equals("#")) {
                    object = new Wall(j, i, Sprite.wall.getFxImage());
                } else if (entity.equals("*")) {
                    object = new Brick(j, i, Sprite.brick.getFxImage());
                } else if (entity.equals("x")) {
                    object = new Brick(j, i, Sprite.brick.getFxImage());
                } else {
                    object = new Grass(j, i, Sprite.grass.getFxImage());
                    if (entity.equals("1")) {
                        Image[] ballomFrames = {
                                Sprite.balloom_left1.getFxImage(),
                                Sprite.balloom_right1.getFxImage(),
                                Sprite.balloom_left2.getFxImage(),
                                Sprite.balloom_left2.getFxImage(),
                                Sprite.balloom_left3.getFxImage(),
                                Sprite.balloom_left3.getFxImage()
                        };
                        Enemy ballom = new Enemy(j, i, null);
                        ballom.setNumberOfFrames(5);
                        ballom.setFrames(ballomFrames);
                        entities.add(ballom);

                    } else if (entity.equals("2")) {
                        Image[] onealFrames = {
                                Sprite.oneal_right1.getFxImage(),
                                Sprite.oneal_left1.getFxImage(),
                                Sprite.oneal_right2.getFxImage(),
                                Sprite.oneal_left2.getFxImage(),
                                Sprite.oneal_right3.getFxImage(),
                                Sprite.oneal_left3.getFxImage()
                        };
                        Enemy oneal = new Enemy(j, i, null);
                        oneal.setNumberOfFrames(5);
                        oneal.setFrames(onealFrames);
                        entities.add(oneal);
                    }
                }
                if (object != null) stillObjects.add(object);
            }
        }
    }

    public void readMap() {
        try {
            File file = new File(getClass().getResource("Level1.txt").toURI());
            Pattern p1 = Pattern.compile("^([0-9]+\\s)([0-9]+\\s)([0-9]+)");
            Pattern p2 = Pattern.compile("^#");
            Matcher m1;
            Matcher m2;
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                m1 = p1.matcher(line);
                m2 = p2.matcher(line);
                if (m1.find()) {
                    level = Integer.parseInt(m1.group(1).trim());
                    height = Integer.parseInt(m1.group(2).trim());
                    width = Integer.parseInt(m1.group(3).trim());
                } else if (m2.find()) {
                    List<String> lineMap = new ArrayList<>();
                    lineMap = Arrays.asList(line.split(""));
                    if (line.length() == width) textMap.add(lineMap);
                }
            }
            scanner.close();
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println(e.getMessage());
        }
    }
}