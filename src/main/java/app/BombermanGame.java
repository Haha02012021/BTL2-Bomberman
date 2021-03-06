package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import entities.AnimatedImage;
import entities.Entity;
import entities.move_entity.Bomberman;
import entities.move_entity.enemies.*;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BombermanGame extends Application {
    //general
    private GraphicsContext gc;
    private static final int NUMBER_OF_LEVELS = 5;
    public static int timeLimitMove = 100;
    private static long timeLimitStill = 120;
    public static ArrayList<String> inputKey = new ArrayList<String>();
    public static AnimationTimer timer;
    private int scoreOfGame;
    private int[] activeLevel = new int[NUMBER_OF_LEVELS];
    private int totalScore;
    private MediaPlayer backgroundPlayer = new MediaPlayer(new Media(getClass().getResource("sound/background.mp3").toExternalForm()));

    //properties of level
    public static VBox fullScreen;
    public static int width;
    public static int height;
    public static int numberOfEnemies;
    public static int level = 1;
    private static int timeOfLevel;
    public static LongProperty time;
    public static IntegerProperty score;
    public static IntegerProperty souls = new SimpleIntegerProperty(3);
    public static List<List<String>> textMap = new ArrayList<>();

    //stage of game
    public static boolean win = false;
    public static boolean lose = false;
    public static boolean startGame = true;
    public static boolean clickMenu = false;
    public static boolean running = false;
    public static int chosenLevel = 1;

    //manage characters
    public static ArrayList<Entity> stillObjects = new ArrayList<>();
    public static ArrayList<Entity> removeStillObjects = new ArrayList<>();
    public static ArrayList<Entity> addStillObjects = new ArrayList<>();
    public static ArrayList<AnimatedImage> entities = new ArrayList<>();
    public static ArrayList<AnimatedImage> removeEntities = new ArrayList<>();

    //main character
    public static Bomberman bomberman;
    public static int startManX;
    public static int startManY;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) throws IOException {
        theStage.setTitle("Bomberman Game!");
        theStage.setResizable(false);

        readScore();

        activeLevel[level - 1] = 0;

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

        backgroundPlayer.setVolume(0.2);

        gameScene.setOnKeyPressed(
                new EventHandler<KeyEvent>() {
                    public void handle(KeyEvent e) {
                        String code = e.getCode().toString();
                        if (!inputKey.contains(code))
                            inputKey.add(code);
                    }
                });

        gameScene.setOnKeyReleased(
                new EventHandler<KeyEvent>() {
                    public void handle(KeyEvent e) {
                        String code = e.getCode().toString();
                        inputKey.remove(code);
                    }
                });

        timer = new AnimationTimer() {
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
            private long lastAddEntity = 0;
            private long saveTime = 0;

            @Override
            public void handle(long l) {
                backgroundPlayer.play();
                long now = System.currentTimeMillis();

                if (!clickMenu && running) {
                    if (timeOfLevel - 1 >= 0) {
                        if ((int)(now - startTime) / 1000 != saveTime) timeOfLevel -= 1;
                        time.set(timeOfLevel);
                        saveTime = (int)(now - startTime) / 1000;
                    }
                    render();

                    if (level == NUMBER_OF_LEVELS && now - lastAddEntity >= 10_000 && entities.size() < 4) {
                        int randomX = (int) (Math.random() * (29 - 1 + 1) + 1);
                        int randomY = (int) (Math.random() * (11 - 1 + 1) + 1);
                        entities.add(new Ghost(randomX, randomY, null));
                        lastAddEntity = now;
                    }

                    if (now - lastUpdateMove >= timeLimitMove) {
                        updateMove();
                        lastUpdateMove = now;
                    }
                    if (now - getLastUpdateStatic >= timeLimitStill) {
                        updateStatic();
                        getLastUpdateStatic = now;
                    }

                    if (lose) {
                        backgroundPlayer.stop();
                        resetGame();

                        Scene loseScene = loseScene(theStage, stageScene);
                        theStage.setScene(loseScene);
                    }

                    if (win) {
                        backgroundPlayer.stop();
                        resetGame();

                        level += 1;
                        scoreOfGame = score.get();
                        Scene winScene = winScene(theStage);
                        theStage.setScene(winScene);
                    }
                }
            }
        };
        timer.start();

        createMap();

        theStage.getIcons().add(new Image(BombermanGame.class.getResourceAsStream("image/angry.png")));
        theStage.show();
    }

    //render characters
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

    //update characters
    public void updateMove() {
        entities.removeAll(removeEntities);
        entities.forEach(e -> {
            e.update();
        });
    }

    public void updateStatic() {
        stillObjects.addAll(addStillObjects);
        stillObjects.removeAll(removeStillObjects);
        stillObjects.forEach(e -> e.update());
    }

    //build map
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
                } else if (entity.equals("x") 
                            || entity.equals("s") 
                            || entity.equals("f") 
                            || entity.equals("d")
                            || entity.equals("b")) {
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
                    } else if (entity.equals("3")) {
                        numberOfEnemies++;
                        Doll doll = new Doll(j, i, null);
                        entities.add(doll);
                    } else if (entity.equals("4")) {
                        numberOfEnemies++;
                        Kondoria kondoria = new Kondoria(j, i, null);
                        entities.add(kondoria);
                    } else if (entity.equals("5")) {
                        numberOfEnemies++;
                        Minvo minvo = new Minvo(j, i, null);
                        entities.add(minvo);
                    } else if (entity.equals("6")) {
                        numberOfEnemies++;
                        Ghost ghost = new Ghost(j, i, null);
                        entities.add(ghost);
                    } else if (entity.equals("p")) {
                        bomberman = new Bomberman(j, i, Sprite.player_right.getFxImage());
                        entities.add(bomberman);
                        startManX = j;
                        startManY = i;
                    }
                    BombermanGame.textMap.get(i).set(j, " ");
                }
                if (object != null) stillObjects.add(object);
            }
        }
    }

    public void readMap() {
        try {
            File file = new File(getClass().getResource("level/Level" + level + ".txt").toURI());
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
            timeOfLevel = 50 + level * 50;
            scanner.close();
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println(e.getMessage());
        }
    }

    public void readScore() {
        File file = null;
        try {
            file = new File(getClass().getResource("level/ScoresPerLevel.txt").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int index = 0;

        while (scanner.hasNextLine()) {
            int score = Integer.parseInt(scanner.nextLine());

            if (index < NUMBER_OF_LEVELS) {
                activeLevel[index] = score;
                index++;
            }
            else totalScore = score;
        }

        scanner.close();
    }

    //reset properties of game
    public static void resetGame() {
        //reset general
        timer.stop();
        inputKey.clear();
        souls = new SimpleIntegerProperty(3);

        //reset stage of game
        win = false;
        lose = false;

        //reset map
        textMap.clear();
        stillObjects.clear();
        entities.clear();
        removeEntities.clear();
        addStillObjects.clear();
    }

    //scenes
    public Scene startScene(Stage theStage, Scene stageScene, Scene gameScene) {
        VBox startWindow = new VBox();
        startWindow.setStyle("-fx-background-color: black;");
        startWindow.setPrefSize(width * Sprite.SCALED_SIZE, height * Sprite.SCALED_SIZE);

        Label nameGame = new Label("BOMBERMAN GAME");
        nameGame.setTextFill(Color.WHITE);
        nameGame.setFont(Font.font("Roboto", FontWeight.BOLD, 30));
        startWindow.getChildren().add(nameGame);

        HBox buttonBar = new HBox();

        Button tutorialButton = new Button("TUTORIAL");
        styleButton(tutorialButton, Color.WHITE, Color.web("#cc3333"));
        tutorialButton.setOnAction(e -> {
            Scene tutorialScene = tutorialScene(theStage, startWindow.getScene());
            theStage.setScene(tutorialScene);
        });

        Button levelButton = new Button("LEVEL");
        styleButton(levelButton, Color.WHITE, Color.web("#cc3333"));
        levelButton.setOnAction(e -> {
            Scene levelScene = levelScene(theStage, startWindow.getScene());
            theStage.setScene(levelScene);
        });

        Button continueButton = new Button("CONTINUE");
        continueButton.setStyle("-fx-background-color: transparent;");
        continueButton.setFont(Font.font("Roboto", FontWeight.BOLD,  20));
        continueButton.setTextFill(Color.web("#cccccc"));
        if (clickMenu) {
            styleButton(continueButton, Color.WHITE, Color.web("#cc3333"));
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
        if (clickMenu && chosenLevel == level) startButton.setText("RESTART");
        styleButton(startButton, Color.WHITE, Color.web("#cc3333"));
        startButton.setOnAction(e -> {
            running = true;
            Timeline timeline = new Timeline();
            timeline.setCycleCount(1);

            timeline.getKeyFrames().add(new KeyFrame(
                    Duration.millis(0),
                    event -> {
                        if (level != chosenLevel) {
                            level = chosenLevel;
                            resetGame();
                            try {
                                BombermanGame.this.start(theStage);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                        theStage.setScene(stageScene(level));
                    }
            ));

            timeline.getKeyFrames().add(new KeyFrame(
                    Duration.millis(1000),
                    event -> {
                        if (!clickMenu && level == chosenLevel) {
                            theStage.setScene(gameScene);
                        }
                        else {
                            resetGame();
                            try {
                                BombermanGame.this.start(theStage);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            if (clickMenu) clickMenu = false;
                        }
                    }
            ));

            timeline.play();
        });

        buttonBar.getChildren().addAll(tutorialButton, levelButton, continueButton, startButton);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(height * Sprite.SCALED_SIZE / 24, 0, 0, 0));
        startWindow.getChildren().add(buttonBar);
        startWindow.setAlignment(Pos.CENTER);

        Scene theStartScene = new Scene(startWindow);

        return theStartScene;
    }

    public Scene levelScene(Stage theStage, Scene startScene) {
        VBox levelBox = new VBox();
        levelBox.setStyle("-fx-background-color: black;");
        levelBox.setPrefSize(width * Sprite.SCALED_SIZE, height * Sprite.SCALED_SIZE);


        Label levelTitle = new Label("LEVEL");
        levelTitle.setFont(Font.font("Roboto", FontWeight.BOLD, 48));
        levelTitle.setTextFill(Color.web("#cc3333"));

        levelBox.getChildren().addAll(levelTitle);
        levelBox.setAlignment(Pos.CENTER);

        Button[] buttons = new Button[NUMBER_OF_LEVELS];

        for (int i = 1; i <= NUMBER_OF_LEVELS; i++) {
            Button button = new Button("LEVEL " + i);
            if (chosenLevel != i) {
                styleButton(button, Color.WHITE, Color.web("#cc3333"));
                if (activeLevel[i - 1] == -1) styleButton(button, Color.web("#cccccc"), Color.web("#cccccc"));
            }
            else styleButton(button, Color.web("#993333"), Color.web("#993333"));
            button.setFont(Font.font("Roboto", FontWeight.BOLD,  32));

            buttons[i - 1] = button;

            int finalI = i;

            if (activeLevel[finalI - 1] != -1) {
                buttons[finalI - 1].setOnAction(e -> {
                    chosenLevel = finalI;
                    for (Button b: buttons) {
                        b.setTextFill(Color.WHITE);
                    }
                    buttons[finalI - 1].setTextFill(Color.web("#993333"));
                });

                buttons[finalI - 1].hoverProperty().addListener((observableValue, aBoolean, t1) -> {
                    if (t1) {
                        buttons[finalI - 1].setTextFill(Color.web("#cc3333"));
                    } else {
                        if (chosenLevel != finalI) buttons[finalI - 1].setTextFill(Color.WHITE);
                        else buttons[finalI - 1].setTextFill(Color.web("#993333"));
                    }
                });
            }
        }

        levelBox.getChildren().addAll(buttons);

        /*HBox scoreSumBox = new HBox();
        Label scoreSum = new Label("Best total score: ");
        scoreSum.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
        scoreSum.setTextFill(Color.WHITE);
        scoreSumBox.setAlignment(Pos.CENTER);

        scoreSumBox.getChildren().add(scoreSum);

        levelBox.getChildren().add(scoreSumBox);*/

        Label goBack = new Label("BACK");
        goBack.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
        goBack.setTextFill(Color.WHITE);
        goBack.setOnMouseClicked(event -> {
            if (chosenLevel == level) theStage.setScene(startScene);
            else theStage.setScene(startScene(theStage, stageScene(chosenLevel), gameScene(theStage, stageScene(chosenLevel))));
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

        levelBox.getChildren().add(pane);

        Scene levelScene = new Scene(levelBox);

        return levelScene;
    }

    public Scene tutorialScene(Stage theStage, Scene startScene) {
        VBox tutorialWindow = new VBox();
        tutorialWindow.setStyle("-fx-background-color: black;");
        tutorialWindow.setPadding(new Insets(20, width * Sprite.SCALED_SIZE / 5, 20, width * Sprite.SCALED_SIZE / 5));
        tutorialWindow.setPrefSize(width * Sprite.SCALED_SIZE, height * Sprite.SCALED_SIZE);

        Label title = new Label("H?????ng d???n");
        title.setFont(Font.font("Roboto", FontWeight.BOLD, 30));
        title.setTextFill(Color.web("#cc3333"));

        TextFlow tf = new TextFlow();
        Text t = new Text("??? Lu???t ch??i: " +
                "\n- Bomberman di chuy???n tr??n c???. " +
                "\n- Bomberman th??? bom ????? ph?? g???ch v?? gi???t Enemy." +
                "\n- Bomberman ch???t khi ch???m ph???i v??? n??? v?? Enemy." +
                "\n- Bomberman qua m??n khi n??? h???t Enemy & ??i qua c???ng." +
                "\n- C???ng ???n d?????i g???ch." +
                "\n- T??? t??m hi???u ????? ??n c??c s???c m???nh b???ng kinh nghi???m :).");
        t.setFill(Color.WHITE);
        t.setFont(Font.font(20));
        tf.getChildren().add(t);
        tf.setPadding(new Insets(10, 0, 0, 0));

        TextFlow tf1 = new TextFlow();
        Text t1 = new Text("??? Ph??m b???m: " +
                "\n- B???m c??c ph??m m??i t??n ????? di chuy???n." +
                "\n- B???m ph??m SPACE ????? th??? bom." +
                "\n- B???m ph??m R ????? ti???p t???c m??n ch??i khi Bomberman ch???t.");
        t1.setFill(Color.WHITE);
        t1.setFont(Font.font(20));
        tf1.getChildren().add(t1);
        tf1.setPadding(new Insets(10, 0, 0, 0));

        Label goBack = new Label("BACK");
        goBack.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
        goBack.setTextFill(Color.WHITE);
        goBack.setOnMouseClicked(event -> {
            theStage.setScene(startScene);
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

        return tutorialScene;
    }

    public Scene stageScene(int level) {
        // stage scene
        System.out.println(level);
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
        fullScreen = new VBox();

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
        levelBox.setPadding(new Insets(0, 0, 0, width * Sprite.SCALED_SIZE / 24));
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

                if (Integer.parseInt(t1) == 0) {
                    if (level != NUMBER_OF_LEVELS) lose = true;
                    else {
                        if (souls.get() > 0) win = true;
                    }
                }
            }
        }));

        timeBox.getChildren().addAll(titleTime, timeDisplay);
        timeBox.setAlignment(Pos.CENTER_LEFT);
        timeBox.setPadding(new Insets(0, width * Sprite.SCALED_SIZE / 24, 0, width * Sprite.SCALED_SIZE / 24));

        HBox scoreBox = new HBox();

        Label titleScore = new Label("Scores: ");
        titleScore.setFont(Font.font("Roboto", FontWeight.BOLD, 20));

        Label scoresDisplay = new Label();
        score = new SimpleIntegerProperty(scoreOfGame);
        scoresDisplay.textProperty().bind(score.asString());
        scoresDisplay.setFont(Font.font("Roboto", FontWeight.BOLD, 20));

        scoreBox.getChildren().addAll(titleScore, scoresDisplay);
        scoreBox.setAlignment(Pos.CENTER_LEFT);
        scoreBox.setPadding(new Insets(0, width * Sprite.SCALED_SIZE / 24, 0, width * Sprite.SCALED_SIZE / 24));

        HBox numberOfSouls = new HBox();

        Label soulTitle = new Label("Souls: ");
        soulTitle.setFont(Font.font("Roboto", FontWeight.BOLD, 20));

        Label soulDisplay = new Label();
        soulDisplay.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
        soulDisplay.textProperty().bind(souls.asString());
        soulDisplay.textProperty().addListener(((observableValue, s, t1) -> {
            if (t1 != null) {
                if (Integer.parseInt(t1) == 0) {
                    MediaPlayer loseSound = new MediaPlayer(new Media(BombermanGame.class.getResource("sound/endgame.wav").toExternalForm()));
                    loseSound.play();
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

        topBar.getChildren().addAll(levelBox, timeBox, numberOfSouls, scoreBox, menuOptionBox);

        Group root = new Group();
        Canvas canvas = new Canvas(Sprite.SCALED_SIZE * width, Sprite.SCALED_SIZE * height);
        root.getChildren().add(canvas);

        fullScreen.getChildren().addAll(topBar, root);

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
        Label scoreDisplay = new Label();
        if (level <= NUMBER_OF_LEVELS) {
            statusGame.setText("YOU WIN");
            scoreDisplay.setText(String.valueOf(score.get()));
        }
        else {
            statusGame.setText("YOU WIN\nCh??a c?? level m???i");
        }
        statusGame.setTextAlignment(TextAlignment.CENTER);
        statusGame.setTextFill(Color.WHITE);
        statusGame.setFont(Font.font("Roboto", FontWeight.BOLD, 30));

        scoreDisplay.setTextAlignment(TextAlignment.CENTER);
        scoreDisplay.setTextFill(Color.WHITE);
        scoreDisplay.setFont(Font.font("Roboto", FontWeight.BOLD, 25));

        winWindow.getChildren().addAll(statusGame, scoreDisplay);

        HBox buttonBar = new HBox();
        Button menuButton = new Button("MENU");
        menuButton.setStyle("-fx-background-color: transparent;");
        menuButton.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
        menuButton.setTextFill(Color.WHITE);
        menuButton.setOnAction(e -> {
            level = 1;
            startGame = true;
            try {
                BombermanGame.this.start(theStage);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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
                            try {
                                BombermanGame.this.start(theStage);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
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
        buttonBar.setPadding(new Insets(height * Sprite.SCALED_SIZE / 24, 0, 0, 0));
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
        Label scoreDisplay = new Label(String.valueOf(score.get()));
        statusGame.setTextFill(Color.WHITE);
        statusGame.setFont(Font.font("Roboto", FontWeight.BOLD, 30));
        scoreDisplay.setTextFill(Color.WHITE);
        scoreDisplay.setFont(Font.font("Roboto", FontWeight.BOLD, 25));
        loseWindow.getChildren().addAll(statusGame, scoreDisplay);

        HBox buttonBar = new HBox();
        Button menuButton = new Button("MENU");
        menuButton.setStyle("-fx-background-color: transparent;");
        menuButton.setFont(Font.font("Roboto", FontWeight.BOLD,  20));
        menuButton.setTextFill(Color.WHITE);
        menuButton.setOnAction(e -> {
            level = 1;
            lose = false;
            startGame = true;
            try {
                BombermanGame.this.start(theStage);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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
                        try {
                            BombermanGame.this.start(theStage);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
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
        buttonBar.setPadding(new Insets(height * Sprite.SCALED_SIZE / 24, 0, 0, 0));
        loseWindow.getChildren().add(buttonBar);
        loseWindow.setAlignment(Pos.CENTER);

        Scene theWinScene = new Scene(loseWindow);
        return theWinScene;
    }

    public void styleButton(Button button, Color color, Color colorHover) {
        button.setStyle("-fx-background-color: transparent;");
        button.setFont(Font.font("Roboto", FontWeight.BOLD,  20));
        button.setTextFill(color);
        button.hoverProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                button.setTextFill(colorHover);
            } else {
                button.setTextFill(color);
            }
        });
    }
}