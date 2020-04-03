package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends Application {
    static int speed = 5;
    static int foodColor = 0;
    static int width = 20;
    static int height = 20;
    static int foodX = 0;
    static int foodY = 0;
    static int cornerSize = 25;
    static List<Corner> snake = new ArrayList<>();
    static Dir direction = Dir.left;
    static Boolean gameOver = false;
    static Random rand = new Random();

    public enum Dir {
        left, right, up, down
    }

    public static class Corner {
        int x;
        int y;

        public Corner(int x, int y) {
            this.x = x;
            this.y = y;

        }
    }

    public void start(Stage primaryStage) throws Exception {
        try {
            newFood();


            VBox root = new VBox();
            Canvas canvas = new Canvas(width * cornerSize, height * cornerSize);
            GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
            root.getChildren().add(canvas);

            new AnimationTimer() {
                long lastTick = 0;

                public void handle(long now) {
                    if (lastTick == 0) {
                        lastTick = now;
                        tick(graphicsContext);
                        return;
                    }
                    if (now - lastTick > 1000000000 / speed) {
                        lastTick = now;
                        tick(graphicsContext);
                    }
                }
            }.start();

            Scene scene = new Scene(root, width * cornerSize, height * cornerSize);
            //Control
            scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
                if (key.getCode() == KeyCode.UP) {
                    direction = Dir.up;
                }
                if (key.getCode() == KeyCode.LEFT) {
                    direction = Dir.left;
                }

                if (key.getCode() == KeyCode.DOWN) {
                    direction = Dir.down;
                }

                if (key.getCode() == KeyCode.RIGHT) {
                    direction = Dir.right;
                }
            });

            // add start snakes part
            snake.add(new Corner(width / 2, height / 2));
            snake.add(new Corner(width / 2, height / 2));
            snake.add(new Corner(width / 2, height / 2));

//            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Kiki");
            primaryStage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //tick
    public static void tick(GraphicsContext graphicsContext) {
        if (gameOver) {
            graphicsContext.setFill(Color.RED);
            graphicsContext.setFont(new Font("", 50));
            graphicsContext.fillText("KIKI PUH PUH", 100, 250);
            return;
        }
        for (int i = snake.size() - 1; i >= 1; i--) {
            snake.get(i).x = snake.get(i - 1).x;
            snake.get(i).y = snake.get(i - 1).y;
        }
        switch (direction) {
            case up:
                snake.get(0).y--;
                if (snake.get(0).y < 0) {
                    gameOver = true;
                }
                break;
            case down:
                snake.get(0).y++;
                if (snake.get(0).y > height) {
                    gameOver = true;
                }
                break;
            case left:
                snake.get(0).x--;
                if (snake.get(0).x < 0) {
                    gameOver = true;
                }
                break;
            case right:
                snake.get(0).x++;
                if (snake.get(0).x > width) {
                    gameOver = true;
                }
                break;
        }

        //eat food
        if (foodX == snake.get(0).x && foodY == snake.get(0).y) {
            snake.add(new Corner(-1, -1));
            newFood();
        }
        // self destroy 
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
                gameOver = true;
            }
        }
        // fill
        //background
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, width * cornerSize, height * cornerSize);

        //score

        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setFont(new Font("", 30));
        graphicsContext.fillText("Score: " + (speed - 6), 10, 30);

        //random foodColor
        Color cc = Color.WHITE;

        switch (foodColor) {
            case 0:
                cc = Color.PURPLE;
                break;
            case 1:
                cc = Color.LIGHTBLUE;
                break;
            case 2:
                cc = Color.YELLOW;
                break;
            case 3:
                cc = Color.PINK;
                break;
            case 4:
                cc = Color.ORANGE;
                break;
        }
        graphicsContext.setFill(cc);
        graphicsContext.fillOval(foodX * cornerSize, foodY * cornerSize, cornerSize, cornerSize);


        //snake

        for (Corner c : snake) {
            graphicsContext.setFill(Color.LIGHTPINK);
            graphicsContext.fillRect(c.x * cornerSize, c.y * cornerSize, cornerSize - 1, cornerSize - 1);
            graphicsContext.setFill(Color.PINK);
            graphicsContext.fillRect(c.x * cornerSize, c.y * cornerSize, cornerSize - 2, cornerSize - 2);
        }

    }

    //food
    public static void newFood() {
        start:
        while (true) {
            foodX = rand.nextInt(width);
            foodY = rand.nextInt(height);

            for (Corner corner : snake) {
                if (corner.x == foodX && corner.y == foodY) {
                    continue start;
                }
            }
            foodColor = rand.nextInt(5);
            speed++;
            break;

        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
