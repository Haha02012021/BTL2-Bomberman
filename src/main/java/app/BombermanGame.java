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
import entities.move_entity.enemies.Balloom;
import entities.move_entity.enemies.Enemy;
import entities.move_entity.enemies.Oneal;
import entities.static_entity.Brick;
import entities.static_entity.Grass;
import entities.static_entity.Wall;
import entities.static_entity.Bomb.Bomb;
import entities.static_entity.Bomb.ChildExplosion;
import graphics.Sprite;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

// Collect the Money Bags!
public class BombermanGame extends Application {
    public static int width;
    public static int height;
    private static final int NUMBER_OF_LEVELS = 2;
    public static int numberOfEnemies;
    public static int timeLimitMove = 100;

    private long stageTime = 3;
    public static int level = 1;
    private static int timeOfLevel;
    private LongProperty time;
    public static List<List<String>> textMap = new ArrayList<>();
    public static boolean win = false;
    public static boolean lose = false;
    private boolean startGame = true;
    private boolean clickMenu = false;

    public static ArrayList<String> input = new ArrayList<String>();
    private GraphicsContext gc;
    public static ArrayList<Entity> stillObjects = new ArrayList<>();
    public static ArrayList<Entity> addStillObjects = new ArrayList<>();
    public static ArrayList<AnimatedImage> entities = new ArrayList<>();
    public static ArrayList<AnimatedImage> removeEntities = new ArrayList<>();
    public static IntegerProperty souls = new SimpleIntegerProperty(3);

    public static ArrayList<Entity> getStillObjects() {
        return stillObjects;
    }

    public static void addStillObjects(Entity entity) {
        stillObjects.add(entity);
    }

    public static void removeStillObjects(Entity entity) {
        stillObjects.remove(entity);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        theStage.setTitle("Bomberman Game!");
        theStage.setResizable(false);

        readMap();

        Scene stageScene = stageScene(level);

        Scene gameScene = gameScene(theStage, stageScene);

        Scene startScene = startScene(theStage, stageScene, gameScene);

        if (startGame) {
            theStage.setScene(startScene);
            startGame = false;
        } else {
            theStage.setScene(gameScene);
        }
        theStage.getIcons().add(new Image(getClass().getResourceAsStream("angry.png")));

        gameScene.setOnKeyPressed(
                new EventHandler<KeyEvent>() {
                    public void handle(KeyEvent e) {
                        String code = e.getCode().toString();
                        if (!input.contains(code))
                            input.add(code);
                    }
                });

        gameScene.setOnKeyReleased(
                new EventHandler<KeyEvent>() {
                    public void handle(KeyEvent e) {
                        String code = e.getCode().toString();
                        input.remove(code);
                    }
                });

        AnimationTimer timer = new AnimationTimer() {
            private long startTime;

            @Override
            public void start() {
                startTime = System.currentTimeMillis();
                super.start();
            }

            @Override
            public void stop() {
                super.stop();
            }

            private long lastUpdateMove = 0;
            private long getLastUpdateStatic = 0;

            @Override
            public void handle(long l) {
                long now = System.currentTimeMillis();

                if (timeOfLevel - (now - startTime) / 1000 >= 0) {
                    if (!clickMenu) time.set(timeOfLevel - (now - startTime) / 1000);
                }
                render();
                if (now - lastUpdateMove >= timeLimitMove) {
                    updateMove();
                    lastUpdateMove = now;
                }
                if (now - getLastUpdateStatic >= 100) {
                    updateStatic();
                    getLastUpdateStatic = now;
                }

                if (lose) {
                    stop();
                    textMap.clear();
                    win = false;

                    timeLimitMove = 100;
                    input.clear();
                    stillObjects.clear();
                    entities.clear();
                    removeEntities.clear();
                    addStillObjects.clear();
                    souls = new SimpleIntegerProperty(3);
                    Scene loseScene = loseScene(theStage, stageScene);
                    theStage.setScene(loseScene);
                }

                if (win) {
                    stop();
                    level += 1;
                    textMap.clear();
                    win = false;

                    input.clear();
                    stillObjects.clear();
                    entities.clear();
                    removeEntities.clear();
                    addStillObjects.clear();
                    souls = new SimpleIntegerProperty(3);
                    Scene winScene = winScene(theStage);
                    theStage.setScene(winScene);
                }
            }
        };
        timer.start();

        createMap();

        theStage.show();
    }

    public Scene startScene(Stage theStage, Scene stageScene, Scene gameScene) {
        // start scene
        VBox startWindow = new VBox();
        startWindow.setStyle("-fx-background-color: black;");
        startWindow.setPrefSize(width * Sprite.SCALED_SIZE, height * Sprite.SCALED_SIZE);

        Label nameGame = new Label("BOMBERMAN GAME");
        nameGame.setTextFill(Color.WHITE);
        nameGame.setFont(Font.font("Roboto", FontWeight.BOLD, 30));
        startWindow.getChildren().add(nameGame);

        HBox buttonBar = new HBox();
        Button tutorialButton = new Button("TUTORIAL");
        styleButton(tutorialButton, Color.WHITE);
        tutorialButton.setOnAction(e -> {
            VBox tutorialWindow = new VBox();
            tutorialWindow.setStyle("-fx-background-color: black;");
            tutorialWindow.setPadding(new Insets(20, width * Sprite.SCALED_SIZE / 5, 20, width * Sprite.SCALED_SIZE / 5));
            tutorialWindow.setPrefSize(width * Sprite.SCALED_SIZE, height * Sprite.SCALED_SIZE);

            Label title = new Label("Hướng dẫn");
            title.setFont(Font.font("Roboto", FontWeight.BOLD, 30));
            title.setTextFill(Color.web("#cc3333"));

            TextFlow tf = new TextFlow();
            Text t = new Text("⁂ Luật chơi: " +
                    "\n- Bomberman di chuyển trên cỏ. " +
                    "\n- Bomberman thả bom để phá gạch và giết Enemy." +
                    "\n- Bomberman chết khi chạm phải vụ nổ và Enemy." +
                    "\n- Bomberman qua màn khi nổ hết Enemy & đi qua cổng." +
                    "\n- Cổng ẩn dưới gạch." +
                    "\n- Tự tìm hiểu để ăn các sức mạnh bằng kinh nghiệm :).");
            t.setFill(Color.WHITE);
            t.setFont(Font.font(20));
            tf.getChildren().add(t);
            tf.setPadding(new Insets(10, 0, 0, 0));

            TextFlow tf1 = new TextFlow();
            Text t1 = new Text("⁂ Phím bấm: " +
                    "\n- Bấm các phím mũi tên để di chuyển." +
                    "\n- Bấm phím SPACE để thả bom." +
                    "\n- Bấm phím R để tiếp tục màn chơi khi Bomberman chết.");
            t1.setFill(Color.WHITE);
            t1.setFont(Font.font(20));
            tf1.getChildren().add(t1);
            tf1.setPadding(new Insets(10, 0, 0, 0));

            Label goBack = new Label("BACK");
            goBack.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
            goBack.setTextFill(Color.WHITE);
            goBack.setOnMouseClicked(event -> {
                theStage.setScene(startWindow.getScene());
            });

            Pane pane = new Pane(goBack);
            goBack.hoverProperty().addListener((observableValue, aBoolean, t11) -> {
                if (t11) {
                    goBack.setTextFill(Color.web("#cc3333"));
                } else {
                    goBack.setTextFill(Color.WHITE);
                }
            });

            goBack.setLayoutX(pane.getLayoutX() + Sprite.SCALED_SIZE * (width - 10));

            tutorialWindow.getChildren().addAll(title, tf, tf1, pane);

            Scene tutorialScene = new Scene(tutorialWindow);

            theStage.setScene(tutorialScene);
        });

        Button continueButton = new Button("CONTINUE");
        continueButton.setStyle("-fx-background-color: transparent;");
        continueButton.setFont(Font.font("Roboto", FontWeight.BOLD,  20));
        continueButton.setTextFill(Color.web("#cccccc"));
        if (clickMenu) {
            styleButton(continueButton, Color.WHITE);
            continueButton.setOnAction(e -> {
                clickMenu = false;
                Timeline timeline = new Timeline();
                timeline.setCycleCount(1);

                timeline.getKeyFrames().add(new KeyFrame(
                        Duration.millis(0),
                        event -> {
                            theStage.setScene(stageScene);
                        }
                ));

                timeline.getKeyFrames().add(new KeyFrame(
                        Duration.millis(1000),
                        event -> {
                            theStage.setScene(gameScene);
                        }
                ));

                timeline.play();
            });
        }

        Button startButton = new Button("START");
        if (clickMenu) startButton.setText("RESTART");
        styleButton(startButton, Color.WHITE);
        startButton.setOnAction(e -> {
            Timeline timeline = new Timeline();
            timeline.setCycleCount(1);

            timeline.getKeyFrames().add(new KeyFrame(
                    Duration.millis(0),
                    event -> {
                        theStage.setScene(stageScene);
                    }
            ));

            timeline.getKeyFrames().add(new KeyFrame(
                    Duration.millis(1000),
                    event -> {
                        if (!clickMenu) theStage.setScene(gameScene);
                        else {
                            textMap.clear();
                            win = false;
                            lose = false;

                            timeLimitMove = 100;
                            input.clear();
                            stillObjects.clear();
                            entities.clear();
                            removeEntities.clear();
                            addStillObjects.clear();
                            BombermanGame.this.start(theStage);
                            clickMenu = false;
                        }
                    }
            ));

            timeline.play();
        });

        buttonBar.getChildren().addAll(tutorialButton, continueButton, startButton);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(height * Sprite.SCALED_SIZE / 10, 0, 0, 0));
        startWindow.getChildren().add(buttonBar);
        startWindow.setAlignment(Pos.CENTER);

        Scene theStartScene = new Scene(startWindow);

        return theStartScene;
    }

    public void styleButton(Button button, Color color) {
        button.setStyle("-fx-background-color: transparent;");
        button.setFont(Font.font("Roboto", FontWeight.BOLD,  20));
        button.setTextFill(color);
        button.hoverProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                button.setTextFill(Color.web("#cc3333"));
            } else {
                button.setTextFill(color);
            }
        });
    }

    public Scene stageScene(int level) {
        // stage scene
        StackPane stageWindow = new StackPane();
        stageWindow.setPrefSize(width * Sprite.SCALED_SIZE, height * Sprite.SCALED_SIZE);
        stageWindow.setStyle("-fx-background-color: black;");

        Label label = new Label("Stage " + level);
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font("Roboto", FontWeight.BOLD, 30));

        stageWindow.getChildren().add(label);

        Scene stageScene = new Scene(stageWindow);

        return stageScene;
    }

    public Scene gameScene(Stage theStage, Scene stageSceen) {
        // full main scene
        VBox fullScreen = new VBox();

        HBox topBar = new HBox();
        topBar.setPrefHeight(50);
        topBar.setStyle("-fx-background-color: #cccccc; -fx-border-color: #dfdfdf; -fx-border-width: 5;");
        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setBlurType(BlurType.ONE_PASS_BOX);
        innerShadow.setOffsetX(-5);
        innerShadow.setOffsetY(-5);
        innerShadow.setColor(Color.web("#666666"));
        topBar.setEffect(innerShadow);

        HBox levelBox = new HBox();

        Label titleLevel = new Label("Level: ");
        titleLevel.setFont(Font.font("Roboto", FontWeight.BOLD, 20));

        Label levelDisplay = new Label(String.valueOf(level));
        levelDisplay.setFont(Font.font("Roboto", FontWeight.BOLD, 20));

        levelBox.getChildren().addAll(titleLevel, levelDisplay);
        levelBox.setPadding(new Insets(0, 0, 0, width * Sprite.SCALED_SIZE / 20));
        levelBox.setAlignment(Pos.CENTER);

        HBox timeBox = new HBox();

        Label titleTime = new Label("Time: ");
        titleTime.setFont(Font.font("Roboto", FontWeight.BOLD, 20));

        Label timeDisplay = new Label();
        time = new SimpleLongProperty(timeOfLevel);
        timeDisplay.textProperty().bind(time.asString());
        timeDisplay.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
        timeDisplay.textProperty().addListener(((observableValue, s, t1) -> {
            if (t1 != null) {
                if (Integer.parseInt(t1) <= 10) {
                    timeDisplay.setTextFill(Color.RED);
                    titleTime.setTextFill(Color.RED);
                }
            }
        }));

        timeBox.getChildren().addAll(titleTime, timeDisplay);
        timeBox.setAlignment(Pos.CENTER_LEFT);
        timeBox.setPadding(new Insets(0, width * Sprite.SCALED_SIZE / 10, 0, width * Sprite.SCALED_SIZE / 10));

        HBox numberOfSouls = new HBox();

        Label soulTitle = new Label("Souls: ");
        soulTitle.setFont(Font.font("Roboto", FontWeight.BOLD, 20));

        Label soulDisplay = new Label();
        soulDisplay.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
        soulDisplay.textProperty().bind(souls.asString());
        soulDisplay.textProperty().addListener(((observableValue, s, t1) -> {
            if (t1 != null) {
                if (Integer.parseInt(t1) == 0) {
                    lose = true;
                }
            }
        }));

        numberOfSouls.getChildren().addAll(soulTitle, soulDisplay);
        numberOfSouls.setAlignment(Pos.CENTER_LEFT);

        HBox menuOptionBox = new HBox();

        Label menuOption = new Label("MENU");
        menuOption.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
        menuOption.hoverProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                menuOption.setTextFill(Color.web("#cc3333"));
            } else {
                menuOption.setTextFill(Color.web("#333333"));
            }
        });

        menuOptionBox.getChildren().add(menuOption);
        menuOptionBox.setAlignment(Pos.CENTER_RIGHT);
        menuOption.setPadding(new Insets(0, 0, 0, width * Sprite.SCALED_SIZE / 2));

        topBar.getChildren().addAll(levelBox, timeBox, numberOfSouls, menuOptionBox);

        Group root = new Group();
        Canvas canvas = new Canvas(Sprite.SCALED_SIZE * width, Sprite.SCALED_SIZE * height);
        System.out.println(canvas.getWidth() + " " + canvas.getHeight());
        root.getChildren().add(canvas);

        fullScreen.getChildren().addAll(topBar, root);
        System.out.println(fullScreen.getWidth() + " " + fullScreen.getHeight());

        Scene gameScene = new Scene(fullScreen);

        menuOption.setOnMouseClicked(e -> {
            clickMenu = true;
            theStage.setScene(startScene(theStage, stageSceen, gameScene));
        });

        gc = canvas.getGraphicsContext2D();

        return gameScene;
    }

    public Scene winScene(Stage theStage) {
        // win scene
        VBox winWindow = new VBox();
        winWindow.setStyle("-fx-background-color: black;");
        winWindow.setPrefSize(width * Sprite.SCALED_SIZE, height * Sprite.SCALED_SIZE);

        Label statusGame = new Label();
        if (level <= NUMBER_OF_LEVELS) statusGame.setText("YOU WIN");
        else {
            statusGame.setText("YOU WIN\nChưa có level mới");
        }
        statusGame.setTextAlignment(TextAlignment.CENTER);

        statusGame.setTextFill(Color.WHITE);
        statusGame.setFont(Font.font("Roboto", FontWeight.BOLD, 30));
        winWindow.getChildren().add(statusGame);

        HBox buttonBar = new HBox();
        Button menuButton = new Button("MENU");
        menuButton.setStyle("-fx-background-color: transparent;");
        menuButton.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
        menuButton.setTextFill(Color.WHITE);
        menuButton.setOnAction(e -> {
            level = 1;
            startGame = true;
            BombermanGame.this.start(theStage);
        });
        menuButton.hoverProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                menuButton.setTextFill(Color.web("#cc3333"));
            } else {
                menuButton.setTextFill(Color.WHITE);
            }
        });
        buttonBar.getChildren().add(menuButton);

        if (level <= NUMBER_OF_LEVELS) {
            Button nextButton = new Button("NEXT");
            nextButton.setStyle("-fx-background-color: transparent;");
            nextButton.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
            nextButton.setTextFill(Color.WHITE);
            nextButton.setOnAction(e -> {
                Timeline timeline = new Timeline();
                timeline.setCycleCount(1);

                timeline.getKeyFrames().add(new KeyFrame(
                        Duration.millis(0),
                        event -> {
                            theStage.setScene(stageScene(level));
                        }
                ));

                timeline.getKeyFrames().add(new KeyFrame(
                        Duration.millis(1000),
                        event -> {
                            BombermanGame.this.start(theStage);
                        }
                ));

                timeline.play();
            });
            nextButton.hoverProperty().addListener((observableValue, aBoolean, t1) -> {
                if (t1) {
                    nextButton.setTextFill(Color.web("#cc3333"));
                } else {
                    nextButton.setTextFill(Color.WHITE);
                }
            });
            buttonBar.getChildren().add(nextButton);
        }

        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(height * Sprite.SCALED_SIZE / 10, 0, 0, 0));
        winWindow.getChildren().add(buttonBar);
        winWindow.setAlignment(Pos.CENTER);

        Scene theWinScene = new Scene(winWindow);
        return theWinScene;
    }

    public Scene loseScene(Stage theStage, Scene stageScene) {
        VBox loseWindow = new VBox();
        loseWindow.setStyle("-fx-background-color: black;");
        loseWindow.setPrefSize(width * Sprite.SCALED_SIZE, height * Sprite.SCALED_SIZE);

        Label statusGame = new Label("YOU LOSE");
        statusGame.setTextFill(Color.WHITE);
        statusGame.setFont(Font.font("Roboto", FontWeight.BOLD, 30));
        loseWindow.getChildren().add(statusGame);

        HBox buttonBar = new HBox();
        Button menuButton = new Button("MENU");
        menuButton.setStyle("-fx-background-color: transparent;");
        menuButton.setFont(Font.font("Roboto", FontWeight.BOLD,  20));
        menuButton.setTextFill(Color.WHITE);
        menuButton.setOnAction(e -> {
            level = 1;
            lose = false;
            startGame = true;
            BombermanGame.this.start(theStage);
        });
        menuButton.hoverProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                menuButton.setTextFill(Color.web("#cc3333"));
            } else {
                menuButton.setTextFill(Color.WHITE);
            }
        });

        Button restartButton = new Button("RESTART");
        restartButton.setStyle("-fx-background-color: transparent;");
        restartButton.setFont(Font.font("Roboto", FontWeight.BOLD,  20));
        restartButton.setTextFill(Color.WHITE);
        restartButton.setOnAction(e -> {
            lose = false;
            Timeline timeline = new Timeline();
            timeline.setCycleCount(1);

            timeline.getKeyFrames().add(new KeyFrame(
                    Duration.millis(0),
                    event -> {
                        theStage.setScene(stageScene);
                    }
            ));

            timeline.getKeyFrames().add(new KeyFrame(
                    Duration.millis(1000),
                    event -> {
                        BombermanGame.this.start(theStage);
                    }
            ));

            timeline.play();
        });
        restartButton.hoverProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                restartButton.setTextFill(Color.web("#cc3333"));
            } else {
                restartButton.setTextFill(Color.WHITE);
            }
        });

        buttonBar.getChildren().addAll(menuButton, restartButton);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(height * Sprite.SCALED_SIZE / 10, 0, 0, 0));
        loseWindow.getChildren().add(buttonBar);
        loseWindow.setAlignment(Pos.CENTER);

        Scene theWinScene = new Scene(loseWindow);
        return theWinScene;
    }

    public void updateMove() {
        entities.removeAll(removeEntities);
        entities.forEach(e -> e.update());
    }

    public void updateStatic() {
        stillObjects.addAll(addStillObjects);
        stillObjects.forEach(e -> e.update());
    }

    public void render() {
        gc.clearRect(0, 0, width * Sprite.SCALED_SIZE, height * Sprite.SCALED_SIZE);
        stillObjects.forEach(g -> {
            if (g instanceof Bomb) {
                ((Bomb) g).render(gc, ((Bomb) g).getIndex());
            } else if (g instanceof ChildExplosion) {
                ((ChildExplosion) g).render(gc, ((ChildExplosion) g).getIndex());
            } else if (g instanceof Brick) {
                ((Brick) g).render(gc, ((Brick) g).getIndex());
            } else g.render(gc);
        });
        entities.forEach(g -> g.render(gc, g.getIndex()));
    }

    private void createMap() {
        System.out.println("create map");
        for (int i = 0; i < textMap.size(); i++) {
            List<String> lineMap = textMap.get(i);
            for (int j = 0; j < lineMap.size(); j++) {
                Entity object = null;
                String entity = lineMap.get(j);
                if (entity.equals("#")) {
                    object = new Wall(j, i, Sprite.wall.getFxImage());
                } else if (entity.equals("*")) {
                    object = new Brick(j, i, Sprite.brick.getFxImage());
                } else if (entity.equals("x") || entity.equals("s") || entity.equals("f")) {
                    object = new Brick(j, i, Sprite.brick.getFxImage());
                } else {
                    object = new Grass(j, i, Sprite.grass.getFxImage());
                    if (entity.equals("1")) {
                        numberOfEnemies++;
                        Balloom ballom = new Balloom(j, i, null);
                        entities.add(ballom);
                    } else if (entity.equals("2")) {
                        numberOfEnemies++;
                        Oneal oneal = new Oneal(j, i, null);
                        entities.add(oneal);
                    } else if (entity.equals("p")) {
                        AnimatedImage bomberman = new Bomberman(1, 1, Sprite.player_right.getFxImage());
                        entities.add(bomberman);
                    }
                }
                if (object != null) stillObjects.add(object);
            }
        }
    }

    public void readMap() {
        try {
            File file = new File(getClass().getResource("Level" + level + ".txt").toURI());
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
                    height = Integer.parseInt(m1.group(2).trim());
                    width = Integer.parseInt(m1.group(3).trim());
                } else if (m2.find()) {
                    List<String> lineMap = new ArrayList<>();
                    lineMap = Arrays.asList(line.split(""));
                    if (line.length() == width) textMap.add(lineMap);
                }
            }
            timeOfLevel = 100 + level * 100;
            scanner.close();
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println(e.getMessage());
        }
    }
}